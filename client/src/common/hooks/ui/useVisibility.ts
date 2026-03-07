import { useState } from "react"

export interface VisibilityController {
    visible: boolean;
    toggleVisibility: () => void;
}

export const useVisibility = (initialValue: boolean = false): VisibilityController => {
    const [visible, setVisible] = useState(initialValue)

    const toggleVisibility = () => {
        setVisible(!visible)
    }

    return { visible, toggleVisibility }
}