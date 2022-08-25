package com.example.swith.ui.study.announce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.swith.R
import com.example.swith.databinding.ActivityAlertBinding
import com.example.swith.ui.MainActivity

class AlertActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityAlertBinding
    //private var mAlertViewModel: AlertViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_alert)

        initData()
        initView()
    }

    private fun initView() {
        binding.clickListener=this@AlertActivity
        //   mAlertViewModel = ViewModelProvider(this@RatingActivity, .Factory()).get(Ra::class.java).apply {
        //loginViewModel = this
        //getCurrentLogin().observe(this@SnsLoginActivity, this@SnsLoginActivity)
        //}
    }

    private fun initData() {

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ib_back->{
                Intent(this@AlertActivity,MainActivity::class.java).run {
                    startActivity(this)
                }
            }
        }
    }
}