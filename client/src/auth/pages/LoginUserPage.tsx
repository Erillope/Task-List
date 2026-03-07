import { Box, Typography, Link } from "@mui/material"
import { LoginUserForm } from "../components/forms/LoginUserForm"
import '../styles/auth-user-page.css'
import { useNavigate } from "react-router-dom"

export const LoginUserPage = () => {
    const navigate = useNavigate()

    return <Box component={'main'} className="auth-user-page">

        <Typography variant="h1" className="title">
            Inicio de sesión
        </Typography>

        <Typography variant="body1" className="info">
            Ingresa tus datos para acceder a tu cuenta.
        </Typography>

        <LoginUserForm onSubmit={() => navigate('/tasks')} />

        <Typography variant="body2" marginTop={1} className="info">
            ¿No tienes una cuenta?{' '}
            <Link href="/register" className="link">
                Regístrate
            </Link>
        </Typography>

    </Box>

}