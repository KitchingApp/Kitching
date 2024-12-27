package com.kitchingapp.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val INTERVAL_TIME = 3000L

fun <T> Flow<T>.throttleFirst() : Flow<T> = flow{
    var throttleTime = 0L
    collect { upFlow ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > INTERVAL_TIME) {
            throttleTime = currentTime
            emit(upFlow)
        }
    }
}