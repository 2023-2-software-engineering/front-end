package com.example.festival

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festival.databinding.ActivityIdeaBinding

class IdeaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdeaBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var ideaAdapter: IdeaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "아이디어 제안"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //툴바에 뒤로 가기 버튼 추가

        recyclerView = binding.ideaRecyclerView // 리사이클러뷰 초기화

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        ideaAdapter = IdeaAdapter(emptyList()) // 초기에 빈 목록으로 어댑터 설정
        recyclerView.adapter = ideaAdapter // 리사이클러뷰에 어댑터 설정

        loadIdeaList() // 아이디어 목록 출력

        binding.ideaAddBtn.setOnClickListener {
            val intent = Intent(this, AddIdeaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadIdeaList() {
        IdeaManager.getIdeaListData(
            onSuccess = { ideaListResponse ->
                val idea = ideaListResponse.map { it }
                ideaAdapter.updateData(idea)
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

        loadIdeaList()
    }
}