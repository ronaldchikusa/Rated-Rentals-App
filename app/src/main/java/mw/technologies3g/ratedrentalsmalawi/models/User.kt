package mw.technologies3g.ratedrentalsmalawi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Stuff about the users
//Parcel it so that you can save some of the data on the user's device

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profile_photo: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted: Int = 0): Parcelable