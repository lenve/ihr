<script setup lang="ts">
import { ref } from 'vue'

const nickname = ref<string>((uni.getStorageSync('ihr_nickname') as string) || '')

function goLogin() {
  if (nickname.value) {
    uni.showModal({
      title: '退出登录',
      content: '确定清除本地登录态吗？',
      success: (res) => {
        if (res.confirm) {
          uni.removeStorageSync('ihr_token')
          uni.removeStorageSync('ihr_nickname')
          nickname.value = ''
          uni.showToast({ title: '已退出', icon: 'none' })
        }
      },
    })
  } else {
    uni.navigateTo({ url: '/pages/login/login' })
  }
}

const cells = ['个人信息', '培训档案', '工资查询', '我的绩效', '设置']

function onCellTap(cell: string) {
  uni.showToast({ title: `${cell} - 页面占位`, icon: 'none' })
}
</script>

<template>
  <view class="page">
    <view class="card profile">
      <view class="avatar">{{ nickname ? nickname.slice(0, 1) : '客' }}</view>
      <view class="profile-info">
        <view class="profile-name">{{ nickname || '未登录' }}</view>
        <view class="profile-desc">{{ nickname ? '登录态有效（本地缓存）' : '点击右上角登录按钮登录' }}</view>
      </view>
    </view>

    <view class="card">
      <view class="cell" v-for="cell in cells" :key="cell" @tap="onCellTap(cell)">
        <text>{{ cell }}</text>
        <text class="arrow">›</text>
      </view>
    </view>

    <view class="logout-btn" @tap="goLogin">{{ nickname ? '退出登录' : '去登录' }}</view>
  </view>
</template>

<style scoped>
.profile {
  display: flex;
  align-items: center;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background-color: #409eff;
  color: #ffffff;
  font-size: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.profile-name {
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 8rpx;
}

.profile-desc {
  font-size: 24rpx;
  color: #909399;
}

.cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 0;
  border-bottom: 1rpx solid #ebeef5;
  font-size: 28rpx;
}

.cell:last-of-type {
  border-bottom: none;
}

.arrow {
  color: #c0c4cc;
}

.logout-btn {
  margin: 40rpx 24rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  background-color: #ffffff;
  border-radius: 16rpx;
  color: #f56c6c;
}
</style>
