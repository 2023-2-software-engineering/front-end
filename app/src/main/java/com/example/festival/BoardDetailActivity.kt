package com.example.festival

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityBoardDetailBinding

class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardDetailBinding
    private var boardId = -1 // 현재 게시판 ID를 담는 변수
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private var authToken: String ?= "" // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        recyclerView = binding.commentRecyclerView

        val layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.layoutManager = layoutManager

        commentAdapter = CommentAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = commentAdapter // 리사이클러뷰에 어댑터 설정

        loadCommentList() // 댓글 목록 불러오기

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 댓글 전송하기 클릭 시
        binding.addCommentBtn.setOnClickListener {
            val commentText = binding.addCommentText.text.toString() //댓글 텍스트
            val commentData = Comment(commentText)
            if (authToken != null) {
                CommentManager.sendCommentToServer(boardId, authToken!!, commentData) { isSuccess ->
                    if (isSuccess) {
                        binding.addCommentText.text.clear() // 댓글 전송 후 텍스트 초기화

                        loadCommentList() // 댓글 리스트 업데이트
                    }
                }
            }
        }
    }

    private fun loadCommentList() {  // 서버에서 댓글 리스트 불러오기
        CommentManager.getCommentListData(
            boardId,
            onSuccess = { commentListResponse ->
                val comment = commentListResponse.map { it }
                commentAdapter.updateData(comment)
            },
            onError = { throwable ->
                Log.e("서버 테스트3", "오류: $throwable")
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_admin_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로 가기 버튼 클릭 시
                finish() // 현재 액티비티 종료
                return true
            }
            R.id.mod_menu -> { //수정하기 버튼 클릭 시
                val intent = Intent(this, AddBoardActivity::class.java)
                // 수정하는 것임을 알림
//                intent.putExtra("new_board", 0)
//                intent.putExtra("board_id", boardId)
//                boardModActivityResult.launch(intent)
                return true
            }
            R.id.remove_menu -> { //삭제하기 버튼 클릭 시
                BoardManager.deleteBoardFromServer(boardId)
                finish() // 현재 일기 삭제 후, 액티비티 종료
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        loadCommentList() // 댓글 리스트 업데이트
    }
}