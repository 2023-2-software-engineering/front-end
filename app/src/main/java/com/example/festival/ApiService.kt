package com.example.festival

import retrofit2.Call
import retrofit2.http.*

interface LogInService {  // 로그인
    @POST
    fun sendLogInRequest(@Url url: String, @Body login: Login): Call<Void>
}
interface FestivalService {  // 페스티벌 리스트
    @GET("festival")
    fun getFestivalList(): Call<List<Festival>>
}

interface BoardService {  // 게시판 작성
    @POST("partner") // 서버 주소/partner 으로 POST
    fun sendBoard(@Body board: Board, @Header("Authorization") authToken: String): Call<Void>
}

interface BoardListService {  // 게시판 리스트
    @GET("partner")
    fun getBoardList(): Call<List<BoardData>>
}

interface BoardDetailService {  // 게시판 상세
    @GET("partner/{partnerId}")
    fun getBoard(@Path("partnerId") boardId: Int): Call<BoardData>
}

interface ModBoardService {  // 게시판 수정
    @PATCH("partner/{partnerId}")
    fun sendModBoard(@Path("partnerId") boardId: Int, @Body board: Board,
                     @Header("Authorization") authToken: String): Call<Void>
}

interface DeleteBoardService {  // 게시판 삭제
    @DELETE("partner/{partnerId}")
    fun deleteBoardData(@Path("partnerId") boardId: Int): Call<Void>
}

interface CommentService {  // 게시판 댓글 작성
    @POST("comment/{partnerId}")
    fun sendComment(
        @Path("partnerId") partnerId: Int, @Header("Authorization") authToken: String,
        @Body comment: Comment
    ): Call<Void>
}

interface CommentListService { // 게시판 댓글 조회
    @GET("comment/{partnerId}")
    fun getCommentListData(@Path("partnerId") partnerId: Int): Call<List<CommentListResponse>>
}

interface ReportService {
    @POST("report")
    fun sendReport(@Body report: Report, @Header("Authorization") authToken: String): Call<Void>
}

interface ReportListService {  // 게시판 리스트
    @GET("report")
    fun getReportList(): Call<List<ReportData>>
}

interface ReportDetailService {  // 게시판 상세
    @GET("report/{reportId}")
    fun getReport(@Path("reportId") reportId: Int): Call<ReportData>
}