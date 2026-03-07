import { getApiError, getHttpClient, type ApiResponse } from "../../common/api/http-client";
import { formatDateAsLocalISO, mapTaskResponse, mapCreateTaskRequest } from "./mappers";
import type { Task } from "./task";
import type { CreateTaskRequest, ListTasksRequest } from "./task-api-requests";

export class TaskApi {
    private static httpClient = getHttpClient()

    static async listTasks(request: ListTasksRequest): Promise<ApiResponse<Task[]>> {
        await sleep(2000)
        try {
            const response = await TaskApi.httpClient.get('/tasks', { params: {
                userID: request.userId,
                scheduledDate: formatDateAsLocalISO(request.date),
                page: request.page,
                size: request.pageSize
            }})
            return { data: response.data.map(mapTaskResponse) }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo obtener la lista de tareas.') }
        }
    }

    static async startTask(userId: string, taskId: string): Promise<ApiResponse<Task>> {
        await sleep(2000)
        try {
            const response = await TaskApi.httpClient.patch(`/tasks/start`, { userId, taskId })
            return { data: mapTaskResponse(response.data) }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo iniciar la tarea.') }
        }
    }

    static async finishTask(userId: string, taskId: string): Promise<ApiResponse<Task>> {
        await sleep(2000)
        try {
            const response = await TaskApi.httpClient.patch(`/tasks/finish`, { userId, taskId })
            return { data: mapTaskResponse(response.data) }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo completar la tarea.') }
        }
    }

    static async createTask(request: CreateTaskRequest): Promise<ApiResponse<Task>> {
        await sleep(2000)
        try {
            const response = await TaskApi.httpClient.post('/tasks', mapCreateTaskRequest(request))
            return { data: mapTaskResponse(response.data) }
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo crear la tarea.') }
        }
    }

    static async deleteTask(userId: string, taskId: string): Promise<ApiResponse<void>> {
        await sleep(2000)
        try {
            await TaskApi.httpClient.delete('/tasks', {
                params: {
                    userID: userId,
                    taskID: taskId,
                }
            })
            return {}
        }
        catch (error) {
            return { error: getApiError(error, 'No se pudo eliminar la tarea.') }
        }
    }

}

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

export const TaskApiErrors: Record<string, string> = {
    'REACHED_LIMIT_TASK': 'ReachedLimitTaskAssigment',
    'REACHED_LIMIT_START_TASK': 'ReachedLimitStartTask'
}