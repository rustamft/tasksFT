package com.rustamft.tasksft.domain.service

interface TaskBackupService {

    fun startExport(run: () -> Unit)
    fun startImport(run: () -> Unit)
}
