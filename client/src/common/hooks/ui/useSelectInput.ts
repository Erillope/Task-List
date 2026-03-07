import { useCallback, useMemo, useState } from "react";
import type { SelectInputProps } from "../../ui/SelectInput";


export interface SelectInputController extends SelectInputProps {
    allValues: string[];
    value: string;
    error: string;
    setAllValues: (values: string[]) => void;
    onSelect: (value: string) => void;
    hasError: () => boolean;
    isEmpty: () => boolean;
    setError: (error: string) => void;
    clearError: () => void;
    validateEmpty: (errorMessage: string) => boolean;
    applyValidation: (validationFn: (value: string) => boolean, errorMessage: string) => boolean;
}

export const useSelectInput = (): SelectInputController => {

    const [allValues, setAllValues] = useState<string[]>([])
    const [value, setValue] = useState('')
    const [error, setError] = useState('')

    const isEmpty = useCallback(() => value.trim() === '', [value])

    const validateEmpty = useCallback((errorMessage: string) => {
        if (isEmpty()) {
            setError(errorMessage)
            return false
        }
        return true
    }, [isEmpty])

    const applyValidation = useCallback((validationFn: (value: string) => boolean, errorMessage: string) => {
        if (!validationFn(value)) {
            setError(errorMessage)
            return false
        }
        return true
    }, [value])

    const hasError = useCallback(() => !!error, [error])
    const clearError = useCallback(() => setError(''), [])
    const onChange = useCallback((nextValue: string) => setValue(nextValue), [])

    return useMemo(() => ({
        allValues,
        value,
        error,
        onSelect: onChange,
        onChange,
        hasError,
        isEmpty,
        setError,
        clearError,
        validateEmpty,
        applyValidation,
        setAllValues,
    }), [allValues, value, error, onChange, hasError, isEmpty, clearError, validateEmpty, applyValidation])

}