
import { Box, InputAdornment, TextField, Typography } from "@mui/material"
import type { ReactNode } from "react"
import { memo } from "react"
import '../styles/input-text.css'

export interface CalendarInputProps {
    label?: string
    width?: string | number
    name?: string
    error?: string
    value?: Date
    onChange?: (value: Date) => void
    leftIcon?: ReactNode
    rightIcon?: ReactNode
}

export const CalendarInput = memo((props: CalendarInputProps) => {

    const handleChange = (value: string) => {
        if (!value) return

        const [year, month, day] = value.split('-').map(Number)
        props.onChange?.(new Date(year, month - 1, day))
    }

    return <Box className="input-text">

        <Typography variant="body1" paddingLeft={'0.5rem'}>
            {props.label}
        </Typography>

        <TextField
            type="date"
            name={props.name}
            variant="outlined"
            value={toIsoDate(props.value)}
            onChange={(e) => handleChange(e.target.value)}
            slotProps={{
                input: {
                    startAdornment: props.leftIcon ? (
                        <InputAdornment position="start" className="start-icon">
                            {props.leftIcon}
                        </InputAdornment>
                    ) : undefined,
                    endAdornment: props.rightIcon ? (
                        <InputAdornment position="end" className="end-icon">
                            {props.rightIcon}
                        </InputAdornment>
                    ) : undefined
                },
                inputLabel: {
                    shrink: true
                }
            }}
            className={`text-field calendar-input ${!props.value ? 'empty' : ''}`}
            style={{ width: props.width || '100%' }}
            error={!!props.error}
            helperText={props.error}
        />

    </Box>

})

const toIsoDate = (date?: Date): string => {
    if (!date) return ''

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
}