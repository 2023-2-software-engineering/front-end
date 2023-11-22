package com.example.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
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

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false // SearchView에 포커스 요청
            binding.searchView.requestFocus()
        }

        // SearchView의 Query(검색어) 입력 이벤트 처리
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼을 눌렀을 때의 동작을 여기에 구현 (검색어 사용)
                // 예를 들어, 검색어를 이용해 서버에서 데이터를 가져와 표시하거나 다른 동작을 수행할 수 있음

                searchFestivalList(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 입력이 변화할 때의 동작을 여기에 구현
                // 예를 들어, 실시간으로 검색어를 이용해 검색 결과를 갱신할 수 있음
                return false
            }
        })

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

    private fun searchFestivalList(query: String) {
        FestivalManager.getFestivalListData(
            onSuccess = { festivalListResponse ->
                val festival = festivalListResponse.map { it }

                // 검색어에 해당하는 플랜만 필터링
                val filteredList = if (query.isNotBlank()) {
                    festival.filter { festivals ->
                        festivals.title.contains(query, ignoreCase = true)
                    }
                } else {
                    festival
                }

                selectFestivalAdapter.updateData(filteredList)
            },
            onError = { throwable ->
                Log.e("서버 테스트3", "오류: $throwable")
            }
        )
    }
}