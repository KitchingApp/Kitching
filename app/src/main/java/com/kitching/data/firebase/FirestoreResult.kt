package com.kitching.data.firebase

sealed class FirebaseResult<out T> {
    data object DummyConstructor : FirebaseResult<Nothing>()

    data object Loading : FirebaseResult<Nothing>()

    data class Success<out T>(
        val data: T
    ) : FirebaseResult<T>()

    data class Failure(
        val throwable: Throwable
    ) : FirebaseResult<Nothing>()
}