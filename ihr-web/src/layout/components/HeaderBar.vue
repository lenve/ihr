<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Fold, Expand } from '@element-plus/icons-vue'

defineProps<{ collapsed: boolean }>()
defineEmits<{ (e: 'toggle'): void }>()

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

/** 面包屑：取匹配路由的 meta.title */
const breadcrumbs = computed(() =>
  route.matched
    .filter((r) => r.meta?.title && r.path !== '/')
    .map((r) => ({ title: r.meta.title as string, path: r.path })),
)

function handleCommand(command: string) {
  if (command === 'logout') {
    userStore.logoutLocal()
    router.push('/login')
  }
}
</script>

<template>
  <div class="header-bar">
    <div class="header-left">
      <el-icon class="collapse-btn" size="18" @click="$emit('toggle')">
        <Expand v-if="collapsed" />
        <Fold v-else />
      </el-icon>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
          {{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <span class="user-entry">
          <el-avatar :size="28" style="background-color: #409eff">
            {{ userStore.displayName().slice(0, 1) }}
          </el-avatar>
          <span class="user-name">{{ userStore.displayName() }}</span>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style scoped>
.header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.collapse-btn {
  cursor: pointer;
}

.header-right .user-entry {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  outline: none;
}

.user-name {
  font-size: 14px;
  color: var(--el-text-color-primary);
}
</style>
