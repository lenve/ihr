<script setup lang="ts">
/**
 * 人事报表：总览统计卡片 + 部门/学历分布（el-progress 列表）。
 */
import { computed, onMounted, ref } from 'vue'
import { Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const overview = ref<Record<string, any>>({})

const statCards = computed(() => [
  { label: '总人数', value: overview.value.totalEmployees },
  { label: '在职', value: overview.value.onJob },
  { label: '试用', value: overview.value.probation },
  { label: '离职', value: overview.value.resigned },
  { label: '本年入职', value: overview.value.entryThisYear },
])

async function load() {
  loading.value = true
  try {
    overview.value = (await request.get<any, any>('/hrservice/report/overview')) || {}
  } finally {
    loading.value = false
  }
}

function pct(count: any): number {
  const total = Number(overview.value.totalEmployees) || 0
  if (!total) return 0
  return Math.round((Number(count) / total) * 1000) / 10
}

const deptStats = computed(() => overview.value.deptStats || [])
const educationStats = computed(() => overview.value.educationStats || [])

onMounted(load)
</script>

<template>
  <div v-loading="loading" class="hr-report">
    <!-- 总览统计卡片 -->
    <div class="stat-cards">
      <el-card v-for="card in statCards" :key="card.label" shadow="hover" class="stat-card">
        <div class="stat-label">{{ card.label }}</div>
        <div class="stat-value">{{ card.value ?? '-' }}</div>
      </el-card>
    </div>

    <el-button :icon="Refresh" class="refresh-btn" @click="load">刷新</el-button>

    <!-- 分布统计 -->
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>按部门分布</template>
          <el-empty v-if="!deptStats.length" description="暂无数据" :image-size="60" />
          <div v-for="item in deptStats" :key="item.name" class="dist-row">
            <span class="dist-name">{{ item.name }}</span>
            <span class="dist-count">{{ item.count }} 人</span>
            <el-progress :percentage="pct(item.count)" class="dist-progress" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>按学历分布</template>
          <el-empty v-if="!educationStats.length" description="暂无数据" :image-size="60" />
          <div v-for="item in educationStats" :key="item.name" class="dist-row">
            <span class="dist-name">{{ item.name }}</span>
            <span class="dist-count">{{ item.count }} 人</span>
            <el-progress :percentage="pct(item.count)" class="dist-progress" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stat-cards {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}
.stat-card {
  flex: 1;
}
.stat-label {
  color: #909399;
  font-size: 14px;
}
.stat-value {
  font-size: 30px;
  font-weight: 700;
  color: #409eff;
  margin-top: 8px;
}
.refresh-btn {
  margin-bottom: 16px;
}
.dist-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
}
.dist-name {
  width: 90px;
  color: #303133;
  font-size: 14px;
}
.dist-count {
  width: 60px;
  color: #606266;
  font-size: 13px;
  text-align: right;
}
.dist-progress {
  flex: 1;
}
</style>
