package com.example.food_trock.models

import android.security.identity.AccessControlProfileId

data class Favorite(
    val id: String = "",
    val userId: AccessControlProfileId,
    val foodTruckProfileId: AccessControlProfileId,
) {
}