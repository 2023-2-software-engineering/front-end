package com.example.festival

import android.content.Context
import android.content.Intent
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

                    Glide.with(this)
                        .load("https://narsha-bucket-s3.s3.ap-northeast-2.amazonaws.com/post/2023/09/03/b4692b00-9c83-4268-96eb-fd82525e27b0.png")
                        .into(binding.festivalImg)
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