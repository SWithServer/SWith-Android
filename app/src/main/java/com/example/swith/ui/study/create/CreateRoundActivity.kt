package com.example.swith.ui.study.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityCreateRoundBinding
import com.example.swith.utils.ToolBarManager

class CreateRoundActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateRoundBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_round)

        ToolBarManager(this).initToolBar(binding.toolbarCreate,
            titleVisible = false,
            backVisible = true
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}