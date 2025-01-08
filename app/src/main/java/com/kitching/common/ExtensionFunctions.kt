package com.kitching.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> ViewModel.firebaseFlowHandler(
    stateFlow: MutableStateFlow<FirebaseResult<T>>,
    fetch: suspend () -> Flow<FirebaseResult<T>>
) {
    viewModelScope.launch {
        stateFlow.value = FirebaseResult.Loading
        fetch().collectLatest {
            stateFlow.value = it
        }
    }
}