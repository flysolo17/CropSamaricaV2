package com.potatodevs.cropsamarica.repositories.tasks

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.potatodevs.cropsamarica.models.rice.RiceField
import com.potatodevs.cropsamarica.models.rice.RiceStage
import com.potatodevs.cropsamarica.models.tasks.Task
import com.potatodevs.cropsamarica.models.tasks.TaskStatus
import com.potatodevs.cropsamarica.ui.utils.TASKS_COLLECTION

import com.potatodevs.cropsamarica.ui.utils.UIState
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val auth : FirebaseAuth
): TaskRepository {
    private val taskRef = firestore.collection(TASKS_COLLECTION)

    override suspend fun insert(task: Task): Result<String> {
        return try {
            task.id = taskRef.document().id
            taskRef.document(task.id).set(task).await()
            Result.success("Task inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(
        task: Task,
        result: (UIState<String>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            result.invoke(UIState.Error("User not found"))
            return
        }
        result.invoke(UIState.Loading)
        val id = taskRef.document().id
        task.id = id
        task.uid = uid
        taskRef.document(id).set(task).addOnCompleteListener {
            result.invoke(
                UIState.Success("Task created successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun statusChange(
        id: String,
        status: TaskStatus,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        taskRef.document(id).update("status", status).addOnCompleteListener {
            result.invoke(
                UIState.Success("Task status updated successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun update(task: Task): Result<String> {
        return try {
            taskRef.document(task.id).set(task).await()
            Result.success("Task updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(id: String): Result<String> {
        return try {
            taskRef.document(id).delete().await()
            Result.success("Task deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertAll(tasks: List<Task>): Result<String> {
        return try {
            val batch = firestore.batch()

            tasks.forEach { task ->
                val taskId = taskRef.document().id
                val taskWithId = task.copy(id = taskId)
                batch.set(taskRef.document(taskId), taskWithId)
            }

            batch.commit().await()
            Result.success("Tasks inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getByFieldId(fieldId: String): Flow<List<Task>> {
        return callbackFlow {
            val listener = taskRef
                .whereEqualTo("fieldId", fieldId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                        trySend(tasks)
                    }
                }
            awaitClose {
                listener.remove()

            }
        }
    }

    override fun getAllByFieldIds(fieldIds: List<String>): Flow<List<Task>> {
        return callbackFlow {
            val listener = taskRef
                .whereIn("fieldId", fieldIds)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                        trySend(tasks)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override fun getAll(riceField: List<RiceField>): Flow<List<Task>> {
        return callbackFlow {
            val fieldIds = riceField.map { it.id }
            val listener = taskRef
                .whereIn("fieldId", fieldIds)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                    val riceFieldTasks = tasks.filter {
                        it.fieldId in fieldIds
                    }

                    trySend(riceFieldTasks)
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override suspend fun getTasksByCropIdAndStage(
        fieldId: String,
        stage: RiceStage
    ): Result<List<Task>> {
        return try {
            val tasks = taskRef
                .whereEqualTo("fieldId", fieldId)
                .whereEqualTo("stage", stage)
                .get()
                .await()
                .toObjects(Task::class.java)
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFertilizerApplications(id: String): Result<List<Task>> {
        return try {
            val tasks = taskRef
                .whereEqualTo("fieldId", id)
                .whereEqualTo("fertilizer",true)
                .orderBy("startDate", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects<Task>()
            Result.success(tasks)
        } catch (e: Exception) {
            Log.d(
                "TaskRepositoryImpl",
                "getFertilizerApplications: ${e.message}"
            )
            Result.failure(e)
        }
    }
}