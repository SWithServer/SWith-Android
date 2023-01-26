package com.example.swith.ui.study.round

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.swith.R
import com.example.swith.databinding.FragmentRoundMemoBinding
import com.example.swith.ui.dialog.BottomSheet
import com.example.swith.ui.dialog.CustomAlertDialog
import com.example.swith.utils.base.BaseFragment
import com.example.swith.viewmodel.RoundViewModel

class RoundMemoFragment(private val curCount: Int) :
    BaseFragment<FragmentRoundMemoBinding>(R.layout.fragment_round_memo) {
    private val viewModel: RoundViewModel by activityViewModels()
    private var beforeText: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding) {
            tvMemoGuide.visibility = View.INVISIBLE
            btnEditFinish.visibility = View.INVISIBLE
            etMemo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    btnEditFinish.visibility = View.INVISIBLE
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // TextWatcher 구현
                    // 변동사항이 생긴 경우에만 수정 완료 버튼 생김
                    if (beforeText.isNullOrEmpty() && !s.isNullOrEmpty() && beforeText != s.toString()) {
                        btnMemoCreate.visibility = View.VISIBLE
                    } else if (!beforeText.isNullOrEmpty() && !s.isNullOrEmpty() && beforeText != s.toString()) {
                        btnEditFinish.visibility = View.VISIBLE
                    } else {
                        btnEditFinish.visibility = View.INVISIBLE
                        btnMemoCreate.visibility = View.INVISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            btnEditFinish.setOnClickListener {
                etMemo.apply {
                    clearFocus()
                    BottomSheet(
                        "${curCount}회차 메모 수정",
                        null,
                        resources.getString(R.string.bottom_memo_update_guide),
                        "수정"
                    ).apply {
                        setCustomListener(object : BottomSheet.customClickListener {
                            override fun onCheckClick() {
                                dismiss()
                                viewModel.updateMemo(etMemo.text.toString().also {
                                    beforeText = it
                                })
                                btnEditFinish.visibility = View.INVISIBLE
                            }
                        })
                    }.show(requireActivity().supportFragmentManager, "bottomMemo")
                }
            }

            btnMemoCreate.setOnClickListener {
                etMemo.apply {
                    clearFocus()
                    BottomSheet(
                        "${curCount}회차 메모 생성",
                        null,
                        resources.getString(R.string.bottom_memo_create_guide),
                        "생성"
                    ).apply {
                        setCustomListener(object : BottomSheet.customClickListener {
                            override fun onCheckClick() {
                                dismiss()
                                viewModel.createMemo(etMemo.text.toString().also {
                                    beforeText = it
                                })
                                btnMemoCreate.visibility = View.INVISIBLE
                            }
                        })
                    }.show(requireActivity().supportFragmentManager, "bottomMemo")
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.sessionLiveData.observe(viewLifecycleOwner, Observer {
            it.userMemo?.let { memo ->
                binding.etMemo.setText(memo)
                beforeText = memo
                binding.btnEditFinish.visibility = View.INVISIBLE
            }
            binding.tvMemoGuide.visibility = View.VISIBLE
            binding.btnMemoCreate.visibility = View.INVISIBLE
        })
        viewModel.memoLiveEvent.observe(viewLifecycleOwner, Observer {
            CustomAlertDialog("메모 생성 및 수정 완료", "생성 및 수정이 완료 되었습니다.")
                .show(requireActivity().supportFragmentManager, "메모 생성 및 수정 완료")
        })
    }
}