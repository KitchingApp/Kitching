package com.kitching.data.datasource

import com.kitching.domain.datasource.NoticeDataSource
import com.kitching.domain.datasource.NoticeInfoDataSource
import com.kitching.domain.datasource.UserDataSource
import com.kitching.domain.entities.NoticeInfo

class NoticeInfoDataSourceImpl(
    private val noticeDataSource: NoticeDataSource = NoticeDataSourceImpl(),
    private val userDataSource: UserDataSource = UserDataSourceImpl()
): NoticeInfoDataSource {
    override suspend fun getNoticeInfos(teamId: String): Result<List<NoticeInfo>> {
        return runCatching {
            noticeDataSource.getNotices(teamId).getOrThrow().map {
                NoticeInfo(
                    notice = it,
                    user = userDataSource.getUser(it.writerId).getOrThrow()
                )
            }
        }
    }
}