package mw.technologies3g.ratedrentalsmalawi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Properties(
    val user_id: String = "",
    val user_name: String = "",
    val property_title: String = "",
    val property_price: String = "",
    val property_district_location: String = "",
    val property_area_location: String = "",
    val property_number_of_garages: String = "",
    val property_number_of_bathrooms: String = "",
    val property_number_of_bedrooms: String = "",
    val furniture_status: String ="",
    val general_property_description: String = "",
    val mobile_contact: String = "",
    val display_image: String = "",
    val second_additional_image: String = "",
    val third_additional_image: String = "",
    val countryCode: String = "",
    val contact_time_frame: String = "",
    val optional_email_address: String = "",
    var property_id: String = "",

):Parcelable