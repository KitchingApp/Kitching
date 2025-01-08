package com.kitching.data.firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * for List<T>
 *  fetcher: DB에서 Entity List 반환 /
 *  mapper: Entity를 DTO로 변환 /
 *  return: Flow<FirebaseResult<MutableList<DTO>>>
 *  */
suspend fun <T, R> fetchFirebaseDataFlow(
    fetcher: suspend () -> List<T>,
    mapper: suspend (T) -> R
): Flow<FirebaseResult<MutableList<R>>> = flow {
    emit(FirebaseResult.Loading) // Loading 상태 emit
//    Thread.sleep(3000) // progress indicator 확인용
    runCatching {
        fetcher().map { mapper(it) }.toMutableList()
    }.fold(
        onSuccess = { emit(FirebaseResult.Success(it)) },
        onFailure = { emit(FirebaseResult.Failure(it)) }
    )
}

/**
 * create, update, delete에 사용
 */
suspend fun fetchFirebaseDataFlow(
    fetcher: Boolean
): Flow<FirebaseResult<Boolean>> = flow {
    emit(FirebaseResult.Loading)
//    Thread.sleep(3000) // progressbar 확인용
    runCatching {
        fetcher
    }.fold(
        onSuccess = { emit(FirebaseResult.Success(it)) },
        onFailure = { emit(FirebaseResult.Failure(it)) }
    )
}