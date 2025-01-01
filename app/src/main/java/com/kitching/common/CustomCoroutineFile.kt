package com.kitching.common

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

const val INTERVAL_TIME = 300L

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

fun View.throttleClicks(lifecycleOwner: LifecycleOwner, onEachAction: () -> Unit) =
    clicks().throttleFirst().onEach { onEachAction() }.launchIn(lifecycleOwner.lifecycleScope)