package com.rustamft.tasksft.data.util

import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun <I, O> I.convertTo(outputType: Class<O>): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, outputType)
}

internal suspend fun <I, O> List<I>.convertToListOf(outputType: Class<O>): List<O> {
    return coroutineScope {
        this@convertToListOf.map { element ->
            async { element.convertTo(outputType) }
        }.awaitAll()
    }
}

internal fun <I, O> Flow<I>.convertToFlowOf(outputType: Class<O>): Flow<O> {
    return this.map { value ->
        value.convertTo(outputType)
    }
}

internal fun <I, O> Flow<List<I>>.convertToFlowOfListOf(outputType: Class<O>): Flow<List<O>> {
    return this.map { value ->
        coroutineScope {
            value.map { element ->
                async {
                    element.convertTo(outputType)
                }
            }.awaitAll()
        }
    }
}
