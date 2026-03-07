import './App.css'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthRoutes } from './auth/routes/AuthRoutes'
import { TaskRoutes } from './tasks/routes/TaskRoutes'
import { ProfileRoutes } from './profile/routes/ProfileRoutes'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/*" element={<AuthRoutes />} />
        <Route path="/tasks/*" element={<TaskRoutes />} />
        <Route path='/profile/*' element={<ProfileRoutes />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
