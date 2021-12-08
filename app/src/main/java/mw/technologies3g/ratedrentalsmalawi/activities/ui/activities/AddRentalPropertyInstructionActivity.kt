package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_add_rental_property_instruction.*
import mw.technologies3g.ratedrentalsmalawi.R

class AddRentalPropertyInstructionActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rental_property_instruction)
        //TODO --> PUT ADS BACK
        MobileAds.initialize(this)
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        mAdView = findViewById(R.id.add_rental_property_adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        setupActionBar()
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_add_property_instruction_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_add_property_instruction_activity.setNavigationOnClickListener { onBackPressed() }
    }
}