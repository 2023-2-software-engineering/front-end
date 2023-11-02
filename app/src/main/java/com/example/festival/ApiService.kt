package com.example.festival

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FestivalService {  // 페스티벌 리스트
    @GET("festival")
    fun getFestivalList(): Call<List<Festival>>
}

interface BoardListService {  // 게시판 리스트
    @GET("partner")
    fun getBoardList(): Call<List<Board>>
}

interface BoardDetailService {  // 게시판 상세
    @GET("partner/{partnerId}")
    fun getBoard(@Path("partnerId") boardId: Int): Call<Board>
}