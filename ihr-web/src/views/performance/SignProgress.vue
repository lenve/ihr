<script setup lang="ts">
import CrudPage, { type ColumnDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'
import { APPRAISAL_STATUS, APPRAISAL_STATUS_OPTIONS, PERIOD_OPTIONS } from './perf-shared'

const columns: ColumnDef[] = [
  { field: 'period', label: '考核周期', width: 100 },
  { field: 'empName', label: '被考核人', width: 100 },
  { field: 'deptName', label: '部门', width: 130 },
  { field: 'superiorName', label: '考核人', width: 100 },
  {
    field: 'status', label: '考核状态', width: 100, type: 'tag',
    tags: Object.fromEntries(
      Object.entries(APPRAISAL_STATUS).map(([k, v]) => [Number(k), { text: v.text, type: v.type }]),
    ),
  },
  {
    field: 'signStatus', label: '签署状态', width: 100, type: 'tag',
    tags: { 0: { text: '未签署', type: 'warning' }, 1: { text: '已签署', type: 'success' } },
  },
]

const searchFields: SearchFieldDef[] = [
  { field: 'empName', label: '员工姓名', type: 'input' },
  { field: 'period', label: '考核周期', type: 'select', options: PERIOD_OPTIONS },
  { field: 'status', label: '考核状态', type: 'select', options: APPRAISAL_STATUS_OPTIONS },
]

const rowActions: RowAction[] = [
  {
    label: '签署',
    method: 'put',
    urlTemplate: '/performance/appraisals/{id}/sign',
    confirm: '确认签署该考核结果？',
    hide: (row: any) => row.signStatus === 1 || row.status !== 6,
  },
]
</script>

<template>
  <CrudPage
    title="签署进度"
    api-prefix="/performance/appraisals"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="[]"
    :row-actions="rowActions"
    :addable="false"
    :editable="false"
    :deletable="false"
  />
</template>
