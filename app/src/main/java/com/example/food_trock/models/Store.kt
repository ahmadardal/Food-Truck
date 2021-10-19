package com.example.food_trock.models

import android.widget.RatingBar
import androidx.cardview.widget.CardView

data class Store(
                 var storeImage: String= "",
                 var storeName: String = "",
                 var storePriceClass: Int = 0,
                 var storeDistance: String = "",
                 var storeRating: Int = 5,
                 var storeOnline: Boolean = false,
                 var storeDescription: String = "")
