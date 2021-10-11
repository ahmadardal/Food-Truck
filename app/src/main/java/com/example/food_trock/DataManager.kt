package com.example.food_trock

object DataManager {

    val stores = mutableListOf<Store>()

    init {
        setData()
    }

    private fun setData() {
        stores.add(Store(R.drawable.hamburger, "Dyra trucken", "En foodtruck med olika slags rätter.", 120, "2.7km", 4))
        stores.add(Store(R.drawable.hamburger, "Mellan truck", "En foodtruck med olika slags rätter.", 80, "2.7km", 4))
        stores.add(Store(R.drawable.hamburger, "Billiga Truck", "En foodtruck med olika slags rätter.", 65, "2.7km", 4))
    }

/*    private fun setData(storeImage: Int, storeName: String, storeDescription: String, storePriceClass: String, storeDistance: String, storeRating: Int) {
        stores.add(Store(storeImage, storeName, storeDescription, storePriceClass, storeDistance, storeRating))
    }*/

}