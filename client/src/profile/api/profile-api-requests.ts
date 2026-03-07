
export interface UpdateUserProfileRequest {
    id: string,
    account: string,
    name: string,
    isVip: boolean,
    password?: string
}