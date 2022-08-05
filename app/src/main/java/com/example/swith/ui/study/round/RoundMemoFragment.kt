package com.example.swith.ui.study.round

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundMemoBinding
import com.example.swith.ui.BaseFragment

class RoundMemoFragment(private val curCount: Int) : BaseFragment<FragmentRoundMemoBinding>(R.layout.fragment_round_memo) {
    private var beforeText: String? = null
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
                    // TextWatcher 구현
                    // 변동사항이 생긴 경우에만 수정 완료 버튼 생김
                    if (beforeText.isNullOrEmpty() && !s.isNullOrEmpty() || beforeText != s.toString())
                        btnEditFinish.visibility = View.VISIBLE
                    else if (beforeText.isNullOrEmpty() && s.isNullOrEmpty() || beforeText == s.toString())
                        btnEditFinish.visibility = View.INVISIBLE
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            btnEditFinish.setOnClickListener {
                etMemo.apply {
                    clearFocus()
                    BottomSheet(curCount, 0, true).apply {
                        setCustomListener(object : BottomSheet.customClickListener{
                            override fun onCheckClick() {
                                dismiss()
                                beforeText = etMemo.text.toString()
                                btnEditFinish.visibility = View.INVISIBLE
                            }

                            override fun onCancelClick() {
                                dismiss()
                            }

                        })
                    }.show(requireActivity().supportFragmentManager, "bottomMemo")
                }
            }
        }
    }
}