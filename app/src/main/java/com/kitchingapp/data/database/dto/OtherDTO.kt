package com.kitchingapp.data.database.dto

import java.time.LocalDate

data class NoticeDTO(
    val date: LocalDate,
    val noticeId: String,
    val writerName: String,
    val title: String,
    val content: String
)

data class DepartmentDTO(val departmentId: String, val departmentName: String, val color: String)

data class StaffLevelDTO(val staffLevelId: String, val staffLevelName: String)

data class MemberDTO(val userId: String, val userName: String, val department: String, val staffLevel: String)