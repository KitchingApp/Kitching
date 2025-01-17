package com.kitching.domain.repository

import com.kitching.data.dto.NoticeDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(teamId: String): Flow<FirebaseResult<List<NoticeDTO>>>

    fun createNotice(userId: String, teamId: String, title: String, content: String): Flow<FirebaseResult<Boolean>>

    fun updateNotice(noticeId: String,title: String, content: String): Flow<FirebaseResult<Boolean>>

    fun deleteNotice(noticeId: String): Flow<FirebaseResult<Boolean>>
}