package com.example.festival

import android.util.Log
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object JoinManager {
    fun sendJoinToServer(join: Join) {
        val apiService = MyApplication().authJoinService
        val call = apiService.sendAuthRequest(join)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object LogInManager {

    val loginURL = "http://10.0.2.2:8080/login"

    fun sendLogInToServer(login: Login, onSuccess: (String) -> Unit) {
        val apiService = MyApplication().logInService
        val call = apiService.sendLogInRequest(loginURL, login)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val authToken = response.headers()["Authorization"]
                    if (authToken != null) {
                        // 토큰을 onSuccess 콜백으로 전달
                        onSuccess(authToken)
                    } else {
                        Log.e("서버 테스트", "토큰이 없습니다.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object UserManager {
    fun getUserData(authToken: String, onSuccess: (User) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().myPageService
        val call = apiService.getUser(authToken)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendUserUpdate(authToken: String, user: UserUpdate, image: MultipartBody.Part?) {  // 게시판 새로 추가
        val apiService = MyApplication().userUpdateService
        val call = apiService.sendUserUpdate(authToken, user, image)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "업데이트 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object FestivalManager {
    fun getFestivalListData(onSuccess: (List<Festival>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().festivalService
        val call = apiService.getFestivalList()

        call.enqueue(object : Callback<List<Festival>> {
            override fun onResponse(call: Call<List<Festival>>, response: Response<List<Festival>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Festival>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getFestivalData(festivalId: Int, onSuccess: (Festival) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().festivalDetailService
        val call = apiService.getFestival(festivalId)

        call.enqueue(object : Callback<Festival> {
            override fun onResponse(call: Call<Festival>, response: Response<Festival>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Festival>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendFestivalLike(festivalId: Int, authToken: String, callback: (Boolean) -> Unit) {
        val apiService = MyApplication().festivalLikeService
        val call = apiService.sendFestivalLike(festivalId, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "성공")
                    callback(true) // 성공 시 true 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(false) // 실패 시 false 전달
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(false) // 실패 시 false 전달
            }
        })
    }

    fun deleteFestivalLike(festivalId: Int, authToken: String, callback: (Boolean) -> Unit) {
        val apiService = MyApplication().festivalUnlikeService
        val call = apiService.deleteFestivalLike(festivalId, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "성공")
                    callback(true) // 성공 시 true 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(false) // 실패 시 false 전달
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(false) // 실패 시 false 전달
            }
        })
    }

    fun getFestivalLike(festivalId: Int, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().festivalLikeListService
        val call = apiService.getFestivalLike(festivalId)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getFestivalLikeCheck(festivalId: Int, authToken: String, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().festivalLikeCheckService
        val call = apiService.getFestivalLikeCheck(festivalId, authToken)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getHotFestival(onSuccess: (List<Festival>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().hotFestivalListService
        val call = apiService.getHotFestivalLike()

        call.enqueue(object : Callback<List<Festival>> {
            override fun onResponse(call: Call<List<Festival>>, response: Response<List<Festival>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Festival>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getFestivalViewTop(onSuccess: (Festival) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().festivalViewTopService
        val call = apiService.getFestivalViewTop()

        call.enqueue(object : Callback<Festival> {
            override fun onResponse(call: Call<Festival>, response: Response<Festival>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Festival>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object EventManager {
    fun getEventListData(onSuccess: (List<Event>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().eventService
        val call = apiService.getEventList()

        call.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getEventData(eventId: Int, onSuccess: (Event) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().eventDetailService
        val call = apiService.getEvent(eventId)

        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendEventLike(eventId: Int, authToken: String, callback: (Boolean) -> Unit) {
        val apiService = MyApplication().eventLikeService
        val call = apiService.sendEventLike(eventId, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "성공")
                    callback(true) // 성공 시 true 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(false) // 실패 시 false 전달
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(false) // 실패 시 false 전달
            }
        })
    }

    fun deleteEventLike(eventId: Int, authToken: String, callback: (Boolean) -> Unit) {
        val apiService = MyApplication().eventUnlikeService
        val call = apiService.deleteEventLike(eventId, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "성공")
                    callback(true) // 성공 시 true 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(false) // 실패 시 false 전달
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(false) // 실패 시 false 전달
            }
        })
    }

    fun getEventLike(eventId: Int, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().eventLikeListService
        val call = apiService.getEventLike(eventId)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getEventLikeCheck(eventId: Int, authToken: String, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().eventLikeCheckService
        val call = apiService.getEventLikeCheck(eventId, authToken)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getEventViewTop(onSuccess: (Event) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().eventViewTopService
        val call = apiService.getEventViewTop()

        call.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object BoardManager {
    fun getBoardListData(onSuccess: (List<BoardData>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().boardListService
        val call = apiService.getBoardList()

        call.enqueue(object : Callback<List<BoardData>> {
            override fun onResponse(call: Call<List<BoardData>>, response: Response<List<BoardData>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<BoardData>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getBoardData(boardId: Int, onSuccess: (BoardData) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().boardDetailService
        val call = apiService.getBoard(boardId)

        call.enqueue(object : Callback<BoardData> {
            override fun onResponse(call: Call<BoardData>, response: Response<BoardData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BoardData>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendBoardToServer(board: Board, image: MultipartBody.Part?, authToken: String) {  // 게시판 새로 추가
        val apiService = MyApplication().boardService
        val call = apiService.sendBoard(board, image, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "추가 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendModBoardToServer(boardId: Int, board: Board, image: MultipartBody.Part?, authToken: String) {  // 게시판 수정
        val apiService = MyApplication().modBoardService
        val call = apiService.sendModBoard(boardId, board, image, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "추가 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun deleteBoardFromServer(boardId: Int) {  // 게시판 삭제
        val apiService = MyApplication().deleteBoardService
        val call = apiService.deleteBoardData(boardId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "삭제 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object CommentManager {  // 댓글
    fun sendCommentToServer(boardId: Int, authToken: String, comment: Comment, callback: (Boolean) -> Unit) {
        val apiService = MyApplication().commentService
        val call = apiService.sendComment(boardId, authToken, comment)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "추가 성공")
                    callback(true) // 성공 시 true 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(true) // 성공 시 true 전달
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(false)
            }
        })
    }

    fun getCommentListData(diaryId: Int, onSuccess: (List<CommentListResponse>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().commentListService
        val call = apiService.getCommentListData(diaryId)

        call.enqueue(object : Callback<List<CommentListResponse>> {
            override fun onResponse(call: Call<List<CommentListResponse>>, response: Response<List<CommentListResponse>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<CommentListResponse>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}

object ReportManager {
    fun getReportListData(onSuccess: (List<ReportData>) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().reportListService
        val call = apiService.getReportList()

        call.enqueue(object : Callback<List<ReportData>> {
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<ReportData>>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun getReportData(reportId: Int, onSuccess: (ReportData) -> Unit, onError: (Throwable) -> Unit) {
        val apiService = MyApplication().reportDetailService
        val call = apiService.getReport(reportId)

        call.enqueue(object : Callback<ReportData> {
            override fun onResponse(call: Call<ReportData>, response: Response<ReportData>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Throwable("Response body is null"))
                    }
                } else {
                    onError(Throwable("API call failed with response code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ReportData>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendReportToServer(authToken: String, report: Report, image: MultipartBody.Part?) {  // 신고 새로 추가
        val apiService = MyApplication().reportService
        val call = apiService.sendReport(authToken, report, image)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "추가 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun sendModReportToServer(reportId: Int, report: Report, image: MultipartBody.Part, authToken: String) {  // 신고 수정
        val apiService = MyApplication().modReportService
        val call = apiService.sendModReport(reportId, report, image, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "추가 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun deleteReportFromServer(reportId: Int) {  // 신고 삭제
        val apiService = MyApplication().deleteReportService
        val call = apiService.deleteReportData(reportId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "삭제 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }

    fun doneReportToServer(reportId: Int) {
        val apiService = MyApplication().doneReportService
        val call = apiService.doneReport(reportId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "수정 성공")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}