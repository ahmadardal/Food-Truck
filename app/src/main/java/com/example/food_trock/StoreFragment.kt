package com.example.food_trock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class StoreFragment : Fragment() {

    lateinit var txtStoreName: TextView
    lateinit var txtStoreInfo: TextView
    lateinit var txtPriceClass: TextView
    lateinit var txtDistance: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //Log.e("TEST",data!!)

        val view = inflater.inflate(R.layout.store_fragment, container, false,)


        txtStoreName = view.findViewById(R.id.txtStoreName)
        txtStoreInfo = view.findViewById(R.id.txtStoreInfo)
        txtPriceClass = view.findViewById(R.id.txtPriceClass)
        txtDistance = view.findViewById(R.id.txtDistance)

        var storeName: String? = arguments?.getString("storeName")
        var storeDescription: String? = arguments?.getString("storeName")
        var storePriceClass: Int? = arguments?.getInt("storePriceClass")
        var storeDistance: String? = arguments?.getString("storeDistance")

        txtStoreName.text = storeName
        txtStoreInfo.text = storeDescription
        txtPriceClass.text = storePriceClass.toString()
        txtDistance.text = storeDistance

        if (storePriceClass != null) {
            if (storePriceClass <= 70) {
                txtPriceClass.text = "$"
            } else if (storePriceClass in 80..110) {
                txtPriceClass.text = "$$"
            } else
                txtPriceClass.text = "$$$"
        }

        return view


    }

}