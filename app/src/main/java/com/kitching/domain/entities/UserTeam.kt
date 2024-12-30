package com.kitching.domain.entities

data class UserTeam(
    val id: String = "",
    val departmentId: String? = "",
    val staffLevelId: String? = "",
    val teamId: String = "",
    val userId: String = "",
    @field:JvmField
    val isManager: Boolean = false,
)
