package com.example.festival

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.festival.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding
    private var authToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        // 저장된 토큰 읽어오기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", AppCompatActivity.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {
            UserManager.getUserData(
                authToken!!,
                onSuccess = { userData ->
                    binding.mypageName.text = userData.username
                    binding.mypageAddress.text = userData.address
                    binding.mypageTel.text = userData.phoneNumber

                    Glide.with(this)
                        .load(userData.image)
                        .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                        .error(R.drawable.user_basic) // 에러 이미지 리소스
                        .into(binding.userImageView )
                },
                onError = { throwable ->
                    Log.e("서버 테스트", "오류3: $throwable")
                }
            )
        }

        binding.userInfo.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.infoUpdate.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.likeList.setOnClickListener {
            val intent = Intent(activity, LikeListActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}