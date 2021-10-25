package com.example.food_trock.models


data class Store(var fullName: String = "",
                 var storeImage: String= "",
                 var storeName: String = "",
                 var storePriceClass: Int = 0,
                 var storeDistance: String = "",
                 var storeRating: Int = 5,
                 var storeStatus: Boolean = false,
                 var UID: String = "")
