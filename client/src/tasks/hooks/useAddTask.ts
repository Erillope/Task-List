import { useEffect, useState } from "react"
import { useCalendarInput, type CalendarInputController } from "../../common/hooks/ui/useCalendarInput"
import { useInputText, type InputTextController } from "../../common/hooks/ui/useInputText"
import { useSelectInput, type SelectInputController } from "../../common/hooks/ui/useSelectInput"
import { useTimeInput, type TimeInputController } from "../../common/hooks/ui/useTimeInput"
import type { CalendarInputProps } from "../../common/ui/CalendarInput"
import type { InputTextProps } from "../../common/ui/InputText"
import type { SelectInputProps } from "../../common/ui/SelectInput"
import type { TimeInputProps } from "../../common/ui/TimeInput"
import type { CreateTaskRequest } from "../api/task-api-requests"
import { TaskValidator } from "../validators/TaskValidator"
import type { TaskPriority } from "../api/task"
import Cookies from "js-cookie"

export interface UseAddTaskOptions {
    onAddTask?: (formData: CreateTaskRequest) => Promise<void>
}

export interface AddTaskController {
    taskNameProps: InputTextProps
    taskPriorityProps: SelectInputProps
    taskDateProps: CalendarInputProps
    taskStartTimeProps: TimeInputProps
    taskEndTimeProps: TimeInputProps
    onSubmit: () => void
    isLoadingSubmit: boolean
}

export const useAddTask = (options?: UseAddTaskOptions): AddTaskController => {
    const registeredUser = Cookies.get('registeredUser')
    const parsedUser = registeredUser ? JSON.parse(registeredUser) : null
    const userId = parsedUser!.id

    const taskNameController = useInputText()
    const taskPriorityController = useSelectInput()
    const taskDateController = useCalendarInput()
    const taskEndTimeController = useTimeInput()
    const taskStartTimeController = useTimeInput()
    const [isLoadingSubmit, setIsLoadingSubmit] = useState(false)

    const { validateForm } = addTaskFormValidator(
        taskNameController,
        taskPriorityController,
        taskDateController,
        taskStartTimeController,
        taskEndTimeController
    )

    const init = () => {
        taskPriorityController.setAllValues(priorities)
    }

    useEffect(init, [])

    const buildRequest = (): CreateTaskRequest => {
        return {
            userId: userId,
            name: taskNameController.value!,
            priority: taskPriorityController.value as any,
            date: taskDateController.value!,
            startTime: taskStartTimeController.value!,
            endTime: taskEndTimeController.value!,
        }
    }

    const onSubmit = async() => {
        if (!validateForm()) return
        const request = buildRequest()

        setIsLoadingSubmit(true)
        await options?.onAddTask?.(request)
        setIsLoadingSubmit(false)
        
    }

    return {
        taskNameProps: taskNameController,
        taskPriorityProps: taskPriorityController,
        taskDateProps: taskDateController,
        taskStartTimeProps: taskStartTimeController,
        taskEndTimeProps: taskEndTimeController,
        onSubmit,
        isLoadingSubmit
    }

}

const addTaskFormValidator = (
    taskNameController: InputTextController,
    taskPriorityController: SelectInputController,
    taskDateController: CalendarInputController,
    taskStartTimeController: TimeInputController,
    taskEndTimeController: TimeInputController
) => {

    const validateForm = (): boolean => {
        clearErrors()
        let isValid = validateTaskName()
        isValid = validateTaskPriority() && isValid
        isValid = validateTaskTimes() && isValid
        isValid = validateTaskDate() && isValid
        return isValid
    }

    const validateTaskName = (): boolean => {
        const emptyMessage = 'El nombre de la tarea es requerido'
        const invalidMessage = 'El nombre de la tarea no es válido'

        if (!taskNameController.validateEmpty(emptyMessage)) return false
        
        if (!taskNameController.applyValidation(TaskValidator.validateName, invalidMessage)) return false

        return true
    }

    const validateTaskPriority = (): boolean => {
        const emptyMessage = 'La prioridad de la tarea es requerida'
        if (!taskPriorityController.validateEmpty(emptyMessage)) return false
        return true
    }

    const validateTaskDate = (): boolean => {
        const emptyMessage = 'La fecha de la tarea es requerida'
        const invalidMessage = 'La fecha de la tarea no puede ser en el pasado'

        if (!taskDateController.validateEmpty(emptyMessage)) return false

        if (!taskDateController.applyValidation((date) => TaskValidator.validateNewDate(date, taskStartTimeController.value!), invalidMessage)) return false
        
        return true
    }

    const validateTaskTimes = (): boolean => {
        const emptyStartMessage = 'La hora de inicio es requerida'
        const emptyEndMessage = 'La hora de fin es requerida'
        const invalidMessage = 'La hora de fin debe ser después de la hora de inicio'

        let isEmpty = taskStartTimeController.validateEmpty(emptyStartMessage)
        isEmpty = taskEndTimeController.validateEmpty(emptyEndMessage) && isEmpty
        
        if (!isEmpty) return false

        if (!TaskValidator.validateTimeRange(taskStartTimeController.value!, taskEndTimeController.value!)) {
            taskEndTimeController.setError(invalidMessage)
            return false
        }

        return true
    }

    const clearErrors = () => {
        taskNameController.clearError()
        taskPriorityController.clearError()
        taskDateController.clearError()
        taskStartTimeController.clearError()
        taskEndTimeController.clearError()
    }

    return {
        validateForm
    }

}

const priorities: TaskPriority[] = [
    'Inamovible',
    'Importante',
    'Urgente',
    'Normal',
    'Secundaria',
    'Posponible'
]