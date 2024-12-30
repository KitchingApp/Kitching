package com.kitching.data.firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend fun <T, R> fetchFirebaseDataFlow(
    fetcher: suspend () -> List<T>,
    mapper: suspend (T) -> R
): Flow<FirebaseResult<MutableList<R>>> = flow {
    emit(FirebaseResult.Loading) // Loading 상태 emit
    runCatching {
        fetcher().map { mapper(it) }.toMutableList()
    }.fold(
        onSuccess = { emit(FirebaseResult.Success(it)) },
        onFailure = { emit(FirebaseResult.Failure(it)) }
    )
}