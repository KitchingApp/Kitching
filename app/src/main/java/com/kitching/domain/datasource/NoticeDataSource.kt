package com.kitching.domain.datasource

import com.kitching.domain.entities.NoticeInfo

interface NoticeDataSource {
    suspend fun getNoticeInfos(teamId: String): Result<List<NoticeInfo>>

    suspend fun createNotice(userId: String, teamId: String, title: String, content: String): Boolean

    suspend fun updateNotice(noticeId: String, title: String, content: String): Boolean

    suspend fun deleteNotice(noticeId: String): Boolean
}