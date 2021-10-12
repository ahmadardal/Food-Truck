package com.example.food_trock.models

import android.security.identity.AccessControlProfileId

data class Dishes(
    val id: String = "",
    val foodTruckProfileId: String = "",
    val approved: Boolean = false,
    val approvementId: AccessControlProfileId,
    val categoryId: String = "",
    val dishName: String = "",
    val description: String = "",
    val price: Double = 0.00
) {
}