package com.example.festival

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.FragmentFestivalBinding
import com.google.android.material.tabs.TabLayout

class FestivalFragment : Fragment() {
    lateinit var binding: FragmentFestivalBinding
    private lateinit var festivalAdapter: FestivalAdapter
    private lateinit var recyclerView: RecyclerView

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
        recyclerView.adapter = festivalAdapter // 리사이클러뷰에 어댑터 설정

        loadFestivalList()
    }

    // 서버에서 페스티벌 리스트 불러오기
    private fun loadFestivalList() {

    }

}