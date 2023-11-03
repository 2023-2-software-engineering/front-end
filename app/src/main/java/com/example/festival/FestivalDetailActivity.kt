package com.example.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.festival.databinding.ActivityFestivalDetailBinding

class FestivalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFestivalDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "축제"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가
    }
}