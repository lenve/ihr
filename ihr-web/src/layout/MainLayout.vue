<script setup lang="ts">
import { ref } from 'vue'
import Sidebar from './components/Sidebar.vue'
import HeaderBar from './components/HeaderBar.vue'

const isCollapse = ref(false)

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}
</script>

<template>
  <el-container class="main-layout">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <Sidebar :collapse="isCollapse" />
    </el-aside>
    <el-container>
      <el-header class="layout-header" height="56px">
        <HeaderBar :collapsed="isCollapse" @toggle="toggleCollapse" />
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.main-layout {
  height: 100vh;
}

.layout-aside {
  transition: width 0.25s;
  overflow-x: hidden;
  border-right: 1px solid var(--el-border-color-light);
  background-color: #fff;
}

.layout-header {
  border-bottom: 1px solid var(--el-border-color-light);
  background-color: #fff;
}

.layout-main {
  background-color: #f5f7fa;
  overflow-y: auto;
}
</style>
