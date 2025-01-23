package com.kitching.domain.datasource

import com.kitching.domain.entities.User

interface UserDataSource {
    suspend fun getUser(userId: String): User?

    suspend fun checkAndSaveUser(userId: String, userName: String, userImage: String): Boolean
}