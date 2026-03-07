import { Box, Button, Typography } from "@mui/material"
import type { Task } from "../../api/task"
import '../../styles/task-info.css'
import { useTaskInfo } from "../../hooks/useTaskInfo"

export interface TaskInfoProps {
    task?: Task
    width?: number | string
    height?: number | string
    onStartedTask?: (task: Task) => Promise<Task | void>
    onFinishedTask?: (task: Task) => Promise<Task | void>
    onDeletedTask?: (task: Task) => Promise<void>
}

export const TaskInfo = (props: TaskInfoProps) => {
    if (!props.task) {
        return <EmptyTaskInfo {...props} />
    }

    return <TaskInfoView {...props} />

}

export const TaskInfoView = (props: TaskInfoProps) => {
    const taskInfoController = useTaskInfo({ 
        task: props.task!,
        onStartedTask: props.onStartedTask,
        onFinishedTask: props.onFinishedTask,
        onDeletedTask: props.onDeletedTask,
     })
    
    const task = taskInfoController.task
    const actionLabel = task.progress === 'Pendiente' ? 'Empezar' : 'Finalizar'
    const actionColor = actionLabel === 'Empezar' ? 'success' : 'error'
    const handleAction = actionLabel === 'Empezar' ? taskInfoController.onStart : taskInfoController.onFinish

    return (
        <Box
            className="task-info-view"
            width={props.width}
            height={props.height ?? '100%'}
        >
            <Typography variant="h6" className="task-info-title">
                {task.name}
            </Typography>

            <Box className="task-info-grid">
                <InfoRow label="Fecha programada" value={getDateLabel(task.scheduledDate)} />
                <InfoRow label="Hora inicio" value={getTimeLabel(task.startTime.hours, task.startTime.minutes)} />
                <InfoRow label="Hora fin" value={getTimeLabel(task.endTime.hours, task.endTime.minutes)} />
                <InfoRow label="Prioridad" value={task.priority} />
                <InfoRow label="Progreso" value={task.progress} />
            </Box>

            {task.progress !== 'Completada' &&
                <Box className="task-info-actions">
                    <Button
                        variant="contained"
                        color={actionColor}
                        className="task-info-action-button"
                        onClick={() => task && handleAction?.(task)}
                        disabled={taskInfoController.isLoadingTaskAction || taskInfoController.isDeleting}
                    >
                        {taskInfoController.isLoadingTaskAction ? 'Cargando...' : actionLabel}
                    </Button>
                    <Button
                        variant="outlined"
                        color="error"
                        className="task-info-action-button"
                        onClick={() => taskInfoController.onDelete(task)}
                        disabled={taskInfoController.isLoadingTaskAction || taskInfoController.isDeleting}
                    >
                        {taskInfoController.isDeleting ? 'Eliminando...' : 'Eliminar'}
                    </Button>
                </Box>
            }

            {task.progress === 'Completada' &&
                <Box className="task-info-actions">
                    <Button
                        variant="outlined"
                        color="error"
                        className="task-info-action-button"
                        onClick={() => taskInfoController.onDelete(task)}
                        disabled={taskInfoController.isLoadingTaskAction || taskInfoController.isDeleting}
                    >
                        {taskInfoController.isDeleting ? 'Eliminando...' : 'Eliminar'}
                    </Button>
                </Box>
            }

        </Box>
    )
}

const EmptyTaskInfo = (props: TaskInfoProps) => {
    return <Box
        className="task-info-empty"
        width={props.width}
        height={props.height ?? '100%'}
    >
        <Typography variant="body1" className="text">
            Ninguna tarea seleccionada.
        </Typography>

    </Box>
}

interface InfoRowProps {
    label: string
    value: string
}

const InfoRow = (props: InfoRowProps) => {
    return (
        <Box className="task-info-row">
            <Typography variant="body2" className="task-info-label">
                {props.label}
            </Typography>
            <Typography variant="body2" className="task-info-value">
                {props.value}
            </Typography>
        </Box>
    )
}

const getTimeLabel = (hours: number, minutes: number): string =>
    `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`

const getDateLabel = (value: Date): string => {
    const date = new Date(value)
    return new Intl.DateTimeFormat('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    }).format(date)
}