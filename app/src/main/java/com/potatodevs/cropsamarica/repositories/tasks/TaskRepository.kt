package com.potatodevs.cropsamarica.repositories.tasks

import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus

import com.potatodevs.cropsamarica.ui.utils.UIState
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insert(task: Task): Result<String>
    suspend fun create(task: Task, result: (UIState<String>) -> Unit)
    suspend fun statusChange(id : String, status: TaskStatus, result: (UIState<String>) -> Unit)
    suspend fun update(task: Task) : Result<String>
    suspend fun delete(id : String) : Result<String>
    suspend fun insertAll(tasks: List<Task>): Result<String>
    fun getByFieldId(fieldId: String): Flow<List<Task>>

    fun getAllByFieldIds(
        fieldIds: List<String>
    ) : Flow<List<Task>>

    fun getAll(
        riceField : List<RiceField>
    ): Flow<List<Task>>

    suspend fun getTasksByCropIdAndStage(
        fieldId : String,
        stage : RiceStage
    ) : Result<List<Task>>


    suspend fun getFertilizerApplications(
        id : String,
    ) : Result<List<Task>>

    suspend fun getFertilizerTasks(
        ids : List<String>,
    ) : Result<List<Task>>



}