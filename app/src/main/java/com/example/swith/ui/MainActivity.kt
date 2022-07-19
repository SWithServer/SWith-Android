package com.example.swith.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityMainBinding
import com.example.swith.ui.home.HomeFragment
import com.example.swith.utils.ToolBarManager


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavigation()

        ToolBarManager(this).initToolBar(binding.mainToolbar,
            titleVisible = false,
            backVisible = false
        )
    }
    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.bottom_nav_home ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_search -> {
                    // TODO: search
                }
                R.id.bottom_nav_profile -> {
                    // TODO: profile
                    // ProfileActivity -> ProfileFragment 로 전환해야 할듯?!
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.toolbar_setting).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_notification ->{
                Toast.makeText(applicationContext, "알림 버튼 Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.toolbar_setting -> {
                Toast.makeText(applicationContext, "설정 버튼 Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}