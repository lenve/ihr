<script setup lang="ts">
/**
 * 面谈填写：列出未填写的面谈记录（fillStatus=0），逐条填写面谈内容。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { fmtTime } from './perf-shared'

const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const data = await request.get<any, any>('/performance/interviews', {
      params: { pageNum: 1, pageSize: 100, fillStatus: 0 },
    })
    records.value = data?.records || []
  } finally {
    loading.value = false
  }
}

// ---------- 填写弹窗 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const currentRow = ref<any>(null)
const form = reactive<Record<string, any>>({})

function openFill(row: any) {
  currentRow.value = row
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, {
    interviewTime: '',
    advantage: '',
    shortage: '',
    improvePlan: '',
  })
  dialogVisible.value = true
}

async function submitFill() {
  if (!form.advantage && !form.shortage && !form.improvePlan) {
    ElMessage.warning('请至少填写一项面谈内容')
    return
  }
  saving.value = true
  try {
    await request.put(`/performance/interviews/${currentRow.value.id}/fill`, {
      interviewTime: form.interviewTime || null,
      advantage: form.advantage,
      shortage: form.shortage,
      improvePlan: form.improvePlan,
    })
    ElMessage.success('面谈记录已提交')
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
      <span class="tip">待填写的面谈记录</span>
      <el-button :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="empName" label="被面谈人" width="110" align="center" />
      <el-table-column prop="interviewer" label="面谈人" width="110" align="center" />
      <el-table-column prop="appraisalId" label="考核 ID" width="90" align="center" />
      <el-table-column label="创建时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openFill(row)">填写</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="填写面谈记录" width="620px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="面谈时间">
        <el-date-picker
          v-model="form.interviewTime"
          type="datetime"
          value-format="YYYY-MM-DDTHH:mm:ss"
          placeholder="选择面谈时间（默认当前）"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="优势肯定">
        <el-input v-model="form.advantage" type="textarea" :rows="3" placeholder="本周期表现出的优势与亮点" />
      </el-form-item>
      <el-form-item label="不足指出">
        <el-input v-model="form.shortage" type="textarea" :rows="3" placeholder="需要改进的不足之处" />
      </el-form-item>
      <el-form-item label="改进计划">
        <el-input v-model="form.improvePlan" type="textarea" :rows="3" placeholder="下一周期的改进计划与期望" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="submitFill">提交填写</el-button>
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
