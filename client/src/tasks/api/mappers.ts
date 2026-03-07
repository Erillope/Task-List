import type { Task, TaskPriority, TaskProgress } from "./task"
import type { CreateTaskRequest } from "./task-api-requests"

export const mapCreateTaskRequest = (request: CreateTaskRequest): any => {
    return {
        userId: request.userId,
        taskName: request.name,
        taskScheduledDate: formatDateAsLocalISO(request.date),
        taskScheduledStartTime: `${request.startTime.hours.toString().padStart(2, '0')}:${request.startTime.minutes.toString().padStart(2, '0')}`,
        taskScheduledEndTime: `${request.endTime.hours.toString().padStart(2, '0')}:${request.endTime.minutes.toString().padStart(2, '0')}`,
        taskPriority: priorityNumber[request.priority]
    }
}

export const mapTaskResponse = (response: any): Task => {
    return {
        id: response.id,
        userId: response.userId,
        name: response.name,
        scheduledDate: parseLocalISODate(response.scheduledDate),
        startTime: {
            hours: parseInt(response.startTime.split(':')[0]),
            minutes: parseInt(response.startTime.split(':')[1])
        },
        endTime: {
            hours: parseInt(response.endTime.split(':')[0]),
            minutes: parseInt(response.endTime.split(':')[1])
        },
        priority: priorityByNumber[response.priority],
        progress: progressByNumber[response.progress]
    }
}

export const formatDateAsLocalISO = (date: Date): string => {
    const year = date.getFullYear()
    const month = (date.getMonth() + 1).toString().padStart(2, '0')
    const day = date.getDate().toString().padStart(2, '0')
    return `${year}-${month}-${day}`
}

export const parseLocalISODate = (value: string): Date => {
    const [year, month, day] = value.split('-').map((part) => parseInt(part, 10))
    return new Date(year, month - 1, day)
}

export const priorityNumber: Record<TaskPriority, number> = {
    'Inamovible': 5,
    'Importante': 4,
    'Urgente': 3,
    'Normal': 2,
    'Secundaria': 1,
    'Posponible': 0
}

export const priorityByNumber: Record<number, TaskPriority> = {
    5: 'Inamovible',
    4: 'Importante',
    3: 'Urgente',
    2: 'Normal',
    1: 'Secundaria',
    0: 'Posponible'
}

export const progressByNumber: Record<number, TaskProgress> = {
    0: 'Pendiente',
    1: 'En Progreso',
    2: 'Completada'
}