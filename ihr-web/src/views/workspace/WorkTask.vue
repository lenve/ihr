<script setup lang="ts">
/**
 * 工作任务页。
 * 因需要展示「进度 xx%」等自定义列格式（CrudPage 不支持单列格式化且禁止修改），采用自实现页面，交互与 CrudPage 保持一致。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: undefined as number | undefined,
})

const priorityTags: Record<number, { text: string; type: 'danger' | 'warning' | 'info' }> = {
  1: { text: '高', type: 'danger' },
  2: { text: '中', type: 'warning' },
  3: { text: '低', type: 'info' },
}

const statusTags: Record<number, { text: string; type: 'warning' | 'success' | 'info' }> = {
  0: { text: '进行中', type: 'warning' },
  1: { text: '已完成', type: 'success' },
  2: { text: '已取消', type: 'info' },
}

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    if (query.keyword) params.keyword = query.keyword
    if (query.status !== undefined && query.status !== null) params.status = query.status
    const data = await request.get<any, any>('/workbench/tasks', { params })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  query.keyword = ''
  query.status = undefined
  query.pageNum = 1
  load()
}

function text(v: any): string {
  return v === null || v === undefined || v === '' ? '-' : String(v)
}

function dateText(v: any): string {
  return v ? String(v).slice(0, 10) : '-'
}

// ---------- 表单 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)

const form = reactive({
  title: '',
  description: '',
  assignerName: '',
  assigneeName: '',
  priority: 2,
  deadline: '',
  status: 0,
})

function handleAdd() {
  editingId.value = null
  Object.assign(form, { title: '', description: '', assignerName: '', assigneeName: '', priority: 2, deadline: '', status: 0 })
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.assign(form, {
    title: row.title ?? '',
    description: row.description ?? '',
    assignerName: row.assignerName ?? '',
    assigneeName: row.assigneeName ?? '',
    priority: row.priority ?? 2,
    deadline: row.deadline ? String(row.deadline).slice(0, 10) : '',
    status: row.status ?? 0,
  })
  dialogVisible.value = true
}

async function submitForm() {
  if (!form.title) {
    ElMessage.warning('请填写「标题」')
    return
  }
  if (!form.assigneeName) {
    ElMessage.warning('请填写「负责人」')
    return
  }
  saving.value = true
  try {
    if (editingId.value == null) {
      await request.post('/workbench/tasks', { ...form })
      ElMessage.success('新增成功')
    } else {
      await request.put(`/workbench/tasks/${editingId.value}`, { ...form })
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该任务吗？', '提示', { type: 'warning' })
  await request.delete(`/workbench/tasks/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

async function handleComplete(row: any) {
  await ElMessageBox.confirm('确认将该任务标记为完成？', '提示', { type: 'warning' })
  await request.put(`/workbench/tasks/${row.id}/complete`)
  ElMessage.success('操作成功')
  load()
}

onMounted(load)
</script>

<template>
  <div class="work-task-page">
    <el-card shadow="never">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-input
            v-model="query.keyword"
            placeholder="搜索标题"
            clearable
            class="search-input"
            @keyup.enter="query.pageNum = 1; load()"
          />
          <el-select v-model="query.status" placeholder="状态" clearable class="search-select">
            <el-option label="进行中" :value="0" />
            <el-option label="已完成" :value="1" />
            <el-option label="已取消" :value="2" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="assignerName" label="派发人" width="100" show-overflow-tooltip>
          <template #default="{ row }">{{ text(row.assignerName) }}</template>
        </el-table-column>
        <el-table-column prop="assigneeName" label="负责人" width="100" show-overflow-tooltip />
        <el-table-column prop="priority" label="优先级" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="priorityTags[row.priority]?.type || 'info'">
              {{ priorityTags[row.priority]?.text ?? text(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="140" align="center">
          <template #default="{ row }">{{ row.progress ?? 0 }}%</template>
        </el-table-column>
        <el-table-column prop="deadline" label="截止日期" width="110" align="center">
          <template #default="{ row }">{{ dateText(row.deadline) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTags[row.status]?.type || 'info'">
              {{ statusTags[row.status]?.text ?? text(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="primary" size="small" @click="handleComplete(row)">完成</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="editingId == null ? '新增工作任务' : '修改工作任务'" width="640px" destroy-on-close>
      <el-form label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="标题" required>
              <el-input v-model="form.title" placeholder="请输入任务标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="派发人">
              <el-input v-model="form.assignerName" placeholder="请输入派发人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" required>
              <el-input v-model="form.assigneeName" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级">
              <el-select v-model="form.priority" style="width: 100%">
                <el-option label="高" :value="1" />
                <el-option label="中" :value="2" />
                <el-option label="低" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止日期">
              <el-date-picker v-model="form.deadline" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="进行中" :value="0" />
                <el-option label="已完成" :value="1" />
                <el-option label="已取消" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入任务描述" />
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
