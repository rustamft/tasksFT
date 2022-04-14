package com.rustamft.tasksft.database.repository

import kotlinx.coroutines.flow.Flow

interface Repo<T> {

    suspend fun insert(entity: T)
    suspend fun insert(list: List<T>)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun delete(list: List<T>)
    suspend fun getById(id: Int): T
    fun getAll(): Flow<List<T>>
    suspend fun getFinished(): List<T>
}
