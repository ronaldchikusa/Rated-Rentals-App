package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_real_estate_agency.*
import mw.technologies3g.ratedrentalsmalawi.R

class RealEstateAgencyActivity : AppCompatActivity() {
    private lateinit var emailMyApp: TextView
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_estate_agency)
        MobileAds.initialize(this)


        //Interstitial Ads
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(ContentValues.TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(ContentValues.TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(ContentValues.TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(ContentValues.TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed( {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            // Call this when your activity is done and should be closed.
        },4000) //Milliseconds of course

        setupActionBar()

        /**Email intent to email app on device**/
        emailMyApp = findViewById(R.id.act_email_my_app)

        emailMyApp.setOnClickListener{
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto","ratedrentalsmalawi@gmail.com",null))
            startActivity(Intent.createChooser(emailIntent, "Send email to Rated Rentals via..."))
        }
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_real_estate_agency_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_real_estate_agency_activity.setNavigationOnClickListener { onBackPressed() }
    }
}