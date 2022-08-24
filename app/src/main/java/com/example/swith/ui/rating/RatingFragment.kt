package com.example.swith.ui.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.swith.R
import com.example.swith.databinding.FragmentRatingBinding
import com.example.swith.utils.base.BaseFragment


class RatingFragment : BaseFragment<FragmentRatingBinding>(R.layout.fragment_rating) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisiblebar(true,false,"","안녕하세요.")
    }

    companion object {
        const val TAG: String = "RatingFragment"
        @JvmStatic
        fun newInstance() =
            RatingFragment()
    }
}