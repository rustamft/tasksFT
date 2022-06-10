package com.rustamft.tasksft.presentation.screen.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.GetListOfTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.screen.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getListOfTasksUseCase: GetListOfTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val errorChannel = Channel<String>()
    val errorFlow = errorChannel.receiveAsFlow()

    private val navArgs = savedStateHandle.navArgs<EditorScreenNavArgs>()
    private val taskFlow = if (navArgs.indexOfTaskInList == -1) {
        emptyFlow()
    } else {
        getListOfTasksUseCase.execute().map { it[navArgs.indexOfTaskInList] }
    }

    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var taskReminderIsSet by mutableStateOf(false)
    var taskReminderCalendar: Calendar by mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR_OF_DAY, 1)
        }
    )

    init {
        viewModelScope.launch {
            withTimeout(5000) {
                taskFlow.collect { task ->
                    with(task) {
                        taskTitle = title
                        taskDescription = description
                        taskReminderIsSet = reminder != 0L
                        taskReminderCalendar = Calendar.getInstance().apply {
                            timeInMillis = reminder
                        }
                    }
                }
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            saveTaskUseCase.execute(task = Task()) // TODO: implement saving
        }
    }
}
