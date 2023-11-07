package com.example.festival

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityReportDetailBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ReportDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportDetailBinding
    private var reportId = -1 // 현재 신고 ID를 담는 변수
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        reportId = intent.getIntExtra("reportId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        recyclerView = binding.commentRecyclerView

        val layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.layoutManager = layoutManager

        commentAdapter = CommentAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = commentAdapter // 리사이클러뷰에 어댑터 설정

        if (reportId != -1) {
            ReportManager.getReportData(
                reportId,
                onSuccess = { reportDetail ->
                    supportActionBar?.title = reportDetail.title
                    binding.reportContent.text = reportDetail.content
                    binding.reportWriter.text = reportDetail.nickname
                    binding.reportAddress.text = reportDetail.address

                    if (reportDetail.done == true) {  // 조치 완료 라면
                        binding.reportYet.visibility = View.GONE
                        binding.reportDone.visibility = View.VISIBLE
                    }

                    val parts = reportDetail.createdAt.split("T")
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
                        binding.reportCreate.text = formattedDate
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로 가기 버튼 클릭 시
                finish() // 현재 액티비티 종료
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}