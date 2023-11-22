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
                    val editName = Editable.Factory.getInstance().newEditable(userData.username)
                    val editAddress = Editable.Factory.getInstance().newEditable(userData.address)
                    val editPhone = Editable.Factory.getInstance().newEditable(userData.phoneNumber)
                    binding.mypageName.text = editName
                    binding.mypageAddress.text = editAddress
                    binding.mypageTel.text = editPhone
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

        return binding.root
    }

}