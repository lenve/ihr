<script setup lang="ts">
/**
 * 工资查询（员工自助视角）：
 * 默认按当前登录用户姓名（localStorage ihr_user 的 nickname）查本人记录；
 * 「查看全员」开关取消姓名过滤（管理员场景），并提供姓名搜索。
 */
import { computed, onMounted, ref } from 'vue'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

function readMyName(): string {
  try {
    const raw = localStorage.getItem('ihr_user')
    const user = raw ? JSON.parse(raw) : null
    return user?.nickname || user?.username || ''
  } catch {
    return ''
  }
}

const myName = readMyName()
const viewAll = ref(false)
const month = ref('')
const keyword = ref('')
const loading = ref(false)
const allRecords = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 500 }
    if (!viewAll.value) {
      params.keyword = myName
    } else if (keyword.value.trim()) {
      params.keyword = keyword.value.trim()
    }
    const data = await request.get<any, any>('/hrservice/salaries', { params })
    allRecords.value = data?.records || []
  } finally {
    loading.value = false
  }
}

/** 月份下拉选项：从当前结果集中提取去重（倒序） */
const months = computed(() => {
  const set = new Set<string>(allRecords.value.map((r) => r.salaryMonth).filter(Boolean))
  return Array.from(set).sort().reverse()
})

/** 月份过滤在前端完成，避免月份下拉选项随过滤消失 */
const records = computed(() =>
  month.value ? allRecords.value.filter((r) => r.salaryMonth === month.value) : allRecords.value,
)

function toggleViewAll(value: boolean) {
  viewAll.value = value
  keyword.value = ''
  month.value = ''
  load()
}

function resetSearch() {
  keyword.value = ''
  month.value = ''
  load()
}

function fmtMoney(v: any): string {
  if (v === null || v === undefined || v === '') return '-'
  return Number(v).toFixed(2)
}

function fmtTime(v: any): string {
  if (!v) return '-'
  return String(v).replace('T', ' ').slice(0, 16)
}

const statusTags: Record<number, { text: string; type: string }> = {
  0: { text: '未发放', type: 'warning' },
  1: { text: '已发放', type: 'success' },
}

/** 合计行：对金额列求和 */
function summarize(param: any) {
  const { columns, data } = param
  const sums: string[] = []
  const moneyFields = ['baseSalary', 'postSalary', 'bonus', 'deduction', 'insurance', 'tax', 'netSalary']
  columns.forEach((col: any, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    const field = col.property
    if (!field || !moneyFields.includes(field)) {
      sums[index] = ''
      return
    }
    const total = data.reduce((acc: number, row: any) => acc + (Number(row[field]) || 0), 0)
    sums[index] = total.toFixed(2)
  })
  return sums
}

onMounted(load)
</script>

<template>
  <div class="salary-query">
    <el-card shadow="never">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-switch
            :model-value="viewAll"
            active-text="查看全员"
            @change="toggleViewAll"
          />
          <el-input
            v-if="viewAll"
            v-model="keyword"
            placeholder="搜索姓名"
            clearable
            class="search-input"
            @keyup.enter="load()"
          />
          <el-select v-model="month" placeholder="工资月份" clearable class="month-select">
            <el-option v-for="m in months" :key="m" :label="m" :value="m" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <span class="my-name-tip">
          {{ viewAll ? '全员工资记录' : `当前查询：${myName || '未知用户'} 的工资记录` }}
        </span>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe show-summary :summary-method="summarize">
        <el-table-column prop="salaryMonth" label="月份" width="100" align="center" />
        <el-table-column label="基本工资" width="110" align="right">
          <template #default="{ row }">{{ fmtMoney(row.baseSalary) }}</template>
        </el-table-column>
        <el-table-column label="岗位工资" width="110" align="right">
          <template #default="{ row }">{{ fmtMoney(row.postSalary) }}</template>
        </el-table-column>
        <el-table-column label="奖金" width="100" align="right">
          <template #default="{ row }">{{ fmtMoney(row.bonus) }}</template>
        </el-table-column>
        <el-table-column label="扣款" width="100" align="right">
          <template #default="{ row }">{{ fmtMoney(row.deduction) }}</template>
        </el-table-column>
        <el-table-column label="社保公积金" width="120" align="right">
          <template #default="{ row }">{{ fmtMoney(row.insurance) }}</template>
        </el-table-column>
        <el-table-column label="个税" width="100" align="right">
          <template #default="{ row }">{{ fmtMoney(row.tax) }}</template>
        </el-table-column>
        <el-table-column label="实发合计" width="120" align="right">
          <template #default="{ row }">
            <span class="net-salary">{{ fmtMoney(row.netSalary) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(statusTags[row.status]?.type as any) || 'info'">
              {{ statusTags[row.status]?.text ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发放时间" width="160" align="center">
          <template #default="{ row }">{{ fmtTime(row.payTime) }}</template>
        </el-table-column>
        <el-table-column v-if="viewAll" prop="empName" label="姓名" width="100" />
      </el-table>
    </el-card>
  </div>
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
  width: 180px;
}
.month-select {
  width: 140px;
}
.my-name-tip {
  color: #909399;
  font-size: 13px;
}
.net-salary {
  font-weight: 700;
  color: #409eff;
}
</style>
