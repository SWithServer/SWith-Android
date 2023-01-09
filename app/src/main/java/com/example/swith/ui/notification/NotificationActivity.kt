package com.example.swith.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swith.R
import com.example.swith.databinding.ActivityNotificationBinding
import com.example.swith.ui.MainActivity
import com.example.swith.ui.adapter.NotificationRVAdapter
import com.example.swith.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel by viewModels<NotificationViewModel>()

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)

        binding.clickListener = this@NotificationActivity

        initView()
        observe()
    }

    private fun initView() {
        with(binding){
            rvNotification.apply {
                adapter = NotificationRVAdapter()
                layoutManager = LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.noticeList.collectLatest {
                        if (it.isNotEmpty())
                            (binding.rvNotification.adapter as NotificationRVAdapter).setData(it)
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ib_back -> {
                Intent(this@NotificationActivity, MainActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }
}