import type { Time } from "../api/task"

export class TaskValidator {

    private static readonly NAME_PATTERN = /^[a-zA-Z0-9._-]{3,}$/

    static validateName(name: string): boolean {
        return TaskValidator.NAME_PATTERN.test(name)
    }

    static validateNewDate(date: Date, startTime: Time): boolean {
        const now = new Date()
        const taskDateTime = new Date(date)

        taskDateTime.setHours(startTime.hours, startTime.minutes, 0, 0)
        return taskDateTime > now
    }

    static validateTimeRange(startTime: Time, endTime: Time): boolean {
        const startTotalMinutes = startTime.hours * 60 + startTime.minutes
        const endTotalMinutes = endTime.hours * 60 + endTime.minutes
        return endTotalMinutes > startTotalMinutes
    }
}