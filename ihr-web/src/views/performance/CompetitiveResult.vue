<script setup lang="ts">
/**
 * 竞争考核结果：按周期查看全部已完成考核，按综合分降序排名，前三名高亮。
 */
import { onMounted, ref } from 'vue'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { GRADE_TAG, PERIOD_OPTIONS } from './perf-shared'

const period = ref('2026-H2')
const loading = ref(false)
const records = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const data = await request.get<any, any>('/performance/appraisals', {
      params: { pageNum: 1, pageSize: 200, period: period.value, status: 7 },
    })
    records.value = (data?.records || [])
      .filter((r: any) => r.totalScore != null)
      .sort((a: any, b: any) => Number(b.totalScore) - Number(a.totalScore))
  } finally {
    loading.value = false
  }
}

function rowClassName({ rowIndex }: { row: any; rowIndex: number }): string {
  if (rowIndex === 0) return 'rank-first'
  if (rowIndex === 1) return 'rank-second'
  if (rowIndex === 2) return 'rank-third'
  return ''
}

const rankMedal = ['🥇', '🥈', '🥉']

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
      <span class="tip">仅展示已完成（含综合分）的考核，按综合分降序排名</span>
    </div>

    <el-table v-loading="loading" :data="records" border stripe :row-class-name="rowClassName">
      <el-table-column label="排名" width="80" align="center">
        <template #default="{ $index }">
          <span v-if="$index < 3" class="medal">{{ rankMedal[$index] }}</span>
          <span class="rank-num">{{ $index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="empName" label="员工" width="120" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="140" show-overflow-tooltip />
      <el-table-column prop="superiorName" label="考核人" width="110" align="center" />
      <el-table-column prop="selfScore" label="自评分" width="100" align="center" />
      <el-table-column prop="superiorScore" label="上级评分" width="100" align="center" />
      <el-table-column prop="totalScore" label="综合分" width="100" align="center">
        <template #default="{ row }">
          <b>{{ row.totalScore }}</b>
        </template>
      </el-table-column>
      <el-table-column label="等级" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="GRADE_TAG[row.grade] || 'info'">{{ row.grade || '-' }}</el-tag>
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
  flex-wrap: wrap;
  gap: 8px;
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
.tip {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
.period-select {
  width: 140px;
}
.medal {
  margin-right: 4px;
}
:deep(.rank-first) {
  background-color: var(--el-color-danger-light-9) !important;
}
:deep(.rank-second) {
  background-color: var(--el-color-warning-light-8) !important;
}
:deep(.rank-third) {
  background-color: var(--el-color-primary-light-8) !important;
}
</style>
