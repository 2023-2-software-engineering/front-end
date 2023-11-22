package com.example.festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.festival.databinding.ActivityAddBoardBinding
import com.example.festival.databinding.ActivityAddIdeaBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class AddIdeaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddIdeaBinding
    private var authToken: String ?= null // 로그인 토큰
    private var new: Int ?= 1 // 새로 작성이면 1, 수정이면 0
    private var ideaId: Int ?= -1 // 수정일 때의 해당 Id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 새로 작성 or 수정 (1이면 새로 작성, 아니면 수정)
        new = intent.getIntExtra("new_idea", 1)

        binding.root.setOnClickListener {
            // 화면의 다른 부분을 클릭하면 EditText의 포커스를 해제하고 키보드를 내림
            binding.ideaAddTitle.clearFocus()
            binding.ideaAddContent.clearFocus()
            hideKeyboard()
        }

        // EditText의 포커스가 변경될 때마다 호출되는 콜백 메서드 등록
        binding.ideaAddTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // EditText의 포커스가 해제되었을 때 처리할 내용
                hideKeyboard()
            }
        }

        binding.ideaAddContent.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // EditText의 포커스가 해제되었을 때 처리할 내용
                hideKeyboard()
            }
        }

        // 툴바 취소 버튼 클릭 시
        binding.ideaCancelBtn.setOnClickListener {
            finish()
        }

        // 툴바 완료 버튼 클릭 시
        binding.ideaSaveBtn.setOnClickListener {
            if (new == 1) {
                saveIdeaToServer() // 서버로 데이터 전송
                finish()
            } else {
                modIdeaToServer()  // 서버로 수정된 데이터 전송
            }
        }

        if (new != 1) { // 기존의 게시판 수정이라면, 기존의 내용 그대로 출력
            ideaId = intent.getIntExtra("idea_id", -1)
            Log.d("my log", "수정 중" + ideaId)
//            IdeaManager.getIdeaData(
//                ideaId!!,
//                onSuccess = { ideaDetail ->
//                    val editTitle = Editable.Factory.getInstance().newEditable(ideaDetail.title)
//                    val editContext = Editable.Factory.getInstance().newEditable(ideaDetail.content)
//                    binding.ideaAddTitle.text = editTitle
//                    binding.ideaAddContent.text = editContext
//                },
//                onError = { throwable ->
//                    Log.e("서버 테스트", "오류3: $throwable")
//                }
//            )
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun saveIdeaToServer() {
        val title = binding.ideaAddTitle.text.toString()
        val content = binding.ideaAddContent.text.toString()

        //val idea = Idea(title, content)
        //Log.d("my log", ""+idea)

        if (authToken != null) {
            //IdeaManager.sendIdeaToServer(board, authToken!!)
        }
    }

    private fun modIdeaToServer() {
        val title = binding.ideaAddTitle.text.toString()
        val content = binding.ideaAddContent.text.toString()

        //val idea = Idea(title, content)
        //Log.d("my log", ""+idea)

        if (authToken != null) {
            //IdeaManager.sendModIdeaToServer(ideaId!!, idea, authToken!!)

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