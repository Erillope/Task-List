import  { Box, Stack, Typography, Chip } from "@mui/material"
import type { Task, TaskProgress, TaskPriority } from "../../api/task"

export interface TaskCardProps {
    task: Task
    onSelect?: (task: Task) => void
    isSelected?: boolean
}

export const TaskCard = (props: TaskCardProps) => {

    const startTimeLabel = getTimeLabel(props.task.startTime.hours, props.task.startTime.minutes)
    const endTimeLabel = getTimeLabel(props.task.endTime.hours, props.task.endTime.minutes)
    
    return <Box 
        className={`task-card ${props.isSelected ? 'selected' : ''}`}
        onClick={() => props.onSelect?.(props.task)}
    >

        <Stack spacing={1}>

            <Typography variant="h5">
                {props.task.name}
            </Typography>


            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                <Chip
                    size="small"
                    label={props.task.priority}
                    color={priorityColors[props.task.priority]}
                />
                <Chip
                    size="small"
                    label={props.task.progress}
                    color={progressColors[props.task.progress]}
                />
            </Stack>

            <Typography variant="body2" className="time">
                {startTimeLabel} - {endTimeLabel}
            </Typography>

        </Stack>

    </Box>

}

const progressColors: Record<TaskProgress, 'default' | 'warning' | 'success'> = {
    'Pendiente': 'default',
    'En Progreso': 'warning',
    'Completada': 'success'
}

const priorityColors: Record<TaskPriority, 'default' | 'error' | 'warning' | 'info' | 'success'> = {
    'Inamovible': 'error',
    'Importante': 'warning',
    'Urgente': 'error',
    'Normal': 'default',
    'Secundaria': 'info',
    'Posponible': 'success'
}

const getTimeLabel = (hours: number, minutes: number): string =>
    `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`