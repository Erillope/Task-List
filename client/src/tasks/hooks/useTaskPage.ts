import { useCallback, useEffect, useState } from "react"
import { TaskApi, TaskApiErrors } from "../api/TaskApi"
import type { Task, TaskPriority } from "../api/task"
import type { CreateTaskRequest, ListTasksRequest } from "../api/task-api-requests"
import Cookies from "js-cookie"
import type { ResponseError } from "../../common/api/http-client"
import { toast } from "react-toastify"
import type { DaysBarProps } from "../components/ui/DaysBar"
import type { TaskListProps } from "../components/info/TaskList"
import type { TaskInfoProps } from "../components/info/TaskInfo"
import type { AddTaskModalProps } from "../components/ui/AddTaskModal"

export interface TaskController {
    daysBarProps: DaysBarProps
    taskListProps: TaskListProps
    taskInfoProps: TaskInfoProps
    addTaskModalProps: AddTaskModalProps
    openAddTaskModal: () => void
}

export const useTaskPage = (): TaskController => {
    const registerdUser = Cookies.get('registeredUser')
    const parsedUser = registerdUser ? JSON.parse(registerdUser) : null
    const userId = parsedUser!.id

    const [selectedDate, setSelectedDate] = useState(new Date())
    const [isAddTaskModalOpen, setIsAddTaskModalOpen] = useState(false)
    const [tasks, setTasks] = useState<Task[]>([])
    const [selectedTask, setSelectedTask] = useState<Task | undefined>(undefined)

    const buildListTasksRequest = (): ListTasksRequest => {
        return {
            userId: userId,
            date: selectedDate,
            page: 0,
            pageSize: 100,
        }
    }

    const refreshTasks = useCallback(async (): Promise<Task[]> => {
        const response = await TaskApi.listTasks(buildListTasksRequest())
        if (!response.data) return []

        const tasks = response.data
        setTasks(tasks)
        setSelectedTask(undefined)
        return tasks
    }, [selectedDate])

    useEffect(() => {
        refreshTasks()
    }, [refreshTasks])

    const onAddTask = async(formData: CreateTaskRequest) => {
        const response = await TaskApi.createTask(formData)
        if (response.error) {
            handleAddTaskApiError(response.error, formData.priority)
            return
        }

        if (!response.data) return
        const createdTask = response.data
        toast("Tarea creada con éxito", { type: 'success' });
        setIsAddTaskModalOpen(false)
        
        if (createdTask.scheduledDate.getDate() == selectedDate.getDate()) {
            setTasks(prevTasks => [...prevTasks, createdTask])
        }
        
    }

    const onStartedTask = async (task: Task) => {
        const response = await TaskApi.startTask(userId, task.id)
        if (response.error) {
            handleApiError(response.error)
            return
        }
        if (!response.data) return

        const updatedTask = response.data
        setTasks(prevTasks => prevTasks.map(t => t.id === updatedTask.id ? updatedTask : t))
        setSelectedTask(updatedTask)

        return updatedTask
    }

    const onFinishedTask = async (task: Task) => {
        const response = await TaskApi.finishTask(userId, task.id)
        if (response.error) {
            handleApiError(response.error)
            return
        }
        if (!response.data) return

        const updatedTask = response.data
        setTasks(prevTasks => prevTasks.map(t => t.id === updatedTask.id ? updatedTask : t))
        setSelectedTask(updatedTask)

        return updatedTask
    }

    const onDeletedTask = async (task: Task) => {
        const response = await TaskApi.deleteTask(userId, task.id)
        if (response.error) {
            handleApiError(response.error)
            return
        }

        setTasks(prevTasks => prevTasks.filter(t => t.id !== task.id))
        setSelectedTask(undefined)
        toast('Tarea eliminada con éxito', { type: 'success' })
    }

    const handleApiError = (error: ResponseError) => {
        if (error.errorName === TaskApiErrors.REACHED_LIMIT_START_TASK) {
            toast(`Has alcanzado el límite de tareas que puedes empezar`, { type: 'error' });
        }
    }

    const handleAddTaskApiError = (error: ResponseError, priority: TaskPriority) => {
        if (error.errorName == TaskApiErrors.REACHED_LIMIT_TASK) {
            toast(`Has alcanzado el límite de tareas ${priority} para hoy`, { type: 'error' });
        }
    }

    return {
        daysBarProps: {
            selectedDate,
            onDateChange: setSelectedDate,
        },
        taskListProps: {
            tasks,
            onSelectTask: setSelectedTask,
            loadTasks: refreshTasks,
            width: '50%',
        },
        taskInfoProps: {
            task: selectedTask!,
            onStartedTask,
            onFinishedTask,
            onDeletedTask,
        },
        addTaskModalProps: {
            isOpen: isAddTaskModalOpen,
            onClose: () => setIsAddTaskModalOpen(false),
            onAddTask,
        },
        openAddTaskModal: () => setIsAddTaskModalOpen(true),
    }

}