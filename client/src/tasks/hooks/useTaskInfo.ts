import { useEffect, useState } from "react";
import type { Task } from "../api/task";

export interface UseTaskInfoOptions {
    task: Task
    onStartedTask?: (task: Task) => Promise<Task | void>
    onFinishedTask?: (task: Task) => Promise<Task | void>
    onDeletedTask?: (task: Task) => Promise<void>
}

export interface TaskInfoController {
    task: Task
    onStart: (task: Task) => void
    onFinish: (task: Task) => void
    onDelete: (task: Task) => void
    isLoadingTaskAction: boolean,
    isDeleting: boolean,
}

export const useTaskInfo = (options: UseTaskInfoOptions): TaskInfoController => {
    const [task, setTask] = useState<Task>(options.task)
    const [isLoadingTaskAction, setIsLoadingTaskAction] = useState(false)
    const [isDeleting, setIsDeleting] = useState(false)

    useEffect(() => {
        setTask(options.task)
    }, [options.task])

    const onStart = async (task: Task) => {
        setIsLoadingTaskAction(true)
        const updatedTask = await options.onStartedTask?.(task)
        setIsLoadingTaskAction(false)

        if (updatedTask) {
            setTask(updatedTask)
        }
    }

    const onFinish = async (task: Task) => {
        setIsLoadingTaskAction(true)
        const updatedTask = await options.onFinishedTask?.(task)
        setIsLoadingTaskAction(false)

        if (updatedTask) {
            setTask(updatedTask)
        }
    }

    const onDelete = async (task: Task) => {
        setIsDeleting(true)
        await options.onDeletedTask?.(task)
        setIsDeleting(false)
    }
    
    return {
        task,
        isLoadingTaskAction,
        isDeleting,
        onStart,
        onFinish,
        onDelete,
    }
}