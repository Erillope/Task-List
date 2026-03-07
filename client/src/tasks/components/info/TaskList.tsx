import { Box, CircularProgress, Stack, Typography } from "@mui/material"
import type { Task } from "../../api/task"
import '../../styles/task-info.css'
import { TaskCard } from "./TaskCard"
import { useTaskList } from "../../hooks/useTaskList"

export interface TaskListProps {
    tasks?: Task[]
    loadTasks?: () => Promise<Task[]>
    onSelectTask?: (task: Task) => void
    width?: number | string
    height?: number | string
}

export const TaskList = (props: TaskListProps) => {
    const taskListController = useTaskList({
        tasks: props.tasks,
        loadTasks: props.loadTasks,
        onSelectTask: props.onSelectTask
    })

    if (taskListController.isLoadingStart) {
        return <LoadingTaskList {...props} />
    }

    if (taskListController.tasks.length === 0) {
        return <EmptyTaskList {...props} />

    }

    return <TaskStack {...props} {...taskListController} />

}

interface TaskStackProps extends TaskListProps {
    selectedTaskId?: string
}

const TaskStack = (props: TaskStackProps) => {
    return <Stack
        spacing={1.5}
        className="task-list"
        width={props.width}
        height={props.height ?? '100%'}
    >
        {props.tasks?.map((task) => (
            <TaskCard
                key={task.id}
                task={task}
                onSelect={props.onSelectTask}
                isSelected={task.id === props.selectedTaskId}
            />
        ))}
    </Stack>

}


const LoadingTaskList = (props: TaskListProps) => {
    return <Box
        className="task-info-empty"
        width={props.width}
        height={props.height ?? '100%'}
    >
        <CircularProgress size={28} className="loading" />
        <Typography variant="body1" className="text">
            Cargando tareas...
        </Typography>
    </Box>
}


const EmptyTaskList = (props: TaskListProps) => {
    return <Box
        className="task-info-empty"
        width={props.width}
        height={props.height ?? '100%'}
    >
        <Typography variant="body1" className="text">
            No hay tareas para este día.
        </Typography>
    </Box>
}