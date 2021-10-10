package com.example.food_trock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        var storeAdapter = storeAdapter(this, DataManager.stores)
        recyclerView.adapter = storeAdapter

        storeAdapter.setOnItemClickListener(object: storeAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {


                val storeFragment = StoreFragment()

                val bundle = Bundle()
                bundle.putString("storeName","hej")
                storeFragment.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.container, storeFragment, "store")
                transaction.commit()




                Log.e("TEST", "TEST")

            }
        })
    }
}