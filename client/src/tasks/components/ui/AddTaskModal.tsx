import { Box, Typography } from '@mui/material'
import { ModalDialog } from '../../../common/ui/ModalDialog'
import { AddTaskForm } from '../forms/AddTaskForm'
import '../../styles/add-task-form.css'
import type { CreateTaskRequest } from '../../api/task-api-requests'

export interface AddTaskModalProps {
    isOpen?: boolean
    width?: number | string
    onClose?: () => void
    onAddTask?: (formData: CreateTaskRequest) => Promise<void>
}

export const AddTaskModal = (props: AddTaskModalProps) => {
    return (
        <ModalDialog isOpen={props.isOpen} width={props.width} onClose={props.onClose}>
            <Box className='add-task-modal'>
                <Typography variant="h4" fontWeight={'bold'}>
                    Agregar nueva tarea
                </Typography>
                <AddTaskForm onAddTask={props.onAddTask} />
            </Box>
        </ModalDialog>
    )
}
