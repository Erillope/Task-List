import type { ReactElement } from "react"
import { Navigate } from "react-router-dom"
import Cookies from 'js-cookie'

export const ProtectedRoute = ({ children }: { children: ReactElement }) => {
  if (!hasRegisteredUser()) {
    return <Navigate to="/register" replace />
  }

  return children
}

const hasRegisteredUser = (): boolean => {
  const registeredUser = Cookies.get('registeredUser')
  if (!registeredUser) return false

  try {
    const parsedUser = JSON.parse(registeredUser)
    return !!parsedUser
  } catch {
    return false
  }
}