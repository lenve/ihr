<script setup lang="ts">
/**
 * 面谈审核：列出已填写待审核（auditStatus=0 且 fillStatus=1）的面谈记录，通过/驳回。
 */
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { fmtTime } from './perf-shared'

const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const data = await request.get<any, any>('/performance/interviews', {
      params: { pageNum: 1, pageSize: 100, fillStatus: 1, auditStatus: 0 },
    })
    records.value = data?.records || []
  } finally {
    loading.value = false
  }
}

async function audit(row: any, passed: boolean) {
  await ElMessageBox.confirm(
    passed ? '确认通过该面谈记录？通过后考核进入待签署' : '确认驳回该面谈记录？驳回后考核回到待面谈',
    '提示',
    { type: 'warning' },
  )
  await request.put(`/performance/interviews/${row.id}/audit`, null, { params: { passed } })
  ElMessage.success('审核完成')
  load()
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <span class="tip">待审核的面谈记录</span>
      <el-button :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="empName" label="被面谈人" width="100" align="center" />
      <el-table-column prop="interviewer" label="面谈人" width="100" align="center" />
      <el-table-column label="面谈时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.interviewTime) }}</template>
      </el-table-column>
      <el-table-column prop="advantage" label="优势肯定" min-width="140" show-overflow-tooltip />
      <el-table-column prop="shortage" label="不足指出" min-width="140" show-overflow-tooltip />
      <el-table-column prop="improvePlan" label="改进计划" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="130" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="success" size="small" @click="audit(row, true)">通过</el-button>
          <el-button link type="danger" size="small" @click="audit(row, false)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
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
