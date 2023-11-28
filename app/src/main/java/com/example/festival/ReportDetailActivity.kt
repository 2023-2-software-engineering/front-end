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
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festival.ReportDetailActivity.Companion.reportModActivityResult
import com.example.festival.databinding.ActivityReportDetailBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ReportDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportDetailBinding
    private var reportId = -1 // 현재 신고 ID를 담는 변수
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgAdapter: MultiImageLoadAdapter
    private lateinit var imgRecyclerView: RecyclerView
    private lateinit var imageList: List<String>
    private var authToken: String ?= null // 로그인 토큰
    private var userIdentity: String ?= null // 로그인된 사용자 아이디
    private var isMe: Boolean ?= false  // 로그인된 사용자와 작성자가 같은지 여부
    private var festivalId: Int ?= -1

    companion object {
        lateinit var reportModActivityResult: ActivityResultLauncher<Intent>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "불량 부스 신고"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        reportId = intent.getIntExtra("reportId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
        userIdentity = sharedPreferences.getString("user_identify", null)

        recyclerView = binding.commentRecyclerView
        imgRecyclerView = binding.imgRecyclerView

        val layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.layoutManager = layoutManager
        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        commentAdapter = CommentAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = commentAdapter // 리사이클러뷰에 어댑터 설정

        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        imgAdapter = MultiImageLoadAdapter(emptyList(), this)
        binding.imgRecyclerView.adapter = imgAdapter

        if (reportId != -1) {
            ReportManager.getReportData(
                reportId,
                onSuccess = { reportDetail ->
                    //supportActionBar?.title = reportDetail.title
                    binding.reportTitle.text = reportDetail.title
                    binding.reportContent.text = reportDetail.content
                    binding.reportWriter.text = reportDetail.nickname
                    binding.reportAddress.text = reportDetail.address

                    val imageString = reportDetail.image
                    imageList = extractImage(imageString)

                    Log.d("my log", "반환된 이미지" + imageList)

                    imgAdapter = MultiImageLoadAdapter(imageList, applicationContext)
                    binding.imgRecyclerView.adapter = imgAdapter

                    Glide.with(this)
                        .load(reportDetail.userimage)
                        .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                        .error(R.drawable.user_basic) // 에러 이미지 리소스
                        .into(binding.writerImg)

                    if (reportDetail.nickname == userIdentity) {
                        isMe = true
                    }

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

                    FestivalManager.getFestivalData(
                        reportDetail.festivalId,
                        onSuccess = { festival ->
                            binding.reportFestival.text = festival.title
                            festivalId = festival.festivalId
                        },
                        onError = { throwable ->
                            Log.e("서버 테스트3", "오류: $throwable")
                        }
                    )

                    val commentList = listOf(
                        CommentListResponse(111, "관리자1", "",
                        "", 1, "안녕하세요, 관리자입니다. " +
                                    "\n확인 후, 조치완료하였습니다.", "2023-11-29 09:28:34"
                        , emptyList())
                    )

                    commentAdapter = CommentAdapter(commentList) // 초기에 빈 목록으로 어댑터 설정
                    recyclerView.adapter = commentAdapter
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
        }

        binding.reportYet.setOnClickListener { // 조치 완료 처리하기
            ReportManager.doneReportToServer(reportId)

            binding.reportYet.visibility = View.GONE
            binding.reportDone.visibility = View.VISIBLE
        }

        binding.reportFestival.setOnClickListener {
            if (festivalId != -1) {
                val intent = Intent(this, FestivalDetailActivity::class.java)
                intent.putExtra("festivalId", festivalId)
                startActivity(intent)
            }
        }

        reportModActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("my log", "수정본")
                // 수정 후 넘어왔을 때 보여줄 내용 추가
                ReportManager.getReportData(
                    reportId,
                    onSuccess = { reportDetail ->
                        supportActionBar?.title = reportDetail.title
                        binding.reportContent.text = reportDetail.content
                        binding.reportWriter.text = reportDetail.nickname
                        binding.reportAddress.text = reportDetail.address

                        if (reportDetail.nickname == userIdentity) {
                            isMe = true
                        }

                        if (reportDetail.done == true) {  // 조치 완료 라면
                            binding.reportYet.visibility = View.GONE
                            binding.reportDone.visibility = View.VISIBLE
                        }

                        Glide.with(this)
                            .load(reportDetail.userimage)
                            .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                            .error(R.drawable.user_basic) // 에러 이미지 리소스
                            .into(binding.writerImg)

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
    }

    private fun extractImage(imageString: String): List<String> {
        // 문자열에서 "[https://"로 시작하고 "," 또는 "]" 전까지의 부분을 추출
        val startIndex = imageString.indexOf("[")
        val endIndex = imageString.indexOf("]", startIndex)

        if (startIndex != -1 && endIndex != -1) {
            val substring = imageString.substring(startIndex, endIndex)
            // "["와  "]"를 제거하고 공백 기준으로 분리하여 리스트로 변환
            return substring.replace("[", "").split(",").map { it.trim() }
        }

        return emptyList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //if (isMe == true) {
            menuInflater.inflate(R.menu.detail_admin_menu, menu)
        //}
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로 가기 버튼 클릭 시
                finish() // 현재 액티비티 종료
                return true
            }
            R.id.mod_menu -> { //수정하기 버튼 클릭 시
                val intent = Intent(this, AddReportActivity::class.java)
                // 수정하는 것임을 알림
                intent.putExtra("new_report", 0)
                intent.putExtra("report_id", reportId)
                reportModActivityResult.launch(intent)
                Log.d("my log", "수정하러 가기")
                return true
            }
            R.id.remove_menu -> { //삭제하기 버튼 클릭 시
                ReportManager.deleteReportFromServer(reportId)
                finish() // 현재 글 삭제 후, 액티비티 종료
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}