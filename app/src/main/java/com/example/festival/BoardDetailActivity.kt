package com.example.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.festival.databinding.ActivityBoardDetailBinding
import com.example.festival.databinding.ActivityFestivalDetailBinding

class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가
    }
}