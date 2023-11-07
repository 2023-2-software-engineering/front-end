package com.example.festival

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.festival.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 자동 로그인 확인
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {  // 저장된 토큰이 있으면 자동으로 홈 화면으로 이동
            moveToHomeScreen()
        }

        binding.login.setOnClickListener { // 로그인 버튼 클릭 시
            val id = binding.loginID.text.toString()
            val password = binding.loginPW.text.toString()

            if (id != null && password != null) {
                val login = Login(id, password)
                Log.d("my log", "로그인 데이터" + login)

                LogInManager.sendLogInToServer(login) { authToken ->
                    val tokenOnly = authToken?.substringAfter("Bearer ")
                    Log.d("my log", "authToken: ${authToken}")

                    if (tokenOnly != null) {
                        val jwt = JWT(tokenOnly)
                        val id = jwt.getClaim("identify").asString()
                        userId = jwt.getClaim("id")?.asInt() ?: -1

                        saveAuthToken(authToken, id ?: "", userId)
                        moveToHomeScreen() // 토큰 저장 후 홈 화면으로 이동
                    }
                }
            }
        }

        //회원가입 화면으로 이동
        binding.signupBTN.setOnClickListener { 
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // 토큰을 저장하는 메서드
    private fun saveAuthToken(token: String, id: String, userId: Int) {
        Log.d("my log", "로그인 토큰 테스트" + token)

        // 토큰 저장
        val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.putString("user_identify", id)
        editor.putInt("userId", userId)
        editor.apply()
    }

    // 홈 화면으로 이동하는 메서드
    private fun moveToHomeScreen() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}