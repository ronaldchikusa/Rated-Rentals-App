package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_add_rental_property.*
import kotlinx.android.synthetic.main.activity_my_property_details.*
import kotlinx.android.synthetic.main.activity_property_details.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader

class MyPropertyDetailsActivity : BaseActivity() {

    private var  mPropertyId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_property_details)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PROPERTY_ID)){
            mPropertyId = intent.getStringExtra(Constants.EXTRA_PROPERTY_ID)!!
            Log.i("PropertyId",mPropertyId)
        }

        getPropertyDetails()
    }


    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_my_property_details_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_my_property_details_activity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun getPropertyDetails(){
        showProgressDialog()
        FirestoreClass().getPropertyDetails(this@MyPropertyDetailsActivity, mPropertyId)
    }

    fun propertyDetailsSuccess(property: Properties){

        GlideLoader(this@MyPropertyDetailsActivity).loadPropertyPicture(property.display_image, iv_my_property_detail_image)
        GlideLoader(this@MyPropertyDetailsActivity).loadPropertyPicture(property.second_additional_image, iv_property_image2_my_property_images)
        GlideLoader(this@MyPropertyDetailsActivity).loadPropertyPicture(property.third_additional_image, iv_property_image3_my_property_details)

        tv_my_property_details_title.text = property.property_title
        my_property_details_property_area.text = property.property_area_location
        property_details_my_property_district.text = property.property_district_location
        property_details_my_property_number_of_bedrooms.text = property.property_number_of_bedrooms
        property_details_my_property_number_of_bathroom.text = property.property_number_of_bathrooms
        property_details_my_property_actual_price.text = property.property_price
        tv_property_my_property_details_description.text = property.general_property_description
        furnishing_my_property_status.text = property.furniture_status
        garage_my_property_status.text = property.property_number_of_garages
        property_details_my_property_actual_time_frame.text = property.contact_time_frame
        property_details_my_property_actual_email.text = property.optional_email_address
        bedroom_status_my_property.text = property.property_number_of_bedrooms
        bathroom_status_my_property.text = property.property_number_of_bathrooms
        property_id_status_my_property.text = mPropertyId

        if (property.optional_email_address == ""){
            ll_my_contact_email.visibility = View.GONE
        }

        if (property.property_number_of_garages == "0"){
            garage_my_property_status.text = "None"
        }


        property_details_my_property_actual_name.text = property.user_name
        property_details_my_property_actual_number.text = property.mobile_contact

        hideProgressDialog()
    }

}