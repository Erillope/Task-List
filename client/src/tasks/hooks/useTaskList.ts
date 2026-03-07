import { useCallback, useEffect, useState } from "react"
import type { Task } from "../api/task"

export interface UseTaskListOptions {
    tasks?: Task[]
    loadTasks?: () => Promise<Task[]>
    onSelectTask?: (task: Task) => void
}

export interface TaskListController {
    tasks: Task[]
    isLoadingStart: boolean
    onSelectTask: (task: Task) => void
    selectedTaskId?: string
    selectedTask: Task | undefined
}

export const useTaskList = (options?: UseTaskListOptions): TaskListController => {

    const [tasks, setTasks] = useState<Task[]>(options?.tasks ?? [])
    const [isLoadingStart, setIsLoadingStart] = useState(false)
    const [selectedTask, setSelectedTask] = useState<Task | undefined>(undefined)

    const initTasks = useCallback(async () => {
        setIsLoadingStart(true)
        const loadedTasks = options?.loadTasks ? await options.loadTasks() : []
        setTasks(loadedTasks)
        setIsLoadingStart(false)
    }, [options?.loadTasks])

    const onSelectTask = (task: Task) => {
        setSelectedTask(task)
        options?.onSelectTask?.(task)
    }

    useEffect(() => {
        initTasks()
    }, [options?.loadTasks, initTasks])

    useEffect(() => {
        if (!options?.tasks) return
        setTasks(options.tasks)
    }, [options?.tasks])

    return {
        tasks,
        isLoadingStart,
        onSelectTask,
        selectedTaskId: selectedTask?.id,
        selectedTask,
    }

}