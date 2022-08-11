package com.example.swith.repository

import com.example.swith.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    // 홈화면 정보 받기
    @GET("/groupinfo/home")
    suspend fun getAllStudy(@Query("userIdx") userIdx: Int) : Response<GroupList>

    // 회차 화면 정보 받기
    @GET("/groupinfo/session")
    suspend fun getAllRound(@Query("userIdx") userIdx: Int, @Query("groupIdx") groupIdx: Int) : Response<RoundResponse>

    // 공지사항 받기
    @GET("/groupinfo/announcement/{groupIdx}")
    suspend fun getAllAnnounce(@Path("groupIdx") groupIdx: Int) : Response<AnnounceList>

    // 회차 생성
    @POST("/groupinfo/session")
    suspend fun createRound(@Body session: Session): Response<SessionResponse>

    // 회차 정보(개요, 출석, 메모)
    @GET("/groupinfo/session/info")
    suspend fun getSessionInfo(@Query("userIdx") userIdx: Int, @Query("sessionIdx") sessionIdx: Int) : Response<SessionInfoResponse>

    // 출석 업데이트
    @PATCH("/groupinfo/attendance")
    suspend fun updateAttend(@Query("userIdx") userIdx: Int, @Query("sessionIdx") sessionIdx: Int) : Response<AttendResponse>

    // 공지사항 삭제
    @PATCH("/groupinfo/announcement/{announcementIdx}/status")
    suspend fun deleteAnnounce(@Path("announcementIdx") announcementIdx: Int) : Response<AnnounceDelete>

    // 공지사항 생성
    @POST("/groupinfo/announcement")
    suspend fun createAnnounce(@Body announceCreate: AnnounceCreate) : Response<Any>

    @POST("/groupinfo")
     fun createStudy(@Body body:StudyGroup) : Call<StudyResponse>

     //임시 스터디 찾기 - 스터디 불러오기 부분
    @GET("/groupinfo")
    fun getSearchStudy(@Query("title") title:String?=null,@Query("region") regionCode : Long?= null, @Query("interest1") interest1:Int?=null, @Query("interest2") intrest2:Int?=null,@Query("sort") sort:Int?=null):Call<getStudyContent>
}