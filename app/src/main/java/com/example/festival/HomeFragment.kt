package com.example.festival

import android.adservices.topics.Topic
import android.os.Bundle
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

    }
}