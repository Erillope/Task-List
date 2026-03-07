
export interface RegisterUserRequest {
    userName: string
    account: string
    password: string
}

export interface LoginUserRequest {
    account: string
    password: string
}