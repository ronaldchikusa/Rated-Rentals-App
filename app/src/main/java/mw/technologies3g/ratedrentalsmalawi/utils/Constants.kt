package mw.technologies3g.ratedrentalsmalawi.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    //Firestore Constants
    const val USERS: String = "users"

    //Property ID Constants
    const val EXTRA_PROPERTY_ID: String = "extra_property_id"

    //Shared Preferences
    const val RATED_RENTALS_PREFERENCES: String = "RatedRentalsPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"

    //Permission Constants
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE_2 = 6
    const val READ_STORAGE_PERMISSION_CODE_3 = 7

    //Gallery Intent Constants
    const val GALLERY_INTENT_CODE_IMAGE_1 = 300
    const val GALLERY_INTENT_CODE_IMAGE_2 = 400
    const val GALLERY_INTENT_CODE_IMAGE_3 = 500

    //User Profile Image
    const val PICK_IMAGE_REQUEST_CODE = 3

    //More Userprofile Constants
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "profile_photo"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    //Firebase Storage Constants
    const val USER_PROFILE_IMAGE = "User_Profile_Image"

    //UserID Constant
    const val USER_ID: String = "user_id"

    //Furniture Constants
    const val FURNISHED: String = "Fully furnished"
    const val NOT_FURNISHED: String = "Not furnished"
    const val SEMI_FURNISHED: String = "Semi furnished"

    //Properties Constant
    const val PROPERTIES: String = "properties"


    fun showImageChooser(activity: Activity){
        //An intent for launching the image selection of phone storage

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Launches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    //function that will get the image extension
    fun getFileExtensions(activity: Activity,uri: Uri?): String?{
        //getting the file ext
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}