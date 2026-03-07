import { Box, InputAdornment, TextField, Typography } from "@mui/material"
import type { ReactNode } from "react"
import { memo } from "react"
import type { Time } from "../../tasks/api/task"
import '../styles/input-text.css'

export interface TimeInputProps {
    label?: string
    width?: string | number
    name?: string
    error?: string
    value?: Time
    onChange?: (value: Time) => void
    leftIcon?: ReactNode
    rightIcon?: ReactNode
}

export const TimeInput = memo((props: TimeInputProps) => {

    const handleChange = (value: string) => {
        if (!value) return

        const [hours, minutes] = value.split(':').map(Number)
        props.onChange?.({
            hours,
            minutes
        })
    }

    return <Box className="input-text">

        <Typography variant="body1" paddingLeft={'0.5rem'}>
            {props.label}
        </Typography>

        <TextField
            type="time"
            name={props.name}
            variant="outlined"
            value={toTimeString(props.value)}
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
            className={`text-field time-input ${!props.value ? 'empty' : ''}`}
            style={{ width: props.width || '100%' }}
            error={!!props.error}
            helperText={props.error}
        />

    </Box>

})

const toTimeString = (value?: Time): string => {
    if (!value) return ''

    return `${String(value.hours).padStart(2, '0')}:${String(value.minutes).padStart(2, '0')}`
}