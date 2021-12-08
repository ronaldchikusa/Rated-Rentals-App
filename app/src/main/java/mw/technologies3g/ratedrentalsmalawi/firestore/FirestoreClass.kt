package mw.technologies3g.ratedrentalsmalawi.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import mw.technologies3g.ratedrentalsmalawi.activities.ui.activities.*
import mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments.AddPropertiesFragment
import mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments.DashboardFragment
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.models.User
import mw.technologies3g.ratedrentalsmalawi.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance() //Global variable for Firestore

    //Registering user in Firestore
    fun registerUser (activity: RegisterActivity, userInfo: User){

        /**The "users" is a collection name. If the collection is already created then it will not
         * create the same  **/

        mFireStore.collection(Constants.USERS)
            //Document Id for users fields. Here the document is the user ID
            .document(userInfo.id)//unique user id

            //Here the userinfo are the fields and the SetOption is set to merge. It is for if we want
            //to merge later on instead of replacing the fields
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener{

                //Here call a function of base activity for transferring the result to it
                activity.userRegistrationSuccessful()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering user",
                    e
                )

            }
    }

    //Getting the user ID
    fun getCurrentUserId(): String{
        //An instance of the current user using FireBaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign the currentUserid if it is not null or else it will be blank
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    //Getting the user's information
    fun getUserDetails(activity: Activity){

        //Here we pass the collection name from which we want the data.
        mFireStore.collection(Constants.USERS)
            //The document id to get the fields of user
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document->

                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we have received the document snapshot which is converted into the user Data model object
                val user = document.toObject(User::class.java)!!

                //SHARED PREFERENCES
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.RATED_RENTALS_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key:Value logged_in_username and the value will be the user first name and the last name:Frank Tank
                //Key:Value logged_in_username: Frank Tank
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )

                editor.apply()

                //Pass the result to the Login Activity

                when(activity){
                    is LoginActivity ->{
                        //call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }

                    is ViewUserProfileActivity->{
                        activity.userDetailSuccess(user)
                    }

                }
            }.addOnFailureListener { e->
                //Hide the progress dialog if there is any error. And print the error in the log
                when(activity){
                    is LoginActivity ->{
                        activity.hideProgressDialog()
                    }

                    is ViewUserProfileActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details",
                    e
                )

            }
    }

    //Updating the user profile data
    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        //Hide the progress dialog if there is an error and print error in Log
                        activity.userProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener { e->
                when(activity){
                    is UserProfileActivity ->{
                        //Hide the progress dialog if there is an error and print error in Log
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating your profile details",
                    e
                )

            }
    }

    //Uploading image to FireStore storage
    fun uploadImageToCloudFireStorage(activity: Activity, imageFileURI: Uri?, imageType: String){

        //First we need to get the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType+System.currentTimeMillis()+"."+ Constants.getFileExtensions(activity,imageFileURI)
        )

        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot->
                //The image upload is a success

                Log.e(
                    "Firebase Image Url",
                    taskSnapshot.metadata!!.reference!!.toString()
                )

                //Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                            uri->
                        Log.e("Download image url",uri.toString())

                        when(activity){
                            is UserProfileActivity ->{
                                activity.imageUploadSuccess(uri.toString())
                            }

                            //is AddRentalPropertyActivity->{
                             //   activity.firstImageUploadSuccess(uri.toString())
                                //When done go to first image upload success
                           // }
                        }

                        }
                    }

            .addOnFailureListener{exception->
                //Hide progress dialog if error occurs and show error in log
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                   // is AddRentalPropertyActivity->{
                    //    activity.hideProgressDialog()
                   // }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    //Upload property details to FireStore
    fun uploadPropertyDetails(activity: AddRentalPropertyActivity, propertyInfo: Properties){
        //Collection
        mFireStore.collection(Constants.PROPERTIES)
            .document()
            .set(propertyInfo, SetOptions.merge())
            .addOnSuccessListener {
                //  Here call a function of base activity for transferring the result to it
                activity.propertyUploadSuccess()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )

            }

    }

    //Getting list of properties
    fun getPropertyList(fragment: Fragment){
        mFireStore.collection(Constants.PROPERTIES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
            .addOnSuccessListener { document->
                Log.e("Properties List",document.documents.toString())
                val propertyList: ArrayList<Properties> = ArrayList()

                for (i in document.documents){

                    val property = i.toObject(Properties::class.java)
                    property!!.property_id = i.id

                    propertyList.add(property)
                }
                when(fragment){
                    is AddPropertiesFragment->{
                        fragment.successPropertyListFromFirestore(propertyList)
                    }
                }
            }

    }
    //Getting list of all properties
    fun getDashboardPropertyList(fragment: DashboardFragment){
        mFireStore.collection(Constants.PROPERTIES)
            .get()
            .addOnSuccessListener { document->
                Log.e(fragment.javaClass.simpleName,
                document.toString())

                val propertiesList: ArrayList<Properties> = ArrayList()

                for (i in document.documents){

                    val property = i.toObject(Properties::class.java)!!
                    property.property_id = i.id
                    propertiesList.add(property)

                }
                fragment.successDashboardPropertiesList(propertiesList)

            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting the properties list",e)
            }


    }

    //Deleting a property from cloud Firestore
    fun deleteProperty(fragment: AddPropertiesFragment, propertyId: String){
        mFireStore.collection(Constants.PROPERTIES)
            .document(propertyId)
            .delete()
            .addOnSuccessListener {
                    fragment.propertyDeleteSuccess()
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the property",
                    e
                )
            }

    }

    //Getting property details for my product
    fun getPropertyDetails(activity: MyPropertyDetailsActivity, propertyId: String){
        mFireStore.collection(Constants.PROPERTIES)
            .document(propertyId)
            .get()
            .addOnSuccessListener {document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val property = document.toObject(Properties::class.java)
                if (property != null) {
                    activity.propertyDetailsSuccess(property)
                }
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while loading properties")
            }
    }


    //Getting property details for my dashboard
    fun getDashboardPropertyDetails(activity: PropertyDetailsActivity, propertyId: String){
        mFireStore.collection(Constants.PROPERTIES)
            .document(propertyId)
            .get()
            .addOnSuccessListener {document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val property = document.toObject(Properties::class.java)
                if (property != null) {
                    activity.propertyDashboardDetailsSuccess(property)
                }
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while loading properties")
            }
    }







}