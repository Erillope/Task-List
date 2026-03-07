import { Routes, Route } from "react-router-dom"
import { ProtectedRoute } from "../../common/routes/ProtectedRoute"
import { ProfilePage } from "../pages/ProfilePage"

export const ProfileRoutes = () => {

    return <Routes>
        <Route
            path="/"
            element={
                <ProtectedRoute>
                    <ProfilePage />
                </ProtectedRoute>
            }
        />
    </Routes>
}