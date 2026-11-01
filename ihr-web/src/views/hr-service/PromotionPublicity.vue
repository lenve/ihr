<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'empName', label: '姓名', width: 100 },
  { field: 'deptName', label: '部门', width: 100 },
  { field: 'fromPosition', label: '原岗位', width: 130 },
  { field: 'toPosition', label: '拟任岗位', width: 130 },
  { field: 'publicityStart', label: '公示开始', width: 110, type: 'date' },
  { field: 'publicityEnd', label: '公示结束', width: 110, type: 'date' },
  {
    field: 'status', label: '状态', width: 100, type: 'tag',
    tags: { 0: { text: '公示中', type: 'warning' }, 1: { text: '已通过', type: 'success' }, 2: { text: '有异议', type: 'danger' } },
  },
  { field: 'applyReason', label: '申报事由', minWidth: 200 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'empName', label: '姓名', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '公示中', value: 0 },
      { label: '已通过', value: 1 },
      { label: '有异议', value: 2 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'empName', label: '姓名', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'fromPosition', label: '原岗位' },
  { field: 'toPosition', label: '拟任岗位' },
  { field: 'publicityStart', label: '公示开始', type: 'date' },
  { field: 'publicityEnd', label: '公示结束', type: 'date' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '公示中', value: 0 },
      { label: '已通过', value: 1 },
      { label: '有异议', value: 2 },
    ],
  },
  { field: 'applyReason', label: '申报事由', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '通过',
    method: 'put',
    urlTemplate: '/hrservice/promotions/{id}/pass',
    confirm: '确认该晋升公示通过？',
  },
  {
    label: '有异议',
    method: 'put',
    urlTemplate: '/hrservice/promotions/{id}/object',
    confirm: '确认将该公示标记为有异议？',
  },
]
</script>

<template>
  <CrudPage
    title="晋升公示"
    api-prefix="/hrservice/promotions"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
