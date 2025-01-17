package com.kitching.data.repository

import com.kitching.data.datasource.OrderDataSourceImpl
import com.kitching.data.dto.OrderCategoryDTO
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.OrderDataSource
import com.kitching.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(private val dataSource: OrderDataSource = OrderDataSourceImpl()): OrderRepository {
    /** OrderCategory */
    override fun getOrderCategory(teamId: String): Flow<FirebaseResult<List<OrderCategoryDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val orderCategories = dataSource.getOrderCategory(teamId).getOrThrow().map {
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
        val result = dataSource.createOrderCategory(teamId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteOrderCategory(categoryId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteOrderCategory(categoryId)
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
        val result = dataSource.updateOrderCategory(categoryId, categoryName, color)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun getOrderList(categoryId: String): Flow<FirebaseResult<List<OrderDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val orderList = dataSource.getOrderList(categoryId).getOrThrow().map {
            OrderDTO(
                categoryId = it.categoryId,
                orderId = it.id,
                orderName = it.name
            )
        }
        emit(FirebaseResult.Success(orderList))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createOrder(categoryId: String, orderName: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.createOrder(categoryId, orderName)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteOrder(orderId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteOrder(orderId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateOrder(orderId: String, orderName: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.updateOrder(orderId, orderName)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}