package com.example.studywith

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.swith.data.StudyGroup
import com.example.swith.databinding.FragmentDialogBinding

class ConfirmDialog(val content: String) : DialogFragment()
{
    lateinit var binding: FragmentDialogBinding
    lateinit var confirmDialogInterface: ConfirmDialogInterface



//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentDialogBinding.inflate(inflater,container,false)
//        val view = binding.root
//
//        binding.tvConfirm.text= content
//        binding.btnNo.setOnClickListener {
//            dismiss()
//        }
//        binding.btnYes.setOnClickListener {
//            this.confirmDialogInterface.onYesButtonClick()
//            dismiss()
//        }
//        return view
//    }
    interface ConfirmDialogInterface
    {
        fun onYesButtonClick()
    }
}
