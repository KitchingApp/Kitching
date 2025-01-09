package com.kitching.common

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ListAdapter
import com.kitching.common.util.ProgressDialog
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

fun <T>Fragment.firebaseResultHandler(
    firebaseResult: FirebaseResult<T>,
    onSuccess: (T) -> Unit
) {
    when (firebaseResult) {
        is FirebaseResult.Success -> {
            onSuccess(firebaseResult.data)
            ProgressDialog.cancel()
        }
        is FirebaseResult.Failure -> {
            ProgressDialog.cancel()
            commonToast("오류로 인해 데이터를 가져오지 못했습니다. : ${firebaseResult.throwable}")
        }
        else -> {
            ProgressDialog.show(requireContext())
        }
    }
}

fun <T>DialogFragment.firebaseResultHandler(
    firebaseResult: FirebaseResult<T>,
    onSuccess: (T) -> Unit
) {
    when (firebaseResult) {
        is FirebaseResult.Success -> {
            onSuccess(firebaseResult.data)
            ProgressDialog.cancel()
        }
        is FirebaseResult.Failure -> {
            ProgressDialog.cancel()
            commonToast("오류로 인해 데이터를 가져오지 못했습니다. : ${firebaseResult.throwable}")
        }
        else -> {
            ProgressDialog.show(requireContext())
        }
    }
}