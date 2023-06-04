package com.rustamft.tasksft.domain.usecase

import com.rustamft.tasksft.domain.global.defaultSort
import com.rustamft.tasksft.domain.repository.BackupRepository
import com.rustamft.tasksft.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class ExportTasksUseCase(
    private val backupRepository: BackupRepository,
    private val tasksRepository: TaskRepository
) {

    private val filePrefix = "tasksFT backup"
    private val fileExtension = "json"

    suspend fun execute(directoryUriString: String) {
        val currentTime = getTimeByUnits(Calendar.getInstance()).joinToString(separator = "-")
        val fileName = "$filePrefix $currentTime.$fileExtension"
        backupRepository.save(
            fileName = fileName,
            tasks = tasksRepository.getAll().first().defaultSort(),
            directoryUriString = directoryUriString
        )
    }

    private fun getTimeByUnits(calendar: Calendar) = arrayOf(
        calendar.get(Calendar.YEAR).toString(),
        calendar.get(Calendar.MONTH).plus(1).format(2),
        calendar.get(Calendar.DAY_OF_MONTH).format(2),
        calendar.get(Calendar.HOUR_OF_DAY).format(2),
        calendar.get(Calendar.MINUTE).format(2),
        calendar.get(Calendar.SECOND).format(2)
    )

    private fun Int.format(fraction: Int) = String.format("%0${fraction}d", this)
}
