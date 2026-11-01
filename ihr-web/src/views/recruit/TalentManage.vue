<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'name', label: '姓名', width: 90 },
  { field: 'gender', label: '性别', width: 70 },
  { field: 'phone', label: '电话', width: 130 },
  { field: 'source', label: '来源', width: 100 },
  { field: 'applyPosition', label: '应聘职位', width: 140, showOverflowTooltip: true },
  { field: 'education', label: '学历', width: 80 },
  { field: 'workYears', label: '工作年限', width: 90 },
  {
    field: 'evaluation', label: '评级', width: 80, type: 'tag',
    tags: {
      S: { text: 'S', type: 'danger' },
      A: { text: 'A', type: 'success' },
      B: { text: 'B', type: 'warning' },
      C: { text: 'C', type: 'info' },
    },
  },
  {
    field: 'status', label: '状态', width: 90, type: 'tag',
    tags: {
      0: { text: '待联系', type: 'info' },
      1: { text: '面试中', type: 'warning' },
      2: { text: '已录用', type: 'success' },
      3: { text: '已淘汰', type: 'danger' },
    },
  },
  { field: 'skill', label: '技能', minWidth: 180, showOverflowTooltip: true },
]

const searchFields: SearchFieldDef[] = [
  { field: 'name', label: '姓名', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '待联系', value: 0 },
      { label: '面试中', value: 1 },
      { label: '已录用', value: 2 },
      { label: '已淘汰', value: 3 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'name', label: '姓名', required: true },
  {
    field: 'gender', label: '性别', type: 'select',
    options: [{ label: '男', value: '男' }, { label: '女', value: '女' }],
  },
  { field: 'phone', label: '电话' },
  { field: 'email', label: '邮箱' },
  { field: 'source', label: '来源', placeholder: '如：BOSS直聘/内推/猎头' },
  { field: 'applyPosition', label: '应聘职位' },
  {
    field: 'education', label: '学历', type: 'select',
    options: [
      { label: '大专', value: '大专' },
      { label: '本科', value: '本科' },
      { label: '硕士', value: '硕士' },
      { label: '博士', value: '博士' },
    ],
  },
  { field: 'workYears', label: '工作年限', type: 'number' },
  {
    field: 'evaluation', label: '评级', type: 'select',
    options: [
      { label: 'S', value: 'S' },
      { label: 'A', value: 'A' },
      { label: 'B', value: 'B' },
      { label: 'C', value: 'C' },
    ],
  },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '待联系', value: 0 },
      { label: '面试中', value: 1 },
      { label: '已录用', value: 2 },
      { label: '已淘汰', value: 3 },
    ],
  },
  { field: 'skill', label: '技能', placeholder: '如：Java/Spring Cloud/K8s' },
  { field: 'remark', label: '备注', type: 'textarea' },
]
</script>

<template>
  <CrudPage
    title="人才"
    api-prefix="/talent/profiles"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
  />
</template>
