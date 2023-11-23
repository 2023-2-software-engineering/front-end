package com.example.festival

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.festival.databinding.ActivityUserInfoBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private var authToken: String ?= null // 로그인 토큰
    private var uriList = ArrayList<Uri>() //대표이미지 추가를 위한

    private val singleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                // 이미 선택된 사진을 지웁니다.
                uriList.clear()

                uriList.add(uri)
                binding.userImg.setImageURI(uri)

                // URI에 대한 지속적인 권한을 부여합니다.
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                applicationContext.contentResolver.takePersistableUriPermission(uri, flag)
            } else {
                Toast.makeText(applicationContext, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {
            UserManager.getUserData(
                authToken!!,
                onSuccess = { userData ->
                    val editName = Editable.Factory.getInstance().newEditable(userData.username)
                    val editAddress = Editable.Factory.getInstance().newEditable(userData.address)
                    binding.userName.text = editName
                    binding.userAddress.text = editAddress

                    Glide.with(this)
                        .load(userData.image)
                        .placeholder(R.drawable.user_basic) // 플레이스홀더 이미지 리소스
                        .error(R.drawable.user_basic) // 에러 이미지 리소스
                        .into(binding.userImg)
                },
                onError = { throwable ->
                    Log.e("서버 테스트", "오류3: $throwable")
                }
            )
        }

        // 툴바 뒤로 버튼 클릭 시
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.imgAddBtn.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.userDone.setOnClickListener {
            saveUserToServer()
            finish()
        }
    }

    private fun saveUserToServer() {
        val name = binding.userName.text.toString()
        val address = binding.userAddress.text.toString()
        var imagePart: MultipartBody.Part ?= null

        val user = UserUpdate(name, address)
        Log.d("my log", ""+user)

        if (authToken != null) {
            if (uriList.isNotEmpty()) {
                val file = File(getRealPathFromURI(uriList[0]))
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            } else {
                // 이미지가 없는 경우 빈 이미지를 생성하여 포함
                val emptyImageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), "")
                imagePart = MultipartBody.Part.createFormData("image", "", emptyImageRequestBody)
            }
            Log.d("my log", ""+user+imagePart)
            UserManager.sendUserUpdate(authToken!!, user, imagePart)
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
}