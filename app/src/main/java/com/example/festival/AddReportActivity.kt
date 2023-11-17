package com.example.festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.festival.databinding.ActivityAddReportBinding

class AddReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReportBinding
    private var authToken: String ?= null // 로그인 토큰
    private var new: Int ?= 1 // 새로 작성이면 1, 수정이면 0
    private var reportId: Int ?= -1 // 수정일 때의 해당 신고 Id
    private var festivalId: Int ?= -1
    private var festivalTitle: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 새로 작성 or 수정 (1이면 새로 작성, 아니면 수정)
        new = intent.getIntExtra("new_report", 1)

        // 툴바 취소 버튼 클릭 시
        binding.reportCancelBtn.setOnClickListener {
            finish()
        }

        // 툴바 완료 버튼 클릭 시
        binding.reportSaveBtn.setOnClickListener {
            if (new == 1) {
                saveReportToServer() // 서버로 데이터 전송
                finish()
            } else {
                modReportToServer()
            }

        }

        if (new != 1) { // 수정이라면, 기존의 내용 그대로 출력
            reportId = intent.getIntExtra("report_id", -1)
            Log.d("my log", "수정 중" + reportId)
            ReportManager.getReportData(
                reportId!!,
                onSuccess = { reportDetail ->
                    val editTitle = Editable.Factory.getInstance().newEditable(reportDetail.title)
                    val editContext = Editable.Factory.getInstance().newEditable(reportDetail.content)
                    binding.reportAddTitle.text = editTitle
                    binding.reportAddContent.text = editContext
                },
                onError = { throwable ->
                    Log.e("서버 테스트", "오류3: $throwable")
                }
            )
        }
    }

    private fun saveReportToServer() {
        val title = binding.reportAddTitle.text.toString()
        val content = binding.reportAddContent.text.toString()

        val report = Report(title, content, festivalId!!)
        Log.d("my log", "" + report)

        if (authToken != null) {
            //ReportManager.sendReportToServer(authToken!!, report)
        }
    }

    private fun modReportToServer() {
        val title = binding.reportAddTitle.text.toString()
        val content = binding.reportAddContent.text.toString()

        val report = Report(title, content, festivalId!!)
        Log.d("my log", ""+report)

        if (authToken != null) {
            //ReportManager.sendModReportToServer(reportId!!, report, authToken!!)

            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            Log.d("my log", "수정본 요청")
            finish() // 현재 액티비티 종료
        }
    }

    override fun onResume() {
        super.onResume()

        // 이전에 입력한 텍스트를 복원하여 보여줌
    }
}