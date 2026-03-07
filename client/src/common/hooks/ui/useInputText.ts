import { useCallback, useMemo, useState } from "react"

export interface InputTextController {
    value: string;
    error: string;
    onChange: (newValue: string) => void;
    hasError: () => boolean;
    isEmpty: () => boolean;
    setError: (error: string) => void;
    clearError: () => void;
    validateEmpty: (errorMessage: string) => boolean;
    applyValidation: (validationFn: (value: string) => boolean, errorMessage: string) => boolean;
}

export const useInputText = (initialValue: string = ''): InputTextController => {

    const [value, setValue] = useState(initialValue);
    const [error, setError] = useState('')

    const validateEmpty = useCallback((errorMessage: string): boolean => {
        if (value.trim() === '') {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const applyValidation = useCallback((validationFn: (value: string) => boolean, errorMessage: string): boolean => {
        if (!validationFn(value)) {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const hasError = useCallback(() => !!error, [error])
    const isEmpty = useCallback(() => value.trim() === '', [value])
    const clearError = useCallback(() => setError(''), [])

    return useMemo(() => ({
        value,
        error,
        onChange: setValue,
        hasError,
        isEmpty,
        setError,
        clearError,
        validateEmpty,
        applyValidation
    }), [value, error, hasError, isEmpty, clearError, validateEmpty, applyValidation])
}