package com.example.swith.repository

import com.example.swith.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDateTime

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
    suspend fun createRound(@Body session: Session): Response<SessionCreate>

    // 회차 정보(개요, 출석, 메모)
    @GET("/groupinfo/session/info")
    suspend fun getSessionInfo(@Query("userIdx") userIdx: Int, @Query("sessionIdx") sessionIdx: Int) : Response<SessionInfoResponse>

    // 스터디, 유저별 출석율 조회
    @GET("/groupinfo/attendance")
    suspend fun getUserAttend(@Query("groupIdx") groupIdx: Int) : Response<UserAttendResponse>

    // 출석 업데이트
    @PATCH("/groupinfo/attendance")
    suspend fun updateAttend(@Query("userIdx") userIdx: Int, @Query("sessionIdx") sessionIdx: Int) : Response<AttendResponse>

    // 메모 생성
    @POST("/groupinfo/memo")
    suspend fun createMemo(@Body memo: Memo) : Response<MemoResponse>

    // 메모 수정
    @PATCH("/groupinfo/memo")
    suspend fun updateMemo(@Body memoUpdate: MemoUpdate) : Response<MemoResponse>

    // 공지사항 삭제
    @PATCH("/groupinfo/announcement/{announcementIdx}/status")
    suspend fun deleteAnnounce(@Path("announcementIdx") announcementIdx: Int) : Response<AnnounceDelete>

    // 공지사항 생성
    @POST("/groupinfo/announcement")
    suspend fun createAnnounce(@Body announceCreate: AnnounceCreate) : Response<Any>

    // 공지사항 수정
    @PATCH("/groupinfo/announcement")
    suspend fun updateAnnounce(@Body announceModify: AnnounceModify) : Response<Any>

    // 회차 삭제
    @PATCH("/groupinfo/session/admin/{sessionIdx}/status")
    suspend fun deleteRound(@Path("sessionIdx") sessionIdx: Int) : Response<SessionResponse>

    // 회차 수정
    @PATCH("/groupinfo/session/admin")
    suspend fun modifyRound(@Body session: SessionModify) : Response<SessionResponse>

    //스터디 개설
    @POST("/groupinfo")
     fun createStudy(@Body body:StudyGroup) : Call<StudyResponse>

     //임시 스터디 찾기 - 스터디 불러오기 부분
     @GET("/groupinfo/search")
     fun getSearchStudy(@Query("size")size:Int=5,
                        @Query("title") title:String?,
                        @Query("regionIdx") regionIdx : String?,
                        @Query("interest1") interest1:Int?,
                        @Query("interest2") interest2:Int?,
                        @Query("groupIdx") groupIdx:Long?,
                        @Query("sortCond") sortCond:Int?,
                        @Query("ClientTime") ClientTime :String):Call<StudyFindResponse>

    //스터디 정보보기
    @GET("/groupinfo/search/{groupIdx}")
    fun getStudyDetail(@Path("groupIdx") groupIdx:Long):Call<StudyDetailResponse>

    //스터디 가입신청
    @POST("/application/apply/{groupIdx}/{applicationMethod}")
    fun postApplication(@Path("groupIdx")groupIdx:Long,@Path("applicationMethod")applicationMethod:Int,@Body body : postApplicationReq) : Call<StudyApplicationResponse>


    //스터디 삭제하기
    @DELETE("/groupinfo")
    fun delStudy(@Query("groupIdx") groupIdx : Int)

    //프로필
    @GET("/profile")
    fun getProfileInfo() : Call<ProfileResponse>
}