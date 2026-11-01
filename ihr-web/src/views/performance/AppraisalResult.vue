<script setup lang="ts">
/**
 * 我的考核结果：本人已完成（status=7）的考核，可对结果发起申诉。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { GRADE_TAG, currentUserName, fmtTime } from './perf-shared'

const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 100, status: 7 }
    const name = currentUserName()
    if (name) params.keyword = name
    const data = await request.get<any, any>('/performance/appraisals', { params })
    records.value = data?.records || []
  } finally {
    loading.value = false
  }
}

// ---------- 发起申诉 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const currentRow = ref<any>(null)
const form = reactive<Record<string, any>>({})

function openAppeal(row: any) {
  currentRow.value = row
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, { reason: '', expect: '' })
  dialogVisible.value = true
}

async function submitAppeal() {
  if (!form.reason) {
    ElMessage.warning('请填写申诉理由')
    return
  }
  saving.value = true
  try {
    await request.post('/performance/appeals', {
      appraisalId: currentRow.value.id,
      empName: currentUserName(),
      reason: form.reason,
      expect: form.expect,
    })
    ElMessage.success('申诉已提交，等待处理')
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
      <span class="tip">我已完成的考核结果（可对结果发起申诉）</span>
      <el-button :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="考核周期" width="100" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="120" />
      <el-table-column prop="superiorName" label="考核人" width="100" align="center" />
      <el-table-column prop="selfScore" label="自评分" width="90" align="center" />
      <el-table-column prop="superiorScore" label="上级评分" width="90" align="center" />
      <el-table-column prop="totalScore" label="综合分" width="90" align="center" />
      <el-table-column label="等级" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="GRADE_TAG[row.grade] || 'info'">{{ row.grade || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="签署时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="110" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="warning" size="small" @click="openAppeal(row)">发起申诉</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="发起绩效申诉" width="560px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="考核周期">
        <el-input :model-value="currentRow?.period" disabled />
      </el-form-item>
      <el-form-item label="申诉理由" required>
        <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请说明对考核结果不服的具体理由" />
      </el-form-item>
      <el-form-item label="期望结果">
        <el-input v-model="form.expect" type="textarea" :rows="2" placeholder="期望的调整结果（可选）" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="submitAppeal">提交申诉</el-button>
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
