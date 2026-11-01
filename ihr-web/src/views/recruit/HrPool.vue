<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'name', label: '姓名', width: 90 },
  { field: 'deptName', label: '部门', width: 100 },
  { field: 'position', label: '岗位', width: 110 },
  { field: 'skill', label: '技能', minWidth: 160, showOverflowTooltip: true },
  {
    field: 'poolType', label: '类型', width: 90, type: 'tag',
    tags: { 1: { text: '内部', type: 'primary' }, 2: { text: '外部', type: 'info' } },
  },
  { field: 'availability', label: '可投入度', width: 110 },
  {
    field: 'status', label: '状态', width: 90, type: 'tag',
    tags: { 1: { text: '可用', type: 'success' }, 2: { text: '已占用', type: 'warning' } },
  },
  { field: 'remark', label: '备注', minWidth: 160, showOverflowTooltip: true },
]

const searchFields: SearchFieldDef[] = [
  { field: 'name', label: '姓名', type: 'input' },
  {
    field: 'poolType', label: '类型', type: 'select',
    options: [
      { label: '内部', value: 1 },
      { label: '外部', value: 2 },
    ],
  },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '可用', value: 1 },
      { label: '已占用', value: 2 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'name', label: '姓名', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'position', label: '岗位' },
  { field: 'skill', label: '技能', placeholder: '如：前端开发/架构设计' },
  {
    field: 'poolType', label: '类型', type: 'select', required: true,
    options: [
      { label: '内部', value: 1 },
      { label: '外部', value: 2 },
    ],
  },
  { field: 'availability', label: '可投入度', placeholder: '如：可投入 40%' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '可用', value: 1 },
      { label: '已占用', value: 2 },
    ],
  },
  { field: 'remark', label: '备注', type: 'textarea' },
]
</script>

<template>
  <CrudPage
    title="人力资源池"
    api-prefix="/talent/pools"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
  />
</template>
