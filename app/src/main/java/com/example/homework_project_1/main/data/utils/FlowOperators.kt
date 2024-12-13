package com.example.homework_project_1.main.data.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.unique(): Flow<T> {
    return flow {
        var lastValue: Any? = NoValue
        collect { value: T->
            if (lastValue != value) {
                lastValue = value
                emit(value)
            }
        }
    }
}

private object NoValue