package com.example.festival

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.festival.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
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
                Log.d("my log","클릭 작동중")
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_2 -> {}
            R.id.menu_3 -> {}
        }
        return true
    }

}