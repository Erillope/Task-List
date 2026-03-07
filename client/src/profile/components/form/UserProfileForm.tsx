import { Box, Button, FormControlLabel, Switch } from "@mui/material"
import { InputText, PasswordInputText } from "../../../common/ui/InputText"
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import '../../../auth/styles/auth-user-form.css'
import { useProfile } from "../../hooks/form/useProfile";

export const UserProfileForm = () => {
    const formController = useProfile()

    return <Box component={'form'} className="auth-user-form">

        <InputText
            name="userName"
            placeholder="Ej: Juan Pérez"
            label="Nombre de usuario"
            leftIcon={<AccountCircleIcon />}
            {...formController.userNameProps}
        />

        <InputText
            name="account"
            placeholder="Ej: correo@dominio.com"
            label="Cuenta (email o celular)"
            leftIcon={<ManageAccountsIcon />}
            {...formController.accountProps}
        />

        <PasswordInputText
            placeholder="********"
            label="Contraseña"
            {...formController.passwordProps}
        />

        <FormControlLabel
            control={
                <Switch
                    checked={formController.user.isVip}
                    onChange={(_, checked) => formController.onToggleVip(checked)}
                />
            }
            label="Usuario VIP"
        />

        <Button
            className="submit-button"
            onClick={formController.onSubmit}
            disabled={formController.isLoadingSubmit}
        >
            {formController.isLoadingSubmit ? 'Guardando...' : 'Guardar'}
        </Button>

    </Box>
}