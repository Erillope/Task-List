import { Box, IconButton, Stack } from '@mui/material'
import { TaskList } from '../components/info/TaskList'
import { DaysBar } from '../components/ui/DaysBar'
import { TaskInfo } from '../components/info/TaskInfo'
import { useTaskPage } from '../hooks/useTaskPage'
import AddIcon from '@mui/icons-material/Add';
import '../styles/task-home.css'
import { AddTaskModal } from '../components/ui/AddTaskModal'
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { useNavigate } from 'react-router-dom'

export const TaskHomePage = () => {

    const navigate = useNavigate()
    const taskController = useTaskPage()

    return <Box component="main" className='task-home'>

        <Stack
            direction={'row'}
            spacing={3}
            className='header'
        >
            <DaysBar {...taskController.daysBarProps} />
            <IconButton
                className='add-task-button'
                onClick={taskController.openAddTaskModal}
            >
                <AddIcon />
            </IconButton>
            <IconButton
                className='profile-button'
                onClick={() => navigate('/profile')}
            >
                <AccountCircleIcon />
            </IconButton>

        </Stack>

        <Stack direction={'row'} className='tasks' >
            <TaskList width={'50%'} {...taskController.taskListProps}/>
            <TaskInfo width={'30%'} {...taskController.taskInfoProps} />
        </Stack>

        <AddTaskModal width={'30%'} {...taskController.addTaskModalProps} />

    </Box>

}