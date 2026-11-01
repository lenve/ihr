<script setup lang="ts">
/**
 * 上级评价：列出本人作为考核人、处于"上级评价"状态的考核，复用 ScoreDialog 评分；
 * 下方展示本人已评价完成（含总分/等级）的考核。
 */
import { computed, onMounted, ref } from 'vue'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPRAISAL_STATUS, GRADE_TAG, currentUserName } from './perf-shared'
import ScoreDialog from './components/ScoreDialog.vue'

const loading = ref(false)
const pending = ref<any[]>([])
const evaluated = ref<any[]>([])

async function load() {
  loading.value = true
  try {
    const data = await request.get<any, any>('/performance/appraisals', {
      params: { pageNum: 1, pageSize: 100 },
    })
    const name = currentUserName()
    const all = (data?.records || []).filter((r: any) => !name || r.superiorName === name)
    pending.value = all.filter((r: any) => r.status === 3)
    evaluated.value = all.filter((r: any) => r.status >= 4 && r.superiorScore != null)
  } finally {
    loading.value = false
  }
}

const scoreDialogVisible = ref(false)
const currentAppraisal = ref<any>(null)

function openScore(row: any) {
  currentAppraisal.value = row
  scoreDialogVisible.value = true
}

const finished = computed(() => evaluated.value)

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <span class="tip">待我评价的考核（状态：上级评价）</span>
      <el-button :icon="Refresh" @click="load">刷新</el-button>
    </div>

    <el-table v-loading="loading" :data="pending" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="考核周期" width="100" align="center" />
      <el-table-column prop="empName" label="被考核人" width="100" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="120" />
      <el-table-column prop="selfScore" label="自评总分" width="90" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="APPRAISAL_STATUS[row.status]?.type || 'info'">
            {{ APPRAISAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openScore(row)">评分</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <ScoreDialog v-model="scoreDialogVisible" :appraisal="currentAppraisal" @done="load" />

  <el-card shadow="never" class="history">
    <template #header>我已完成的评价（含总分与等级）</template>
    <el-table :data="finished" border stripe size="small">
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="period" label="周期" width="100" align="center" />
      <el-table-column prop="empName" label="被考核人" width="100" align="center" />
      <el-table-column prop="selfScore" label="自评分" width="90" align="center" />
      <el-table-column prop="superiorScore" label="上级评分" width="90" align="center" />
      <el-table-column prop="totalScore" label="综合分" width="90" align="center" />
      <el-table-column label="等级" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.grade" :type="GRADE_TAG[row.grade] || 'info'">{{ row.grade }}</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.tip {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
.history {
  margin-top: 14px;
}
</style>
