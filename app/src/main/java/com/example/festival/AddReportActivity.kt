package com.example.festival

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.festival.databinding.ActivityAddReportBinding

class AddReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReportBinding
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 툴바 취소 버튼 클릭 시
        binding.reportCancelBtn.setOnClickListener {
            finish()
        }

        // 툴바 완료 버튼 클릭 시
        binding.reportSaveBtn.setOnClickListener {
            saveReportToServer() // 서버로 데이터 전송
            finish()
        }
    }

    private fun saveReportToServer() {
        val title = binding.reportAddTitle.text.toString()
        val content = binding.reportAddContent.text.toString()

        val report = Report(title, content)
        Log.d("my log", "" + report)

        if (authToken != null) {
            ReportManager.sendReportToServer(report, authToken!!)
        }
    }

    override fun onResume() {
        super.onResume()

        // 이전에 입력한 텍스트를 복원하여 보여줌
    }
}