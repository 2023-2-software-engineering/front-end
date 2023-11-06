package com.example.festival

import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MyApplication {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/") // 로컬 URL (본인 걸로 변경)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .build()

    fun getGson(): Gson {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // 원하는 시간 형식 지정

        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Date 형식 지정
            .create()
    }

    val festivalService = retrofit.create(FestivalService::class.java)

    val boardService = retrofit.create(BoardService::class.java)
    val boardListService = retrofit.create(BoardListService::class.java)
    val boardDetailService = retrofit.create(BoardDetailService::class.java)
    val deleteBoardService = retrofit.create(DeleteBoardService::class.java)

    val commentService = retrofit.create(CommentService::class.java)
    val commentListService = retrofit.create(CommentListService::class.java)

}