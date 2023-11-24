package com.example.festival

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festival.databinding.ActivityIdeaDetailBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class IdeaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdeaDetailBinding
    private var ideaId = -1 // 현재 아이디어 ID를 담는 변수
    private lateinit var imgAdapter: MultiImageLoadAdapter
    private var authToken: String ?= null // 로그인 토큰
    private lateinit var imageList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdeaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        ideaId = intent.getIntExtra("ideaId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        imgAdapter = MultiImageLoadAdapter(emptyList(), this)
        binding.imgRecyclerView.adapter = imgAdapter

        if (ideaId != -1) {
            IdeaManager.getIdeaData(
                ideaId,
                onSuccess = { ideaDetail ->
                    supportActionBar?.title = ideaDetail.title
                    binding.ideaContent.text = ideaDetail.content
                    binding.ideaWriter.text = ideaDetail.user.nickname
                    binding.ideaAddress.text = ideaDetail.user.address

                    Glide.with(this)
                        .load(ideaDetail.user.image)
                        .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                        .error(R.drawable.user_basic) // 에러 이미지 리소스
                        .into(binding.writerImg)

                    val imageString = ideaDetail.image
                    imageList = extractImage(imageString)

                    Log.d("my log", "반환된 이미지" + imageList)

                    imgAdapter = MultiImageLoadAdapter(imageList, applicationContext)
                    binding.imgRecyclerView.adapter = imgAdapter

//                    if (ideaDetail.user.nickname == userIdentity) {
//                        isMe = true
//                    }

                    val parts = ideaDetail.createdAt.split("T")
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
                        binding.ideaCreate.text = formattedDate
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