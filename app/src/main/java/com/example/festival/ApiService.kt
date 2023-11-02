package com.example.festival

import retrofit2.Call
import retrofit2.http.GET

interface FestivalService {  // 페스티벌 리스트
    @GET("festival")
    fun getFestivalList(): Call<List<Festival>>
}