<script setup lang="ts">
import type { RouteRecordRaw } from 'vue-router'

/**
 * 递归菜单项：有 children 渲染子菜单，否则渲染叶子菜单。
 * el-menu 开启 router 模式，index 即为完整路由路径。
 */
defineProps<{ item: RouteRecordRaw }>()
</script>

<template>
  <el-sub-menu v-if="item.children && item.children.length" :index="item.path">
    <template #title>
      <el-icon v-if="item.meta?.icon"><component :is="item.meta.icon" /></el-icon>
      <span>{{ item.meta?.title }}</span>
    </template>
    <MenuItem v-for="child in item.children" :key="child.path" :item="child" />
  </el-sub-menu>
  <el-menu-item v-else :index="item.path">
    <el-icon v-if="item.meta?.icon"><component :is="item.meta.icon" /></el-icon>
    <template #title>{{ item.meta?.title }}</template>
  </el-menu-item>
</template>

<script lang="ts">
export default { name: 'MenuItem' }
</script>
