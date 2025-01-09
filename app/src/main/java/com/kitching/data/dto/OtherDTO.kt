package com.kitching.data.dto

import android.os.Parcel
import android.os.Parcelable
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

data class ParcelableNoticeDTO(
    val dateString: String,
    val noticeId: String,
    val writerId: String,
    val writerName: String,
    val title: String,
    val content: String
): Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dateString)
        parcel.writeString(noticeId)
        parcel.writeString(writerId)
        parcel.writeString(writerName)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableNoticeDTO> {
        override fun createFromParcel(parcel: Parcel): ParcelableNoticeDTO {
            return ParcelableNoticeDTO(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableNoticeDTO?> {
            return arrayOfNulls(size)
        }
    }
}

data class DepartmentDTO(val departmentId: String, val departmentName: String, val color: String)

data class StaffLevelDTO(val staffLevelId: String, val staffLevelName: String)

data class MemberDTO(val userId: String, val userName: String, val departmentName: String?, val staffLevelName: String?)

data class MemberListDTO(val teamName: String, val members: List<MemberDTO>)

//data class ScheduleTimeListDTO(val scheduleTimeId: String, val scheduleTimeName: String, val color: String, val startTime: LocalTime, val endTime: LocalTime)

data class ScheduleTimeListDTO(val scheduleTimeId: String, val scheduleTimeName: String, val color: String, val startTime: String, val endTime: String)