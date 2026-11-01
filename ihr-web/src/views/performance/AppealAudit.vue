<script setup lang="ts">
/**
 * 申诉审核：全部申诉列表 + 审核弹窗（已受理/已驳回/已调整 + 意见）。
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { fmtTime } from './perf-shared'

const APPEAL_STATUS: Record<number, { text: string; type: any }> = {
  0: { text: '申诉中', type: 'warning' },
  1: { text: '已受理', type: 'primary' },
  2: { text: '已驳回', type: 'danger' },
  3: { text: '已调整', type: 'success' },
}

const loading = ref(false)
const records = ref<any[]>([])
const total = ref(0)
const query = reactive({ pageNum: 1, pageSize: 10, keyword: '', status: undefined as number | undefined })

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: query.pageNum, pageSize: query.pageSize }
    if (query.keyword) params.keyword = query.keyword
    if (query.status !== undefined && query.status !== null) params.status = query.status
    const data = await request.get<any, any>('/performance/appeals', { params })
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

// ---------- 审核弹窗 ----------
const dialogVisible = ref(false)
const saving = ref(false)
const currentRow = ref<any>(null)
const form = reactive<Record<string, any>>({})

function openAudit(row: any) {
  currentRow.value = row
  Object.keys(form).forEach((k) => delete form[k])
  Object.assign(form, { status: 1, auditComment: '' })
  dialogVisible.value = true
}

async function submitAudit() {
  if (!form.status) {
    ElMessage.warning('请选择审核结果')
    return
  }
  saving.value = true
  try {
    await request.put(`/performance/appeals/${currentRow.value.id}/audit`, {
      status: form.status,
      auditComment: form.auditComment,
    })
    ElMessage.success('审核完成')
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input
          v-model="query.keyword"
          placeholder="搜索申诉人姓名"
          clearable
          class="search-input"
          @keyup.enter="query.pageNum = 1; load()"
        />
        <el-select v-model="query.status" placeholder="申诉状态" clearable class="search-select">
          <el-option
            v-for="(v, k) in APPEAL_STATUS"
            :key="k"
            :label="v.text"
            :value="Number(k)"
          />
        </el-select>
        <el-button type="primary" :icon="Search" @click="query.pageNum = 1; load()">查询</el-button>
        <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="empName" label="申诉人" width="100" align="center" />
      <el-table-column prop="appraisalId" label="考核 ID" width="90" align="center" />
      <el-table-column prop="reason" label="申诉理由" min-width="170" show-overflow-tooltip />
      <el-table-column prop="expect" label="期望结果" min-width="130" show-overflow-tooltip />
      <el-table-column label="状态" width="95" align="center">
        <template #default="{ row }">
          <el-tag :type="APPEAL_STATUS[row.status]?.type || 'info'">
            {{ APPEAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditComment" label="审核意见" min-width="130" show-overflow-tooltip />
      <el-table-column label="申请时间" width="160" align="center">
        <template #default="{ row }">{{ fmtTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="90" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openAudit(row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

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

  <el-dialog v-model="dialogVisible" title="申诉审核" width="560px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="申诉人">
        <el-input :model-value="currentRow?.empName" disabled />
      </el-form-item>
      <el-form-item label="申诉理由">
        <el-input :model-value="currentRow?.reason" type="textarea" :rows="3" disabled />
      </el-form-item>
      <el-form-item label="审核结果" required>
        <el-select v-model="form.status" style="width: 100%">
          <el-option label="已受理" :value="1" />
          <el-option label="已驳回" :value="2" />
          <el-option label="已调整" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核意见">
        <el-input v-model="form.auditComment" type="textarea" :rows="3" placeholder="处理意见说明" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="submitAudit">提交审核</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.toolbar-left {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.search-input {
  width: 200px;
}
.search-select {
  width: 140px;
}
.pagination {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
