<script setup lang="ts">
const todos = [
  { id: 1, title: '2026年Q3 绩效面谈确认', status: '进行中' },
  { id: 2, title: '职称申报材料补充', status: '待提交' },
  { id: 3, title: '培训课程完成确认', status: '已完成' },
]
</script>

<template>
  <view class="page">
    <view class="card">
      <view class="card-title">我的待办</view>
      <view v-for="todo in todos" :key="todo.id" class="todo-item">
        <view class="todo-title">{{ todo.title }}</view>
        <view class="todo-tag" :class="{ done: todo.status === '已完成' }">{{ todo.status }}</view>
      </view>
      <view class="placeholder-text">待办数据为占位示例 —— 功能建设中</view>
    </view>
  </view>
</template>

<style scoped>
.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #ebeef5;
}

.todo-item:last-of-type {
  border-bottom: none;
}

.todo-title {
  font-size: 28rpx;
  color: #303133;
}

.todo-tag {
  font-size: 22rpx;
  color: #409eff;
}

.todo-tag.done {
  color: #67c23a;
}
</style>
