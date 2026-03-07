import { Box, Button } from "@mui/material"
import { InputText } from "../../../common/ui/InputText"
import { SelectInput } from "../../../common/ui/SelectInput"
import { CalendarInput } from "../../../common/ui/CalendarInput"
import { TimeInput } from "../../../common/ui/TimeInput"
import '../../styles/add-task-form.css'
import { useAddTask } from "../../hooks/useAddTask"
import type { CreateTaskRequest } from "../../api/task-api-requests"

export interface AddTaskFormProps {
    width?: string | number
    onAddTask?: (formData: CreateTaskRequest) => Promise<void>
}

export const AddTaskForm = (props: AddTaskFormProps) => {
    const formController = useAddTask({ onAddTask: props.onAddTask })

    return <Box
        component={'form'}
        className="add-task-form"
        width={props.width}
    >

        <InputText
            name="taskName"
            placeholder="Ej: Comprar leche"
            label="Nombre de la tarea"
            {...formController.taskNameProps}
        />

        <SelectInput
            name="taskPriority"
            label="Prioridad"
            placeholder="Seleccione una prioridad"
            {...formController.taskPriorityProps}
        />

        <CalendarInput
            name="taskDate"
            label="Fecha a programar"
            {...formController.taskDateProps}
        />

        <TimeInput
            name="taskStartTime"
            label="Hora de inicio"
            {...formController.taskStartTimeProps}
        />

        <TimeInput
            name="taskEndTime"
            label="Hora de fin"
            {...formController.taskEndTimeProps}
        />

        <Button
            className="submit-button"
            onClick={formController.onSubmit}
            disabled={formController.isLoadingSubmit}
        >
            {formController.isLoadingSubmit ? 'Agregando...' : 'Agregar tarea'}
        </Button>

    </Box>

}