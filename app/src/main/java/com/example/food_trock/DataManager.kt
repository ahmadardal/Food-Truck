package com.example.food_trock

object DataManager {

    val stores = mutableListOf<Store>()

    init {
        setData()
    }

    private fun setData() {
        stores.add(Store(R.drawable.hamburger, "Lillans Truck", "En foodtruck med olika slags rätter.", "$$", "2.7km", 4))
        stores.add(Store(R.drawable.hamburger, "Lillans Truck", "En foodtruck med olika slags rätter.", "$$", "2.7km", 4))
        stores.add(Store(R.drawable.hamburger, "Lillans Truck", "En foodtruck med olika slags rätter.", "$$", "2.7km", 4))
    }

}