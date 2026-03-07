
import { Box, InputAdornment, MenuItem, TextField, Typography } from "@mui/material"
import type { ReactNode } from "react"
import { memo } from "react"
import '../styles/input-text.css'

export interface SelectInputProps {
    label?: string
    width?: string | number
    name?: string
    placeholder?: string
    error?: string
    value?: string
    allValues?: string[]
    onSelect?: (value: string) => void
    leftIcon?: ReactNode
    rightIcon?: ReactNode
}

export const SelectInput = memo((props: SelectInputProps) => {

    return <Box className="input-text">

        <Typography variant="body1" paddingLeft={'0.5rem'}>
            {props.label}
        </Typography>

        <TextField
            select
            name={props.name}
            variant="outlined"
            value={props.value ?? ''}
            onChange={(e) => props.onSelect?.(e.target.value)}
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
                select: {
                    displayEmpty: true,
                    renderValue: (selected) => {
                        if (!selected) {
                            return (
                                <Typography component="span" className="select-placeholder">
                                    {props.placeholder || 'Selecciona una opción'}
                                </Typography>
                            )
                        }
                        return selected as string
                    },
                    MenuProps: {
                        PaperProps: {
                            className: 'select-menu-paper'
                        }
                    }
                }
            }}
            className="text-field"
            style={{ width: props.width || '100%' }}
            error={!!props.error}
            helperText={props.error}
        >
            {(props.allValues ?? []).map((option) => (
                <MenuItem key={option} value={option}>
                    {option}
                </MenuItem>
            ))}
        </TextField>

    </Box>

})