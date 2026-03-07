import { useState, useMemo } from "react"

export interface UseDaysBarOptions {
    selectedDate?: Date,
    onDateChange?: (date: Date) => void
}

export interface DaysBarController {
    visibleDates: Date[],
    selectedDate: Date
    onDateChange: (date: Date) => void
    onPrevDay: () => void,
    onNextDay: () => void,
    handlePickDate: (value: string) => void
}

export const useDaysBar = (options?: UseDaysBarOptions): DaysBarController => {
    const today = new Date()

    const [startOffset, setStartOffset] = useState(-3)
    const [selectedDate, setSelectedDate] = useState(options?.selectedDate ?? today)

    const visibleDates = useMemo(
        () => Array.from({ length: 7 }, (_, index) => addDays(today, startOffset + index)),
        [today, startOffset]
    )

    const onPrevDay = () => {
        setStartOffset((previous) => previous - 1)
    }

    const onNextDay = () => {
        setStartOffset((previous) => previous + 1)
    }

    const handlePickDate = (value: string) => {
        if (!value) return
        const nextDate = parseIsoDate(value)
        setSelectedDate(nextDate)
        options?.onDateChange?.(nextDate)
        setStartOffset(getDiffDays(today, nextDate))
    }

    return {
        visibleDates,
        selectedDate,
        onDateChange: (date: Date) => {
            setSelectedDate(date)
            options?.onDateChange?.(date)
        },
        handlePickDate,
        onPrevDay,
        onNextDay
    }
}

const addDays = (date: Date, days: number): Date => {
    const nextDate = new Date(date)
    nextDate.setDate(nextDate.getDate() + days)
    return nextDate
}

const parseIsoDate = (value: string): Date => {
    const [year, month, day] = value.split('-').map(Number)
    return new Date(year, month - 1, day)
}

const getDiffDays = (from: Date, to: Date): number => {
    const millisecondsPerDay = 24 * 60 * 60 * 1000
    return Math.round((to.getTime() - from.getTime()) / millisecondsPerDay)
}