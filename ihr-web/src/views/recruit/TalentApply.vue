<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

/**
 * 默认申报人：CrudPage 表单无默认值钩子且禁止修改共享组件，
 * 前端不做预填，由后端在新增时自动填充当前用户姓名（申报人留空即可）。
 */
const columns: ColumnDef[] = [
  { field: 'declarerName', label: '申报人', width: 100 },
  { field: 'declareType', label: '类型', width: 110 },
  { field: 'personName', label: '被申报人', width: 100 },
  { field: 'reason', label: '理由', minWidth: 200, showOverflowTooltip: true },
  {
    field: 'status', label: '状态', width: 90, type: 'tag',
    tags: {
      0: { text: '待审核', type: 'warning' },
      1: { text: '通过', type: 'success' },
      2: { text: '驳回', type: 'danger' },
    },
  },
  { field: 'auditComment', label: '审核意见', minWidth: 140, showOverflowTooltip: true },
  { field: 'auditor', label: '审核人', width: 100 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'personName', label: '被申报人', type: 'input' },
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
  { field: 'declarerName', label: '申报人', placeholder: '留空自动填充当前用户' },
  {
    field: 'declareType', label: '类型', type: 'select',
    options: [
      { label: '内部推荐', value: '内部推荐' },
      { label: '高端人才', value: '高端人才' },
      { label: '团队引进', value: '团队引进' },
    ],
  },
  { field: 'personName', label: '被申报人', required: true },
  { field: 'materialUrl', label: '材料地址', placeholder: '如：/materials/xxx.pdf' },
  { field: 'reason', label: '申报理由', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '通过',
    method: 'put',
    urlTemplate: '/talent/declarations/{id}/pass',
    confirm: '确认通过该人才申报？',
    hide: (row: any) => row.status !== 0,
  },
  {
    label: '驳回',
    method: 'put',
    urlTemplate: '/talent/declarations/{id}/reject',
    confirm: '确认驳回该人才申报？',
    hide: (row: any) => row.status !== 0,
  },
]
</script>

<template>
  <CrudPage
    title="人才申报"
    api-prefix="/talent/declarations"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
