<script setup lang="ts">
const notices = [
  { id: 1, title: '关于开展2026年度绩效考核的通知', date: '2026-10-28' },
  { id: 2, title: '晋升提拔公示（第42期）', date: '2026-10-25' },
  { id: 3, title: '出入境办事指南更新说明', date: '2026-10-20' },
]
</script>

<template>
  <view class="page">
    <view class="card">
      <view class="card-title">通知公告</view>
      <view v-for="notice in notices" :key="notice.id" class="notice-item">
        <view class="notice-title">{{ notice.title }}</view>
        <view class="notice-date">{{ notice.date }}</view>
      </view>
      <view class="placeholder-text">公告数据为占位示例 —— 功能建设中</view>
    </view>
  </view>
</template>

<style scoped>
.notice-item {
  padding: 24rpx 0;
  border-bottom: 1rpx solid #ebeef5;
}

.notice-item:last-of-type {
  border-bottom: none;
}

.notice-title {
  font-size: 28rpx;
  color: #303133;
  margin-bottom: 8rpx;
}

.notice-date {
  font-size: 22rpx;
  color: #909399;
}
</style>
