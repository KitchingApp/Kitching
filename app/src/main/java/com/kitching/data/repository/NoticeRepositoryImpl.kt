package com.kitching.data.repository

import com.kitching.common.util.dateFormatter
import com.kitching.data.datasource.NoticeDataSourceImpl
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.domain.datasource.NoticeDataSource
import com.kitching.domain.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class NoticeRepositoryImpl(private val dataSource: NoticeDataSource = NoticeDataSourceImpl()) : NoticeRepository {
    override fun getNotices(teamId: String): Flow<FirebaseResult<List<NoticeDTO>>> = flow {
        emit(FirebaseResult.Loading)
        val notices = dataSource.getNoticeInfos(teamId).getOrThrow().map {
            NoticeDTO(
                noticeId = it.notice.id,
                title = it.notice.title,
                content = it.notice.content,
                date = LocalDate.parse(it.notice.date, dateFormatter),
                writerId = it.notice.writerId,
                writerName = it.user.userName
            )
        }
        emit(FirebaseResult.Success(notices))
    }

    override fun createNotice(
        userId: String,
        teamId: String,
        title: String,
        content: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.createNotice(userId, teamId, title, content)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun updateNotice(
        noticeId: String,
        title: String,
        content: String
    ): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.updateNotice(noticeId, title, content)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }

    override fun deleteNotice(noticeId: String): Flow<FirebaseResult<Boolean>> = flow {
        emit(FirebaseResult.Loading)
        val result = dataSource.deleteNotice(noticeId)
        emit(FirebaseResult.Success(result))
    }.catch {
        emit(FirebaseResult.Failure(it))
    }
}