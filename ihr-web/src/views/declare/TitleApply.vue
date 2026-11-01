<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'applicantName', label: '申请人', width: 100 },
  { field: 'deptName', label: '部门', width: 100 },
  { field: 'declareTitle', label: '申报职称', width: 120 },
  { field: 'declareLevel', label: '级别', width: 90 },
  { field: 'major', label: '专业', width: 120 },
  { field: 'obtainDate', label: '取得时间', width: 110, type: 'date' },
  {
    field: 'status', label: '状态', width: 100, type: 'tag',
    tags: { 0: { text: '待审核', type: 'warning' }, 1: { text: '通过', type: 'success' }, 2: { text: '驳回', type: 'danger' } },
  },
  { field: 'auditComment', label: '审核意见', minWidth: 200 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'applicantName', label: '申请人', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '待审核', value: 0 },
      { label: '通过', value: 1 },
      { label: '驳回', value: 2 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'applicantName', label: '申请人', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'declareTitle', label: '申报职称', required: true },
  {
    field: 'declareLevel', label: '级别', type: 'select',
    options: [
      { label: '初级', value: '初级' },
      { label: '中级', value: '中级' },
      { label: '副高', value: '副高' },
      { label: '正高', value: '正高' },
    ],
  },
  { field: 'major', label: '专业' },
  { field: 'obtainDate', label: '取得时间', type: 'date' },
  { field: 'materialUrl', label: '材料路径', placeholder: '如 /materials/xxx.pdf' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '待审核', value: 0 },
      { label: '通过', value: 1 },
      { label: '驳回', value: 2 },
    ],
  },
  { field: 'auditComment', label: '审核意见', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '通过',
    method: 'put',
    urlTemplate: '/declare/titles/{id}/pass',
    confirm: '确认通过该职称申报？',
    hide: (row: any) => row.status !== 0,
  },
  {
    label: '驳回',
    method: 'put',
    urlTemplate: '/declare/titles/{id}/reject',
    confirm: '确认驳回该职称申报？',
    hide: (row: any) => row.status !== 0,
  },
]
</script>

<template>
  <CrudPage
    title="职称申报"
    api-prefix="/declare/titles"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
