<script setup lang="ts">
/**
 * 我的申诉：本人申诉列表 + 新建申诉（选择本人已完成的考核）。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { currentUserName, fmtTime } from './perf-shared'

const APPEAL_STATUS: Record<number, { text: string; type: any }> = {
  0: { text: '申诉中', type: 'warning' },
  1: { text: '已受理', type: 'primary' },
  2: { text: '已驳回', type: 'danger' },
  3: { text: '已调整', type: 'success' },
}

const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 100 }
    const name = currentUserName()
    if (name) params.keyword = name
    const data = await request.get<any, any>('/performance/appeals', { params })
    records.value = data?.records || []
  } finally {
    loading.value = false
  }
}

// ---------- 新建申诉 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const finishedAppraisals = ref<any[]>([])
const form = reactive<Record<string, any>>({})

async function openCreate() {
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, { appraisalId: undefined, reason: '', expect: '' })
  const params: Record<string, any> = { pageNum: 1, pageSize: 100, status: 7 }
  const name = currentUserName()
  if (name) params.keyword = name
  const data = await request.get<any, any>('/performance/appraisals', { params })
  finishedAppraisals.value = data?.records || []
  if (!finishedAppraisals.value.length) {
    ElMessage.info('暂无可申诉的已完成考核')
    return
  }
  form.appraisalId = finishedAppraisals.value[0].id
  dialogVisible.value = true
}

async function submitCreate() {
  if (!form.appraisalId) {
    ElMessage.warning('请选择要申诉的考核')
    return
  }
  if (!form.reason) {
    ElMessage.warning('请填写申诉理由')
    return
  }
  saving.value = true
  try {
    await request.post('/performance/appeals', {
      appraisalId: form.appraisalId,
      empName: currentUserName(),
      reason: form.reason,
      expect: form.expect,
    })
    ElMessage.success('申诉已提交')
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <span class="tip">我的绩效申诉记录</span>
      <el-button type="primary" :icon="Plus" @click="openCreate">新建申诉</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="appraisalId" label="考核 ID" width="90" align="center" />
      <el-table-column prop="reason" label="申诉理由" min-width="180" show-overflow-tooltip />
      <el-table-column prop="expect" label="期望结果" min-width="140" show-overflow-tooltip />
      <el-table-column label="状态" width="95" align="center">
        <template #default="{ row }">
          <el-tag :type="APPEAL_STATUS[row.status]?.type || 'info'">
            {{ APPEAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditComment" label="审核意见" min-width="150" show-overflow-tooltip />
      <el-table-column label="审核时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.auditTime) }}</template>
      </el-table-column>
      <el-table-column label="申请时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新建绩效申诉" width="560px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="选择考核" required>
        <el-select v-model="form.appraisalId" style="width: 100%">
          <el-option
            v-for="a in finishedAppraisals"
            :key="a.id"
            :label="`${a.period} - 综合分 ${a.totalScore ?? '-'}（等级 ${a.grade ?? '-'}）`"
            :value="a.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申诉理由" required>
        <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请说明申诉的具体理由" />
      </el-form-item>
      <el-form-item label="期望结果">
        <el-input v-model="form.expect" type="textarea" :rows="2" placeholder="期望的调整结果（可选）" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="submitCreate">提交</el-button>
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
</style>
