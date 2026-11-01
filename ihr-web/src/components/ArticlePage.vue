<script setup lang="ts">
/**
 * 内容文章通用页面：政策解读（policy）/ 办事指南（guide）/ 出入境指南（exit_entry）复用。
 * 列表 + 查看抽屉（content 保留换行）+ 管理 CRUD。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const props = defineProps<{
  category: string
  title: string
}>()

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive<Record<string, any>>({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: undefined,
})

const statusTags: Record<number, { text: string; type: string }> = {
  0: { text: '下线', type: 'info' },
  1: { text: '上线', type: 'success' },
}

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      category: props.category,
    }
    if (query.keyword) params.keyword = query.keyword
    if (query.status !== undefined && query.status !== null && query.status !== '') params.status = query.status
    const data = await request.get<any, any>('/hrservice/articles', { params })
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
  form.summary = ''
  form.author = ''
  form.status = 1
  form.publishTime = ''
  form.content = ''
  dialogVisible.value = true
}

function handleEdit(row: any) {
  editingId.value = row.id
  Object.keys(form).forEach((k) => delete form[k])
  form.title = row.title ?? ''
  form.summary = row.summary ?? ''
  form.author = row.author ?? ''
  form.status = row.status ?? 1
  form.publishTime = row.publishTime ? String(row.publishTime).slice(0, 19) : ''
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
    const body: Record<string, any> = {
      category: props.category,
      title: form.title,
      summary: form.summary,
      author: form.author,
      status: form.status,
      publishTime: form.publishTime || null,
      content: form.content,
    }
    if (editingId.value == null) {
      await request.post('/hrservice/articles', body)
      ElMessage.success('新增成功')
    } else {
      await request.put(`/hrservice/articles/${editingId.value}`, body)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该文章吗？', '提示', { type: 'warning' })
  await request.delete(`/hrservice/articles/${row.id}`)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>

<template>
  <div class="article-page">
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
            <el-option label="上线" :value="1" />
            <el-option label="下线" :value="0" />
          </el-select>
          <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </div>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="summary" label="摘要" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="100" />
        <el-table-column label="发布时间" width="160" align="center">
          <template #default="{ row }">{{ fmtTime(row.publishTime) }}</template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="90" align="center" />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(statusTags[row.status]?.type as any) || 'info'">
              {{ statusTags[row.status]?.text ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
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
    <el-drawer v-model="drawerVisible" :title="current.title || '文章详情'" size="480px">
      <div class="drawer-meta">
        <span class="meta-text">作者：{{ current.author || '-' }}</span>
        <span class="meta-text">发布时间：{{ fmtTime(current.publishTime) }}</span>
        <span class="meta-text">浏览量：{{ current.viewCount ?? 0 }}</span>
      </div>
      <div v-if="current.summary" class="drawer-summary">{{ current.summary }}</div>
      <div class="drawer-content">{{ current.content || '（无内容）' }}</div>
    </el-drawer>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="editingId == null ? `新增${props.title}` : `修改${props.title}`" width="640px" destroy-on-close>
      <el-form label-width="90px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="请输入摘要" maxlength="300" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" placeholder="请输入作者/部门" />
        </el-form-item>
        <el-form-item label="发布时间">
          <el-date-picker
            v-model="form.publishTime"
            type="datetime"
            value-format="YYYY-MM-DDTHH:mm:ss"
            placeholder="选择发布时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" required>
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="上线" :value="1" />
            <el-option label="下线" :value="0" />
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
  gap: 12px;
  margin-bottom: 12px;
}
.meta-text {
  color: #909399;
  font-size: 13px;
}
.drawer-summary {
  color: #606266;
  font-size: 13px;
  background: #f5f7fa;
  border-radius: 4px;
  padding: 10px 12px;
  margin-bottom: 14px;
}
.drawer-content {
  white-space: pre-line;
  line-height: 1.8;
  font-size: 14px;
  color: #303133;
}
</style>
