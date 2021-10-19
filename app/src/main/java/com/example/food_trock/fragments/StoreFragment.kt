package com.example.food_trock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.food_trock.R

class StoreFragment : Fragment() {

    lateinit var txtStoreName: TextView
    lateinit var txtStoreInfo: TextView
    lateinit var txtPriceClass: TextView
    lateinit var txtDistance: TextView
    lateinit var storeImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view = inflater.inflate(R.layout.store_fragment, container, false,)


        val returnBtn = view.findViewById<ImageButton>(R.id.storeReturnBtn)
        txtStoreName = view.findViewById(R.id.txtStoreName)
        txtPriceClass = view.findViewById(R.id.txtPriceClass)
        txtDistance = view.findViewById(R.id.txtDistance)
        storeImage = view.findViewById(R.id.imagewView2)

        var storeName: String? = arguments?.getString("storeName")
        var storePriceClass: Int? = arguments?.getInt("storePriceClass")
        var storeDistance: String? = arguments?.getString("storeDistance")
        var storeIMG: String? = arguments?.getString("storeImage")

        txtStoreName.text = storeName
        txtPriceClass.text = storePriceClass.toString()
        txtDistance.text = storeDistance
        if (storeIMG != null) {
            Glide.with(this).load(storeIMG).into(storeImage)
        }


        if (storePriceClass != null) {
            if (storePriceClass <= 70) {
                txtPriceClass.text = "$"
            } else if (storePriceClass in 80..110) {
                txtPriceClass.text = "$$"
            } else
                txtPriceClass.text = "$$$"
        }

        returnBtn.setOnClickListener() {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return view


    }

}