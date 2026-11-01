<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type SearchFieldDef } from '@/components/CrudPage.vue'
import { PERIOD_OPTIONS } from './perf-shared'

const columns: ColumnDef[] = [
  { field: 'period', label: '考核周期', width: 100 },
  { field: 'empName', label: '被考核人', width: 100 },
  { field: 'deptName', label: '部门', width: 130 },
  { field: 'superiorName', label: '考核人', width: 100 },
  {
    field: 'relationType', label: '关系类型', width: 100, type: 'tag',
    tags: {
      1: { text: '上级', type: 'primary' },
      2: { text: '同级', type: 'info' },
      3: { text: '下级', type: 'warning' },
      4: { text: 'HRBP', type: 'danger' },
    },
  },
]

const searchFields: SearchFieldDef[] = [
  { field: 'empName', label: '员工姓名', type: 'input' },
  { field: 'period', label: '考核周期', type: 'select', options: PERIOD_OPTIONS },
]

const formFields: FormFieldDef[] = [
  { field: 'period', label: '考核周期', type: 'select', required: true, options: PERIOD_OPTIONS },
  { field: 'empName', label: '被考核人', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'superiorName', label: '考核人', required: true },
  {
    field: 'relationType', label: '关系类型', type: 'select', required: true,
    options: [
      { label: '上级', value: 1 },
      { label: '同级', value: 2 },
      { label: '下级', value: 3 },
      { label: 'HRBP', value: 4 },
    ],
  },
]
</script>

<template>
  <CrudPage
    title="考核关系"
    api-prefix="/performance/relations"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
  />
</template>
