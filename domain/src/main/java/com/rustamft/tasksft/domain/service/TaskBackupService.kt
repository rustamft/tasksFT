package com.rustamft.tasksft.domain.service

interface TaskBackupService {

    enum class OperationType {
        EXPORT,
        IMPORT
    }

    fun start(operationType: OperationType, block: suspend () -> Unit)
}
