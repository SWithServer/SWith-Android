package com.example.swith.data

import com.google.gson.annotations.SerializedName

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
    @SerializedName("last") val last : Boolean, // 다음페이지 유무
    @SerializedName("size") val size : Int,  //현재페이지?
    @SerializedName("number") val number : Int,
    @SerializedName("sort") val sort : Sort,
    @SerializedName("empty") val empty : Boolean
)

data class Content (
    @SerializedName("groupIdx") val groupIdx : Int,
    @SerializedName("title") val title : String,
    @SerializedName("groupContent") val groupContent : String,
    @SerializedName("regionIdx1") val regionIdx1 : String,
    @SerializedName("regionIdx2") val regionIdx2 : String,
    @SerializedName("recruitmentEndDate") val recruitmentEndDate : List<Int>,
    @SerializedName("memberLimit") val memberLimit : Int,
    @SerializedName("createdAt") val createdAt : List<Int>,
    @SerializedName("numOfApplicants") val numOfApplicants : Int,
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