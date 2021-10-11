package com.example.food_trock.models

import android.security.identity.AccessControlProfileId

data class Favorits(
    val id: String = "",
    val userId: AccessControlProfileId,
    val foodTruckProfileId: AccessControlProfileId,

) {
}