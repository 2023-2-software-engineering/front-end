package com.example.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.festival.databinding.ActivityIdeaDetailBinding

class IdeaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdeaDetailBinding
    private var ideaId = -1 // 현재 아이디어 ID를 담는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdeaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        ideaId = intent.getIntExtra("ideaId", -1)

        if (ideaId != -1) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로 가기 버튼 클릭 시
                finish() // 현재 액티비티 종료
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}