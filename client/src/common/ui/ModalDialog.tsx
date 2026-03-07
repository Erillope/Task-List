import { Box, IconButton, Modal } from '@mui/material'
import CloseIcon from '@mui/icons-material/Close'
import type { ReactNode } from 'react'
import '../styles/modal-dialog.css'

export interface ModalDialogProps {
    width?: number | string
    height?: number | string
    children?: ReactNode
    isOpen?: boolean
    onClose?: () => void
}

export const ModalDialog = (props: ModalDialogProps) => {
    return (
        <Modal open={props.isOpen ?? false} onClose={props.onClose}>
            <Box
                width={props.width}
                height={props.height} 
                className="modal-dialog">

                <IconButton className="modal-dialog-close" onClick={props.onClose}>
                    <CloseIcon fontSize="small" />
                </IconButton>
                
                {props.children}
            </Box>
        </Modal>
    )
}