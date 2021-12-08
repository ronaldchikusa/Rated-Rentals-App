package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_add_rental_property.*
import kotlinx.android.synthetic.main.activity_property_details.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader
import java.net.URLEncoder
import java.security.AccessController.getContext

class PropertyDetailsActivity : BaseActivity() {

    private var mDashboardProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PROPERTY_ID)){
            mDashboardProductId = intent.getStringExtra(Constants.EXTRA_PROPERTY_ID)!!

        }
        getDashboardProductDetails()


    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_property_details_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_property_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getDashboardProductDetails(){
        showProgressDialog()
        FirestoreClass().getDashboardPropertyDetails(this@PropertyDetailsActivity, mDashboardProductId)
    }
    fun propertyDashboardDetailsSuccess(property: Properties){

        GlideLoader(this@PropertyDetailsActivity).loadPropertyPicture(property.display_image,iv_property_detail_image)
        GlideLoader(this@PropertyDetailsActivity).loadPropertyPicture(property.second_additional_image,iv_property_image2_property_images)
        GlideLoader(this@PropertyDetailsActivity).loadPropertyPicture(property.third_additional_image,iv_property_image3_property_details)
        tv_product_details_title.text = property.property_title
        property_details_property_area.text = property.property_area_location
        property_details_property_district.text = property.property_district_location
        property_details_number_of_bedrooms.text = property.property_number_of_bedrooms
        property_details_number_of_bathroom.text = property.property_number_of_bathrooms
        property_details_actual_price.text = property.property_price
        tv_property_details_description.text = property.general_property_description
        furnishing_status.text = property.furniture_status
        garage_status.text = property.property_number_of_garages
        property_details_actual_contact_time_frame.text = property.contact_time_frame
        property_details_actual_email.text = property.optional_email_address
        bedroom_status.text = property.property_number_of_bedrooms
        bathroom_status.text = property.property_number_of_bathrooms
        property_id_status.text = mDashboardProductId
        property_details_actual_name.text = property.user_name
        if (property.optional_email_address == ""){
            ll_owner_contact_email.visibility = View.GONE
        }
        if (property.property_number_of_garages == "0"){
            garage_status.text = "None"
        }

        hideProgressDialog()


        button_call.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + property.countryCode+ property.mobile_contact)
            startActivity(dialIntent)
        }
        button_whatsapp.setOnClickListener {

            try {
                val packageManager = this.packageManager
                val intent = Intent(Intent.ACTION_VIEW)
                val url = "https://api.whatsapp.com/send?phone=" +property.countryCode+ property.mobile_contact + "&text=" +
                        URLEncoder.encode("Hello, I am interested in this property: "+"\n"+"\n"+"Title: "+"" +
                                "${property.property_title}" +"\n"+"\n"+"Located in: "+"${property.property_area_location}"+","
                                +"${property.property_district_location}")
                intent.setPackage("com.whatsapp")
                intent.data = Uri.parse(url)
                if (intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Please Install WhatsApp", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Error while loading WhatsApp"+e.stackTrace, Toast.LENGTH_SHORT).show()
            }
        }
    }






}