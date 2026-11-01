<script setup lang="ts">
import CrudPage, { type ColumnDef, type FormFieldDef, type RowAction, type SearchFieldDef } from '@/components/CrudPage.vue'

const columns: ColumnDef[] = [
  { field: 'plateNo', label: '车牌', width: 110 },
  { field: 'driverName', label: '驾驶人', width: 100 },
  { field: 'violateTime', label: '违规时间', width: 160, type: 'datetime' },
  { field: 'violateType', label: '违规类型', width: 140 },
  { field: 'location', label: '地点', minWidth: 160 },
  { field: 'fine', label: '罚款(元)', width: 100, type: 'money' },
  { field: 'points', label: '扣分', width: 80 },
  {
    field: 'deductStatus', label: '状态', width: 90, type: 'tag',
    tags: { 0: { text: '未处理', type: 'warning' }, 1: { text: '已处理', type: 'success' } },
  },
  { field: 'handleRemark', label: '处理备注', minWidth: 140 },
]

const searchFields: SearchFieldDef[] = [
  { field: 'plateNo', label: '车牌', type: 'input' },
  {
    field: 'deductStatus', label: '状态', type: 'select',
    options: [
      { label: '未处理', value: 0 },
      { label: '已处理', value: 1 },
    ],
  },
]

const formFields: FormFieldDef[] = [
  { field: 'plateNo', label: '车牌', required: true },
  { field: 'driverName', label: '驾驶人' },
  { field: 'violateTime', label: '违规时间', type: 'datetime' },
  { field: 'violateType', label: '违规类型' },
  { field: 'location', label: '地点' },
  { field: 'fine', label: '罚款(元)', type: 'number' },
  { field: 'points', label: '扣分', type: 'number' },
  {
    field: 'deductStatus', label: '状态', type: 'select', required: true,
    options: [
      { label: '未处理', value: 0 },
      { label: '已处理', value: 1 },
    ],
  },
  { field: 'handleRemark', label: '处理备注', type: 'textarea' },
]

const rowActions: RowAction[] = [
  {
    label: '处理',
    method: 'put',
    urlTemplate: '/hrservice/vehicle-violations/{id}/handle',
    confirm: '确认将该违规记录标记为已处理？',
    hide: (row: any) => row.deductStatus === 1,
  },
]
</script>

<template>
  <CrudPage
    title="车辆违规"
    api-prefix="/hrservice/vehicle-violations"
    :columns="columns"
    :search-fields="searchFields"
    :form-fields="formFields"
    :row-actions="rowActions"
  />
</template>
