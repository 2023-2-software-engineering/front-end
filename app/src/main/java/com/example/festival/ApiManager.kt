package com.example.festival

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BoardManager {
    fun sendBoardToServer(board: Board, authToken: String) {  // 게시판 새로 추가
        val apiService = MyApplication().boardService
        val call = apiService.sendBoard(board, authToken)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("서버 테스트", "성공")
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