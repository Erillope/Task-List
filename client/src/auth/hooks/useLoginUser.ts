import { useState } from "react";
import { useInputText, type InputTextController } from "../../common/hooks/ui/useInputText";
import type { LoginUserRequest } from "../api/auth-api-requests";
import { AuthApi, AuthApiErrors } from "../api/AuthApi";
import type { ResponseError } from "../../common/api/http-client";
import Cookies from "js-cookie";

export interface UseLoginUserOptions {
    onSubmit?: () => void
}

export interface LoginUserFormController {
    accountProps: InputTextController,
    passwordProps: InputTextController,
    onSubmit: () => void,
    isLoadingSubmit: boolean
}

export const useLoginUser = (options?: UseLoginUserOptions): LoginUserFormController => {
    const accountController = useInputText('')
    const passwordController = useInputText('')
    const { validateForm } = loginFormValidator(accountController, passwordController)
    const [isLoadingSubmit, setIsLoadingSubmit] = useState(false)

    const buildRequest = (): LoginUserRequest => {
        return {
            account: accountController.value,
            password: passwordController.value
        }
    }

    const onSubmit = async () => {
        if (!validateForm()) return
        const request = buildRequest()

        setIsLoadingSubmit(true)
        const response = await AuthApi.loginUser(request)
        setIsLoadingSubmit(false)

        if (response.error) handleApiError(response.error)
        if (!response.data) return

        const user = response.data
        Cookies.set('registeredUser', JSON.stringify(user), { expires: 7 })
        options?.onSubmit?.()
    }

    const handleApiError = (error: ResponseError) => {
        if (error.errorName == AuthApiErrors.USER_NOT_FOUND) {
            accountController.setError('No se encontró una cuenta registrada con ese correo o número de teléfono')
        }
        if (error.errorName == AuthApiErrors.INCORRECT_PASSWORD) {
            passwordController.setError('La contraseña es incorrecta')
        }
    }

    return {
        accountProps: accountController,
        passwordProps: passwordController,
        onSubmit,
        isLoadingSubmit
    }
}

const loginFormValidator = (
    accountController: InputTextController,
    passwordController: InputTextController
) => {

    const validateForm = (): boolean => {
        clearErrors()

        let isValid = accountController.validateEmpty('La cuenta es requerida')
        isValid = passwordController.validateEmpty('La contraseña es requerida') && isValid

        return isValid

    }

    const clearErrors = () => {
        accountController.clearError()
        passwordController.clearError()
    }

    return {
        validateForm
    }
}