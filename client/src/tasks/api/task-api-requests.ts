import type { TaskPriority, Time } from "./task"

export interface CreateTaskRequest {
    userId: string
    name: string
    date: Date
    startTime: Time
    endTime: Time
    priority: TaskPriority
}

export interface ListTasksRequest {
    userId: string
    date: Date
    page: number
    pageSize: number
}