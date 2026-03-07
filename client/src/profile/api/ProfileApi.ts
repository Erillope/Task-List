import type { User } from "../../auth/api/user";
import { getApiError, getHttpClient, type ApiResponse } from "../../common/api/http-client";
import type { UpdateUserProfileRequest } from "./profile-api-requests";

export class ProfileApi {
    private static httpClient = getHttpClient()

    public static async updateProfile(request: UpdateUserProfileRequest): Promise<ApiResponse<User>> {
        await sleep(2000)
        let response = await ProfileApi.changeProfileData(request)
        if (response.error) return response

        if (request.password) {
            const passwordResponse = await ProfileApi.changePassword(request)
            if (passwordResponse.error) return { error: passwordResponse.error }
        }

        if (request.isVip) {
            response = await ProfileApi.upgradeToVip(request)
            if (response.error) return response
        }

        else {
            response = await ProfileApi.downgradeFromVip(request)
            if (response.error) return response
        }

        return response
    }

    private static async changeProfileData(request: UpdateUserProfileRequest): Promise<ApiResponse<User>> {
        try {
            const response = await ProfileApi.httpClient.put('/users', {
                id: request.id,
                newAccount: request.account,
                newUserName: request.name
            })
            return { data: response.data as User }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo actualizar el perfil del usuario.') }
        }
    }
    
    private static async changePassword(request: UpdateUserProfileRequest): Promise<ApiResponse<void>> {
        try {
            await ProfileApi.httpClient.patch('/users/password', {
                id: request.id,
                newPassword: request.password
            })
            return { data: undefined }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo actualizar la contraseña del usuario.') }
        }
    }

    private static async upgradeToVip(request: UpdateUserProfileRequest): Promise<ApiResponse<User>> {
        try {
            const response = await ProfileApi.httpClient.patch('/users/upgrade-vip', request)
            return { data: response.data as User }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo actualizar el estado VIP del usuario.') }
        }
    }

    private static async downgradeFromVip(request: UpdateUserProfileRequest): Promise<ApiResponse<User>> {
        try {
            const response = await ProfileApi.httpClient.patch('/users/remove-vip', request)
            return { data: response.data as User }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo actualizar el estado VIP del usuario.') }
        }
    }

}

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

export const ProfileApiErrors: Record<string, string> = {
    'USER_ALREADY_EXISTS': 'UserAlreadyRegistered'
}