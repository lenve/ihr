<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'applicantName', label: '申请人', width: 100 },
  { field: 'deptName', label: '部门', width: 100 },
  { field: 'country', label: '国家', width: 100 },
  { field: 'visaType', label: '签证类型', width: 110 },
  { field: 'entryCount', label: '入境次数', width: 90 },
  { field: 'planDeparture', label: '计划出发', width: 110, type: 'date' },
  { field: 'planReturn', label: '计划返回', width: 110, type: 'date' },
  { field: 'reason', label: '事由', minWidth: 180 },
  {
    field: 'status', label: '状态', width: 100, type: 'tag',
    tags: {
      0: { text: '待审批', type: 'warning' },
      1: { text: '已通过', type: 'success' },
      2: { text: '已驳回', type: 'danger' },
      3: { text: '已办理', type: 'primary' },
    },
  },
  { field: 'auditComment', label: '审批意见', minWidth: 160 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'applicantName', label: '申请人', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '待审批', value: 0 },
      { label: '已通过', value: 1 },
      { label: '已驳回', value: 2 },
      { label: '已办理', value: 3 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'applicantName', label: '申请人', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'country', label: '国家', required: true },
  {
    field: 'visaType', label: '签证类型', type: 'select',
    options: [
      { label: 'B1 商务', value: 'B1 商务' },
      { label: 'B2 旅游', value: 'B2 旅游' },
      { label: '旅游', value: '旅游' },
      { label: '工作', value: '工作' },
    ],
  },
  {
    field: 'entryCount', label: '入境次数', type: 'select',
    options: [
      { label: '单次', value: '单次' },
      { label: '多次', value: '多次' },
    ],
  },
  { field: 'planDeparture', label: '计划出发', type: 'date' },
  { field: 'planReturn', label: '计划返回', type: 'date' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '待审批', value: 0 },
      { label: '已通过', value: 1 },
      { label: '已驳回', value: 2 },
      { label: '已办理', value: 3 },
    ],
  },
  { field: 'reason', label: '事由', type: 'textarea' },
  { field: 'auditComment', label: '审批意见', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '通过',
    method: 'put',
    urlTemplate: '/declare/visas/{id}/approve',
    confirm: '确认通过该签证申请？',
    hide: (row: any) => row.status !== 0,
  },
  {
    label: '驳回',
    method: 'put',
    urlTemplate: '/declare/visas/{id}/reject',
    confirm: '确认驳回该签证申请？',
    hide: (row: any) => row.status !== 0,
  },
  {
    label: '已办理',
    method: 'put',
    urlTemplate: '/declare/visas/{id}/done',
    confirm: '确认该签证已办理完成？',
    hide: (row: any) => row.status !== 1,
  },
]
</script>

<template>
  <CrudPage
    title="签证申请"
    api-prefix="/declare/visas"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
