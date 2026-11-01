<script setup lang="ts">
/**
 * 考核表评分（HR 视角全部考核）：列表 + 评分对话框（复用 ScoreDialog）。
 */
import { onMounted, reactive, ref } from 'vue'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPRAISAL_STATUS, APPRAISAL_STATUS_OPTIONS, GRADE_TAG, PERIOD_OPTIONS } from './perf-shared'
import ScoreDialog from './components/ScoreDialog.vue'

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive<Record<string, any>>({ pageNum: 1, pageSize: 10, keyword: '', period: undefined, status: undefined })

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    if (query.keyword) params.keyword = query.keyword
    if (query.period) params.period = query.period
    if (query.status !== undefined && query.status !== null && query.status !== '') params.status = query.status
    const data = await request.get<any, any>('/performance/appraisals', { params })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  query.keyword = ''
  query.period = undefined
  query.status = undefined
  query.pageNum = 1
  load()
}

const scoreDialogVisible = ref(false)
const currentAppraisal = ref<any>(null)

function openScore(row: any) {
  currentAppraisal.value = row
  scoreDialogVisible.value = true
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="query.keyword"
          placeholder="搜索员工姓名"
          clearable
          class="search-input"
          @keyup.enter="query.pageNum = 1; load()"
        />
        <el-select v-model="query.period" placeholder="考核周期" clearable class="search-select">
          <el-option v-for="o in PERIOD_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
        </el-select>
        <el-select v-model="query.status" placeholder="考核状态" clearable class="search-select">
          <el-option v-for="o in APPRAISAL_STATUS_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
        <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="周期" width="100" align="center" />
      <el-table-column prop="empName" label="员工" width="90" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="110" show-overflow-tooltip />
      <el-table-column prop="superiorName" label="考核人" width="90" align="center" />
      <el-table-column label="状态" width="95" align="center">
        <template #default="{ row }">
          <el-tag :type="APPRAISAL_STATUS[row.status]?.type || 'info'">
            {{ APPRAISAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="selfScore" label="自评分" width="85" align="center" />
      <el-table-column prop="superiorScore" label="上级评分" width="85" align="center" />
      <el-table-column prop="totalScore" label="综合分" width="85" align="center" />
      <el-table-column label="等级" width="75" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.grade" :type="GRADE_TAG[row.grade] || 'info'">{{ row.grade }}</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="90" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" :disabled="row.status !== 3" @click="openScore(row)">评分</el-button>
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
  </el-card>

  <ScoreDialog v-model="scoreDialogVisible" :appraisal="currentAppraisal" @done="load" />
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
  flex-wrap: wrap;
  gap: 8px;
}
.search-input {
  width: 200px;
}
.search-select {
  width: 140px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
