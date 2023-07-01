package com.rustamft.tasksft.presentation.global

import com.rustamft.tasksft.presentation.model.UIText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

class SnackbarFlow {

    private val snackbarFlow = MutableSharedFlow<UIText>(
        replay = 0,
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun tryEmit(message: UIText) = snackbarFlow.tryEmit(message)
    suspend fun emit(message: UIText) = snackbarFlow.emit(message)
    suspend fun collect(collector: FlowCollector<UIText>): Nothing = snackbarFlow.collect(collector)
}
