import { getApiError, getHttpClient, type ApiResponse } from "../../common/api/http-client";
import type { LoginUserRequest, RegisterUserRequest } from "./auth-api-requests";
import type { User } from "./user";

export class AuthApi {
    private static httpClient = getHttpClient()

    static async registerUser(request: RegisterUserRequest): Promise<ApiResponse<User>> {
        await sleep(2000)
        try {
            const response = await AuthApi.httpClient.post('/auth/register', request)
            return { data: response.data as User }

        } catch (error) {
            return { error: getApiError(error, 'No se pudo registrar el usuario.') }
        }
    }

    static async loginUser(request: LoginUserRequest): Promise<ApiResponse<User>> {
        await sleep(2000)
        try {
            const response = await AuthApi.httpClient.post('/auth/login', request)
            return { data: response.data as User }
        } catch (error) {
            return { error: getApiError(error, 'No se pudo iniciar sesión.') }
        }
    }

}

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

export const AuthApiErrors: Record<string, string> = {
    'USER_ALREADY_EXISTS': 'UserAlreadyRegistered',
    'INCORRECT_PASSWORD': 'IncorrectPassword',
    'USER_NOT_FOUND': 'UserNotFound'
}