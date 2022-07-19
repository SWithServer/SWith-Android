package com.example.swith.ui.study.create

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.swith.databinding.ActivitySelectPlaceBinding

class SelectPlaceActivity :  AppCompatActivity(){
    lateinit var binding:ActivitySelectPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySelectPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}