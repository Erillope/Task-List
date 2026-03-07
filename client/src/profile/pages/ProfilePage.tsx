import { Box, Typography } from "@mui/material"
import { UserProfileForm } from "../components/form/UserProfileForm"

export const ProfilePage = () => {

    return <Box component={'main'} className="auth-user-page">

        <Typography variant="h1" className="title">
            Bienvenido!
        </Typography>

        <Typography variant="body1" className="info">
            Verifica los datos de tu cuenta.
        </Typography>

        <UserProfileForm />

    </Box>

}