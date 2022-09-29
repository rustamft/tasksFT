package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun <I, O> I.mapTo(outputType: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, outputType)
}

internal suspend fun <I, O> List<I>.mapToListOf(outputType: Class<O>): List<O> {
    return coroutineScope {
        this@mapToListOf.map { element ->
            async { element.mapTo(outputType) }
        }.awaitAll()
    }
}

internal fun <I, O> Flow<I>.mapToFlowOf(outputType: Class<O>): Flow<O> {
    return this.map { value ->
        value.mapTo(outputType)
    }
}

internal fun <I, O> Flow<List<I>>.mapToFlowOfListOf(outputType: Class<O>): Flow<List<O>> {
    return this.map { value ->
        coroutineScope {
            value.map { element ->
                async {
                    element.mapTo(outputType)
                }
            }.awaitAll()
        }
    }
}
