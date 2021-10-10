package com.example.food_trock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class StoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var data: String? = arguments?.getString("storeName")

        Log.e("TEST",data!!)

        val view = inflater.inflate(R.layout.store_fragment, container, false,)

        return view

    }

}