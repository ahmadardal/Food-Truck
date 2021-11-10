package com.example.food_trock.models

import android.security.identity.AccessControlProfileId
import java.time.Instant
import java.util.*

data class Approvement(
    val id : String = "",
    val adminId : String = "",
    val approvementDate : Instant = Instant.now(),
    val foodTruckProfileId: String
) {
}