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
            coEvery { taskRepository.save(task) } answers {}
            every { taskNotificationScheduler.cancel(task) } answers {}
            every { taskNotificationScheduler.schedule(task) } answers {
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
                task = Task(
                    id = 0,
                    created = 0L,
                    title = "",
                    description = "",
                    reminder = 1L,
                    repeatCalendarUnit = 0,
                    finished = false,
                    color = 0x00000000
                ),
                shouldSchedule = true
            )
            test(
                task = Task(
                    id = 0,
                    created = 0L,
                    title = "",
                    description = "",
                    reminder = -1L,
                    repeatCalendarUnit = 0,
                    finished = false,
                    color = 0x00000000
                ),
                shouldSchedule = false
            )
            test(
                task = Task(
                    id = 0,
                    created = 0L,
                    title = "",
                    description = "",
                    reminder = 1L,
                    repeatCalendarUnit = 0,
                    finished = true,
                    color = 0x00000000
                ),
                shouldSchedule = false
            )
        }
    }
}
