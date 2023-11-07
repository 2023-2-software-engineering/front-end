package com.example.festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.festival.BoardManager.sendBoardToServer
import com.example.festival.BoardManager.sendModBoardToServer
import com.example.festival.databinding.ActivityAddBoardBinding

class AddBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBoardBinding
    private var authToken: String ?= null // 로그인 토큰
    private var new: Int ?= 1 // 새로 작성이면 1, 수정이면 0
    private var boardId: Int ?= -1 // 수정일 때의 해당 게시판 Id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 새로 작성 or 수정 (1이면 새로 작성, 아니면 수정)
        new = intent.getIntExtra("new_board", 1)

        // 툴바 취소 버튼 클릭 시
        binding.boardCancelBtn.setOnClickListener {
            finish()
        }

        // 툴바 완료 버튼 클릭 시
        binding.boardSaveBtn.setOnClickListener {
            if (new == 1) {
                saveBoardToServer() // 서버로 데이터 전송
                finish()
            } else {
                modBoardToServer()  // 서버로 수정된 데이터 전송
            }
        }

        if (new != 1) { // 기존의 게시판 수정이라면, 기존의 내용 그대로 출력
            boardId = intent.getIntExtra("board_id", -1)
            Log.d("my log", "수정 중" + boardId)
            BoardManager.getBoardData(
                boardId!!,
                onSuccess = { boardDetail ->
                    val editTitle = Editable.Factory.getInstance().newEditable(boardDetail.title)
                    val editContext = Editable.Factory.getInstance().newEditable(boardDetail.content)
                    binding.boardAddTitle.text = editTitle
                    binding.boardAddContent.text = editContext
                },
                onError = { throwable ->
                    Log.e("서버 테스트", "오류3: $throwable")
                }
            )
        }
    }

    private fun saveBoardToServer() {
        val title = binding.boardAddTitle.text.toString()
        val content = binding.boardAddContent.text.toString()

        val board = Board(title, content)
        Log.d("my log", ""+board)

        if (authToken != null) {
            sendBoardToServer(board, authToken!!)
        }
    }

    private fun modBoardToServer() {
        val title = binding.boardAddTitle.text.toString()
        val content = binding.boardAddContent.text.toString()

        val board = Board(title, content)
        Log.d("my log", ""+board)

        if (authToken != null) {
            sendModBoardToServer(boardId!!, board, authToken!!)

            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            Log.d("my log", "수정본 요청")
            finish() // 현재 액티비티 종료
        }
    }

    override fun onResume() {
        super.onResume()

        // 이전에 입력한 텍스트를 복원하여 보여줌
    }
}