<script setup lang="ts">
/**
 * 通用增删改查页面组件。
 * 约定后端接口：
 * - GET  {apiPrefix}?pageNum=&pageSize=&keyword=...  → IPage { records, total }
 * - POST {apiPrefix}                                  → 新增（body 为实体 JSON）
 * - PUT  {apiPrefix}/{id}                             → 修改
 * - DELETE {apiPrefix}/{id}                           → 删除
 * 行内动作通过 rowActions 声明（如标记完成、审核通过等专用接口）。
 */
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

export interface TagDef {
  text: string
  type?: 'success' | 'info' | 'warning' | 'danger' | 'primary'
}

export interface ColumnDef {
  field: string
  label: string
  width?: number | string
  /** text 默认 | tag 枚举标签 | money 金额 | datetime 时间 | date 日期 */
  type?: 'text' | 'tag' | 'money' | 'datetime' | 'date'
  tags?: Record<string | number, TagDef>
}

export interface FormFieldDef {
  field: string
  label: string
  type?: 'input' | 'textarea' | 'number' | 'select' | 'date' | 'datetime'
  options?: { label: string; value: string | number }[]
  required?: boolean
  placeholder?: string
}

export interface SearchFieldDef {
  field: string
  label: string
  type?: 'input' | 'select'
  options?: { label: string; value: string | number }[]
}

export interface RowAction {
  label: string
  method?: 'put' | 'post'
  /** URL 模板，{id} 会被行数据 id 替换，如 '/workbench/todos/{id}/finish' */
  urlTemplate: string
  confirm?: string
  /** 返回 true 时隐藏该按钮 */
  hide?: (row: any) => boolean
}

const props = withDefaults(
  defineProps<{
    title: string
    apiPrefix: string
    columns: ColumnDef[]
    formFields: FormFieldDef[]
    searchFields?: SearchFieldDef[]
    rowActions?: RowAction[]
    addable?: boolean
    editable?: boolean
    deletable?: boolean
  }>(),
  { searchFields: () => [], rowActions: () => [], addable: true, editable: true, deletable: true },
)

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)

const query = reactive<Record<string, any>>({ pageNum: 1, pageSize: 10 })
for (const f of props.searchFields) query[f.field] = f.type === 'input' ? '' : undefined

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    for (const f of props.searchFields) {
      const v = query[f.field]
      if (v !== undefined && v !== null && v !== '') {
        params[f.type === 'input' ? 'keyword' : f.field] = v
      }
    }
    const data = await request.get<any, any>(props.apiPrefix, { params })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  for (const f of props.searchFields) query[f.field] = f.type === 'input' ? '' : undefined
  query.pageNum = 1
  load()
}

function fmt(row: any, col: ColumnDef): string {
  const v = row[col.field]
  if (v === null || v === undefined || v === '') return '-'
  if (col.type === 'money') return Number(v).toFixed(2)
  const s = String(v)
  if (col.type === 'datetime') return s.replace('T', ' ').slice(0, 16)
  if (col.type === 'date') return s.slice(0, 10)
  return s
}

// ---------- 表单 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const form = reactive<Record<string, any>>({})
const editingId = ref<number | null>(null)

function handleAdd() {
  editingId.value = null
  Object.keys(form).forEach((k) => delete form[k])
  for (const f of props.formFields) form[f.field] = f.type === 'number' ? 0 : f.type === 'select' ? (f.options?.[0]?.value ?? undefined) : ''
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.keys(form).forEach((k) => delete form[k])
  for (const f of props.formFields) {
    form[f.field] = row[f.field] ?? (f.type === 'number' ? 0 : f.type === 'select' ? (f.options?.[0]?.value ?? '') : '')
  }
  dialogVisible.value = true
}

async function submitForm() {
  for (const f of props.formFields) {
    if (f.required && (form[f.field] === '' || form[f.field] === null || form[f.field] === undefined)) {
      ElMessage.warning(`请填写「${f.label}」`)
      return
    }
  }
  saving.value = true
  try {
    if (editingId.value == null) {
      await request.post(props.apiPrefix, { ...form })
      ElMessage.success('新增成功')
    } else {
      await request.put(`${props.apiPrefix}/${editingId.value}`, { ...form })
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm(`确认删除该记录吗？`, '提示', { type: 'warning' })
  await request.delete(`${props.apiPrefix}/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

async function handleRowAction(action: RowAction, row: any) {
  if (action.confirm) {
    await ElMessageBox.confirm(action.confirm, '提示', { type: 'warning' })
  }
  const url = action.urlTemplate.replace('{id}', String(row.id))
  await (action.method === 'post' ? request.post(url) : request.put(url))
  ElMessage.success('操作成功')
  load()
}

const dialogTitle = computed(() => (editingId.value == null ? `新增${props.title}` : `修改${props.title}`))

onMounted(load)
</script>

<template>
  <div class="crud-page">
    <el-card shadow="never">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-input
            v-for="f in searchFields.filter((s) => s.type === 'input')"
            :key="f.field"
            v-model="query[f.field]"
            :placeholder="`搜索${f.label}`"
            clearable
            class="search-input"
            @keyup.enter="query.pageNum = 1; load()"
          />
          <el-select
            v-for="f in searchFields.filter((s) => s.type !== 'input')"
            :key="f.field"
            v-model="query[f.field]"
            :placeholder="f.label"
            clearable
            class="search-select"
          >
            <el-option v-for="o in f.options" :key="o.value" :label="o.label" :value="o.value" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <el-button v-if="addable" type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column
          v-for="col in columns"
          :key="col.field"
          :prop="col.field"
          :label="col.label"
          :width="col.width"
          align="left"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-tag v-if="col.type === 'tag'" :type="col.tags?.[row[col.field]]?.type || 'info'">
              {{ col.tags?.[row[col.field]]?.text ?? row[col.field] }}
            </el-tag>
            <span v-else>{{ fmt(row, col) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="editable || deletable || rowActions.length" label="操作" width="60 + (editable ? 60 : 0) + (deletable ? 60 : 0) + rowActions.length * 90" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              v-for="a in rowActions.filter((x) => !x.hide || !x.hide(row))"
              :key="a.label"
              link type="primary" size="small"
              @click="handleRowAction(a, row)"
            >{{ a.label }}</el-button>
            <el-button v-if="editable" link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="deletable" link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @current-change="load"
        @size-change="query.pageNum = 1; load()"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px" destroy-on-close>
      <el-form label-width="110px">
        <el-row :gutter="16">
          <el-col v-for="f in formFields" :key="f.field" :span="f.type === 'textarea' ? 24 : 12">
            <el-form-item :label="f.label" :required="f.required">
              <el-input v-if="f.type === 'input' || !f.type" v-model="form[f.field]" :placeholder="f.placeholder" />
              <el-input v-else-if="f.type === 'textarea'" v-model="form[f.field]" type="textarea" :rows="3" :placeholder="f.placeholder" />
              <el-input-number v-else-if="f.type === 'number'" v-model="form[f.field]" :min="0" style="width: 100%" />
              <el-select v-else-if="f.type === 'select'" v-model="form[f.field]" style="width: 100%">
                <el-option v-for="o in f.options" :key="o.value" :label="o.label" :value="o.value" />
              </el-select>
              <el-date-picker v-else-if="f.type === 'date'" v-model="form[f.field]" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
              <el-date-picker v-else-if="f.type === 'datetime'" v-model="form[f.field]" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  flex-wrap: wrap;
  gap: 8px;
}
.toolbar-left {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.search-input {
  width: 220px;
}
.search-select {
  width: 150px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
