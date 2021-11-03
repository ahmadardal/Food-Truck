package com.example.food_trock.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.food_trock.DataManager
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView


    lateinit var storeSize: TextView
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var kebab : Button
    lateinit var korv : Button

    lateinit var husmanskost : Button

    lateinit var vegetarian : Button

    lateinit var asian : Button

    lateinit var pizza : Button
    lateinit var searchFilter : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_store)
        pizza = findViewById(R.id.pizza)
        korv = findViewById(R.id.korv)
        kebab = findViewById(R.id.kebab)
        husmanskost = findViewById(R.id.husmanskost)
        asian = findViewById(R.id.asian)
        vegetarian = findViewById(R.id.vegetarian)
        searchFilter = findViewById(R.id.searchfilter)
        searchfilter.setOnClickListener{
            if (pizza.isSelected()||korv.isSelected()||kebab.isSelected()||asian.isSelected()||husmanskost.isSelected()||vegetarian.isSelected())
            {
                mainFilter()

            }
            else {
                DataManager.tempStores.addAll(DataManager.stores)
            }

        }

        pizza.setOnClickListener{
            pizza.isSelected= !pizza.isSelected
        }
        korv.setOnClickListener{
            korv.isSelected= !korv.isSelected
        }
        kebab.setOnClickListener{
            kebab.isSelected= !kebab.isSelected
        }
        asian.setOnClickListener{
            asian.isSelected= !asian.isSelected
        }
        husmanskost.setOnClickListener{
            husmanskost.isSelected= !husmanskost.isSelected
        }
        vegetarian.setOnClickListener{
            vegetarian.isSelected= !vegetarian.isSelected
        }



        storeSize = findViewById(R.id.txtStoreSize)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        var storeAdapter = storeAdapter(this, DataManager.stores        )
        recyclerView.adapter = storeAdapter
        val search = findViewById<EditText>(R.id.searchView)
        val loginBtn = findViewById<ImageButton>(R.id.loginBtn)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser

        loginBtn.setOnClickListener() {
            OpenUserProfile()
        }



        /** Queries through the collection-path FoodTrucks in the database to find data changes
         * If store is online, the storelist is cleared and the online stores are added to the recyclerview
         */

        db.collection("FoodTrucks").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                DataManager.stores.clear()
                if (value != null) {
                    for(document in value.documents) {
                        val store = document.toObject(Store::class.java)
                        if(store != null) {
                            if(store.storeStatus) {
                                DataManager.stores.add(store)
                            } else if (!store.storeStatus) {
                                DataManager.stores.remove(store)
                            }
                        }
                        storeSize.text = "Result: ${DataManager.stores.size}"
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        storeAdapter.setOnItemClickListener(object : storeAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val storeFragment = StoreFragment()

                var selectedStore: Store = storeAdapter.storeList[position]
                val bundle = Bundle()
                storeFragment.arguments = bundle
                bundle.putString("storeName", selectedStore.storeName)
                bundle.putInt("storePriceClass", selectedStore.storePriceClass)
                bundle.putString("storeImage", selectedStore.storeImage)
                bundle.putString("storeID", selectedStore.UID)

                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.container, storeFragment, "store")
                transaction.commit()
            }
        })
    }


     val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.favourites -> {
                bottomNavigationView.menu.getItem(1).isChecked = false
                val intent = Intent(this@StoreActivity, FavouritesActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.trucks -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.settings -> {
                bottomNavigationView.menu.getItem(1).isChecked = false
                val intent = Intent(this@StoreActivity, OwnerSettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.maps -> {
                bottomNavigationView.menu.getItem(1).isChecked = false
               val intent = Intent(this@StoreActivity, MapsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }








    /**
     * Opens the user profile, if signed in. Else opens the login activity.
     */


    fun OpenUserProfile() {
        if (auth.currentUser != null) {
            val intent = Intent(this, OwnerSettingsActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun filterPizza(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){

                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }

    fun filterkorv(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){
                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }


    fun filterVegetarian(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){
                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }

    fun filterAsian(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){
                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }

    fun filterkebab(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){
                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }

    fun filterHusmanskost(temp :MutableList<Store>) : MutableList<Store>
    {
        for(objekt in DataManager.stores)
        {
            if (objekt.pizza){
                DataManager.tempStores.add(objekt)


            }
            else return temp

        }
        return DataManager.tempStores

    }
    fun mainFilter()
    {


        filterPizza(DataManager.stores)
        filterAsian(DataManager.stores)
        filterHusmanskost(DataManager.stores)
        filterkebab(DataManager.stores)
        filterkorv(DataManager.stores)
        filterVegetarian(DataManager.stores)
        DataManager.temp2Stores.addAll(DataManager.tempStores.distinct())
    }

}
