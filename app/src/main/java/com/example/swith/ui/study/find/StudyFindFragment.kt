package com.example.swith.ui.study.find

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.swith.R
import com.example.swith.databinding.FragmentStudyFindBinding
import com.example.swith.ui.study.create.SelectPlaceActivity
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.StudyFindViewModel


class StudyFindFragment : BaseFragment<FragmentStudyFindBinding>(R.layout.fragment_study_find) {
    private val viewModel: StudyFindViewModel by viewModels()

    private var region:String?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val text = data!!.getCharSequenceExtra("지역")
                binding.tvSelectRegion.text = text
            }
        }

        with(binding)
        {
            tvSelectRegion.setOnClickListener {
                var intent = Intent(requireActivity(), SelectPlaceActivity::class.java)
                intent.putExtra("번호",3)
                activityResultLauncher.launch(intent)
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