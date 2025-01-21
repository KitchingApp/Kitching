package com.kitching.domain.datasource

import com.kitching.domain.entities.NoticeInfo

interface NoticeInfoDataSource {
    suspend fun getNoticeInfos(teamId: String): Result<List<NoticeInfo>>
}