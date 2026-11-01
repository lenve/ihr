<script setup lang="ts">
/**
 * 通知公告：自定义页面（CrudPage 无法承载「查看抽屉」行操作）。
 * 列表 + 查看（抽屉，content 保留换行）+ 发布/下线 + 新增/编辑/删除。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive<Record<string, any>>({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  noticeType: undefined,
  status: undefined,
})

const noticeTypeTags: Record<number, { text: string; type: string }> = {
  1: { text: '通知', type: 'primary' },
  2: { text: '公告', type: 'success' },
  3: { text: '制度', type: 'warning' },
}

const statusTags: Record<number, { text: string; type: string }> = {
  0: { text: '草稿', type: 'info' },
  1: { text: '已发布', type: 'success' },
  2: { text: '已下线', type: 'danger' },
}

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    if (query.keyword) params.keyword = query.keyword
    if (query.noticeType !== undefined && query.noticeType !== null && query.noticeType !== '') params.noticeType = query.noticeType
    if (query.status !== undefined && query.status !== null && query.status !== '') params.status = query.status
    const data = await request.get<any, any>('/hrservice/notices', { params })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  query.keyword = ''
  query.noticeType = undefined
  query.status = undefined
  query.pageNum = 1
  load()
}

function fmtTime(v: any): string {
  if (!v) return '-'
  return String(v).replace('T', ' ').slice(0, 16)
}

// ---------- 查看抽屉 ----------
const drawerVisible = ref(false)
const current = ref<any>({})

function handleView(row: any) {
  current.value = row
  drawerVisible.value = true
}

// ---------- 新增/编辑 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)
const form = reactive<Record<string, any>>({})

function handleAdd() {
  editingId.value = null
  Object.keys(form).forEach((k) => delete form[k])
  form.title = ''
  form.noticeType = 1
  form.publisher = ''
  form.status = 0
  form.content = ''
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.keys(form).forEach((k) => delete form[k])
  form.title = row.title ?? ''
  form.noticeType = row.noticeType ?? 1
  form.publisher = row.publisher ?? ''
  form.status = row.status ?? 0
  form.content = row.content ?? ''
  dialogVisible.value = true
}

async function submitForm() {
  if (!form.title) {
    ElMessage.warning('请填写「标题」')
    return
  }
  saving.value = true
  try {
    if (editingId.value == null) {
      await request.post('/hrservice/notices', { ...form })
      ElMessage.success('新增成功')
    } else {
      await request.put(`/hrservice/notices/${editingId.value}`, { ...form })
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该通知公告吗？', '提示', { type: 'warning' })
  await request.delete(`/hrservice/notices/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

async function handlePublish(row: any) {
  await ElMessageBox.confirm('确认发布该通知公告？', '提示', { type: 'warning' })
  await request.put(`/hrservice/notices/${row.id}/publish`)
  ElMessage.success('发布成功')
  load()
}

async function handleOffline(row: any) {
  await ElMessageBox.confirm('确认下线该通知公告？', '提示', { type: 'warning' })
  await request.put(`/hrservice/notices/${row.id}/offline`)
  ElMessage.success('已下线')
  load()
}

onMounted(load)
</script>

<template>
  <div class="notice-page">
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
          <el-select v-model="query.noticeType" placeholder="类型" clearable class="search-select">
            <el-option label="通知" :value="1" />
            <el-option label="公告" :value="2" />
            <el-option label="制度" :value="3" />
          </el-select>
          <el-select v-model="query.status" placeholder="状态" clearable class="search-select">
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
            <el-option label="已下线" :value="2" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column label="类型" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(noticeTypeTags[row.noticeType]?.type as any) || 'info'">
              {{ noticeTypeTags[row.noticeType]?.text ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="发布人" width="110" />
        <el-table-column label="发布时间" width="160" align="center">
          <template #default="{ row }">{{ fmtTime(row.publishTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(statusTags[row.status]?.type as any) || 'info'">
              {{ statusTags[row.status]?.text ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 0" link type="success" size="small" @click="handlePublish(row)">发布</el-button>
            <el-button v-if="row.status === 1" link type="warning" size="small" @click="handleOffline(row)">下线</el-button>
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

    <!-- 查看抽屉 -->
    <el-drawer v-model="drawerVisible" :title="current.title || '通知公告详情'" size="480px">
      <div class="drawer-meta">
        <el-tag :type="(noticeTypeTags[current.noticeType]?.type as any) || 'info'" class="meta-tag">
          {{ noticeTypeTags[current.noticeType]?.text ?? '-' }}
        </el-tag>
        <el-tag :type="(statusTags[current.status]?.type as any) || 'info'">
          {{ statusTags[current.status]?.text ?? '-' }}
        </el-tag>
        <span class="meta-text">发布人：{{ current.publisher || '-' }}</span>
        <span class="meta-text">发布时间：{{ fmtTime(current.publishTime) }}</span>
      </div>
      <div class="drawer-content">{{ current.content || '（无内容）' }}</div>
    </el-drawer>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editingId == null ? '新增通知公告' : '修改通知公告'" width="640px" destroy-on-close>
      <el-form label-width="90px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="类型" required>
          <el-select v-model="form.noticeType" style="width: 100%">
            <el-option label="通知" :value="1" />
            <el-option label="公告" :value="2" />
            <el-option label="制度" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布人">
          <el-input v-model="form.publisher" placeholder="请输入发布人" />
        </el-form-item>
        <el-form-item label="状态" required>
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入内容（支持换行）" />
        </el-form-item>
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
  width: 130px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
.drawer-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}
.meta-tag {
  margin-right: 0;
}
.meta-text {
  color: #909399;
  font-size: 13px;
}
.drawer-content {
  white-space: pre-line;
  line-height: 1.8;
  font-size: 14px;
  color: #303133;
}
</style>
