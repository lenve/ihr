<script setup lang="ts">
/**
 * 上级评分对话框：逐目标填写上级评分与评语，保存后提交上级评价。
 * SuperiorReview.vue 与 ScoreForm.vue 复用。
 */
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const props = defineProps<{
  modelValue: boolean
  appraisal: any
}>()

const emit = defineEmits<{ (e: 'update:modelValue', v: boolean): void; (e: 'done'): void }>()

const visible = ref(false)
watch(
  () => props.modelValue,
  (v) => {
    visible.value = v
    if (v) loadGoals()
  },
)
watch(visible, (v) => emit('update:modelValue', v))

const loading = ref(false)
const submitting = ref(false)
const goals = ref<any[]>([])

async function loadGoals() {
  if (!props.appraisal?.id) return
  loading.value = true
  try {
    goals.value = await request.get<any, any>('/performance/goals', {
      params: { appraisalId: props.appraisal.id },
    })
  } finally {
    loading.value = false
  }
}

async function submit() {
  for (const g of goals.value) {
    if (g.superiorScore === null || g.superiorScore === undefined) {
      ElMessage.warning(`请为目标「${g.goalName}」填写上级评分`)
      return
    }
  }
  submitting.value = true
  try {
    for (const g of goals.value) {
      await request.put(`/performance/goals/${g.id}`, {
        superiorScore: g.superiorScore,
        superiorComment: g.superiorComment || '',
      })
    }
    await request.put(`/performance/appraisals/${props.appraisal.id}/submit-superior-review`)
    ElMessage.success('上级评价已提交')
    visible.value = false
    emit('done')
  } finally {
    submitting.value = false
  }
}

</script>

<template>
  <el-dialog v-model="visible" title="上级评分" width="860px" destroy-on-close>
    <el-descriptions v-if="appraisal" :column="4" border size="small" class="mb">
      <el-descriptions-item label="考核周期">{{ appraisal.period }}</el-descriptions-item>
      <el-descriptions-item label="被考核人">{{ appraisal.empName }}</el-descriptions-item>
      <el-descriptions-item label="部门">{{ appraisal.deptName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="自评总分">{{ appraisal.selfScore ?? '-' }}</el-descriptions-item>
    </el-descriptions>

    <el-table v-loading="loading" :data="goals" border stripe>
      <el-table-column prop="goalName" label="目标名称" min-width="140" show-overflow-tooltip />
      <el-table-column prop="dimension" label="维度" width="70" align="center" />
      <el-table-column prop="weight" label="权重%" width="70" align="center" />
      <el-table-column prop="targetValue" label="目标值" min-width="110" show-overflow-tooltip />
      <el-table-column label="自评分" width="80" align="center">
        <template #default="{ row }">{{ row.selfScore ?? '-' }}</template>
      </el-table-column>
      <el-table-column label="上级评分" width="150" align="center">
        <template #default="{ row }">
          <el-input-number v-model="row.superiorScore" :min="0" :max="100" :precision="1" size="small" style="width: 130px" />
        </template>
      </el-table-column>
      <el-table-column label="上级评语" min-width="180">
        <template #default="{ row }">
          <el-input v-model="row.superiorComment" size="small" placeholder="评分说明（可选）" />
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submit">保存并提交评价</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.mb {
  margin-bottom: 12px;
}
</style>
