import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const MainLayout = () => import('@/layout/MainLayout.vue')

/** 视图工厂：按分组目录约定解析组件 */
const view = (group: string, name: string) => () => import(`@/views/${group}/${name}.vue`)

/**
 * 路由即菜单：侧边栏根据 meta.title / meta.icon 渲染分组菜单。
 * 叶子路由指向 views/<分组>/<页面>.vue。
 */
export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '登录', hidden: true },
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/workspace/personal-info',
    children: [
      // ==================== 1. 个人工作台 ====================
      {
        path: '/workspace',
        meta: { title: '个人工作台', icon: 'Monitor' },
        children: [
          { path: '/workspace/personal-info', component: view('workspace', 'PersonalInfo'), meta: { title: '个人信息' } },
          { path: '/workspace/todo-notice', component: view('workspace', 'TodoNotice'), meta: { title: '待办通知' } },
          { path: '/workspace/work-task', component: view('workspace', 'WorkTask'), meta: { title: '工作任务' } },
          { path: '/workspace/alert-notice', component: view('workspace', 'AlertNotice'), meta: { title: '预警通知' } },
        ],
      },
      // ==================== 2. 招聘与人才 ====================
      {
        path: '/recruit',
        meta: { title: '招聘与人才', icon: 'User' },
        children: [
          { path: '/recruit/info', component: view('recruit', 'RecruitInfo'), meta: { title: '招聘信息' } },
          { path: '/recruit/talent-manage', component: view('recruit', 'TalentManage'), meta: { title: '人才管理' } },
          { path: '/recruit/talent-apply', component: view('recruit', 'TalentApply'), meta: { title: '人才申报' } },
          { path: '/recruit/hr-pool', component: view('recruit', 'HrPool'), meta: { title: '人力资源池' } },
        ],
      },
      // ==================== 3. 绩效考核 ====================
      {
        path: '/performance',
        meta: { title: '绩效考核', icon: 'DataAnalysis' },
        children: [
          { path: '/performance/score-form', component: view('performance', 'ScoreForm'), meta: { title: '考核表评分' } },
          { path: '/performance/assess-relation', component: view('performance', 'AssessRelation'), meta: { title: '考核关系' } },
          { path: '/performance/my-achievement', component: view('performance', 'MyAchievement'), meta: { title: '我的业绩' } },
          { path: '/performance/team-achievement-apply', component: view('performance', 'TeamAchievementApply'), meta: { title: '团队业绩申请' } },
          {
            path: '/performance/my-appraisal',
            meta: { title: '我的绩效', icon: 'Tickets' },
            redirect: '/performance/my-appraisal/goal-setting',
            children: [
              { path: '/performance/my-appraisal/goal-setting', component: view('performance', 'GoalSetting'), meta: { title: '目标制定' } },
              { path: '/performance/my-appraisal/self-review', component: view('performance', 'SelfReview'), meta: { title: '员工自评' } },
              { path: '/performance/my-appraisal/superior-review', component: view('performance', 'SuperiorReview'), meta: { title: '上级评价' } },
              { path: '/performance/my-appraisal/sign-progress', component: view('performance', 'SignProgress'), meta: { title: '签署进度' } },
              { path: '/performance/my-appraisal/interview-fill', component: view('performance', 'InterviewFill'), meta: { title: '面谈填写' } },
              { path: '/performance/my-appraisal/interview-audit', component: view('performance', 'InterviewAudit'), meta: { title: '面谈审核' } },
              { path: '/performance/my-appraisal/appraisal-result', component: view('performance', 'AppraisalResult'), meta: { title: '绩效考核结果' } },
              { path: '/performance/my-appraisal/competitive-result', component: view('performance', 'CompetitiveResult'), meta: { title: '竞争考核结果' } },
              { path: '/performance/my-appraisal/my-appeal', component: view('performance', 'MyAppeal'), meta: { title: '我的申诉' } },
              { path: '/performance/my-appraisal/appeal-audit', component: view('performance', 'AppealAudit'), meta: { title: '申诉审核' } },
              { path: '/performance/my-appraisal/my-subordinates', component: view('performance', 'MySubordinates'), meta: { title: '我的下属' } },
            ],
          },
        ],
      },
      // ==================== 4. 人事服务 ====================
      {
        path: '/hr-service',
        meta: { title: '人事服务', icon: 'Service' },
        children: [
          { path: '/hr-service/report', component: view('hr-service', 'HrReport'), meta: { title: '人事报表' } },
          { path: '/hr-service/notice', component: view('hr-service', 'Notice'), meta: { title: '通知公告' } },
          { path: '/hr-service/policy', component: view('hr-service', 'Policy'), meta: { title: '政策解读' } },
          { path: '/hr-service/salary-query', component: view('hr-service', 'SalaryQuery'), meta: { title: '工资查询' } },
          { path: '/hr-service/guide', component: view('hr-service', 'Guide'), meta: { title: '办事指南' } },
          { path: '/hr-service/training-archive', component: view('hr-service', 'TrainingArchive'), meta: { title: '培训档案' } },
          { path: '/hr-service/exit-entry-guide', component: view('hr-service', 'ExitEntryGuide'), meta: { title: '出入境指南' } },
          { path: '/hr-service/promotion-publicity', component: view('hr-service', 'PromotionPublicity'), meta: { title: '晋升提拔公示' } },
          { path: '/hr-service/vehicle-violation', component: view('hr-service', 'VehicleViolation'), meta: { title: '车辆违规查询' } },
        ],
      },
      // ==================== 5. 申报与审批 ====================
      {
        path: '/declare',
        meta: { title: '申报与审批', icon: 'Document' },
        children: [
          { path: '/declare/visa-apply', component: view('declare', 'VisaApply'), meta: { title: '签证申请' } },
          { path: '/declare/title-apply', component: view('declare', 'TitleApply'), meta: { title: '职称申报' } },
          { path: '/declare/my-title', component: view('declare', 'MyTitle'), meta: { title: '我的职称' } },
        ],
      },
      // ==================== 6. 平台功能 ====================
      {
        path: '/platform',
        meta: { title: '平台功能', icon: 'Message' },
        children: [
          { path: '/platform/message', component: view('platform', 'Message'), meta: { title: '信息收发平台' } },
        ],
      },
    ],
  },
  // 兜底
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 登录守卫：无 token 一律跳转登录页
router.beforeEach((to) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && userStore.token) {
    return { path: '/' }
  }
})

export default router
