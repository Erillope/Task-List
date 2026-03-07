
import { Navigate, Route, Routes } from "react-router-dom"
import { LoginUserPage } from "../pages/LoginUserPage"
import { RegisterUserPage } from "../pages/RegisterUserPage"

export const AuthRoutes = () => {
	return (
		<Routes>
			<Route path="/" element={<RegisterUserPage />} />
			<Route path="/register" element={<RegisterUserPage />} />
			<Route path="/login" element={<LoginUserPage />} />
			<Route path="*" element={<Navigate to="/register" replace />} />
		</Routes>
	)
}