import { Box, IconButton, TextField, Typography } from '@mui/material'
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew'
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import '../../styles/days-bar.css'
import '../../../common/styles/input-text.css'
import { useDaysBar } from '../../hooks/useDaysBar'

export interface DaysBarProps {
    selectedDate?: Date
    onDateChange?: (date: Date) => void
}


export const DaysBar = (props: DaysBarProps) => {
    const daysBarController = useDaysBar({ selectedDate: props.selectedDate, onDateChange: props.onDateChange })

    return (
        <Box className="days-bar">

            <Box className="days-container">

                <IconButton onClick={daysBarController.onPrevDay} className='arrow-button'>
                    <ArrowBackIosNewIcon fontSize="small" />
                </IconButton>

                <Box className="bar-container">
                    {daysBarController.visibleDates.map((date) => {
                        const isSelected = toIsoDate(date) === toIsoDate(daysBarController.selectedDate)

                        return (
                            <DayBox
                                key={toIsoDate(date)}
                                date={date}
                                isSelected={isSelected}
                                onSelect={daysBarController.onDateChange}
                            />
                        )
                    })}
                </Box>

                <IconButton onClick={daysBarController.onNextDay} className='arrow-button'>
                    <ArrowForwardIosIcon fontSize="small" />
                </IconButton>

            </Box>

            <TextField
                type="date"
                size="small"
                label="Calendario"
                className="date-picker"
                value={toIsoDate(daysBarController.selectedDate)}
                onChange={(event) => daysBarController.handlePickDate(event.target.value)}
                slotProps={{
                    inputLabel: {
                        shrink: true
                    }
                }}
            />

        </Box>
    )
}

interface DayBoxProps {
    date: Date,
    isSelected?: boolean,
    onSelect?: (date: Date) => void
}

const DayBox = (props: DayBoxProps) => {
    const isToday = toIsoDate(props.date) === toIsoDate(new Date())

    return <Box
        key={toIsoDate(props.date)}
        onClick={() => props.onSelect?.(props.date)}
        className={`day-box ${props.isSelected ? 'selected' : ''} ${isToday ? 'today' : ''}`}
    >
        <Typography variant="caption" className='day-text'>
            {weekDayFormatter.format(props.date)}
        </Typography>

        <Typography variant="body2" fontWeight={600}>
            {monthDayFormatter.format(props.date)}
        </Typography>

    </Box>

}


const toIsoDate = (date: Date): string => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
}

const weekDayFormatter = new Intl.DateTimeFormat('es-ES', {
    weekday: 'short'
})

const monthDayFormatter = new Intl.DateTimeFormat('es-ES', {
    day: '2-digit',
    month: '2-digit'
})