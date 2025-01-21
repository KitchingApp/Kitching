package com.kitching.domain.repository

import com.kitching.data.dto.DropDownMembersDTO
import com.kitching.data.dto.MemberListDTO
import com.kitching.data.firebase.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface UserTeamRepository {
    fun getAllMembers(teamId: String): Flow<FirebaseResult<MemberListDTO>>

    fun getAllMembersForSchedule(teamId: String): Flow<FirebaseResult<List<DropDownMembersDTO>>>
}