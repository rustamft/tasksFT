package com.rustamft.tasksft.database.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.rustamft.tasksft.database.entity.Task
import com.rustamft.tasksft.database.room.TasksRoomDao
import com.rustamft.tasksft.utils.datetime.DateTimeProvider
import com.rustamft.tasksft.utils.displayToast
import com.rustamft.tasksft.utils.schedule.TasksWorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TasksRoomRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workManager: TasksWorkManager,
    private val dao: TasksRoomDao
) : Repo<Task> {

    override suspend fun insert(entity: Task) {
        withContext(Dispatchers.IO) {
            launch { handleReminderWithToast(entity) }
            launch { dao.insert(entity.copy(isNew = false)) }
        }
    }

    override suspend fun insert(list: List<Task>) {
        withContext(Dispatchers.IO) {
            for (task in list) {
                launch { handleReminder(task) }
            }
            launch { dao.insert(list) }
        }
    }

    override suspend fun update(entity: Task) {
        withContext(Dispatchers.IO) {
            launch { handleReminderWithToast(entity) }
            launch { dao.update(entity) }
        }
    }

    override suspend fun delete(entity: Task) {
        withContext(Dispatchers.IO) {
            launch { workManager.cancel(entity) }
            launch { dao.delete(entity) }
        }
    }

    override suspend fun delete(list: List<Task>) {
        withContext(Dispatchers.IO) {
            for (task in list) {
                launch { workManager.cancel(task) }
            }
            launch { dao.delete(list) }
        }
    }

    override suspend fun getById(id: Int): Task = dao.get(id)

    override fun getAll(): Flow<List<Task>> = dao.getAll()

    override suspend fun getFinished(): List<Task> = dao.getFinished()

    private fun handleReminder(task: Task): Boolean {
        if (!task.isNew) {
            workManager.cancel(task)
        }
        val hasReminder = !task.isFinished && DateTimeProvider.isInFuture(task.reminder)
        if (hasReminder) {
            workManager.scheduleOneTime(task)
        }
        return hasReminder
    }

    private fun handleReminderWithToast(task: Task) {
        val hasReminder = handleReminder(task)
        if (hasReminder) {
            val message = DateTimeProvider.buildUntilReminderString(context, task)
            Handler(Looper.getMainLooper()).post {
                context.displayToast(message)
            }
        }
    }
}
