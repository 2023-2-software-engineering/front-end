package com.example.festival

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.festival.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDetailBinding
    private var eventId = -1 // 현재 이벤트 ID를 담는 변수
    private var authToken: String ?= null // 로그인 토큰
    private var likeCount: Int ?= 0
    private var isLiked:Boolean = false // 초기에는 좋아요가 되지 않은 상태로

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "이벤트"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        eventId = intent.getIntExtra("eventId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        if (eventId != -1) {
            EventManager.getEventData(
                eventId,
                onSuccess = { eventDetail ->
                    binding.eventTitle.text = eventDetail.title
                    binding.eventText.text = eventDetail.content
                    binding.eventStart.text = eventDetail.startDay
                    binding.eventEnd.text = eventDetail.endDay
                    binding.eventRegister.text = eventDetail.register
                    binding.viewCount.text = "${eventDetail.view} 회"

                    Glide.with(this)
                        .load(eventDetail.image)
                        .into(binding.eventImg)
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
            EventManager.getEventLike(
                eventId,
                onSuccess = { eventLike ->
                    likeCount = eventLike
                    binding.eventLike.text = "$eventLike"

                    // 좋아요 상태에 따라 UI 업데이트
                    updateLikeUI()
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
            if (authToken != null) {
                EventManager.getEventLikeCheck(
                    eventId, authToken!!,
                    onSuccess = { isMe ->
                        isLiked = (isMe == 1)
                        updateLikeUI()
                    },
                    onError = { throwable ->
                        Log.e("서버 테스트3", "오류: $throwable")
                    }
                )
            }
        }

        binding.eventLikeImg.setOnClickListener { // 좋아요 버튼 클릭 시
            if (authToken != null) {
                isLiked = !isLiked // 토글 형식으로 상태 변경
                if (isLiked) { //좋아요 요청
                    EventManager.sendEventLike(eventId, authToken!!) { isSuccess ->
                        if (isSuccess) {
                            updateLikeUI() // 좋아요 상태 UI 업데이트
                            // 좋아요 수 업데이트
                            EventManager.getEventLike(
                                eventId,
                                onSuccess = { eventLike ->
                                    likeCount = eventLike
                                    binding.eventLike.text = "$eventLike"
                                },
                                onError = { throwable ->
                                    Log.e("서버 테스트3", "오류: $throwable")
                                }
                            )
                        }
                    }
                } else { //좋아요 해제
                    EventManager.deleteEventLike(eventId, authToken!!) { isSuccess ->
                        if (isSuccess) {
                            updateLikeUI() // 좋아요 상태 UI 업데이트
                            // 좋아요 수 업데이트
                            EventManager.getEventLike(
                                eventId,
                                onSuccess = { eventLike ->
                                    likeCount = eventLike
                                    binding.eventLike.text = "$eventLike"
                                },
                                onError = { throwable ->
                                    Log.e("서버 테스트3", "오류: $throwable")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateLikeUI() {
        if (isLiked) { //로그인 한 유저가 좋아요를 누른 상태라면
            binding.eventLikeImg.text = "♥ "
        } else { //로그인 한 유저가 좋아요를 누른 상태가 아니라면
            binding.eventLikeImg.text = "♡ "
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