<script setup lang="ts">
import { onLaunch } from '@dcloudio/uni-app'

onLaunch(() => {
  console.log('iHR 小程序启动')
})
</script>

<style>
page {
  background-color: #f5f7fa;
  font-family: -apple-system, 'Helvetica Neue', 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', sans-serif;
  font-size: 28rpx;
  color: #303133;
}

/* 通用卡片样式 */
.card {
  margin: 24rpx;
  padding: 32rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
}

.card-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16rpx;
}

.placeholder-text {
  color: #909399;
  font-size: 26rpx;
  text-align: center;
  padding: 32rpx 0;
}
</style>
