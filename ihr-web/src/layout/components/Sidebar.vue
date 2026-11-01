<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, type RouteRecordRaw } from 'vue-router'
import router from '@/router'
import MenuItem from './MenuItem.vue'

defineProps<{ collapse: boolean }>()

const route = useRoute()

/** 从路由配置推导侧边栏菜单树（根布局 '/' 的 children） */
const menuRoutes = computed<RouteRecordRaw[]>(() => {
  const root = router.options.routes.find((r) => r.path === '/')
  return (root?.children ?? []).filter((r) => !r.meta?.hidden)
})

const activeMenu = computed(() => route.path)
</script>

<template>
  <div class="sidebar">
    <div class="sidebar-logo" :class="{ collapsed: collapse }">
      <span class="logo-text">iHR</span>
      <span v-if="!collapse" class="logo-sub">智慧人力资源系统</span>
    </div>
    <el-scrollbar class="sidebar-scroll">
      <el-menu
        :default-active="activeMenu"
        :collapse="collapse"
        :collapse-transition="false"
        router
        unique-opened
        background-color="#001529"
        text-color="rgba(255, 255, 255, 0.68)"
        active-text-color="#ffffff"
        class="sidebar-menu"
      >
        <MenuItem v-for="item in menuRoutes" :key="item.path" :item="item" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script lang="ts">
export default { name: 'Sidebar' }
</script>

<style scoped>
.sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #001529;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  height: 56px;
  padding: 0 16px;
  color: #fff;
  white-space: nowrap;
  overflow: hidden;
}

.sidebar-logo.collapsed {
  justify-content: center;
  padding: 0;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #409eff;
}

.logo-sub {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.55);
}

.sidebar-scroll {
  flex: 1;
}

.sidebar-menu {
  border-right: none;
}
</style>
