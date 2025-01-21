package com.kitching.domain.datasource

import com.kitching.domain.entities.MemberInfo

interface MemberInfoDataSource {
    suspend fun getMemberInfos(teamId: String): Result<List<MemberInfo>>
}