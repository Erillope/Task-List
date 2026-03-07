import { Box, Button } from "@mui/material"
import { InputText, PasswordInputText } from "../../../common/ui/InputText"
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import '../../styles/auth-user-form.css'
import { useLoginUser } from "../../hooks/useLoginUser";

export interface LoginUserProps {
    onSubmit?: () => void
}

export const LoginUserForm = (props: LoginUserProps) => {
    const formController = useLoginUser({ onSubmit: props.onSubmit })

    return <Box component={'form'} className="auth-user-form">
        
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
            {formController.isLoadingSubmit ? 'Iniciando sesión...' : 'Iniciar sesión'}
        </Button>
        
    </Box>

}