package com.example.festival

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun sendBoardToServer(board: Board, authToken: String) {  // 게시판 새로 추가
        val apiService = MyApplication().boardService
        val call = apiService.sendBoard(board, authToken)
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

    fun sendModBoardToServer(boardId: Int, board: Board, authToken: String) {  // 게시판 수정
        val apiService = MyApplication().modBoardService
        val call = apiService.sendModBoard(boardId, board, authToken)
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