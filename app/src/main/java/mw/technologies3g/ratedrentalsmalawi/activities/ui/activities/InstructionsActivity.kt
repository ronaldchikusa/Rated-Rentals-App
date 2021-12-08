package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.activity_instructions.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass

class InstructionsActivity : BaseActivity(), View.OnClickListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)



        setupActionBar()
        button_how_to_edit_profile_activity.setOnClickListener(this)
        button_how_to_add_rental_property.setOnClickListener(this)
        button_how_to_delete_rental_property.setOnClickListener(this)
        button_how_to_view_rental_property.setOnClickListener(this)
        button_how_to_contact_property_owners.setOnClickListener(this)
        button_how_to_refresh_a_page.setOnClickListener(this)
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_instructions_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_instructions_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {

        if (view != null){

            when(view.id){

                R.id.button_how_to_edit_profile_activity->{

                    startActivity(Intent(this@InstructionsActivity, EditUserProfileInstructionActivity::class.java))
                }

                R.id.button_how_to_add_rental_property->{
                    startActivity(Intent(this@InstructionsActivity, AddRentalPropertyInstructionActivity::class.java))
                }

                R.id.button_how_to_delete_rental_property->{
                    startActivity(Intent(this@InstructionsActivity, DeleteRentalPropertyInstructionActivity::class.java))
                }

                R.id.button_how_to_view_rental_property->{
                    startActivity(Intent(this@InstructionsActivity, ViewRentalPropertyInstructionActivity::class.java))
                }

                R.id.button_how_to_contact_property_owners->{
                    startActivity(Intent(this@InstructionsActivity, ContactRentalPropertyOwnersInstructionActivity::class.java))
                }

                R.id.button_how_to_refresh_a_page->{
                    startActivity(Intent(this@InstructionsActivity, RefreshPageInstructionActivity::class.java))
                }

             }
            }
        }
    }

