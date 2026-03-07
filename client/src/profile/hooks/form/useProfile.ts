import { useState } from "react";
import type { User } from "../../../auth/api/user";
import { useInputText, type InputTextController } from "../../../common/hooks/ui/useInputText";
import type { UpdateUserProfileRequest } from "../../api/profile-api-requests";
import { ProfileApi, ProfileApiErrors } from "../../api/ProfileApi";
import { UserValidator } from "../../../auth/validators/UserValidator";
import Cookies from "js-cookie";
import type { ResponseError } from "../../../common/api/http-client";
import { toast } from "react-toastify";

export interface UserProfileFormController {
    user: User,
    accountProps: InputTextController,
    userNameProps: InputTextController,
    passwordProps: InputTextController,
    onToggleVip: (checked: boolean) => void,
    onSubmit: () => void,
    isLoadingSubmit: boolean
}


export const useProfile = (): UserProfileFormController => {
    const registeredUser = Cookies.get('registeredUser')
    const parsedUser: User = registeredUser ? JSON.parse(registeredUser) : null

    const [user, setUser] = useState<User>(parsedUser)
    const accountProps = useInputText(user.account)
    const userNameProps = useInputText(user.name)
    const passwordProps = useInputText()
    const [isLoadingSubmit, setIsLoadingSubmit] = useState(false)

    const { validateForm } = userFormValidator(
        userNameProps,
        accountProps,
        passwordProps
    )

    const buildRequest = (): UpdateUserProfileRequest => {
        return {
            id: user.id,
            account: accountProps.value,
            name: userNameProps.value,
            isVip: user.isVip,
            password: passwordProps.isEmpty() ? undefined : passwordProps.value
        }
    }

    const onSubmit = async() => {
        if (!validateForm()) return
        const request = buildRequest()

        setIsLoadingSubmit(true)
        const response = await ProfileApi.updateProfile(request)
        setIsLoadingSubmit(false)

        if (response.error) {
            handleApiError(response.error)
            return
        }
        if (!response.data) return

        const updatedUser = response.data
        Cookies.set('registeredUser', JSON.stringify(updatedUser), { expires: 7 })
        setUser(updatedUser)
        toast("Usuario guardado con éxito", { type: 'success' });
    }

    const handleApiError = (error: ResponseError) => {
        if (error.errorName == ProfileApiErrors.USER_ALREADY_EXISTS) {
            accountProps.setError('Ya existe una cuenta registrada con ese correo o número de teléfono')
        }
    }

    return {
        user,
        accountProps,
        userNameProps,
        onToggleVip: (checked: boolean) => setUser((previous) => ({ ...previous, isVip: checked })),
        onSubmit,
        passwordProps,
        isLoadingSubmit
    }
}

const userFormValidator = (
    userNameController: InputTextController,
    accountController: InputTextController,
    passwordController: InputTextController
) => {

    const validateForm = (): boolean => {
        clearErrors()
        let isValid = validateUserName()
        isValid = validateAccount() && isValid
        isValid = validatePassword() && isValid
        return isValid
    }

    const validateUserName = (): boolean => {
        const emptyMessage = 'El nombre de usuario es requerido'
        const invalidMessage = 'El nombre de usuario no es válido'

        if (!userNameController.validateEmpty(emptyMessage)) return false

        if (!userNameController.applyValidation(UserValidator.validateUserName, invalidMessage)) {
            return false
        }
        return true
    }

    const validateAccount = (): boolean => {
        const emptyMessage = 'La cuenta es requerida'
        const invalidMessage = 'La cuenta debe ser un correo Gmail o un número de teléfono válido'
        
        if (!accountController.validateEmpty(emptyMessage)) return false
        
        if (!accountController.applyValidation(UserValidator.validateAccount, invalidMessage)) {
            return false
        }
        return true
    }

    const validatePassword = (): boolean => {
        const invalidMessage = 'La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula y un número'

        if (passwordController.isEmpty()) return true

        if (!passwordController.applyValidation(UserValidator.validatePassword, invalidMessage)) {
            return false
        }
        return true
    }

    const clearErrors = () => {
        userNameController.clearError()
        accountController.clearError()
        passwordController.clearError()
    }

    return {
        validateForm
    }
}