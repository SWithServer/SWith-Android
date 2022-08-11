package com.example.swith.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.*

// 스터디 개설할때 만들어지는 스터디 data들
data class StudyGroup(
    var adminIdx:Long,
    var groupImgUrl:String,
    var title:String,
    var meet:Int,
    var frequency:Int?,
    var periods:String?,
    var online:Int,
    var regionIdx1:Long?,
    var regionIdx2:Long?,
    var interest:Int,
    var topic:String,
    var memberLimit:Int,
    var applicationMethod:Int,

    var recruitmentEndDate: String,
    var groupStart:String,
    var groupEnd:String,

    var attendanceValidTime:Int,
    var groupContent:String,
)

data class StudyGroupResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val studyGroup:ArrayList<StudyGroup>
)

data class  getStudyContent(
    var groupIndx:Int,
    var result : ArrayList<getStudyResponse>
)

data class getStudyResponse(
    var title : String,
    var regionIdx1:Long,
    var regionIdx2:Long,
    var deadline:LocalDate,
    var createDate : LocalDate,
    var memberLimit: Int,
    var groupContent : String
)

