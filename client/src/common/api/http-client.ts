import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8082'

export const getHttpClient = () => axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
})

export interface ApiResponse<T> {
    data?: T;
    error?: ResponseError
}

export interface ResponseError {
    message: string;
    errorName: string;
}

export const getApiError = (error: unknown, fallbackMessage: string): ResponseError => {
    console.error('API Error:', error)
    if (!axios.isAxiosError(error)) {
        return { message: fallbackMessage, errorName: fallbackMessage }
    }

    const data = error.response?.data

    if (typeof data === 'string' && data.trim() !== '') {
        return { message: data, errorName: data }
    }

    if (data && typeof data === 'object') {
        const typedData = data as Record<string, unknown>
        const message = typedData.message
        const errorName = typedData.errorName

        if (typeof message === 'string' && typeof errorName === 'string') {
            return { message, errorName }
        }
    }

    return { message: fallbackMessage, errorName: fallbackMessage }
}