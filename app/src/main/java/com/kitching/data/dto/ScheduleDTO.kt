package com.kitching.data.dto

import java.time.LocalTime

data class ScheduleDTO(
    val scheduleId: String,
    val date: String,
    val departmentName: String,
    val userId: String,
    val userName: String,
    val scheduleTimeName: String,
    val isFix: Boolean
)

data class ScheduleTimeDTO(
    val scheduleTimeId: String,
    val scheduleTimeName: String,
    val startTime: LocalTime,
    val endTime: LocalTime
)

data class dropDownDepartmentsDTO(
    val departmentId: String,
    val departmentName: String
)