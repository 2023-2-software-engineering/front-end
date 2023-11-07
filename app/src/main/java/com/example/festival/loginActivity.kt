package com.example.festival

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.festival.databinding.ActivityLoginBinding
import com.example.festival.databinding.ActivityMainBinding

class loginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //회원가입 화면으로 이동
        binding.signupBTN.setOnClickListener { 
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }



    }

}