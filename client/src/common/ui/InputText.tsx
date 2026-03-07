import { Box, InputAdornment, TextField, Typography } from "@mui/material"
import LockPersonIcon from '@mui/icons-material/LockPerson';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import { useVisibility } from "../hooks/ui/useVisibility";
import '../styles/input-text.css'
import { memo } from "react";

export interface InputTextProps {
    label?: string
    width?: string | number
    name?: string
    placeholder?: string
    error?: string
    value?: string
    type?: string
    onChange?: (value: string) => void
    leftIcon?: React.ReactNode
    rightIcon?: React.ReactNode
}

export const InputText = memo((props: InputTextProps) => {

    return <Box className="input-text">

        <Typography variant="body1" paddingLeft={'0.5rem'}>
            {props.label}
        </Typography>

        <TextField
            name={props.name}
            placeholder={props.placeholder}
            variant="outlined"
            value={props.value}
            type={props.type}
            onChange={(e) => props.onChange?.(e.target.value)}
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
                }
            }}
            className="text-field"
            sx={ {width: props.width || '100%'} }
            error={!!props.error}
            helperText={props.error}
        />

    </Box>

})

export const PasswordInputText = (props: InputTextProps) => {
    const { visible, toggleVisibility } = useVisibility()

    return <InputText
        {...props}
        type={visible ? "text" : "password"}
        leftIcon={<LockPersonIcon />}
        rightIcon={
            visible ? (
                <VisibilityOffIcon
                    onClick={toggleVisibility}
                    sx={{ cursor: 'pointer' }}
                />
            ) : (
                <VisibilityIcon
                    onClick={toggleVisibility}
                    sx={{ cursor: 'pointer' }}
                />
            )
        }
    />

}