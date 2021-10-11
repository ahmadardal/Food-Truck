package com.example.food_trock.models

import android.security.identity.AccessControlProfileId

class FoodTruckAdministration(
//    val foodTruckProfileId: AccessControlProfileId
    val foodTruckProfileId : String = "",
    val  approved : Boolean = false,
    val email : String = "",
    val approvementId : String = "",
    val rating : Int = 0,
    val priceRange : Int = 0
) {
}