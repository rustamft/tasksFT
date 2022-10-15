package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.notification.TaskNotificationScheduler
import com.rustamft.tasksft.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SaveTaskUseCaseTest {

    private val taskRepository = mockkClass(TaskRepository::class)
    private val taskNotificationScheduler = mockkClass(TaskNotificationScheduler::class)
    private val saveTaskUseCase = SaveTaskUseCase(
        taskRepository = taskRepository,
        taskNotificationScheduler = taskNotificationScheduler
    )

    @Test
    fun `task is rescheduled correctly`() {
        suspend fun test(task: Task, shouldSchedule: Boolean) {
            var taskScheduled = false
            coEvery { taskRepository.saveTask(task) } answers {}
            every { taskNotificationScheduler.cancel(task) } answers {}
            every { taskNotificationScheduler.scheduleOneTime(task) } answers {
                taskScheduled = true
            }
            saveTaskUseCase.execute(task)
            assert(taskScheduled == shouldSchedule) {
                throw AssertionError(
                    "ERROR: taskScheduled is $taskScheduled for $task"
                )
            }
        }
        runBlocking {
            test(
                task = Task(finished = false, reminder = 1L),
                shouldSchedule = true
            )
            test(
                task = Task(finished = false, reminder = -1L),
                shouldSchedule = false
            )
            test(
                task = Task(finished = true, reminder = 1L),
                shouldSchedule = false
            )
        }
    }
}
