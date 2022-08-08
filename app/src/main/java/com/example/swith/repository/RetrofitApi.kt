package com.example.swith.repository

import com.example.swith.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {
    @GET("/groupinfo/home")
    suspend fun getAllStudy(@Query("userIdx") userIdx: Int) : Response<GroupList>

    @GET("/groupinfo/session")
    suspend fun getAllRound(@Query("userIdx") userIdx: Int, @Query("groupIdx") groupIdx: Int) : Response<RoundResponse>

    @GET("/groupinfo/announcement/{groupIdx}")
    suspend fun getAllAnnounce(@Path("groupIdx") groupIdx: Int) : Response<AnnounceList>

    @POST("/groupinfo/session")
    suspend fun createRound(@Body session: Session): Response<SessionResponse>

    @GET("/groupinfo/session/info")
    suspend fun getSessionInfo(@Query("userIdx") userIdx: Int, @Query("sessionIdx") sessionIdx: Int) : Response<SessionInfoResponse>

    @POST("/groupinfo")
     fun createStudy(@Body body:StudyGroup) : Call<StudyResponse>
}