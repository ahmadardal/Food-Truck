package com.example.food_trock.firebase

import android.app.Activity
import android.util.Log
import com.example.food_trock.DataManager
import com.example.food_trock.activities.AddNewAdminActivity
import com.example.food_trock.activities.AdminAddFoodTruckActivity
import com.example.food_trock.activities.LoginActivity
import com.example.food_trock.activities.RegisterAccountActivity
import com.example.food_trock.models.Approvement
import com.example.food_trock.models.FoodTruckAdministration
import com.example.food_trock.models.Store
import com.example.food_trock.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun registerUser(activity: RegisterAccountActivity, userInfo: com.example.food_trock.models.User) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }
    /**
     * A function to make an entry of the registered foodtruck in the firestore database.
     */
    fun registerUser(activity: AdminAddFoodTruckActivity, userInfo: com.example.food_trock.models.User) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                val foodTruck = Store(
                    UID = userInfo.id,
                    fullName = userInfo.fullName
                )
/*                val approvement = Approvement(
                    adminId = getCurrentUserID(),
                    foodTruckProfileId = foodTruck.UID
                )
                val ftAdministration = FoodTruckAdministration(
                    approved = true,
                    approvementId = approvement.id,
                    foodTruckProfileId = foodTruck.UID,
                    email = userInfo.email
                )*/
                registerFoodTruck(activity,foodTruck)
                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }
    /**
     * A function to make an entry of the registered admin in the firestore database.
     */
    fun registerUser(activity: AddNewAdminActivity, userInfo: com.example.food_trock.models.User) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the registered foodtruck in the firestore database.
     */
    fun registerFoodTruck(activity: AdminAddFoodTruckActivity, foodTruckInfo: Store) {

        mFireStore.collection("FoodTrucks").document(foodTruckInfo.UID)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(foodTruckInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    this.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }


    /**
     * A function to SignIn using firebase and get the user details from Firestore Database.
     */
    fun loadUserData(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser = document.toObject(com.example.food_trock.models.User::class.java)!!
                DataManager.currentUserId = getCurrentUserID()
                DataManager.currentUserRole = loggedInUser.role

                // Here call a function of base activity for transferring the result to it.
                when (activity) {
                    is LoginActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }




}