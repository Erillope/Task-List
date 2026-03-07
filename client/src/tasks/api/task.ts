
export interface Task {
    id: string
    userId: string
    name: string
    scheduledDate: Date
    startTime: Time
    endTime: Time
    priority: TaskPriority
    progress: TaskProgress
}

export interface Time {
    hours: number
    minutes: number
}

export type TaskPriority = 'Inamovible' | 'Importante' | 'Urgente' | 'Normal' | 'Secundaria' | 'Posponible'

export type TaskProgress = 'Pendiente' | 'En Progreso' | 'Completada'