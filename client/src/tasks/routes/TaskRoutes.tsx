
import { Route, Routes } from "react-router-dom"
import { ProtectedRoute } from "../../common/routes/ProtectedRoute"
import { TaskHomePage } from "../pages/TaskHomePage"

export const TaskRoutes = () => {
	return (
		<Routes>
			<Route
				path="/"
				element={
					<ProtectedRoute>
						<TaskHomePage />
					</ProtectedRoute>
				}
			/>
		</Routes>
	)
}