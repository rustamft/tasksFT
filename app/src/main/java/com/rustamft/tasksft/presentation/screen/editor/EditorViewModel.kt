package com.rustamft.tasksft.presentation.screen.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.usecase.GetTaskByIdUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.model.MutableTask
import com.rustamft.tasksft.presentation.screen.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val errorChannel = Channel<String>()
    val errorFlow = errorChannel.receiveAsFlow()

    private val navArgs = savedStateHandle.navArgs<EditorScreenNavArgs>()
    private val taskFlow = if (navArgs.taskId == -1) {
        emptyFlow()
    } else {
        getTaskByIdUseCase.execute(taskId = navArgs.taskId)
    }

    val mutableTask = MutableTask()

    init {
        viewModelScope.launch {
            withTimeout(5000) {
                taskFlow.collect { task ->
                    mutableTask.setFieldsFromTask(task = task)
                }
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            kotlin.runCatching {
                saveTaskUseCase.execute(task = mutableTask.toTask())
            }.onFailure {
                errorChannel.send(it.message.toString())
            }
        }
    }
}
