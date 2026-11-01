/**
 * 绩效模块前端共享常量与工具。
 */
import { useUserStore } from '@/stores/user'

/** 考核状态（8 态） */
export const APPRAISAL_STATUS: Record<number, { text: string; type: 'info' | 'primary' | 'warning' | 'success' | 'danger' }> = {
  0: { text: '目标制定', type: 'info' },
  1: { text: '目标确认', type: 'info' },
  2: { text: '自评中', type: 'primary' },
  3: { text: '上级评价', type: 'primary' },
  4: { text: '待面谈', type: 'warning' },
  5: { text: '面谈审核', type: 'warning' },
  6: { text: '待签署', type: 'warning' },
  7: { text: '已完成', type: 'success' },
  8: { text: '申诉中', type: 'danger' },
}

/** 考核状态下拉选项 */
export const APPRAISAL_STATUS_OPTIONS = Object.entries(APPRAISAL_STATUS).map(([value, v]) => ({
  label: v.text,
  value: Number(value),
}))

/** 绩效等级 tag */
export const GRADE_TAG: Record<string, 'danger' | 'success' | 'primary' | 'warning' | 'info'> = {
  S: 'danger',
  A: 'success',
  B: 'primary',
  C: 'warning',
  D: 'info',
}

/** 考核周期选项 */
export const PERIOD_OPTIONS = ['2025-H1', '2025-H2', '2026-H1', '2026-H2', '2027-H1'].map((p) => ({
  label: p,
  value: p,
}))

/** 目标维度 */
export const DIMENSION_OPTIONS = ['业绩', '行为', '协作'].map((d) => ({ label: d, value: d }))

/** 当前登录用户姓名（nickname 优先） */
export function currentUserName(): string {
  return useUserStore().displayName()
}

/** 格式化时间（去掉 T，保留到分钟） */
export function fmtTime(v?: string | null): string {
  if (!v) return '-'
  return String(v).replace('T', ' ').slice(0, 16)
}
