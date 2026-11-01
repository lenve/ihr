<script setup lang="ts">
import { ref } from 'vue'

const nickname = ref<string>((uni.getStorageSync('ihr_nickname') as string) || '')

interface QuickEntry {
  title: string
  desc: string
}

const entries: QuickEntry[] = [
  { title: '待办通知', desc: '待处理事项' },
  { title: '通知公告', desc: '最新公告' },
  { title: '工资查询', desc: '工资条占位' },
  { title: '我的绩效', desc: '绩效流程占位' },
  { title: '办事指南', desc: '指南占位' },
  { title: '培训档案', desc: '档案占位' },
]

function goNotice() {
  uni.navigateTo({ url: '/pages/notice/notice' })
}

function goTodo() {
  uni.switchTab({ url: '/pages/todo/index' })
}

function onEntryTap(entry: QuickEntry) {
  if (entry.title === '通知公告') {
    goNotice()
  } else if (entry.title === '待办通知') {
    goTodo()
  } else {
    uni.showToast({ title: `${entry.title} - 页面占位`, icon: 'none' })
  }
}
</script>

<template>
  <view class="page">
    <view class="hero card">
      <view class="hero-title">{{ nickname ? `你好，${nickname}` : 'iHR 智慧人力资源系统' }}</view>
      <view class="hero-desc">个人工作台 · 框架演示（假数据登录）</view>
    </view>

    <view class="card">
      <view class="card-title">快捷入口</view>
      <view class="grid">
        <view v-for="entry in entries" :key="entry.title" class="grid-item" @tap="onEntryTap(entry)">
          <view class="grid-item-title">{{ entry.title }}</view>
          <view class="grid-item-desc">{{ entry.desc }}</view>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="card-title">系统公告</view>
      <view class="placeholder-text">公告列表占位 —— 功能建设中</view>
    </view>
  </view>
</template>

<style scoped>
.page {
  padding-bottom: 40rpx;
}

.hero {
  background: linear-gradient(135deg, #1f3b73, #409eff);
  color: #ffffff;
}

.hero-title {
  font-size: 36rpx;
  font-weight: 600;
  margin-bottom: 12rpx;
}

.hero-desc {
  font-size: 24rpx;
  opacity: 0.8;
}

.grid {
  display: flex;
  flex-wrap: wrap;
}

.grid-item {
  width: 33.33%;
  padding: 24rpx 0;
  text-align: center;
  box-sizing: border-box;
}

.grid-item-title {
  font-size: 28rpx;
  color: #303133;
  margin-bottom: 8rpx;
}

.grid-item-desc {
  font-size: 22rpx;
  color: #909399;
}
</style>
