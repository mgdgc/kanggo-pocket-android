package com.RiDsoft.kangwonhighschool.ui.preview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R


/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview1, container, false)
    }

}// Required empty public constructor
