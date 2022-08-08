package com.example.swith.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.swith.R
import com.example.swith.databinding.DialogAlertBinding

class CustomAlertDialog(private val title: String, private val content: String) : DialogFragment() {
    private lateinit var binding: DialogAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_alert, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            tvAlertTitle.text = title
            tvAlertContent.text = content
            btnAlertConfirm.setOnClickListener { dismiss() }
        }
    }
}