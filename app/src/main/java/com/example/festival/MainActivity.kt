package com.example.festival

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.festival.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //toolbar
        setSupportActionBar(binding.toolbar)

        //bottom navigator
        binding.bottomMenu.setOnItemSelectedListener {item ->
            changeFragment(
                when (item.itemId) {
                    R.id.home -> HomeFragment()
                    R.id.festival -> FestivalFragment()
                    R.id.board -> BoardFragment()
                    else -> MyPageFragment()
                }
            )
            true
        }

        binding.navView.setNavigationItemSelectedListener(this)

        // 초기화면으로 HomeFragment를 보여줄 수 있도록 설정
        changeFragment(HomeFragment())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.MANAGE_MEDIA) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mainactivity", "권한 요청")
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(
                    arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.MANAGE_MEDIA),
                    2
                );
            }
        }

        // 권한 요청 코드
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되지 않은 경우 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)  //menu_actionbar 불러 와서 사용할 것임, 연결 (menu 와)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.toolbar_nav) { // 툴바의 toolbar_nav 버튼 클릭 시
            if (binding.drawer.isDrawerOpen(GravityCompat.END)) {
                binding.drawer.closeDrawer(GravityCompat.END) // 드로어 레이아웃 닫기
            } else {
                binding.drawer.openDrawer(GravityCompat.END) // 드로어 레이아웃 열기
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // 드로어 레이아웃(네비게이션 뷰)의 아이템들 선택될 때마다 속성(아이디) 전달 되어 실행
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_1 -> {
                Log.d("my log","이상 부스 신고 이동")
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_2 -> {
                Log.d("my log","아이디어 제안 이동")
                val intent = Intent(this, IdeaActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_3 -> {
                val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("auth_token", null)
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}