package com.kitching.data.repository

import com.kitching.data.datasource.OrderDataSourceImpl
import com.kitching.data.dto.OrderDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.OrderDataSource
import com.kitching.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(
    private val orderDataSource: OrderDataSource = OrderDataSourceImpl()
) : OrderRepository {
    override fun getOrderList(categoryId: String): Flow<FirebaseResult<List<OrderDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val orderList = orderDataSource.getOrderList(categoryId)
        if (orderList.isEmpty()) emit(FirebaseResult.Success(emptyList()))
        else emit(FirebaseResult.Success(orderList.map {
            OrderDTO(
                categoryId = it.categoryId,
                orderId = it.id,
                orderName = it.name
            )
        }))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun createOrder(categoryId: String, orderName: String): Flow<FirebaseResult<Boolean>> =
        flow {
            emit(FirebaseResult.Loading)
            val result = orderDataSource.createOrder(categoryId, orderName)
            emit(FirebaseResult.Success(result))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }

    override fun deleteOrder(orderId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = orderDataSource.deleteOrder(orderId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateOrder(orderId: String, orderName: String): Flow<FirebaseResult<Boolean>> =
        flow {
            emit(FirebaseResult.Loading)
            val result = orderDataSource.updateOrder(orderId, orderName)
            emit(FirebaseResult.Success(result))
        }.catch {
            emit(FirebaseResult.Failure(it))
        }
}