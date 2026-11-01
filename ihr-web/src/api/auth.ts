import request from '@/utils/request'

export interface UserInfoVO {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  roles: string[]
}

export interface LoginVO {
  token: string
  expiresIn: number
  user: UserInfoVO
}

/** 登录（后端框架阶段为假数据校验） */
export function loginApi(data: { username: string; password: string }) {
  return request.post<any, LoginVO>('/auth/login', data)
}

/** 当前登录用户信息 */
export function meApi() {
  return request.get<any, UserInfoVO>('/auth/me')
}
