/**
 * 后端接口地址：本地开发时为局域网/本机地址，
 * 微信开发者工具需勾选「不校验合法域名」（manifest 已默认 urlCheck: false）。
 */
export const BASE_URL = 'http://localhost:8090'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

/** uni.request 简单封装：自动携带 token、解包统一 Result 结构 */
export function request<T = any>(
  method: 'GET' | 'POST',
  url: string,
  data?: Record<string, any>,
): Promise<T> {
  const token = uni.getStorageSync('ihr_token') as string
  return new Promise<T>((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}${url}`,
      method,
      data,
      header: token ? { Authorization: `Bearer ${token}` } : {},
      success: (res) => {
        const body = res.data as ApiResult<T>
        if (res.statusCode === 200 && body && body.code === 0) {
          resolve(body.data)
        } else {
          const msg = body?.message || `请求失败（HTTP ${res.statusCode}）`
          uni.showToast({ title: msg, icon: 'none' })
          reject(new Error(msg))
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常', icon: 'none' })
        reject(err)
      },
    })
  })
}
