package com.kitching.domain.datasource

import com.kitching.domain.entities.Notice

interface NoticeDataSource {
    suspend fun getNotices(teamId: String): Result<List<Notice>>

    suspend fun createNotice(userId: String, teamId: String, title: String, content: String): Boolean

    suspend fun updateNotice(noticeId: String, title: String, content: String): Boolean

    suspend fun deleteNotice(noticeId: String): Boolean
}