<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Document } from '@element-plus/icons-vue'

const route = useRoute()
const title = computed(() => (route.meta?.title as string) || '占位页面')
</script>

<template>
  <div class="placeholder-page">
    <el-card shadow="never" class="placeholder-card">
      <el-icon :size="48" color="#409eff"><Document /></el-icon>
      <h2 class="placeholder-title">{{ title }}</h2>
      <p class="placeholder-desc">页面占位 —— 功能建设中</p>
      <p class="placeholder-path">当前路由：{{ route.path }}</p>
    </el-card>
  </div>
</template>

<style scoped>
.placeholder-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100%;
}

.placeholder-card {
  width: 420px;
  text-align: center;
  border-radius: 8px;
}

.placeholder-title {
  margin: 16px 0 8px;
  font-size: 18px;
  color: #303133;
}

.placeholder-desc {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.placeholder-path {
  margin: 8px 0 0;
  font-size: 12px;
  color: #c0c4cc;
}
</style>
