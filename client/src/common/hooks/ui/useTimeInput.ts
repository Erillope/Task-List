import { useCallback, useMemo, useState } from "react";
import type { Time } from "../../../tasks/api/task";

export interface TimeInputController {
    value?: Time;
    error: string;
    onChange: (newValue: Time) => void;
    hasError: () => boolean;
    isEmpty: () => boolean;
    setError: (error: string) => void;
    clearError: () => void;
    validateEmpty: (errorMessage: string) => boolean;
    applyValidation: (validationFn: (value: Time) => boolean, errorMessage: string) => boolean;
}

export const useTimeInput = (): TimeInputController => {

    const [value, setValue] = useState<Time | undefined>(undefined);
    const [error, setError] = useState('')

    const validateEmpty = useCallback((errorMessage: string): boolean => {
        if (!value) {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const applyValidation = useCallback((validationFn: (value: Time) => boolean, errorMessage: string): boolean => {
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