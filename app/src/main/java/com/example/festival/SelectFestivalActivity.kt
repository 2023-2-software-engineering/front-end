package com.example.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivitySelectFestivalBinding

class SelectFestivalActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectFestivalBinding
    private lateinit var selectFestivalAdapter: SelectFestivalAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectFestivalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        recyclerView = binding.festivalRecyclerView

        val layoutManager = LinearLayoutManager(this)
        binding.festivalRecyclerView.layoutManager = layoutManager

        selectFestivalAdapter = SelectFestivalAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = selectFestivalAdapter // 리사이클러뷰에 어댑터 설정

        loadFestivalList()

        binding.boardCancelBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadFestivalList() {  // 서버에서 리스트 불러오기
        FestivalManager.getFestivalListData(
            onSuccess = { festivalListResponse ->
                val festival = festivalListResponse.map { it }
                selectFestivalAdapter.updateData(festival)
            },
            onError = { throwable ->
                Log.e("서버 테스트3", "오류: $throwable")
            }
        )
    }
}