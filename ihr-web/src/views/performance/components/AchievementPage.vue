<script setup lang="ts">
/**
 * 业绩记录通用页面：按 achieveType 区分个人业绩(1)/团队业绩申请(2)。
 * MyAchievement.vue 与 TeamAchievementApply.vue 复用。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { PERIOD_OPTIONS, currentUserName } from '../perf-shared'

const props = defineProps<{
  achieveType: number
  title: string
}>()

const ACHIEVE_STATUS: Record<number, { text: string; type: any }> = {
  0: { text: '草稿', type: 'info' },
  1: { text: '已提交', type: 'warning' },
  2: { text: '已确认', type: 'success' },
  3: { text: '已驳回', type: 'danger' },
}

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10, keyword: '' })

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      achieveType: props.achieveType,
    }
    if (query.keyword) params.keyword = query.keyword
    const data = await request.get<any, any>('/performance/achievements', { params })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  query.keyword = ''
  query.pageNum = 1
  load()
}

// ---------- 新增/编辑 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<Record<string, any>>({})

function handleAdd() {
  editingId.value = null
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, {
    period: '2026-H2',
    empName: currentUserName(),
    deptName: '',
    title: '',
    description: '',
    amount: 0,
  })
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, {
    period: row.period,
    empName: row.empName,
    deptName: row.deptName,
    title: row.title,
    description: row.description,
    amount: row.amount,
  })
  dialogVisible.value = true
}

async function submitForm() {
  if (!form.title) {
    ElMessage.warning('请填写业绩名称')
    return
  }
  saving.value = true
  try {
    const body = { ...form, achieveType: props.achieveType, status: editingId.value == null ? 0 : undefined }
    if (editingId.value == null) {
      await request.post('/performance/achievements', body)
      ElMessage.success('新增成功')
    } else {
      await request.put(`/performance/achievements/${editingId.value}`, body)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该记录吗？', '提示', { type: 'warning' })
  await request.delete(`/performance/achievements/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

async function doAction(row: any, action: 'submit' | 'confirm' | 'reject') {
  const tips: Record<string, string> = { submit: '确认提交该业绩记录？', confirm: '确认通过该业绩记录？', reject: '确认驳回该业绩记录？' }
  await ElMessageBox.confirm(tips[action], '提示', { type: 'warning' })
  await request.put(`/performance/achievements/${row.id}/${action}`)
  ElMessage.success('操作成功')
  load()
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="query.keyword"
          placeholder="搜索业绩名称"
          clearable
          class="search-input"
          @keyup.enter="query.pageNum = 1; load()"
        />
        <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
        <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
      </div>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="周期" width="100" align="center" />
      <el-table-column prop="title" label="业绩名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
      <el-table-column prop="empName" label="申请人" width="90" align="center" />
      <el-table-column label="金额" width="110" align="right">
        <template #default="{ row }">{{ row.amount != null ? Number(row.amount).toFixed(2) : '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="ACHIEVE_STATUS[row.status]?.type || 'info'">{{ ACHIEVE_STATUS[row.status]?.text ?? row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditComment" label="审核意见" min-width="120" show-overflow-tooltip />
      <el-table-column label="操作" width="240" fixed="right" align="center">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" link type="primary" size="small" @click="doAction(row, 'submit')">提交</el-button>
          <el-button v-if="row.status === 1" link type="success" size="small" @click="doAction(row, 'confirm')">确认</el-button>
          <el-button v-if="row.status === 1" link type="danger" size="small" @click="doAction(row, 'reject')">驳回</el-button>
          <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.pageNum"
      v-model:page-size="query.pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      class="pagination"
      @current-change="load"
      @size-change="query.pageNum = 1; load()"
    />

    <el-dialog v-model="dialogVisible" :title="(editingId == null ? '新增' : '修改') + props.title" width="640px" destroy-on-close>
      <el-form label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="周期" required>
              <el-select v-model="form.period" style="width: 100%">
                <el-option v-for="o in PERIOD_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人" required>
              <el-input v-model="form.empName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-input v-model="form.deptName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额">
              <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业绩名称" required>
              <el-input v-model="form.title" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  flex-wrap: wrap;
  gap: 8px;
}
.toolbar-left {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.search-input {
  width: 220px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
