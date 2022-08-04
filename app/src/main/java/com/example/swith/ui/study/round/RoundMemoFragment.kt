package com.example.swith.ui.study.round

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundMemoBinding
import com.example.swith.ui.BaseFragment

class RoundMemoFragment : BaseFragment<FragmentRoundMemoBinding>(R.layout.fragment_round_memo) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        with(binding) {
            btnEditFinish.visibility = View.INVISIBLE
            etMemo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    btnEditFinish.visibility = View.INVISIBLE
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnEditFinish.visibility = View.VISIBLE
                }

                override fun afterTextChanged(s: Editable?) {
                    // focus 없애기?
                }

            })

            btnEditFinish.setOnClickListener {
                // 수정 완료
            }
        }
    }
}