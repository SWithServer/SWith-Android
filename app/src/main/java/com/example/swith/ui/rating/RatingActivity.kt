package com.example.swith.ui.rating

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.data.R
import com.example.data.databinding.ActivityRatingBinding
import com.example.swith.domain.entity.RatingResponse
import com.example.swith.domain.entity.RatingResult
import com.example.data.ui.MainActivity
import com.example.data.ui.adapter.RatingAdapter
import com.example.data.viewmodel.RatingViewModel
import com.example.swith.domain.entity.ProfileRequest

class RatingActivity : AppCompatActivity(), View.OnClickListener, Observer<RatingResponse> {
    lateinit var binding: ActivityRatingBinding
    var groupIdx: String = ""
    private var mRatingViewModel: RatingViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating)

        initData()
        initView()

    }

    private fun initView() {
        binding.clickListener = this@RatingActivity
        mRatingViewModel = ViewModelProvider(this@RatingActivity, RatingViewModel.Factory()).get(
            RatingViewModel::class.java
        ).apply {
            this.getCurrentUserList().observe(this@RatingActivity, this@RatingActivity)
        }
        mRatingViewModel?.requestCurrentUserList(
            groupIdx,
            ProfileRequest(2)
        )


    }

    private fun initData() {
        groupIdx = intent.getStringExtra("Rating") ?: "0"
        Log.e("doori", "groupIdx = $groupIdx")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_save -> {
                Intent(this@RatingActivity, MainActivity::class.java).run {
                    startActivity(this)
                    finishAffinity()
                }
            }
            R.id.ib_back -> {
                Intent(this@RatingActivity, MainActivity::class.java).run {
                    startActivity(this)
                    finishAffinity()
                }
            }
        }
    }

    private fun setAdaper(data: List<RatingResult>) {
        var adapter = RatingAdapter()
        adapter.setData(data)
//        adapter.setItemClickListener(object : RatingAdapter.OnItemClickListener{
//            override fun onClick(v: View, position: Int, rating: Float) {
//                Log.e("doori",rating.toString())
//            }
//        })
        binding.rcRating.adapter = adapter
    }


    override fun onChanged(ratingResponse: RatingResponse?) {
        ratingResponse?.apply {
            Log.e("doori", "ratingResponse = $ratingResponse")
            setAdaper(ratingResponse.result)
        }
    }
}