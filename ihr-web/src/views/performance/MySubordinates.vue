<script setup lang="ts">
/**
 * 我的下属：本人作为考核人的考核总览，可查看考核详情（考核 + 目标 + 面谈记录）。
 */
import { onMounted, reactive, ref } from 'vue'
import { Refresh, Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { APPRAISAL_STATUS, GRADE_TAG, PERIOD_OPTIONS, currentUserName, fmtTime } from './perf-shared'

const loading = ref(false)
const records = ref<any[]>([])
const period = ref<string | undefined>(undefined)

async function load() {
  loading.value = true
  try {
    const params: Record<string, any> = { pageNum: 1, pageSize: 100 }
    if (period.value) params.period = period.value
    const data = await request.get<any, any>('/performance/appraisals', { params })
    const name = currentUserName()
    records.value = (data?.records || []).filter((r: any) => !name || r.superiorName === name)
  } finally {
    loading.value = false
  }
}

// ---------- 详情弹窗 ----------
const detailVisible = ref(false)
const detailLoading = ref(false)
const detail = reactive<Record<string, any>>({})

async function openDetail(row: any) {
  detailVisible.value = true
  detailLoading.value = true
  try {
    const data = await request.get<any, any>(`/performance/appraisals/${row.id}/detail`)
    Object.keys(detail).forEach((k) => delete detail[k])
    Object.assign(detail, data || {})
  } finally {
    detailLoading.value = false
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-select v-model="period" placeholder="考核周期" clearable class="period-select" @change="load">
          <el-option v-for="o in PERIOD_OPTIONS" :key="o.value" :label="o.label" :value="o.value" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button :icon="Refresh" @click="period = undefined; load()">重置</el-button>
      </div>
      <span class="tip">我作为考核人的下属考核总览</span>
    </div>

    <el-table v-loading="loading" :data="records" border stripe>
      <el-table-column type="index" label="#" width="55" align="center" />
      <el-table-column prop="empName" label="员工" width="100" align="center" />
      <el-table-column prop="deptName" label="部门" min-width="120" show-overflow-tooltip />
      <el-table-column prop="period" label="周期" width="100" align="center" />
      <el-table-column label="状态" width="95" align="center">
        <template #default="{ row }">
          <el-tag :type="APPRAISAL_STATUS[row.status]?.type || 'info'">
            {{ APPRAISAL_STATUS[row.status]?.text ?? row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="selfScore" label="自评分" width="85" align="center" />
      <el-table-column prop="superiorScore" label="上级评分" width="85" align="center" />
      <el-table-column prop="totalScore" label="总分" width="85" align="center" />
      <el-table-column label="等级" width="75" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.grade" :type="GRADE_TAG[row.grade] || 'info'">{{ row.grade }}</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openDetail(row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="detailVisible" title="考核详情" width="900px" destroy-on-close>
    <div v-loading="detailLoading">
      <el-descriptions v-if="detail.appraisal" :column="4" border size="small" class="mb">
        <el-descriptions-item label="周期">{{ detail.appraisal.period }}</el-descriptions-item>
        <el-descriptions-item label="被考核人">{{ detail.appraisal.empName }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ detail.appraisal.deptName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="考核人">{{ detail.appraisal.superiorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          {{ APPRAISAL_STATUS[detail.appraisal.status]?.text ?? detail.appraisal.status }}
        </el-descriptions-item>
        <el-descriptions-item label="自评分">{{ detail.appraisal.selfScore ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="上级评分">{{ detail.appraisal.superiorScore ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="综合分/等级">
          {{ detail.appraisal.totalScore ?? '-' }} / {{ detail.appraisal.grade || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <h4 class="section-title">考核目标</h4>
      <el-table :data="detail.goals || []" border size="small">
        <el-table-column prop="goalName" label="目标名称" min-width="130" show-overflow-tooltip />
        <el-table-column prop="dimension" label="维度" width="65" align="center" />
        <el-table-column prop="weight" label="权重%" width="65" align="center" />
        <el-table-column prop="selfScore" label="自评分" width="75" align="center" />
        <el-table-column prop="superiorScore" label="上级评分" width="80" align="center" />
        <el-table-column prop="selfComment" label="自评说明" min-width="120" show-overflow-tooltip />
        <el-table-column prop="superiorComment" label="上级评语" min-width="120" show-overflow-tooltip />
      </el-table>

      <h4 class="section-title">面谈记录</h4>
      <el-table :data="detail.interviews || []" border size="small">
        <el-table-column prop="interviewer" label="面谈人" width="90" align="center" />
        <el-table-column label="面谈时间" width="150" align="center">
          <template #default="{ row }">{{ fmtTime(row.interviewTime) }}</template>
        </el-table-column>
        <el-table-column prop="advantage" label="优势肯定" min-width="120" show-overflow-tooltip />
        <el-table-column prop="shortage" label="不足指出" min-width="120" show-overflow-tooltip />
        <el-table-column prop="improvePlan" label="改进计划" min-width="140" show-overflow-tooltip />
        <el-table-column label="填写/审核" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.fillStatus === 1 ? 'success' : 'info'" size="small">
              {{ row.fillStatus === 1 ? '已填写' : '未填写' }}
            </el-tag>
            <el-tag :type="row.auditStatus === 1 ? 'success' : row.auditStatus === 2 ? 'danger' : 'info'" size="small" class="ml4">
              {{ row.auditStatus === 1 ? '已通过' : row.auditStatus === 2 ? '已驳回' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <template #footer>
      <el-button @click="detailVisible = false">关闭</el-button>
    </template>
  </el-dialog>
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
  gap: 8px;
}
.tip {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
.period-select {
  width: 140px;
}
.mb {
  margin-bottom: 12px;
}
.section-title {
  margin: 14px 0 8px;
  font-size: 14px;
  color: var(--el-text-color-primary);
}
.ml4 {
  margin-left: 4px;
}
</style>
