package com.example.festival

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityLikeListBinding

class LikeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikeListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var festivalAdapter: FestivalAdapter
    private lateinit var eventAdapter: EventAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        recyclerView = binding.listRecyclerView // 리사이클러뷰 초기화

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        festivalAdapter = FestivalAdapter(emptyList())  // 초기에 빈 목록으로 어댑터 설정
        eventAdapter = EventAdapter(emptyList())
        recyclerView.adapter = festivalAdapter // 리사이클러뷰에 어댑터 설정

        loadFestivalList()

//        binding.eventBtn.setOnClickListener { //이벤트 버튼 클릭 시 이벤트만 보여주기
//            recyclerView.adapter = eventAdapter
//            loadEventList()
//
//            binding.festivalBtn.setBackgroundResource(android.R.color.white)
//            binding.eventBtn.setBackgroundResource(R.drawable.select_button)
//        }
//
//        binding.festivalBtn.setOnClickListener { // 축제 버튼 클릭 시 축제만 보여주기
//            recyclerView.adapter = festivalAdapter
//            loadFestivalList()
//
//            binding.eventBtn.setBackgroundResource(android.R.color.white)
//            binding.festivalBtn.setBackgroundResource(R.drawable.select_button)
//        }

        // 툴바 뒤로 버튼 클릭 시
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadFestivalList() {
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        SearchManager.getMyFestival(
            authToken!!,
            onSuccess = { festivalListResponse ->
                val festival = festivalListResponse.map { it }
                festivalAdapter.updateData(festival)

                // 어댑터에 authToken 전달
                festivalAdapter.authToken = authToken
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }

//    private fun loadEventList() {
//        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
//        authToken = sharedPreferences.getString("auth_token", null)
//
//        SearchManager.getMyEvent(
//            authToken!!,
//            onSuccess = { eventListResponse ->
//                val event = eventListResponse.map { it }
//                eventAdapter.updateData(event)
//
//                // 어댑터에 authToken 전달
//                eventAdapter.authToken = authToken
//            },
//            onError = { throwable ->
//                Log.e("서버 테스트", "오류3: $throwable")
//            }
//        )
//    }
}