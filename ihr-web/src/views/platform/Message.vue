<script setup lang="ts">
/**
 * 信息收发平台：收件箱/已发送双 Tab + 写消息 + 标记已读/删除 + 未读角标。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

const activeTab = ref('inbox')
const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10 })
const unread = ref(0)

async function load() {
  loading.value = true
  try {
    const data = await request.get<any, any>('/platform/messages', {
      params: { pageNum: query.pageNum, pageSize: query.pageSize, box: activeTab.value },
    })
    records.value = data?.records || []
    total.value = Number(data?.total || 0)
  } finally {
    loading.value = false
  }
}

async function loadUnread() {
  try {
    unread.value = Number(await request.get<any, any>('/platform/messages/unread-count')) || 0
  } catch {
    unread.value = 0
  }
}

function onTabChange(tab: string | number) {
  activeTab.value = String(tab)
  query.pageNum = 1
  load()
}

/** 未读消息整行加粗 */
function rowClassName({ row }: { row: any }): string {
  return activeTab.value === 'inbox' && row.readFlag === 0 ? 'unread-row' : ''
}

function fmtTime(v: any): string {
  if (!v) return '-'
  return String(v).replace('T', ' ').slice(0, 16)
}

const readTags: Record<number, { text: string; type: string }> = {
  0: { text: '未读', type: 'danger' },
  1: { text: '已读', type: 'info' },
}

// ---------- 查看抽屉 ----------
const drawerVisible = ref(false)
const current = ref<any>({})

async function handleView(row: any) {
  current.value = row
  drawerVisible.value = true
  // 收件箱中查看未读消息时自动标记已读
  if (activeTab.value === 'inbox' && row.readFlag === 0) {
    try {
      await request.put(`/platform/messages/${row.id}/read`)
      row.readFlag = 1
      loadUnread()
    } catch {
      /* 忽略标记失败，不影响查看 */
    }
  }
}

async function handleMarkRead(row: any) {
  await request.put(`/platform/messages/${row.id}/read`)
  ElMessage.success('已标记为已读')
  row.readFlag = 1
  loadUnread()
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm('确认删除该消息吗？', '提示', { type: 'warning' })
  await request.delete(`/platform/messages/${row.id}`)
  ElMessage.success('删除成功')
  load()
  loadUnread()
}

// ---------- 写消息 ----------
const dialogVisible = ref(false)
const sending = ref(false)
const form = reactive({ receiverName: '', title: '', content: '' })

function openSend() {
  form.receiverName = ''
  form.title = ''
  form.content = ''
  dialogVisible.value = true
}

async function submitSend() {
  if (!form.receiverName.trim()) {
    ElMessage.warning('请填写「收件人」')
    return
  }
  if (!form.title.trim()) {
    ElMessage.warning('请填写「标题」')
    return
  }
  sending.value = true
  try {
    await request.post('/platform/messages/send', { ...form })
    ElMessage.success('发送成功')
    dialogVisible.value = false
    if (activeTab.value === 'sent') {
      query.pageNum = 1
      load()
    }
  } finally {
    sending.value = false
  }
}

onMounted(() => {
  load()
  loadUnread()
})
</script>

<template>
  <div class="message-page">
    <el-card shadow="never">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-tabs v-model="activeTab" class="message-tabs" @tab-change="onTabChange">
          <el-tab-pane name="inbox">
            <template #label>
              收件箱<el-badge v-if="unread > 0" :value="unread" class="tab-badge" />
            </template>
          </el-tab-pane>
          <el-tab-pane label="已发送" name="sent" />
        </el-tabs>
        <div class="toolbar-right">
          <el-button :icon="Refresh" @click="() => { load(); loadUnread() }">刷新</el-button>
          <el-button type="primary" :icon="Edit" @click="openSend">写消息</el-button>
        </div>
      </div>

      <!-- 收件箱 -->
      <el-table
        v-if="activeTab === 'inbox'"
        v-loading="loading"
        :data="records"
        border
        stripe
        :row-class-name="rowClassName"
      >
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="senderName" label="发件人" width="130" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column label="时间" width="170" align="center">
          <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="(readTags[row.readFlag]?.type as any) || 'info'">
              {{ readTags[row.readFlag]?.text ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.readFlag !== 1" link type="success" size="small" @click="handleMarkRead(row)">标为已读</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 已发送 -->
      <el-table v-else v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="receiverName" label="收件人" width="130" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column label="时间" width="170" align="center">
          <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
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
    <el-drawer v-model="drawerVisible" :title="current.title || '消息详情'" size="440px">
      <div class="drawer-meta">
        <span class="meta-text">发件人：{{ current.senderName || '-' }}</span>
        <span class="meta-text">收件人：{{ current.receiverName || '-' }}</span>
        <span class="meta-text">时间：{{ fmtTime(current.createTime) }}</span>
      </div>
      <div class="drawer-content">{{ current.content || '（无内容）' }}</div>
    </el-drawer>

    <!-- 写消息弹窗 -->
    <el-dialog v-model="dialogVisible" title="写消息" width="560px" destroy-on-close>
      <el-form label-width="80px">
        <el-form-item label="收件人" required>
          <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" maxlength="50" />
        </el-form-item>
        <el-form-item label="标题" required>
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入消息内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="sending" @click="submitSend">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.message-tabs {
  flex: 1;
}
.message-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.tab-badge {
  margin-left: 6px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
:deep(.unread-row) td {
  font-weight: 600;
}
.drawer-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
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
