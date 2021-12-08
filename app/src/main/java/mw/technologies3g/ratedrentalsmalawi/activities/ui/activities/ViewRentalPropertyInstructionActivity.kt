package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_view_rental_property_instruction.*
import mw.technologies3g.ratedrentalsmalawi.R

class ViewRentalPropertyInstructionActivity : AppCompatActivity() {
        lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rental_property_instruction)

        MobileAds.initialize(this)
        val adView = AdView(this)

        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"



        mAdView = findViewById(R.id.view_rental_properties_adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        setupActionBar()
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_view_property_instruction_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_view_property_instruction_activity.setNavigationOnClickListener { onBackPressed() }
    }
}