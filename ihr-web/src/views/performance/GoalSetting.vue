<script setup lang="ts">
/**
 * 目标制定：选择周期 → 列出当前用户的考核 → 展开维护目标 → 确认目标。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPRAISAL_STATUS, DIMENSION_OPTIONS, PERIOD_OPTIONS, currentUserName } from './perf-shared'

const period = ref('2026-H2')
const loading = ref(false)
const records = ref<any[]>([])
const goalsMap = reactive<Record<number, any[]>>({})

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 50, period: period.value }
    const name = currentUserName()
    if (name) params.keyword = name
    const data = await request.get<any, any>('/performance/appraisals', { params })
    records.value = data?.records || []
    Object.keys(goalsMap).forEach((k) => delete goalsMap[Number(k)])
  } finally {
    loading.value = false
  }
}

async function loadGoals(appraisalId: number) {
  goalsMap[appraisalId] = await request.get<any, any>('/performance/goals', { params: { appraisalId } })
}

/** el-table 展开行变化时加载目标 */
function onExpand(row: any, expandedRows: any[]) {
  if (expandedRows.some((r) => r.id === row.id) && !goalsMap[row.id]) {
    loadGoals(row.id)
  }
}

// ---------- 目标维护 ----------
const goalDialogVisible = ref(false)
const goalSaving = ref(false)
const editingGoalId = ref<number | null>(null)
const currentAppraisal = ref<any>(null)
const goalForm = reactive<Record<string, any>>({})

function handleAddGoal(row: any) {
  if (row.status !== 0) {
    ElMessage.warning('目标已确认，不能再修改')
    return
  }
  currentAppraisal.value = row
  editingGoalId.value = null
  Object.keys(goalForm).forEach((k) => delete goalForm[k])
  Object.assign(goalForm, { goalName: '', dimension: '业绩', weight: 20, targetValue: '', standard: '' })
  goalDialogVisible.value = true
}

function handleEditGoal(appraisal: any, goal: any) {
  if (appraisal.status !== 0) {
    ElMessage.warning('目标已确认，不能再修改')
    return
  }
  currentAppraisal.value = appraisal
  editingGoalId.value = goal.id
  Object.keys(goalForm).forEach((k) => delete goalForm[k])
  Object.assign(goalForm, {
    goalName: goal.goalName,
    dimension: goal.dimension,
    weight: goal.weight,
    targetValue: goal.targetValue,
    standard: goal.standard,
  })
  goalDialogVisible.value = true
}

async function submitGoal() {
  if (!goalForm.goalName) {
    ElMessage.warning('请填写目标名称')
    return
  }
  goalSaving.value = true
  try {
    const body = { ...goalForm, appraisalId: currentAppraisal.value.id }
    if (editingGoalId.value == null) {
      await request.post('/performance/goals', body)
      ElMessage.success('目标已添加')
    } else {
      await request.put(`/performance/goals/${editingGoalId.value}`, body)
      ElMessage.success('目标已修改')
    }
    goalDialogVisible.value = false
    loadGoals(currentAppraisal.value.id)
  } finally {
    goalSaving.value = false
  }
}

async function handleDeleteGoal(appraisal: any, goal: any) {
  if (appraisal.status !== 0) {
    ElMessage.warning('目标已确认，不能再删除')
    return
  }
  await ElMessageBox.confirm(`确认删除目标「${goal.goalName}」？`, '提示', { type: 'warning' })
  await request.delete(`/performance/goals/${goal.id}`)
  ElMessage.success('删除成功')
  loadGoals(appraisal.id)
}

async function confirmGoal(row: any) {
  await ElMessageBox.confirm('确认提交目标？确认后将进入考核流程', '提示', { type: 'warning' })
  await request.put(`/performance/appraisals/${row.id}/confirm-goal`)
  ElMessage.success('目标已确认')
  load()
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <div class="toolbar-left">
        <span class="label">考核周期</span>
        <el-select v-model="period" class="period-select" @change="load">
          <el-option v-for="o in PERIOD_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button :icon="Refresh" @click="period = '2026-H2'; load()">重置</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="records" border stripe row-key="id" @expand-change="onExpand">
      <el-table-column type="expand">
        <template #default="{ row }">
          <div class="goal-panel">
            <div class="goal-panel-header">
              <span>考核目标（权重合计建议 100%）</span>
              <el-button type="primary" size="small" :disabled="row.status !== 0" @click="handleAddGoal(row)">新增目标</el-button>
            </div>
            <el-table :data="goalsMap[row.id] || []" border size="small">
              <el-table-column prop="goalName" label="目标名称" min-width="140" show-overflow-tooltip />
              <el-table-column prop="dimension" label="维度" width="70" align="center" />
              <el-table-column prop="weight" label="权重%" width="70" align="center" />
              <el-table-column prop="targetValue" label="目标值" min-width="110" show-overflow-tooltip />
              <el-table-column prop="standard" label="评分标准" min-width="160" show-overflow-tooltip />
              <el-table-column v-if="row.status === 0" label="操作" width="130" align="center">
                <template #default="scope">
                  <el-button link type="primary" size="small" @click="handleEditGoal(row, scope.row)">编辑</el-button>
                  <el-button link type="danger" size="small" @click="handleDeleteGoal(row, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="考核周期" width="100" align="center" />
      <el-table-column prop="empName" label="被考核人" width="100" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="120" />
      <el-table-column prop="superiorName" label="考核人" width="100" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="APPRAISAL_STATUS[row.status]?.type || 'info'">
            {{ APPRAISAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="110" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" :disabled="row.status !== 0" @click="confirmGoal(row)">确认目标</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="goalDialogVisible" :title="editingGoalId == null ? '新增目标' : '修改目标'" width="560px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="目标名称" required>
        <el-input v-model="goalForm.goalName" placeholder="如：销售额达成 500 万" />
      </el-form-item>
      <el-form-item label="维度" required>
        <el-select v-model="goalForm.dimension" style="width: 100%">
          <el-option v-for="o in DIMENSION_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="权重%" required>
        <el-input-number v-model="goalForm.weight" :min="1" :max="100" style="width: 100%" />
      </el-form-item>
      <el-form-item label="目标值">
        <el-input v-model="goalForm.targetValue" placeholder="如：500 万 / 3 个项目" />
      </el-form-item>
      <el-form-item label="评分标准">
        <el-input v-model="goalForm.standard" type="textarea" :rows="3" placeholder="评分依据与标准说明" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="goalDialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="goalSaving" @click="submitGoal">保存</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}
.label {
  font-size: 14px;
  color: var(--el-text-color-regular);
}
.period-select {
  width: 140px;
}
.goal-panel {
  padding: 8px 24px;
}
.goal-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
</style>
