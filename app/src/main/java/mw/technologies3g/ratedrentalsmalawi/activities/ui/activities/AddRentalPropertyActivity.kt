package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_rental_property.*
import kotlinx.android.synthetic.main.activity_developers.*
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader
import java.io.IOException
import android.R.attr.bitmap
import com.bumptech.glide.Glide
import java.io.File
import java.lang.RuntimeException


class AddRentalPropertyActivity : BaseActivity(), View.OnClickListener{

    private var mSelectedImageFileURI: Uri? = null
    private var mSelectedSecondImageFileURI: Uri? = null
    private var mSelectedThirdImageFileURI: Uri? = null

    private var mPropertyImageURL: String = ""
    private var mPropertySecondImageURL: String = ""
    private var mPropertyThirdImageURL: String = ""

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rental_property)


        /**Mobile Ads*/
        MobileAds.initialize(this@AddRentalPropertyActivity)


        /**Banner Ads*/
        //var adRequest = AdRequest.Builder().build()
        //adView.loadAd(adRequest)


        //Interstitial Ads
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }



        setupActionBar()
        iv_add_update_property.setOnClickListener(this)
        iv_add_update_property2.setOnClickListener(this)
        iv_add_update_property3.setOnClickListener(this)
        btn_upload_add_prod.setOnClickListener(this)
    }



    override fun onClick(view: View?) {

        if (view != null){

            when(view.id){

                R.id.iv_add_update_property->{
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED

                    ){
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_1)//For getting image
                    }else
                    {
                        /*Requests permissions to be granted to this app. These permissions must
                        * be requested in your manifest, they should not be granted to your app, and
                        * they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.iv_add_update_property2->{

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED

                    ){
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_2)//For getting image
                    }else
                    {
                        /*Requests permissions to be granted to this app. These permissions must
                        * be requested in your manifest, they should not be granted to your app, and
                        * they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE_2
                        )
                    }
                }

                R.id.iv_add_update_property3->{

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED

                    ){
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_3)//For getting image
                    }else
                    {
                        /*Requests permissions to be granted to this app. These permissions must
                        * be requested in your manifest, they should not be granted to your app, and
                        * they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE_3
                        )
                    }
                }

                R.id.btn_upload_add_prod->{

                   if (validateRentalPropertyDetails()){
                       //showErrorSnackBar("Your property information is valid",errorMessage = false)
                       //InterstitialAd
                       if (mInterstitialAd != null) {
                           mInterstitialAd?.show(this)
                       } else {
                           Log.d("TAG", "The interstitial ad wasn't ready yet.")
                       }

                       uploadPropertyImage()
                   }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //if permission is granted

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            when(requestCode) {
            //showErrorSnackBar("The storage permission is granted.",false)

            Constants.READ_STORAGE_PERMISSION_CODE->{
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_1)//For getting image

            }

            Constants.READ_STORAGE_PERMISSION_CODE_2->{
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_2)//For getting image

            }

            Constants.READ_STORAGE_PERMISSION_CODE_3->{
                val galleryIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(galleryIntent,Constants.GALLERY_INTENT_CODE_IMAGE_3)//For getting image

                }
            }
        }else{
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
            /**First Image**/
            if (requestCode == Constants.GALLERY_INTENT_CODE_IMAGE_1){

                if (data != null){


                    iv_add_update_property.setImageDrawable(ContextCompat
                        .getDrawable(this, R.drawable.ic_baseline_edit_24))

                    try {
                        mSelectedImageFileURI = data.data!!
                    Glide.with(this@AddRentalPropertyActivity).load(mSelectedImageFileURI).into(iv_main_property_image)
                      //iv_main_property_image.setImageURI(mSelectedImageFileURI)
                    }catch (e: IOException){

                        e.printStackTrace()
                        Toast.makeText(this,
                            "Image Selection Failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }

            }
            /**Second Image**/
            else if(requestCode == Constants.GALLERY_INTENT_CODE_IMAGE_2) {
                if (data != null) {

                    iv_add_update_property2.setImageDrawable(
                        ContextCompat
                            .getDrawable(this, R.drawable.ic_baseline_edit_24)
                    )

                   try {
                        mSelectedSecondImageFileURI = data.data!!
                       Glide.with(this@AddRentalPropertyActivity).load(mSelectedSecondImageFileURI).into(iv_property_image2)
                       // iv_property_image2.setImageURI(mSelectedSecondImageFileURI)
                    }catch (e: IOException) {

                        e.printStackTrace()
                    }
                }
            }
            /**Third Image**/
            else if(requestCode == Constants.GALLERY_INTENT_CODE_IMAGE_3) {

                if (data != null) {
                    iv_add_update_property3.setImageDrawable(
                        ContextCompat
                            .getDrawable(this, R.drawable.ic_baseline_edit_24)
                    )

                   try {
                        mSelectedThirdImageFileURI = data.data!!
                       Glide.with(this@AddRentalPropertyActivity).load(mSelectedThirdImageFileURI).into(iv_property_image3)
                        //iv_property_image3.setImageURI(mSelectedThirdImageFileURI)//used glide because this was bringing bitmapSize error
                    } catch (e: IOException) {

                        e.printStackTrace()
                    }
                }
            }

        }else if(resultCode == Activity.RESULT_CANCELED){
            //A log is printed when user closes or cancels action
            Log.e("Request Cancelled","Image selection cancelled")
        }

    }

    private fun validateRentalPropertyDetails(): Boolean{

        return when{

            mSelectedImageFileURI == null ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_select_product_image),true)
                false
            }

            TextUtils.isEmpty(edit_text_property_title.text.toString().trim() {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_title),true)
                false
            }
            TextUtils.isEmpty(et_property_price.text.toString().trim() {it <= ' '}) ->{ //Modified validation
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_price),true)
                false
            }
            TextUtils.isEmpty(et_property_district.text.toString().trim() {it <= ' '})  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_property_district),true)
                false
            }
            TextUtils.isEmpty(et_property_area.text.toString().trim() {it <= ' '})  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_area),true)
                false
            }
            TextUtils.isEmpty(et_property_garage.text.toString().trim() {it <= ' '}) ->{ //Modified validation
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_garage),true)
                false
            }
            TextUtils.isEmpty(et_property_bathrooms.text.toString().trim() {it <= ' '})  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_property_bathroom),true)
                false
            }
            TextUtils.isEmpty(et_property_bedrooms.text.toString().trim() {it <= ' '})  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_bedrooms),true)
                false
            }
            TextUtils.isEmpty(et_property_description.text.toString().trim() {it <= ' '})  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_general_description),true)
                false
            }
            TextUtils.isEmpty(et_property_contact_details.text.toString().trim() {it <= ' '})||
                    et_property_contact_details.length() < 9 || et_property_contact_details.length() > 9  ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_contact),true)
                false
            }
            TextUtils.isEmpty(et_property_contact_hours.text.toString().trim() {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_property_contact_hours),true)
                false
            }
           mSelectedSecondImageFileURI == null ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_select_second_property_image),true)
                false
            }

            mSelectedThirdImageFileURI == null ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_select_third_property_image),true)
                false
            }
            else->{
                true
            }
        }

    }

    private fun firstImageUploadSuccess(imageURL: String){
        //hideProgressDialog()
        //showErrorSnackBar("Property images have been uploaded successfully. Main Image URL: $imageURL",false)
        mPropertyImageURL = imageURL
        uploadSecondImage()
        //After this go to upload second image
        //uploadsecondImage()
        //mPropertySecondImageURL = secondImageURL
        //mPropertyThirdImageURL = thirdImageURL
        // upload product details to cloud
        //uploadProductDetails()

    }

    private fun secondImageUploadSuccess(secondImageURL: String) {
        mPropertySecondImageURL = secondImageURL
        uploadThirdImage()

    }

    private fun thirdImageUploadSuccess(thirdImageURL: String) {

        mPropertyThirdImageURL = thirdImageURL
        uploadPropertyDetails()
    }

    fun propertyUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.property_upload_success_message),
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    private fun uploadPropertyDetails(){
        val username = this.getSharedPreferences(Constants.RATED_RENTALS_PREFERENCES,
            Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")!!

        val furnish =  when {
            rb_not_furnished.isChecked -> {
                Constants.NOT_FURNISHED
            }
            rb_partial_furnished.isChecked -> {
                Constants.SEMI_FURNISHED
                }
            else -> {
                Constants.FURNISHED
                }
            }

        val myCountryCode = country_Code.selectedCountryCodeWithPlus.toString()
        //val formattedPrice = NumberFormat.getCurrencyInstance().format(et_property_price)


        val property = Properties(
            FirestoreClass().getCurrentUserId(),
            username,
            edit_text_property_title.text.toString().trim{it <= ' '},
            et_property_price.text.toString().trim{it <= ' '},
            et_property_district.text.toString().trim{it <= ' '},
            et_property_area.text.toString().trim{it <= ' '},
            et_property_garage.text.toString().trim{it <= ' '},
            et_property_bathrooms.text.toString().trim{it <= ' '},
            et_property_bedrooms.text.toString().trim{it <= ' '},
            furnish,
            et_property_description.text.toString().trim{it <= ' '},
            et_property_contact_details.text.toString().trim(){it<' '},
            mPropertyImageURL,
            mPropertySecondImageURL,
            mPropertyThirdImageURL,
            myCountryCode,
            et_property_contact_hours.text.toString().trim{it <= ' '},
            et_property_contact_email.text.toString().trim{it <= ' '}
        )

        FirestoreClass().uploadPropertyDetails(this,property)
    }

    private fun uploadPropertyImage(){
        showProgressDialog()
        uploadFirstImage()
        //FirestoreClass().uploadImageToCloudFireStorage(this,mSelectedImageFileURI, Constants.PROPERTY_DISPLAY_IMAGE)
        //When done go upload to FireStore storage
        //FirestoreClass().uploadImageToCloudFireStorage(this,mSelectedSecondImageFileURI,Constants.PROPERTY_SECOND_ADDITIONAL_IMAGE)
        //FirestoreClass().uploadImageToCloudFireStorage(this,mSelectedThirdImageFileURI,Constants.PROPERTY_THIRD_ADDITIONAL_IMAGE)
    }

    /**Upload Image One**/
    private fun uploadFirstImage(){

        if (mSelectedImageFileURI != null){

            val imageExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(mSelectedImageFileURI!!))

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "displayRentalPropertyImage " + System.currentTimeMillis() + "." + imageExtension
            )

            //Put File Online
            sRef.putFile(mSelectedImageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                           firstImageUploadSuccess(uri.toString())


                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.e(javaClass.simpleName, exception.message, exception)

                        }
                }
        }
        else {
            Toast.makeText(
                this,
                "Please select an image to upload",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    /**Upload Image Two**/
    private fun uploadSecondImage(){
        if (mSelectedSecondImageFileURI != null){

            val imageExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(mSelectedSecondImageFileURI!!))

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "secondRentalPropertyImage " + System.currentTimeMillis() + "." + imageExtension
            )

            //Put File Online
            sRef.putFile(mSelectedSecondImageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            secondImageUploadSuccess(uri.toString())


                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.e(javaClass.simpleName, exception.message, exception)

                        }
                }
        }
        else {
            Toast.makeText(
                this,
                "Please select an image to upload",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    /**Upload Image Three**/
    private fun uploadThirdImage(){
        if (mSelectedThirdImageFileURI != null){

            val imageExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(mSelectedThirdImageFileURI!!))

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "thirdRentalPropertyImage " + System.currentTimeMillis() + "." + imageExtension
            )

            //Put File Online
            sRef.putFile(mSelectedThirdImageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            thirdImageUploadSuccess(uri.toString())
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                exception.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.e(javaClass.simpleName, exception.message, exception)

                        }
                }
        }
        else {
            Toast.makeText(
                this,
                "Please select an image to upload",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_add_property_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_add_property_activity.setNavigationOnClickListener { onBackPressed() }
    }




}