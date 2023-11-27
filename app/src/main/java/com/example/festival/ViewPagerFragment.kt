package com.example.festival

import android.adservices.topics.Topic
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.festival.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding

    companion object {
        fun newInstance(mainTopic: Main): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val args = Bundle()
            args.putInt("festivalId", mainTopic.festivalId)
            args.putString("title", mainTopic.title)
            args.putString("image", mainTopic.image)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val festivalId = arguments?.getInt("festivalId")
        val title = arguments?.getString("title")
        val image = arguments?.getString("image")

        binding.mainTitle.text = title

        binding.main.setOnClickListener {
            val intent = Intent(activity, FestivalDetailActivity::class.java)
            intent.putExtra("festivalId", festivalId)
            startActivity(intent)
        }

        // 이미지 리소스 ID 가져오기
        val imageResourceId = context?.resources?.getIdentifier(image, "drawable", context?.packageName)

        // 이미지 리소스 ID가 유효하면 ImageView에 설정
        imageResourceId?.let {
            binding.mainImg.setImageResource(it)
        }

        return binding.root
    }
}