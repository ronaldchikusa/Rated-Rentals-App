package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_view_user_profile.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.User
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null //For the profile pic on device
    private var mUserProfileImageURL: String = ""//For your browser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)



        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //Get the user details
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }


        edit_text_complete_profile_first_name.setText(mUserDetails.firstName)
        edit_text_complete_profile_last_name.setText(mUserDetails.lastName)
        edit_text_complete_profile_email.isEnabled = false
        edit_text_complete_profile_email.setText(mUserDetails.email)

        if (mUserDetails.profileCompleted == 0){
            tv_title_toolbar_user_profile.text = resources.getString(R.string.title_complete_profile)

            //Making sure that these cannot be edited
            edit_text_complete_profile_first_name.isEnabled = false

            edit_text_complete_profile_last_name.isEnabled = false



        }else{
            setupActionBar()
            tv_title_toolbar_user_profile.text = resources.getString(R.string.title_edit_profile)

            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.profile_photo, user_profile_picture)


            if (mUserDetails.mobile != 0L){
                edit_text_complete_profile_mobile_number.setText(mUserDetails.mobile.toString())
            }

            if (mUserDetails.gender == Constants.MALE){
                rb_male.isChecked = true
            }else{
                rb_female.isChecked = true
            }
        }

        //On click listeners
        user_profile_picture.setOnClickListener(this@UserProfileActivity)
        button_submit_complete_profile.setOnClickListener(this@UserProfileActivity)
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.user_profile_picture -> {
                    //we check if permission is allowed or not
                    //Do this by checking READ_EXTERNAL_STORAGE permission and if not allowed we request

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //showErrorSnackBar("You have already granted storage permission",false)
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {
                        /**Request for storage permission from user, permissions must be requested in your manifest, they
                         * should not be granted to your app and should have a certain level of protection**/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }

                }

                R.id.button_submit_complete_profile ->{

                    if (validateUserProfileDetails()){
                        showProgressDialog()

                        if (mSelectedImageFileUri != null){
                        FirestoreClass().uploadImageToCloudFireStorage(this, mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE)
                        }
                        else{
                            updateUserProfileDetails()
                        }
                    }

                }
            }
        }
    }

    private fun updateUserProfileDetails(){
        //use a hashmap
        val userHashMap = HashMap<String ,Any>()

        val firstName = edit_text_complete_profile_first_name.text.toString().trim{it <= ' '}

        if(firstName != mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = edit_text_complete_profile_last_name.text.toString().trim{it <= ' '}

        if(lastName != mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = lastName
        }

        //getting mobile_num and get gender
        val mobile = edit_text_complete_profile_mobile_number.text.toString().trim{ it <= ' '}
        val gender = if(rb_male.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if(mobile.isNotEmpty() && mobile != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobile.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }


        //key: gender value: male
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1//You know what this does fam

        //showProgressDialog()
        //update the data
        FirestoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)
        //showErrorSnackBar("Your profile has been updated", false)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //if permission is granted
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //showErrorSnackBar("The storage permission is granted.",false)
            Constants.showImageChooser(this@UserProfileActivity)
        } else {
            //Displaying another toast if the permission is granted
            Toast.makeText(
                this,
                resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){

                if (data != null){
                    try {
                        //The uri of selected image from phone storage
                         mSelectedImageFileUri = data.data!!

                        //user_profile_picture.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, user_profile_picture)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            //A log is printed when user closes or cancels action
            Log.e("Request Cancelled","Image selection cancelled")
        }

    }

    private fun validateUserProfileDetails():Boolean{
        return when {

            TextUtils.isEmpty(edit_text_complete_profile_mobile_number.text.toString().trim{ it <=' '}) ||
                    edit_text_complete_profile_mobile_number.length() < 9 || edit_text_complete_profile_mobile_number.length() > 10->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_correct_mobile_number),true)
                false
            }else->{
                true
            }
        }
    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.message_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String){

        mUserProfileImageURL = imageURL
        updateUserProfileDetails()

    }
}


