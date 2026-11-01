<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'empName', label: '姓名', width: 110 },
  { field: 'titleName', label: '职称名称', width: 140 },
  { field: 'titleLevel', label: '级别', width: 100 },
  { field: 'certificateNo', label: '证书编号', width: 150 },
  { field: 'obtainedDate', label: '取得日期', width: 120, type: 'date' },
  { field: 'validityEnd', label: '有效期至', width: 120, type: 'date' },
  {
    field: 'status', label: '状态', width: 100, type: 'tag',
    tags: { 1: { text: '有效', type: 'success' }, 0: { text: '失效', type: 'info' } },
  },
]

const searchFields: SearchFieldDef[] = [
  { field: 'empName', label: '姓名', type: 'input' },
  {
    field: 'status', label: '状态', type: 'select',
    options: [
      { label: '有效', value: 1 },
      { label: '失效', value: 0 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'empName', label: '姓名', required: true },
  { field: 'titleName', label: '职称名称', required: true },
  {
    field: 'titleLevel', label: '级别', type: 'select',
    options: [
      { label: '初级', value: '初级' },
      { label: '中级', value: '中级' },
      { label: '副高', value: '副高' },
      { label: '正高', value: '正高' },
    ],
  },
  { field: 'certificateNo', label: '证书编号' },
  { field: 'obtainedDate', label: '取得日期', type: 'date' },
  { field: 'validityEnd', label: '有效期至', type: 'date' },
  {
    field: 'status', label: '状态', type: 'select', required: true,
    options: [
      { label: '有效', value: 1 },
      { label: '失效', value: 0 },
    ],
  },
]
</script>

<template>
  <!-- 只读感查询页：仅保留删除 -->
  <CrudPage
    title="职称记录"
    api-prefix="/declare/title-records"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :addable="false"
    :editable="false"
  />
</template>
