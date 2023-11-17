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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.festival.BoardManager.sendBoardToServer
import com.example.festival.BoardManager.sendModBoardToServer
import com.example.festival.databinding.ActivityAddBoardBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AddBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBoardBinding
    private var authToken: String ?= null // 로그인 토큰
    private var new: Int ?= 1 // 새로 작성이면 1, 수정이면 0
    private var boardId: Int ?= -1 // 수정일 때의 해당 게시판 Id
    private lateinit var adapter: MultiImageAdapter
    private var uriList = ArrayList<Uri>()  // 선택한 이미지 uri
    private var festivalId: Int ?= -1
    private var festivalTitle: String ?= null

//    private val awsAccessKey = ""
//    private val awsSecretKey = ""
//    private val awsCredentials = BasicAWSCredentials(awsAccessKey, awsSecretKey)
//    private lateinit var transferUtility: TransferUtility
//    val s3Client = AmazonS3Client(
//        awsCredentials,
//        com.amazonaws.regions.Region.getRegion(Regions.AP_NORTHEAST_2)
//    )

    companion object {
        lateinit var selectFestivalActivityResult: ActivityResultLauncher<Intent>
    }

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
        binding = ActivityAddBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 새로 작성 or 수정 (1이면 새로 작성, 아니면 수정)
        new = intent.getIntExtra("new_board", 1)

        // RecyclerView에 LinearLayoutManager 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = MultiImageAdapter(uriList, this)
        binding.recyclerView.adapter = adapter

        selectFestivalActivityResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("my log", "선택 완료")
                // 선택 후 넘어왔을 때 보여줄 내용 추가

                val data = result.data
                festivalId = data?.getIntExtra("festivalId", -1)
                festivalTitle = data?.getStringExtra("festivalTitle")

                if (festivalId != -1) {
                    binding.festivalTitle.text = festivalTitle
                }
            }
        }

        // 툴바 취소 버튼 클릭 시
        binding.boardCancelBtn.setOnClickListener {
            finish()
        }

        // 툴바 완료 버튼 클릭 시
        binding.boardSaveBtn.setOnClickListener {
            if (new == 1) {
                saveBoardToServer() // 서버로 데이터 전송
                finish()
            } else {
                modBoardToServer()  // 서버로 수정된 데이터 전송
            }
        }

        // 축제 추가 버튼 클릭 시
        binding.addFestival.setOnClickListener {
            val intent = Intent(this, SelectFestivalActivity::class.java)
            selectFestivalActivityResult.launch(intent)
            Log.d("my log", "추가하러 가기")
        }

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

        if (new != 1) { // 기존의 게시판 수정이라면, 기존의 내용 그대로 출력
            boardId = intent.getIntExtra("board_id", -1)
            Log.d("my log", "수정 중" + boardId)
            BoardManager.getBoardData(
                boardId!!,
                onSuccess = { boardDetail ->
                    val editTitle = Editable.Factory.getInstance().newEditable(boardDetail.title)
                    val editContext = Editable.Factory.getInstance().newEditable(boardDetail.content)
                    binding.boardAddTitle.text = editTitle
                    binding.boardAddContent.text = editContext
                },
                onError = { throwable ->
                    Log.e("서버 테스트", "오류3: $throwable")
                }
            )
        }
    }

    private fun saveBoardToServer() {
        val title = binding.boardAddTitle.text.toString()
        val content = binding.boardAddContent.text.toString()
        var imagePart: MultipartBody.Part ?= null

        val board = Board(title, content, festivalId!!)
        Log.d("my log", ""+board)

        if (authToken != null) {
            if (uriList.isNotEmpty()) {
                val file = File(getRealPathFromURI(uriList[0]))
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
            sendBoardToServer(board, imagePart!!, authToken!!)
        }
    }

    private fun modBoardToServer() {
        val title = binding.boardAddTitle.text.toString()
        val content = binding.boardAddContent.text.toString()
        var imagePart: MultipartBody.Part ?= null

        val board = Board(title, content, festivalId!!)
        Log.d("my log", ""+board)

        if (authToken != null) {
            if (uriList.isNotEmpty()) {
                val file = File(getRealPathFromURI(uriList[0]))
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
            sendModBoardToServer(boardId!!, board, imagePart!!, authToken!!)

            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
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