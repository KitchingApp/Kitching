package com.kitching.data.repository

import com.kitching.data.datasource.OrderCategoryDataSourceImpl
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.OrderCategoryDataSource
import com.kitching.domain.repository.OrderCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class OrderCategoryRepositoryImpl(
    private val orderCategoryDataSource: OrderCategoryDataSource = OrderCategoryDataSourceImpl(),
): OrderCategoryRepository {
    override fun getOrderCategory(teamId: String): Flow<FirebaseResult<List<OrderCategoryDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val orderCategories = orderCategoryDataSource.getOrderCategory(teamId).getOrThrow().map {
            OrderCategoryDTO(
                categoryId = it.id,
                categoryName = it.name,
                color = it.color,
            )
        }
        emit(FirebaseResult.Success(orderCategories))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createOrderCategory(
        teamId: String,
        categoryName: String,
        color: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = orderCategoryDataSource.createOrderCategory(teamId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteOrderCategory(categoryId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = orderCategoryDataSource.deleteOrderCategory(categoryId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateOrderCategory(
        categoryId: String,
        categoryName: String,
        color: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = orderCategoryDataSource.updateOrderCategory(categoryId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}