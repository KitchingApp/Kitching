package com.kitchingapp.data.database.dto

import java.time.LocalDate
import java.time.LocalTime

data class NoticeDTO(
    val date: LocalDate,
    val noticeId: String,
    val writerId: String,
    val writerName: String,
    val title: String,
    val content: String
)

data class DepartmentDTO(val departmentId: String, val departmentName: String, val color: String)

data class StaffLevelDTO(val staffLevelId: String, val staffLevelName: String)

data class MemberDTO(val userId: String, val userName: String, val departmentName: String?, val staffLevelName: String?)

data class MemberListDTO(val teamName: String, val members: List<MemberDTO>)

data class ScheduleTimeListDTO(val scheduleTimeId: String, val scheduleTimeName: String, val color: String, val startTime: LocalTime, val endTime: LocalTime)