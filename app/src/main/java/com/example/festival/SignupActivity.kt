package com.example.festival

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.festival.JoinManager.sendJoinToServer
import com.example.festival.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.signupBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupBtn.setOnClickListener {
            val username = binding.name.text.toString()
            val nickname = binding.nickname.text.toString()
            val id = binding.id.text.toString()
            val password = binding.password.text.toString()
            val phone = binding.phoneNumber.text.toString()
            val address = binding.address.text.toString()

            val join = Join(username, nickname, id, address, password, phone)

            sendJoinToServer(join)

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }


    }
}