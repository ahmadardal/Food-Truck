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
import com.example.food_trock.DataManager.tempStores
import com.example.food_trock.R
import com.example.food_trock.models.Store
import com.example.food_trock.fragments.StoreFragment
import com.example.food_trock.adapters.storeAdapter
import com.example.food_trock.models.Dishes
import com.example.food_trock.models.MenuItem
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

    lateinit var kebab: Button
    lateinit var korv: Button
    lateinit var husmanskost: Button
    lateinit var vegetarian: Button
    lateinit var asian: Button
    lateinit var pizza: Button
    lateinit var searchFilter: Button
    lateinit var storeAdapter: storeAdapter
    lateinit var tempList: MutableList<Store>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_store)
        pizza = findViewById(R.id.bt_pizza)
        korv = findViewById(R.id.bt_korv)
        kebab = findViewById(R.id.bt_kebab)
        husmanskost = findViewById(R.id.bt_husmanskost)
        asian = findViewById(R.id.bt_asian)
        vegetarian = findViewById(R.id.bt_vegetarian)
        searchFilter = findViewById(R.id.bt_searchfilter)
        //tempcat.add("test")
        // bt_searchfilter.setOnClickListener{
        /* if (pizza.isSelected()||korv.isSelected()||kebab.isSelected()||asian.isSelected()||husmanskost.isSelected()||vegetarian.isSelected())
            {
                DataManager.tempStores.clear()
                mainFilter()

            }
            else {
                DataManager.tempStores.addAll(DataManager.stores)
            }

        }

            */

        pizza.setOnClickListener {
            pizza.isSelected = !pizza.isSelected
            korv.isSelected = false
            kebab.isSelected = false
            husmanskost.isSelected = false
            asian.isSelected = false
            vegetarian.isSelected = false




            DataManager.stores.clear()
            filterPizza()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }
        korv.setOnClickListener {
            korv.isSelected = !korv.isSelected
            kebab.isSelected = false
            husmanskost.isSelected = false
            asian.isSelected = false
            vegetarian.isSelected = false
            pizza.isSelected = false

            DataManager.stores.clear()
            filterkorv()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }
            /*if (korv.isSelected)
            {
                db.collection("FoodTrucks").addSnapshotListener(object: EventListener<QuerySnapshot> {
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        DataManager.stores.clear()
                        if (value != null) {
                            for(document in value.documents) {
                                val store = document.toObject(Store::class.java)
                                if(store != null) {
                                    for( objekt in categoryList)

                                    if(store.storeStatus && store.korv) {
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

            }
            else {
                db.collection("FoodTrucks").addSnapshotListener(object: EventListener<QuerySnapshot> {
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        DataManager.stores.clear()
                        if (value != null) {
                            for(document in value.documents) {
                                val store = document.toObject(Store::class.java)
                                if(store != null) {
                                    if(store.storeStatus ) {
                                        DataManager.stores.add(store)
                                        templist.add(store)
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



            }
            */



        kebab.setOnClickListener {
            kebab.isSelected = !kebab.isSelected
            korv.isSelected = false
            husmanskost.isSelected = false
            asian.isSelected = false
            vegetarian.isSelected = false
            pizza.isSelected = false

            DataManager.stores.clear()
            filterkebab()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }
        asian.setOnClickListener {
            asian.isSelected = !asian.isSelected
            korv.isSelected = false
            kebab.isSelected = false
            husmanskost.isSelected = false
            vegetarian.isSelected = false
            pizza.isSelected = false

            DataManager.stores.clear()
            filterAsian()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }
        husmanskost.setOnClickListener {
            husmanskost.isSelected = !husmanskost.isSelected
            korv.isSelected = false
            kebab.isSelected = false
            asian.isSelected = false
            vegetarian.isSelected = false
            pizza.isSelected = false

            DataManager.stores.clear()
            filterHusmanskost()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }
        vegetarian.setOnClickListener {
            vegetarian.isSelected = !vegetarian.isSelected
            korv.isSelected = false
            kebab.isSelected = false
            husmanskost.isSelected = false
            asian.isSelected = false
            pizza.isSelected = false

            DataManager.stores.clear()
            filterVegetarian()
            storeSize.text = "Result: ${DataManager.stores.size}"
            recyclerView.adapter?.notifyDataSetChanged()
        }



        storeSize = findViewById(R.id.txtStoreSize)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        storeAdapter = storeAdapter(this, DataManager.stores)


        recyclerView.adapter = storeAdapter
        val search = findViewById<EditText>(R.id.searchView)
        val loginBtn = findViewById<ImageButton>(R.id.loginBtn)


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        bottomNavigationView.menu.getItem(1).isChecked = true

        db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser


        /*val user1 = user?.email?.let { User(user!!.uid,"hello","hello2", it,"",0,) }
        if (user1 != null) {
            db.collection("Users").document(user!!.uid).set(user1)
        }

         */

        loginBtn.setOnClickListener() {
            OpenUserProfile()
        }


        /** Queries through the collection-path FoodTrucks in the database to find data changes
         * If store is online, the storelist is cleared and the online stores are added to the recyclerview
         */

        db.collection("FoodTrucks").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                DataManager.stores.clear()
                if (value != null) {
                    for (document in value.documents) {
                        val store = document.toObject(Store::class.java)
                        if (store != null) {
                            if (store.storeStatus) {
                                DataManager.stores.add(store)
                                tempStores.add(store)

                            } else if (!store.storeStatus) {
                                DataManager.stores.remove(store)
                                tempStores.remove(store)
                            }
                        }
                        storeSize.text = "Result: ${DataManager.stores.size}"
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        /*db.collection("FoodTrucks").document(auth.currentUser!!.uid).set(Store("blabla" ,
            "","Rai ",
            10,5,
            false,auth.currentUser!!.uid,
            0.0,0.0,
            "Asian","Vegetarian",true,true,false,false,false,false))*/

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




    fun filterPizza() {
        if (pizza.isSelected) {
            for (store in tempStores) {
                if (store.pizza) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!pizza.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }


    fun filterkorv() {
        if (korv.isSelected) {
            for (store in tempStores) {
                if (store.korv) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!korv.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }


    fun filterVegetarian() {
        if (vegetarian.isSelected) {
            for (store in tempStores) {
                if (store.vegetarian) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!vegetarian.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }

    fun filterAsian() {
        if (asian.isSelected) {
            for (store in tempStores) {
                if (store.asian) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!asian.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }




    fun filterkebab(){
        if (kebab.isSelected) {
            for (store in tempStores) {
                if (store.kebab) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!kebab.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }

    fun filterHusmanskost(){
        if (husmanskost.isSelected) {
            for (store in tempStores) {
                if (store.husmaskost) {
                    DataManager.stores.add(store)
                }
            }
        } else if (!husmanskost.isSelected) {
            for (store in tempStores) {

                DataManager.stores.add(store)
            }
        }

    }
}




