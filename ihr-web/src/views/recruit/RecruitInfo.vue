<script setup lang="ts">
/**
 * 招聘信息页。
 * 因需要展示「薪资下限-上限」合并列（CrudPage 不支持单列格式化且禁止修改），采用自实现页面，交互与 CrudPage 保持一致。
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

const statusTags: Record<number, { text: string; type: 'success' | 'warning' | 'info' }> = {
  0: { text: '招聘中', type: 'success' },
  1: { text: '暂停', type: 'warning' },
  2: { text: '关闭', type: 'info' },
}

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    if (query.keyword) params.keyword = query.keyword
    if (query.status !== undefined && query.status !== null) params.status = query.status
    const data = await request.get<any, any>('/recruit/positions', { params })
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

function datetimeText(v: any): string {
  return v ? String(v).replace('T', ' ').slice(0, 16) : '-'
}

function salaryText(row: any): string {
  if (row.salaryMin == null && row.salaryMax == null) return '-'
  return `${row.salaryMin ?? '-'}-${row.salaryMax ?? '-'}`
}

// ---------- 表单 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)

const form = reactive({
  title: '',
  deptName: '',
  headcount: 1,
  salaryMin: undefined as number | undefined,
  salaryMax: undefined as number | undefined,
  education: '',
  experience: '',
  status: 0,
  publisher: '',
  publishTime: '',
  jobDesc: '',
  requirement: '',
})

function handleAdd() {
  editingId.value = null
  Object.assign(form, {
    title: '', deptName: '', headcount: 1, salaryMin: undefined, salaryMax: undefined,
    education: '', experience: '', status: 0, publisher: '', publishTime: '', jobDesc: '', requirement: '',
  })
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.assign(form, {
    title: row.title ?? '',
    deptName: row.deptName ?? '',
    headcount: row.headcount ?? 1,
    salaryMin: row.salaryMin ?? undefined,
    salaryMax: row.salaryMax ?? undefined,
    education: row.education ?? '',
    experience: row.experience ?? '',
    status: row.status ?? 0,
    publisher: row.publisher ?? '',
    publishTime: row.publishTime ? String(row.publishTime).slice(0, 19) : '',
    jobDesc: row.jobDesc ?? '',
    requirement: row.requirement ?? '',
  })
  dialogVisible.value = true
}

async function submitForm() {
  if (!form.title) {
    ElMessage.warning('请填写「职位名称」')
    return
  }
  saving.value = true
  try {
    if (editingId.value == null) {
      await request.post('/recruit/positions', { ...form })
      ElMessage.success('新增成功')
    } else {
      await request.put(`/recruit/positions/${editingId.value}`, { ...form })
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该招聘职位吗？', '提示', { type: 'warning' })
  await request.delete(`/recruit/positions/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<template>
  <div class="recruit-info-page">
    <el-card shadow="never">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-input
            v-model="query.keyword"
            placeholder="搜索职位名称"
            clearable
            class="search-input"
            @keyup.enter="query.pageNum = 1; load()"
          />
          <el-select v-model="query.status" placeholder="状态" clearable class="search-select">
            <el-option label="招聘中" :value="0" />
            <el-option label="暂停" :value="1" />
            <el-option label="关闭" :value="2" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="职位名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="deptName" label="部门" width="100" show-overflow-tooltip>
          <template #default="{ row }">{{ text(row.deptName) }}</template>
        </el-table-column>
        <el-table-column prop="headcount" label="人数" width="70" align="center" />
        <el-table-column label="薪资" width="130" align="center">
          <template #default="{ row }">{{ salaryText(row) }}</template>
        </el-table-column>
        <el-table-column prop="education" label="学历" width="80" align="center">
          <template #default="{ row }">{{ text(row.education) }}</template>
        </el-table-column>
        <el-table-column prop="experience" label="经验" width="90" align="center">
          <template #default="{ row }">{{ text(row.experience) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTags[row.status]?.type || 'info'">
              {{ statusTags[row.status]?.text ?? text(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center">
          <template #default="{ row }">{{ datetimeText(row.publishTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right" align="center">
          <template #default="{ row }">
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
    <el-dialog v-model="dialogVisible" :title="editingId == null ? '新增招聘职位' : '修改招聘职位'" width="680px" destroy-on-close>
      <el-form label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="职位名称" required>
              <el-input v-model="form.title" placeholder="请输入职位名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门">
              <el-input v-model="form.deptName" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘人数">
              <el-input-number v-model="form.headcount" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资下限">
              <el-input-number v-model="form.salaryMin" :min="0" :step="1000" style="width: 100%" placeholder="元/月" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资上限">
              <el-input-number v-model="form.salaryMax" :min="0" :step="1000" style="width: 100%" placeholder="元/月" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历">
              <el-select v-model="form.education" clearable style="width: 100%">
                <el-option label="不限" value="不限" />
                <el-option label="大专" value="大专" />
                <el-option label="本科" value="本科" />
                <el-option label="硕士" value="硕士" />
                <el-option label="博士" value="博士" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经验要求">
              <el-input v-model="form.experience" placeholder="如：3-5年" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="招聘中" :value="0" />
                <el-option label="暂停" :value="1" />
                <el-option label="关闭" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发布人">
              <el-input v-model="form.publisher" placeholder="留空自动填充当前用户" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="职位描述">
              <el-input v-model="form.jobDesc" type="textarea" :rows="3" placeholder="请输入职位描述" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任职要求">
              <el-input v-model="form.requirement" type="textarea" :rows="3" placeholder="请输入任职要求" />
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
