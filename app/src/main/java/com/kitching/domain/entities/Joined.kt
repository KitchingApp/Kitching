package com.kitching.domain.entities

/** schedule, department, scheduleTime을 join하기 위한 클래스 */
data class ScheduleInfo(
    val schedule: Schedule,
    val user: User,
    val department: Department?,
    val scheduleTime: ScheduleTime
)


/** User, UserTeam, Team, Department, StaffLevel을 join하기 위한 클래스 */
data class MemberInfo(
    val userTeam: UserTeam,
    val user: User,
    val department: Department?,
    val staffLevel: StaffLevel?
)

/** Notice와 User를 join하기 위한 클래스 */
data class NoticeInfo(
    val notice: Notice,
    val user: User
)