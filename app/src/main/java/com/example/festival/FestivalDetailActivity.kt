package com.example.festival

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.festival.databinding.ActivityFestivalDetailBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class FestivalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFestivalDetailBinding
    private var festivalId = -1 // 현재 축제 ID를 담는 변수
    private var authToken: String ?= null // 로그인 토큰
    private var likeCount: Int ?= 0
    private var isLiked:Boolean = false // 초기에는 좋아요가 되지 않은 상태로

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "축제"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        festivalId = intent.getIntExtra("festivalId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        if (festivalId != -1) {
            FestivalManager.getFestivalData(
                festivalId,
                onSuccess = { festivalDetail ->
                    binding.festivalTitle.text = festivalDetail.title
                    binding.festivalText.text = festivalDetail.content
                    //binding.festivalDate.text = festivalDetail.date.toString()
                    binding.festivalPlace.text = festivalDetail.location
                    binding.viewCount.text = "${festivalDetail.view} 회"

                    Glide.with(this)
                        .load(festivalDetail.image)
                        .into(binding.festivalImg)
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
            FestivalManager.getFestivalLike(
                festivalId,
                onSuccess = { festivallike ->
                    likeCount = festivallike
                    binding.festivalLike.text = "$festivallike"

                    // 좋아요 상태에 따라 UI 업데이트
                    updateLikeUI()
                },
                onError = { throwable ->
                    Log.e("서버 테스트3", "오류: $throwable")
                }
            )
            if (authToken != null) {
                FestivalManager.getFestivalLikeCheck(
                    festivalId, authToken!!,
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

        binding.festivalLikeImg.setOnClickListener { // 좋아요 버튼 클릭 시
            if (authToken != null) {
                isLiked = !isLiked // 토글 형식으로 상태 변경
                if (isLiked) { //좋아요 요청
                    FestivalManager.sendFestivalLike(festivalId, authToken!!) { isSuccess ->
                        if (isSuccess) {
                            updateLikeUI() // 좋아요 상태 UI 업데이트
                            // 좋아요 수 업데이트
                            FestivalManager.getFestivalLike(
                                festivalId,
                                onSuccess = { festivallike ->
                                    likeCount = festivallike
                                    binding.festivalLike.text = "$festivallike"
                                },
                                onError = { throwable ->
                                    Log.e("서버 테스트3", "오류: $throwable")
                                }
                            )
                        }
                    }
                } else { //좋아요 해제
                    FestivalManager.deleteFestivalLike(festivalId, authToken!!) { isSuccess ->
                        if (isSuccess) {
                            updateLikeUI() // 좋아요 상태 UI 업데이트
                            // 좋아요 수 업데이트
                            FestivalManager.getFestivalLike(
                                festivalId,
                                onSuccess = { festivallike ->
                                    likeCount = festivallike
                                    binding.festivalLike.text = "$festivallike"
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
            binding.festivalLikeImg.text = "♥ "
        } else { //로그인 한 유저가 좋아요를 누른 상태가 아니라면
            binding.festivalLikeImg.text = "♡ "
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