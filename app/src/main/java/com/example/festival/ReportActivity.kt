package com.example.festival

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "축제 이상 부스 신고"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        recyclerView = binding.reportRecyclerView // 리사이클러뷰 초기화

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        reportAdapter = ReportAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = reportAdapter // 리사이클러뷰에 어댑터 설정

        loadReportList()

        binding.reportBtn.setOnClickListener {
            val intent = Intent(this, AddReportActivity::class.java)
            startActivity(intent)
        }
    }

    // 서버에서 게시판 리스트 불러오기
    private fun loadReportList() {
        ReportManager.getReportListData(
            onSuccess = { reportListResponse ->
                val report = reportListResponse.map { it }
                reportAdapter.updateData(report)
            },
            onError = { throwable ->
                Log.e("서버 테스트", "오류3: $throwable")
            }
        )
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

    override fun onResume() {
        super.onResume()

        loadReportList()
    }
}