<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  // 框架阶段假数据登录：admin / 123456
  username: 'admin',
  password: '123456',
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    const redirect = (route.query.redirect as string) || '/'
    router.replace(redirect)
  } catch (e) {
    // 错误提示已在 axios 拦截器统一弹出
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="login-card" shadow="always">
      <div class="login-title">
        <h2>iHR 智慧人力资源管理系统</h2>
        <p>框架演示账号：admin / 123456</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <div class="login-footer">iHR Framework · Vue 3 + TypeScript + Element Plus</div>
  </div>
</template>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f3b73 0%, #2f6fb5 60%, #409eff 100%);
}

.login-card {
  width: 380px;
  border-radius: 8px;
}

.login-title {
  text-align: center;
  margin-bottom: 24px;
}

.login-title h2 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #1f3b73;
}

.login-title p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.login-btn {
  width: 100%;
}

.login-footer {
  margin-top: 24px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
}
</style>
