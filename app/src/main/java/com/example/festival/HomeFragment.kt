package com.example.festival

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.festival.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hotAdapter: HotAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var childFragMang: FragmentManager
    private var festivalId = -1 // 조회수 top 축제 ID를 담는 변수
    private var eventId = -1 // 조회수 top 이벤트 ID를 담는 변수

    class MainTopicAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle, private val mainList: List<Main>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return mainList.size
        }

        override fun createFragment(position: Int): Fragment {
            val main = mainList[position]
            return ViewPagerFragment.newInstance(main)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.hotRecyclerView

        viewPager = binding.viewPager

        childFragMang = childFragmentManager

        loadViewPager()

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        hotAdapter = HotAdapter(emptyList())
        recyclerView.adapter = hotAdapter

        showHot() // 핫토픽 출력

        showViewTop() // 조회수 1위 출력

        binding.festivalCardView.setOnClickListener {
            if(festivalId != -1) {
                val intent = Intent(activity, FestivalDetailActivity::class.java)
                intent.putExtra("festivalId", festivalId)
                startActivity(intent)
            }
        }

        binding.festivalCardView2.setOnClickListener {
            if(eventId != -1) {
                val intent = Intent(activity, EventDetailActivity::class.java)
                intent.putExtra("eventId", eventId)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun loadViewPager() {
        val mainList = listOf(
            Main(1, "2023 청년 상인 축제", null),
            Main(2, "ㅎㅇ", null),
            Main(3, "3번째", null)
        )

        val adapter = MainTopicAdapter(childFragMang, lifecycle, mainList)
        viewPager.adapter = adapter
    }

    private fun showHot() {
        FestivalManager.getHotFestival(
            onSuccess = { hotList ->
                val hot = hotList.map { it }
                Log.d("my log", "핫 축제 목록"+ hot)
                if (hot.isEmpty()) {
                    hotAdapter.updateData(emptyList())
                } else {
                    hotAdapter.updateData(hot)
                }
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
                hotAdapter.updateData(emptyList())
            }
        )
    }

    private fun showViewTop() {
        FestivalManager.getFestivalViewTop(
            onSuccess = { top ->
                Log.d("my log", "조회수 1위 축제"+ top)
                binding.newTitle1.text = top.title
                binding.newContent1.text = top.content
                festivalId = top.festivalId
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
        EventManager.getEventViewTop(
            onSuccess = { top ->
                Log.d("my log", "조회수 1위 이벤트"+ top)
                binding.newTitle2.text = top.title
                binding.newContent2.text = top.content
                eventId = top.eventId
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
    }
}