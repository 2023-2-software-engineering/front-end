package com.example.festival

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.key
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.FragmentFestivalBinding
import okhttp3.MultipartBody.Part.Companion.createFormData

class FestivalFragment : Fragment() {
    lateinit var binding: FragmentFestivalBinding
    private lateinit var festivalAdapter: FestivalAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private var authToken: String ?= null // 로그인 토큰
    private var searchWord: String ?= "" // 검색어
    private var searchPlace: String ?= "모든 지역"
    private var searchIng: String ?= "전체"
    private var type: Int ?= 1 // 1: 축제, 0: 이벤트
    private var state: Int ?= null // 모든, 1: 진행중, 0: 진행 완료
    private var region: String ?= "" // 지역

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFestivalBinding.inflate(inflater, container, false)

//        val tabs: TabLayout = binding.tabs
//        tabs.addTab(tabs.newTab().setText("축제"))
//        tabs.addTab(tabs.newTab().setText("이벤트"))
//        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val position = tab!!.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                // 구현 필요 없음
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                // 구현 필요 없음
//            }
//        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.festivalRecyclerView) // 리사이클러뷰 초기화

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        festivalAdapter = FestivalAdapter(emptyList())  // 초기에 빈 목록으로 어댑터 설정
        eventAdapter = EventAdapter(emptyList())
        recyclerView.adapter = festivalAdapter // 리사이클러뷰에 어댑터 설정

        loadFestivalList()

        binding.eventBtn.setOnClickListener { //이벤트 버튼 클릭 시 이벤트만 보여주기
            recyclerView.adapter = eventAdapter
            loadEventList()

            binding.festivalBtn.setBackgroundResource(android.R.color.white)
            binding.eventBtn.setBackgroundResource(R.drawable.stroke_button)
            type = 0
        }

        binding.festivalBtn.setOnClickListener { // 축제 버튼 클릭 시 축제만 보여주기
            recyclerView.adapter = festivalAdapter
            loadFestivalList()

            binding.eventBtn.setBackgroundResource(android.R.color.white)
            binding.festivalBtn.setBackgroundResource(R.drawable.stroke_button)
            type = 1
        }

        binding.searchPlaceLayout.setOnClickListener {  // 지역 선택
            val selectPlaceDialog = BottomSearchPlaceFragment()
            selectPlaceDialog.setPlaceChangeListener(object : BottomSearchPlaceFragment.PlaceChangeListener{
                override fun onPlaceChanged(place: String) {
                    binding.searchPlace.text = place

                    if (place == "모든 지역") { region = "" }
                    else { region = place }

                    if (type == 0) {
                        loadEventList()
                    } else {
                        loadFestivalList()
                    }
                }
            })
            selectPlaceDialog.show(childFragmentManager, "select_place_dialog")
        }

        binding.searchDateLayout.setOnClickListener { // 진행 상황 선택
            val selectIngDialog = BottomSearchIngFragment()
            selectIngDialog.setIngChangeListener(object : BottomSearchIngFragment.IngChangeListener{
                override fun onIngChanged(ing: String) {
                    binding.searchDate.text = ing

                    if (ing == "전체") { state = null }
                    else if (ing == "진행중") { state = 1 }
                    else { state = 0 }

                    if (type == 0) {
                        loadEventList()
                    } else {
                        loadFestivalList()
                    }
                }
            })
            selectIngDialog.show(childFragmentManager, "select_ing_dialog")
        }

        // 검색창에서 검색 시
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    if (type == 0) {
                        searchEvent(query)
                        hideKeyBoard()
                    } else {
                        searchFestival(query)
                        hideKeyBoard()
                    }
                } else {
                    // 검색어가 비어 있을 때 Toast 메시지를 표시
                    Toast.makeText(requireContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 쿼리 텍스트 변경 처리 (사용자가 입력하는 동안)
                // newText를 사용하여 데이터 필터링 및 표시된 데이터 업데이트 가능
                return true
            }
        })

        // 레이아웃의 루트 뷰에 대한 클릭 리스너 설정
        binding.root.setOnClickListener {
            hideKeyBoard()
        }
    }

    private fun searchFestival(keyword: String?= "") {
        SearchManager.getSearchFestival(
            state, region, keyword,
            onSuccess = { festivalList ->
                val festival = festivalList.map { it }
                // 어댑터에 authToken 전달
                festivalAdapter.authToken = authToken

                festivalAdapter.updateData(festival)
                Log.d("my log", "검색 요청" + state + region + keyword)
                Log.d("my log", "검색 결과" + festivalList)
            },
            onError = { throwable ->  // 검색어에 맞는 검색 결과가 없으면 여기로 옴
                Log.e("서버 테스트", "오류3: $throwable")
                festivalAdapter.updateData(emptyList())
            }
        )
    }

    private fun searchEvent(keyword: String?= "") {
        SearchManager.getSearchEvent(
            state, region, keyword,
            onSuccess = { eventList ->
                val event = eventList.map { it }
                // 어댑터에 authToken 전달
                eventAdapter.authToken = authToken

                eventAdapter.updateData(event)
            },
            onError = { throwable ->  // 검색어에 맞는 검색 결과가 없으면 여기로 옴
                Log.e("서버 테스트", "오류3: $throwable")
                eventAdapter.updateData(emptyList())
            }
        )
    }

    // 서버에서 페스티벌 리스트 불러오기
    private fun loadFestivalList() {
        val sharedPreferences = requireContext().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        FestivalManager.getFestivalListData(
            onSuccess = { festivalListResponse ->
                if (state == null && region == "") {
                    val festival = festivalListResponse.map { it }
                    festivalAdapter.updateData(festival)
                } else if (state == null) {
                    val filteredFestivals = festivalListResponse.filter { festival ->
                        festival.region == region
                    }
                    val festival = filteredFestivals.map { it }
                    festivalAdapter.updateData(festival)
                } else if (region == "") {
                    val filteredFestivals = festivalListResponse.filter { festival ->
                        festival.state == state
                    }
                    val festival = filteredFestivals.map { it }
                    festivalAdapter.updateData(festival)
                } else {
                    val filteredFestivals = festivalListResponse.filter { festival ->
                        festival.state == state && festival.region == region
                    }
                    val festival = filteredFestivals.map { it }
                    festivalAdapter.updateData(festival)
                }

                // 어댑터에 authToken 전달
                festivalAdapter.authToken = authToken
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }

    private fun loadEventList() {
        val sharedPreferences = requireContext().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        EventManager.getEventListData(
            onSuccess = { eventListResponse ->
                if (state == null && region == "") {
                    val event = eventListResponse.map { it }
                    eventAdapter.updateData(event)
                } else if (state == null) {
                    val filteredEvents = eventListResponse.filter { event ->
                        event.region == region
                    }
                    val event = filteredEvents.map { it }
                    eventAdapter.updateData(event)
                } else if (region == "") {
                    val filteredEvents = eventListResponse.filter { event ->
                        event.state == state
                    }
                    val event = filteredEvents.map { it }
                    eventAdapter.updateData(event)
                } else {
                    val filteredEvents = eventListResponse.filter { event ->
                        event.state == state && event.region == region
                    }
                    val event = filteredEvents.map { it }
                    eventAdapter.updateData(event)
                }

                // 어댑터에 authToken 전달
                eventAdapter.authToken = authToken
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }

    private fun hideKeyBoard() {
        Log.d("my log", "레이아웃 클릭")
        // 검색창의 초점을 없애고 키보드를 내림
        binding.searchView.clearFocus()
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}