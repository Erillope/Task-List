import { useCallback, useMemo, useState } from "react";

export interface CalendarInputController {
    value?: Date;
    error: string;
    onChange: (newValue: Date) => void;
    hasError: () => boolean;
    isEmpty: () => boolean;
    setError: (error: string) => void;
    clearError: () => void;
    validateEmpty: (errorMessage: string) => boolean;
    applyValidation: (validationFn: (value: Date) => boolean, errorMessage: string) => boolean;
}

export const useCalendarInput = (): CalendarInputController => {

    const [value, setValue] = useState<Date | undefined>(undefined);
    const [error, setError] = useState('')

    const validateEmpty = useCallback((errorMessage: string): boolean => {
        if (!value) {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const applyValidation = useCallback((validationFn: (value: Date) => boolean, errorMessage: string): boolean => {
        if (!validationFn(value!)) {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const hasError = useCallback(() => !!error, [error])
    const isEmpty = useCallback(() => !value, [value])
    const clearError = useCallback(() => setError(''), [])

    return useMemo(() => ({
        value: value,
        error,
        onChange: setValue,
        hasError,
        isEmpty,
        setError,
        clearError,
        validateEmpty,
        applyValidation,
    }), [value, error, hasError, isEmpty, clearError, validateEmpty, applyValidation])
    
}