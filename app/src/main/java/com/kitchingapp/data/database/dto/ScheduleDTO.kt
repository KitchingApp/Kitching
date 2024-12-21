package com.kitchingapp.data.database.dto

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleDTO(
    val scheduleId: String,
    val date: LocalDate,
    val departmentName: String,
    val userId: String,
    val userName: String,
    val scheduleTime: String,
    val isFix: Boolean
)

data class ScheduleTimeDTO(
    val scheduleTimeId: String,
    val scheduleTimeName: String,
    val startTime: LocalTime,
    val endTime: LocalTime
)