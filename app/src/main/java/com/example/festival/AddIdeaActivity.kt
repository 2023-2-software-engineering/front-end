package com.example.festival

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.festival.databinding.ActivityAddBoardBinding
import com.example.festival.databinding.ActivityAddIdeaBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

class AddIdeaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddIdeaBinding
    private var authToken: String ?= null // 로그인 토큰
    private var new: Int ?= 1 // 새로 작성이면 1, 수정이면 0
    private var ideaId: Int ?= -1 // 수정일 때의 해당 Id
    private lateinit var adapter: MultiImageAdapter
    private var uriList = ArrayList<Uri>()  // 선택한 이미지 uri

    private val multipleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris: List<Uri>? ->
            if (uris != null) {
                val totalSelectedImages = uriList.size + uris.size
                if (totalSelectedImages > 10) {
                    Toast.makeText(applicationContext, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // URI에 대한 지속적인 권한을 부여합니다.
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    for (uri in uris) {
                        applicationContext.contentResolver.takePersistableUriPermission(uri, flag)
                    }
                    uriList.addAll(0, uris)
                    Log.d("my log", ""+uriList)
                    adapter = MultiImageAdapter(uriList, applicationContext)
                    binding.recyclerView.adapter = adapter
                }
            } else {
                Toast.makeText(applicationContext, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }

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

        // RecyclerView에 LinearLayoutManager 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = MultiImageAdapter(uriList, this)
        binding.recyclerView.adapter = adapter

        // 이미지 추가 버튼 클릭 시
        binding.addImg.setOnClickListener {
            // 이미지 선택을 위해 ActivityResultLauncher 실행
            val remainingImages = 10 - uriList.size
            if (remainingImages > 0) {
                multipleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            } else {
                Toast.makeText(applicationContext, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }

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
        var imageParts: List<MultipartBody.Part> ?= null

        val idea = Idea(title, content)
        Log.d("my log", ""+idea)

        if (authToken != null) {
            if (uriList.isNotEmpty()) {
                imageParts = uriList.map { uri ->
                    val file = File(getRealPathFromURI(uri))
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                }
            } else {
                // 이미지가 없는 경우 빈 이미지를 생성하여 포함
                val emptyImageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), "")
                imageParts = listOf(MultipartBody.Part.createFormData("image", "", emptyImageRequestBody))
            }
            IdeaManager.sendIdeaToServer(authToken!!, idea, imageParts)
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

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath ?: ""
        }
        return ""
    }

    override fun onResume() {
        super.onResume()

        // 이전에 입력한 텍스트를 복원하여 보여줌
    }
}