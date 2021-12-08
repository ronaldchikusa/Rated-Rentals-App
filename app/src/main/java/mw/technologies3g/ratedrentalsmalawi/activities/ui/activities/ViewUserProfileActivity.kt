package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_view_user_profile.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.User
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader

class ViewUserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_profile)
        setupActionBar()
        tv_edit_my_profile.setOnClickListener(this)
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_pd_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_pd_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showProgressDialog()
        FirestoreClass().getUserDetails(this@ViewUserProfileActivity)
    }

    fun userDetailSuccess(user: User){
        mUserDetails = user
        GlideLoader(this@ViewUserProfileActivity).loadUserPicture(user.profile_photo,iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
        hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {

        if (view != null){

            when(view.id){

                R.id.tv_edit_my_profile->{

                    val intent = Intent(this@ViewUserProfileActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)

                }
            }
        }
    }
}