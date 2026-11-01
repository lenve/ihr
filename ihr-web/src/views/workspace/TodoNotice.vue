<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'title', label: '待办事项', minWidth: 180 },
  { field: 'bizType', label: '业务类型', width: 110 },
  { field: 'handlerName', label: '处理人', width: 100 },
  { field: 'dueTime', label: '截止时间', width: 160, type: 'datetime' },
  {
    field: 'status', label: '状态', width: 90, type: 'tag',
    tags: { 0: { text: '待办', type: 'warning' }, 1: { text: '已完成', type: 'success' } },
  },
  { field: 'content', label: '内容说明', minWidth: 200 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'title', label: '标题', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '待办', value: 0 },
      { label: '已完成', value: 1 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'title', label: '待办事项', required: true },
  { field: 'bizType', label: '业务类型', placeholder: '如：绩效考核' },
  { field: 'handlerName', label: '处理人', required: true },
  { field: 'dueTime', label: '截止时间', type: 'datetime' },
  { field: 'status', label: '状态', type: 'select', required: true, options: [{ label: '待办', value: 0 }, { label: '已完成', value: 1 }] },
  { field: 'content', label: '内容说明', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '标记完成',
    method: 'put',
    urlTemplate: '/workbench/todos/{id}/finish',
    confirm: '确认将该待办标记为完成？',
    hide: (row: any) => row.status === 1,
  },
]
</script>

<template>
  <CrudPage
    title="待办"
    api-prefix="/workbench/todos"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>