/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'vue-router' {
  interface RouteMeta {
    /** 菜单/页面标题 */
    title?: string
    /** 菜单图标（Element Plus 图标组件名） */
    icon?: string
    /** 是否在菜单中隐藏 */
    hidden?: boolean
  }
}
