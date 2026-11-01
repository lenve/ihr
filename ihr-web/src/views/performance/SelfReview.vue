<script setup lang="ts">
/**
 * 员工自评：列出本人处于"目标确认/自评中"的考核，逐目标填写自评分与说明后提交。
 */
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPRAISAL_STATUS, currentUserName } from './perf-shared'

const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 100 }
    const name = currentUserName()
    if (name) params.keyword = name
    const data = await request.get<any, any>('/performance/appraisals', { params })
    records.value = (data?.records || []).filter((r: any) => r.status === 1 || r.status === 2)
  } finally {
    loading.value = false
  }
}

// ---------- 自评对话框 ----------
const dialogVisible = ref(false)
const submitting = ref(false)
const goalsLoading = ref(false)
const currentAppraisal = ref<any>(null)
const goals = ref<any[]>([])

async function openReview(row: any) {
  currentAppraisal.value = row
  dialogVisible.value = true
  goalsLoading.value = true
  try {
    goals.value = await request.get<any, any>('/performance/goals', { params: { appraisalId: row.id } })
  } finally {
    goalsLoading.value = false
  }
}

async function submitReview() {
  for (const g of goals.value) {
    if (g.selfScore === null || g.selfScore === undefined) {
      ElMessage.warning(`请为目标「${g.goalName}」填写自评分`)
      return
    }
  }
  submitting.value = true
  try {
    for (const g of goals.value) {
      await request.put(`/performance/goals/${g.id}`, {
        selfScore: g.selfScore,
        selfComment: g.selfComment || '',
      })
    }
    await request.put(`/performance/appraisals/${currentAppraisal.value.id}/submit-self-review`)
    ElMessage.success('自评已提交，等待上级评价')
    dialogVisible.value = false
    load()
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <span class="tip">待自评考核（状态：目标确认 / 自评中）</span>
      <el-button :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
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
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openReview(row)">自评</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="员工自评" width="860px" destroy-on-close>
    <el-descriptions v-if="currentAppraisal" :column="3" border size="small" class="mb">
      <el-descriptions-item label="考核周期">{{ currentAppraisal.period }}</el-descriptions-item>
      <el-descriptions-item label="考核人">{{ currentAppraisal.superiorName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="部门">{{ currentAppraisal.deptName || '-' }}</el-descriptions-item>
    </el-descriptions>

    <el-table v-loading="goalsLoading" :data="goals" border stripe>
      <el-table-column prop="goalName" label="目标名称" min-width="130" show-overflow-tooltip />
      <el-table-column prop="dimension" label="维度" width="70" align="center" />
      <el-table-column prop="weight" label="权重%" width="70" align="center" />
      <el-table-column prop="targetValue" label="目标值" min-width="100" show-overflow-tooltip />
      <el-table-column prop="standard" label="评分标准" min-width="140" show-overflow-tooltip />
      <el-table-column label="自评分(0-100)" width="150" align="center">
        <template #default="{ row }">
          <el-input-number v-model="row.selfScore" :min="0" :max="100" :precision="1" size="small" style="width: 130px" />
        </template>
      </el-table-column>
      <el-table-column label="自评说明" min-width="180">
        <template #default="{ row }">
          <el-input v-model="row.selfComment" size="small" placeholder="完成情况说明（可选）" />
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submitReview">保存并提交自评</el-button>
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
.tip {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
.mb {
  margin-bottom: 12px;
}
</style>
