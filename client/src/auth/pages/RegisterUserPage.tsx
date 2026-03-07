import { Box, Link, Typography } from "@mui/material"
import { RegisterUserForm } from "../components/forms/RegisterUserForm"
import '../styles/auth-user-page.css'
import { useNavigate } from "react-router-dom"

export const RegisterUserPage = () => {
	const navigate = useNavigate()
	
	return <Box component={'main'} className="auth-user-page">

		<Typography variant="h1" className="title">
			Registro de usuario
		</Typography>

		<Typography variant="body1" className="info">
			Completa tus datos para crear una cuenta.
		</Typography>

		<RegisterUserForm onSubmit={() => navigate('/tasks')} />

		<Typography variant="body2" marginTop={1} className="info">
			¿Ya tienes una cuenta?{' '}
			<Link href="/login" className="link">
				Inicia sesión
			</Link>
		</Typography>

	</Box>
}