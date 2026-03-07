import { useState } from "react";
import { useInputText, type InputTextController } from "../../common/hooks/ui/useInputText";
import { AuthApi, AuthApiErrors } from "../api/AuthApi";
import type { RegisterUserRequest } from "../api/auth-api-requests";
import { UserValidator } from "../validators/UserValidator";
import Cookies from "js-cookie";
import type { ResponseError } from "../../common/api/http-client";

export interface UseRegisterOptions {
    onSubmit?: () => void
}

export interface RegisterUserFormController {
    userNameProps: InputTextController
    accountProps: InputTextController
    passwordProps: InputTextController
    onSubmit: () => void
    isLoadingSubmit: boolean
}

export const useRegisterUser = (options?: UseRegisterOptions): RegisterUserFormController => {

    const userNameController = useInputText('')
    const accountController = useInputText('')
    const passwordController = useInputText('')
    const { validateForm } = userFormValidator(
        userNameController,
        accountController,
        passwordController
    )
    const [isLoadingSubmit, setIsLoadingSubmit] = useState(false)

    const buildRequest = (): RegisterUserRequest => {
        return {
            userName: userNameController.value,
            account: accountController.value,
            password: passwordController.value
        }
    }

    const onSubmit = async () => {
        if (!validateForm()) return
        const request = buildRequest()

        setIsLoadingSubmit(true)
        const response = await AuthApi.registerUser(request)
        setIsLoadingSubmit(false)

        if (response.error) handleApiError(response.error)
        if (!response.data) return

        const user = response.data
        Cookies.set('registeredUser', JSON.stringify(user), { expires: 7 })
        options?.onSubmit?.()
    }

    const handleApiError = (error: ResponseError) => {
        if (error.errorName == AuthApiErrors.USER_ALREADY_EXISTS) {
            accountController.setError('Ya existe una cuenta registrada con ese correo o número de teléfono')
        }
    }

    return {
        userNameProps: userNameController,
        accountProps: accountController,
        passwordProps: passwordController,
        onSubmit,
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
        const emptyMessage = 'La contraseña es requerida'
        if (!passwordController.validateEmpty(emptyMessage)) return false

        const invalidMessage = 'La contraseña debe tener al menos 8 caracteres, incluyendo letras y números'
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