<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request } from '@/utils/request'

interface LoginVO {
  token: string
  user: { nickname: string; username: string }
}

const form = reactive({
  username: 'admin',
  password: '123456',
})
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) {
    uni.showToast({ title: '请输入账号和密码', icon: 'none' })
    return
  }
  loading.value = true
  try {
    // 调用后端登录接口（假数据校验），需后端 8090 端口运行中
    const data = await request<LoginVO>('POST', '/api/auth/login', { ...form })
    uni.setStorageSync('ihr_token', data.token)
    uni.setStorageSync('ihr_nickname', data.user.nickname)
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => uni.switchTab({ url: '/pages/mine/index' }), 600)
  } catch (e) {
    // 错误提示已在 request 封装中统一弹出
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <view class="page">
    <view class="brand">
      <view class="brand-title">iHR 智慧人力资源系统</view>
      <view class="brand-desc">框架演示账号：admin / 123456</view>
    </view>

    <view class="card">
      <view class="form-item">
        <input v-model="form.username" class="input" placeholder="用户名" placeholder-class="ph" />
      </view>
      <view class="form-item">
        <input
          v-model="form.password"
          class="input"
          password
          placeholder="密码"
          placeholder-class="ph"
        />
      </view>
      <button class="login-btn" :loading="loading" @tap="handleLogin">登 录</button>
    </view>
  </view>
</template>

<style scoped>
.brand {
  text-align: center;
  padding: 80rpx 0 40rpx;
}

.brand-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #1f3b73;
  margin-bottom: 16rpx;
}

.brand-desc {
  font-size: 24rpx;
  color: #909399;
}

.form-item {
  margin-bottom: 32rpx;
}

.input {
  height: 88rpx;
  padding: 0 24rpx;
  border: 1rpx solid #dcdfe6;
  border-radius: 12rpx;
  font-size: 28rpx;
}

.ph {
  color: #c0c4cc;
}

.login-btn {
  margin-top: 16rpx;
  background-color: #409eff;
  color: #ffffff;
  border-radius: 12rpx;
  font-size: 30rpx;
}
</style>
