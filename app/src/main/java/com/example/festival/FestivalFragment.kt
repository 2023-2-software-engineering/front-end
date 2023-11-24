package com.example.festival

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.FragmentFestivalBinding

class FestivalFragment : Fragment() {
    lateinit var binding: FragmentFestivalBinding
    private lateinit var festivalAdapter: FestivalAdapter
    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private var authToken: String ?= null // 로그인 토큰
    private var searchWord: String ?= null // 검색어
    private var searchPlace: String ?= "모든 지역"
    private var searchIng: String ?= "진행예정"

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
        }

        binding.festivalBtn.setOnClickListener { // 축제 버튼 클릭 시 축제만 보여주기
            recyclerView.adapter = festivalAdapter
            loadFestivalList()

            binding.eventBtn.setBackgroundResource(android.R.color.white)
            binding.festivalBtn.setBackgroundResource(R.drawable.stroke_button)
        }

        binding.searchPlaceLayout.setOnClickListener {  // 지역 선택
            val selectPlaceDialog = BottomSearchPlaceFragment()
            selectPlaceDialog.setPlaceChangeListener(object : BottomSearchPlaceFragment.PlaceChangeListener{
                override fun onPlaceChanged(place: String) {
                    binding.searchPlace.text = place
                    searchPlace = place


                }
            })
            selectPlaceDialog.show(childFragmentManager, "select_place_dialog")
        }

        binding.searchDateLayout.setOnClickListener { // 진행 상황 선택
            val selectIngDialog = BottomSearchIngFragment()
            selectIngDialog.setIngChangeListener(object : BottomSearchIngFragment.IngChangeListener{
                override fun onIngChanged(ing: String) {
                    binding.searchDate.text = ing
                    searchIng = ing

                    if (ing == "전체") {
                        if (searchPlace == "모든 지역") {

                        } else {

                        }
                    } else if (ing == "진행중") {
                        if (searchPlace == "모든 지역") {

                        } else {

                        }
                    } else { // 진행 완료
                        if (searchPlace == "모든 지역") {

                        } else {

                        }
                    }
                }
            })
            selectIngDialog.show(childFragmentManager, "select_ing_dialog")
        }

        // 검색창에서 검색 시
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (!query.isNullOrBlank()) {
//
//                } else {
//                    // 검색어가 비어 있을 때 Toast 메시지를 표시
//                    Toast.makeText(requireContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
//                }
//                return true
//            }
//        }
//        )
    }

    private fun searchFestival() {

    }

    // 서버에서 페스티벌 리스트 불러오기
    private fun loadFestivalList() {
        val sharedPreferences = requireContext().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        FestivalManager.getFestivalListData(
            onSuccess = { festivalListResponse ->
                val festival = festivalListResponse.map { it }
                // 어댑터에 authToken 전달
                festivalAdapter.authToken = authToken

                festivalAdapter.updateData(festival)
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
                val event = eventListResponse.map { it }
                // 어댑터에 authToken 전달
                eventAdapter.authToken = authToken

                eventAdapter.updateData(event)
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }
}