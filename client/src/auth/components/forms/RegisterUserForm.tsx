import { Box, Button } from "@mui/material"
import { InputText, PasswordInputText } from "../../../common/ui/InputText"
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import '../../styles/auth-user-form.css'
import { useRegisterUser } from "../../hooks/useRegisterUser";

export interface RegisterUserFormProps{
    onSubmit?: () => void
}

export const RegisterUserForm = (props: RegisterUserFormProps) => {

    const formController = useRegisterUser({ onSubmit: props.onSubmit })

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

        <Button
            className="submit-button"
            onClick={formController.onSubmit}
            disabled={formController.isLoadingSubmit}
        >
            {formController.isLoadingSubmit ? 'Registrando...' : 'Registrarme'}
        </Button>

    </Box>

}