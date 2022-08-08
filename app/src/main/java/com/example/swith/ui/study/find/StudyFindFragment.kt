package com.example.swith.ui.study.find

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.fragment.app.viewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel

class StudyFindFragment : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {
    //private val viewModel: StudyFindViewModel by viewModels()

    private var region:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("create 접근","true")
        arguments?.apply{
            Log.e("지역","${this.getString("지역")}")
            region = this.getString("지역")
            binding.tvSelectRegion.text=region
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("view create","True")
        with(binding)
        {
            tvSelectRegion.setOnClickListener {
                startActivity(Intent(activity, SelectPlaceActivity::class.java).apply {
                    putExtra("번호", 3)
                })
            }
            spinnerInterest1.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.intersting,
                android.R.layout.simple_spinner_item
            )

            spinnerInterest2.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.intersting,
                android.R.layout.simple_spinner_item
            )

            spinnerDeadline.adapter = ArrayAdapter.createFromResource(activity!!.applicationContext,
                R.array.deadLineOrLatest,
                android.R.layout.simple_spinner_item
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("resume","True")
    }

}