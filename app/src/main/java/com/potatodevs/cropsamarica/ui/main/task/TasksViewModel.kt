package com.potatodevs.cropsamarica.ui.main.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.ui.utils.OneTimeEvents
import com.potatodevs.cropsamarica.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class TasksViewModel  @Inject constructor(
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository,
    private val riceFieldRepository: RiceFieldRepository
): ViewModel() {

    private var _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()
    private val _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        loadTask()
    }
    fun events(e : TaskEvent) {
        when(e) {
            is TaskEvent.LoadTask -> loadTask()
            is TaskEvent.OnSelected -> _state.value = _state.value.copy(selectedIndex = e.index)
            is TaskEvent.OnCreateTask -> createTask(e.task,e.result)
            is TaskEvent.OnDeleteTask -> deleteTask(e.taskId)
            is TaskEvent.OnUpdateTask -> updatetask(e.task)
            is TaskEvent.OnTaskSelected -> _state.value = _state.value.copy(selectedTask = e.task)
        }
    }


    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.delete(taskId).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun updatetask(task: Task) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.update(task).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))

            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun createTask(
        task: Task,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            taskRepository.create(task, result)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTask() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUser() ?: return@launch
            riceFieldRepository.getAllByUid(uid.uid)
                .onStart {
                    _state.update { it.copy(isLoading = true) }
                }
                .flatMapLatest { data ->
                    // Update rice fields in state
                    _state.update { it.copy(riceFields = data) }


                    val riceFields: List<RiceField> = data.mapNotNull { it.riceField }

                    if (riceFields.isNotEmpty()) {
                        taskRepository.getAll(riceFields)
                    } else {
                        flowOf(emptyList())
                    }
                }
                .onEach { tasks ->
                    _state.update { it.copy(isLoading = false, tasks = tasks) }
                }
                .launchIn(this) // launch in the coroutine scope
        }
    }

}