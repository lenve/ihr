<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  {
    field: 'level', label: '级别', width: 90, type: 'tag',
    tags: { 1: { text: '提示', type: 'info' }, 2: { text: '警告', type: 'warning' }, 3: { text: '严重', type: 'danger' } },
  },
  { field: 'title', label: '标题', minWidth: 160 },
  { field: 'content', label: '内容', minWidth: 220 },
  { field: 'source', label: '来源', width: 110 },
  {
    field: 'status', label: '状态', width: 90, type: 'tag',
    tags: { 0: { text: '未处理', type: 'warning' }, 1: { text: '已处理', type: 'success' } },
  },
  { field: 'handleRemark', label: '处理备注', minWidth: 160 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'title', label: '标题', type: 'input' },
  {
    field: 'level', label: '级别', type: 'select',
    options: [
      { label: '提示', value: 1 },
      { label: '警告', value: 2 },
      { label: '严重', value: 3 },
    ],
  },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '未处理', value: 0 },
      { label: '已处理', value: 1 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  {
    field: 'level', label: '级别', type: 'select', required: true,
    options: [
      { label: '提示', value: 1 },
      { label: '警告', value: 2 },
      { label: '严重', value: 3 },
    ],
  },
  { field: 'title', label: '标题', required: true },
  { field: 'source', label: '来源', placeholder: '如：人事合同' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [{ label: '未处理', value: 0 }, { label: '已处理', value: 1 }],
  },
  { field: 'content', label: '内容', type: 'textarea' },
  { field: 'handleRemark', label: '处理备注', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '处理',
    method: 'put',
    urlTemplate: '/workbench/alerts/{id}/handle',
    confirm: '确认将该预警标记为已处理？',
    hide: (row: any) => row.status === 1,
  },
]
</script>

<template>
  <CrudPage
    title="预警"
    api-prefix="/workbench/alerts"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
