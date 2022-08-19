package com.example.swith.data

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class StudyFindResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
    )

data class Result (
    @SerializedName("content") val content : List<Content>,
    @SerializedName("pageable") val pageable : Pageable,
    @SerializedName("numberOfElements") val numberOfElements : Int, //리스트 개수
    @SerializedName("first") val first : Boolean,
    @SerializedName("last") val last : Boolean, // 마지막인지 유무
    @SerializedName("size") val size : Int,
    @SerializedName("number") val number : Int,
    @SerializedName("sort") val sort : Sort,
    @SerializedName("empty") val empty : Boolean
)

data class Content (
    @SerializedName("groupIdx") val groupIdx : Long,
    @SerializedName("title") val title : String,
    @SerializedName("groupContent") val groupContent : String,
    @SerializedName("regionIdx1") val regionIdx1 : String,
    @SerializedName("regionIdx2") val regionIdx2 : String,
    @SerializedName("recruitmentEndDate") val recruitmentEndDate : List<Int>,
    @SerializedName("memberLimit") val memberLimit : Int, //멤버 제한 수
    @SerializedName("createdAt") val createdAt : List<Int>,
    @SerializedName("numOfApplicants") val numOfApplicants : Int, //지원자 수
    @SerializedName("applicationMethod") val applicationMethod : Int
)

data class Pageable (
    @SerializedName("sort") val sort : Sort,
    @SerializedName("pageNumber") val pageNumber : Int,
    @SerializedName("pageSize") val pageSize : Int,
    @SerializedName("offset") val offset : Int,
    @SerializedName("paged") val paged : Boolean,
    @SerializedName("unpaged") val unpaged : Boolean
)

data class Sort (
    @SerializedName("unsorted") val unsorted : Boolean,
    @SerializedName("sorted") val sorted : Boolean,
    @SerializedName("empty") val empty : Boolean
)

data class studyReqest(
    @SerializedName("title") val title : String?,
    @SerializedName("regionIdx") val regionIdx : String?,
    @SerializedName("groupIdx") val groupIdx : Long?,
    @SerializedName("interest1") val interest1 : Int?,
    @SerializedName("interest2") val interest2 : Int?,
    @SerializedName("sortCond") val sortCond : Int,
    @SerializedName("ClientTime") val ClientTime : LocalDateTime
)