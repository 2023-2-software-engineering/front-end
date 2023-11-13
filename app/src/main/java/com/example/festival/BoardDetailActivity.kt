package com.example.festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityBoardDetailBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardDetailBinding
    private var boardId = -1 // 현재 게시판 ID를 담는 변수
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var imgAdapter: MultiImageAdapter
    private lateinit var imgRecyclerView: RecyclerView
    private var authToken: String ?= null // 로그인 토큰
    private var uriList = ArrayList<Uri>()  // 이미지 uri

    companion object {
        lateinit var boardModActivityResult: ActivityResultLauncher<Intent>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        boardId = intent.getIntExtra("boardId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        commentRecyclerView = binding.commentRecyclerView
        imgRecyclerView = binding.imgRecyclerView

        val layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.layoutManager = layoutManager
        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        commentAdapter = CommentAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        commentRecyclerView.adapter = commentAdapter // 리사이클러뷰에 어댑터 설정

        imgAdapter = MultiImageAdapter(uriList, this)
        binding.imgRecyclerView.adapter = imgAdapter

        if (boardId != -1) {
            BoardManager.getBoardData(
                boardId,
                onSuccess = { boardDetail ->
                    supportActionBar?.title = boardDetail.title
                    binding.boardContent.text = boardDetail.content
                    binding.boardWriter.text = boardDetail.nickname
                    binding.boardAddress.text = boardDetail.address
                    binding.commentNum.text = "${boardDetail.count}"

                    val parts = boardDetail.createdAt.split("T")
                    if (parts.size == 2) {
                        val datePart = parts[0]
                        val timeWithMillisPart = parts[1]

                        // 밀리초 부분을 제외한 시간 부분 추출
                        val timePart = timeWithMillisPart.substring(0, 8)

                        // 날짜와 시간을 조합하여 Timestamp로 변환
                        val timestampString = "$datePart $timePart"
                        val dateFormat =
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val parsedTimestamp = Timestamp(dateFormat.parse(timestampString).time)

                        // SimpleDateFormat을 사용하여 원하는 형식으로 포맷
                        val outputDateFormat =
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        outputDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 원하는 시간대 설정
                        val formattedDate = outputDateFormat.format(parsedTimestamp)

                        // 결과를 출력
                        Log.d("Formatted Date", formattedDate)
                        binding.boardCreate.text = formattedDate
                    } else {
                        // 올바른 형식이 아닐 경우 오류 처리
                        Log.e("Error", "Invalid timestamp format")
                    }
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )

            loadCommentList() // 댓글 목록 불러오기
        }

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

        boardModActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("my log", "수정본")
                // 수정 후 넘어왔을 때 보여줄 내용 추가
                BoardManager.getBoardData(
                    boardId,
                    onSuccess = { boardDetail ->
                        supportActionBar?.title = boardDetail.title
                        binding.boardContent.text = boardDetail.content
                        binding.boardWriter.text = boardDetail.nickname
                        binding.boardAddress.text = boardDetail.address
                        binding.commentNum.text = "${boardDetail.count}"

                        val parts = boardDetail.createdAt.split("T")
                        if (parts.size == 2) {
                            val datePart = parts[0]
                            val timeWithMillisPart = parts[1]

                            // 밀리초 부분을 제외한 시간 부분 추출
                            val timePart = timeWithMillisPart.substring(0, 8)

                            // 날짜와 시간을 조합하여 Timestamp로 변환
                            val timestampString = "$datePart $timePart"
                            val dateFormat =
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            val parsedTimestamp = Timestamp(dateFormat.parse(timestampString).time)

                            // SimpleDateFormat을 사용하여 원하는 형식으로 포맷
                            val outputDateFormat =
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            outputDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 원하는 시간대 설정
                            val formattedDate = outputDateFormat.format(parsedTimestamp)

                            // 결과를 출력
                            Log.d("Formatted Date", formattedDate)
                            binding.boardCreate.text = formattedDate
                        } else {
                            // 올바른 형식이 아닐 경우 오류 처리
                            Log.e("Error", "Invalid timestamp format")
                        }
                    },
                    onError = { throwable ->
                        Log.e("서버 테스트3", "오류: $throwable")
                    }
                )
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
        if (authToken != null) {
            menuInflater.inflate(R.menu.detail_admin_menu, menu)
        }
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
                intent.putExtra("new_board", 0)
                intent.putExtra("board_id", boardId)
                boardModActivityResult.launch(intent)
                Log.d("my log", "수정하러 가기")
                return true
            }
            R.id.remove_menu -> { //삭제하기 버튼 클릭 시
                BoardManager.deleteBoardFromServer(boardId)
                finish() // 현재 글 삭제 후, 액티비티 종료
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