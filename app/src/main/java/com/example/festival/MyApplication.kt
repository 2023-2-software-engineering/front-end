package com.example.festival

import com.google.gson.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*

class MyApplication {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/") // 로컬 URL (본인 걸로 변경)
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .client(OkHttpClient.Builder().build())
        .build()

    fun getGson(): Gson {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // 원하는 시간 형식 지정

        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Date 형식 지정
            .create()
    }

    val logInService = retrofit.create(LogInService::class.java)
    val authJoinService = retrofit.create(AuthJoinService::class.java)
    val myPageService = retrofit.create(MyPageService::class.java)
    val userUpdateService = retrofit.create(UserUpdateService::class.java)

    val festivalService = retrofit.create(FestivalListService::class.java)
    val festivalDetailService = retrofit.create(FestivalDetailService::class.java)

    val eventService = retrofit.create(EventListService::class.java)
    val eventDetailService = retrofit.create(EventDetailService::class.java)

    val festivalLikeService = retrofit.create(FestivalLikeService::class.java)
    val festivalUnlikeService = retrofit.create(FestivalUnlikeService::class.java)
    val festivalLikeListService = retrofit.create(FestivalLikeListService::class.java)
    val festivalLikeCheckService = retrofit.create(FestivalLikeCheckService::class.java)

    val hotFestivalListService = retrofit.create(HotFestivalListService::class.java)

    val eventLikeService = retrofit.create(EventLikeService::class.java)
    val eventUnlikeService = retrofit.create(EventUnlikeService::class.java)
    val eventLikeListService = retrofit.create(EventLikeListService::class.java)
    val eventLikeCheckService = retrofit.create(EventLikeCheckService::class.java)

    val festivalViewTopService = retrofit.create(FestivalViewTopService::class.java)
    val eventViewTopService = retrofit.create(EventViewTopService::class.java)

    val boardService = retrofit.create(BoardService::class.java)
    val boardListService = retrofit.create(BoardListService::class.java)
    val boardDetailService = retrofit.create(BoardDetailService::class.java)
    val modBoardService = retrofit.create(ModBoardService::class.java)
    val deleteBoardService = retrofit.create(DeleteBoardService::class.java)

    val commentService = retrofit.create(CommentService::class.java)
    val commentListService = retrofit.create(CommentListService::class.java)

    val reportService = retrofit.create(ReportService::class.java)
    val reportListService = retrofit.create(ReportListService::class.java)
    val reportDetailService = retrofit.create(ReportDetailService::class.java)
    val modReportService = retrofit.create(ModReportService::class.java)
    val deleteReportService = retrofit.create(DeleteReportService::class.java)
    val doneReportService = retrofit.create(DoneReportService::class.java)

    val ideaService = retrofit.create(IdeaService::class.java)
    val ideaListService = retrofit.create(IdeaListService::class.java)
    val ideaDetailService = retrofit.create(IdeaDetailService::class.java)

    val searchFestivalService = retrofit.create(SearchFestivalService::class.java)
    val searchEventService = retrofit.create(SearchEventService::class.java)

    val myFestivalService = retrofit.create(MyFestivalListService::class.java)
    val myEventService = retrofit.create(MyEventListService::class.java)
}