<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'empName', label: '姓名', width: 100 },
  { field: 'deptName', label: '部门', width: 100 },
  { field: 'courseName', label: '课程', minWidth: 160 },
  { field: 'category', label: '分类', width: 90 },
  { field: 'trainer', label: '讲师', width: 100 },
  { field: 'startDate', label: '开始日期', width: 110, type: 'date' },
  { field: 'endDate', label: '结束日期', width: 110, type: 'date' },
  { field: 'hours', label: '学时', width: 80 },
  { field: 'score', label: '成绩', width: 80 },
  { field: 'certificate', label: '证书', minWidth: 150 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'empName', label: '姓名/课程', type: 'input' },
]

const formFields: FormFieldDef[] = [
  { field: 'empName', label: '姓名', required: true },
  { field: 'deptName', label: '部门' },
  { field: 'courseName', label: '课程', required: true },
  { field: 'category', label: '分类', placeholder: '如：技术/合规/管理' },
  { field: 'trainer', label: '讲师' },
  { field: 'startDate', label: '开始日期', type: 'date' },
  { field: 'endDate', label: '结束日期', type: 'date' },
  { field: 'hours', label: '学时', type: 'number' },
  { field: 'score', label: '成绩', type: 'number' },
  { field: 'certificate', label: '证书' },
]
</script>

<template>
  <CrudPage
    title="培训档案"
    api-prefix="/hrservice/trainings"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
  />
</template>
