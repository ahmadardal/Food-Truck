package com.example.food_trock.models

import android.widget.RatingBar
import androidx.cardview.widget.CardView

data class Store(var storeImage: Int = 0,
                 var storeName: String = " ",
                 var storePriceClass: Int = 0,
                 var storeDistance: String = " ",
                 var storeRating: Int = 1,
                 var storeOnline: Boolean = true)
