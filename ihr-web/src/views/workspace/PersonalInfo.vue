<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import request from '@/utils/request'

const loading = ref(false)
const saving = ref(false)
const profile = ref<any>(null)
const dialogVisible = ref(false)

const form = reactive({
  id: undefined as number | undefined,
  phone: '',
  email: '',
  workPlace: '',
  remark: '',
})

const employmentStatusMap: Record<number, { text: string; type: 'success' | 'warning' | 'danger' }> = {
  1: { text: '在职', type: 'success' },
  2: { text: '试用', type: 'warning' },
  3: { text: '离职', type: 'danger' },
}

function text(v: any): string {
  return v === null || v === undefined || v === '' ? '-' : String(v)
}

function dateText(v: any): string {
  return v ? String(v).slice(0, 10) : '-'
}

async function load() {
  loading.value = true
  try {
    profile.value = await request.get<any, any>('/workbench/profile')
  } finally {
    loading.value = false
  }
}

function handleEdit() {
  const p = profile.value || {}
  Object.assign(form, {
    id: p.id,
    phone: p.phone ?? '',
    email: p.email ?? '',
    workPlace: p.workPlace ?? '',
    remark: p.remark ?? '',
  })
  dialogVisible.value = true
}

async function submit() {
  saving.value = true
  try {
    await request.put('/workbench/profile', { ...form })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<template>
  <div class="personal-info">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>我的档案</span>
          <el-button type="primary" :icon="Edit" @click="handleEdit">编辑</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="工号">{{ text(profile?.empNo) }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ text(profile?.realName) }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ text(profile?.gender) }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ dateText(profile?.birthDate) }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ text(profile?.deptName) }}</el-descriptions-item>
        <el-descriptions-item label="岗位">{{ text(profile?.position) }}</el-descriptions-item>
        <el-descriptions-item label="职级">{{ text(profile?.jobLevel) }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ text(profile?.education) }}</el-descriptions-item>
        <el-descriptions-item label="毕业院校">{{ text(profile?.school) }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ text(profile?.major) }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{ dateText(profile?.entryDate) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="employmentStatusMap[profile?.employmentStatus]?.type || 'info'">
            {{ employmentStatusMap[profile?.employmentStatus]?.text ?? '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="工作地">{{ text(profile?.workPlace) }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ text(profile?.phone) }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ text(profile?.email) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ text(profile?.remark) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑个人信息" width="560px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="工作地">
          <el-input v-model="form.workPlace" placeholder="请输入工作地" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
