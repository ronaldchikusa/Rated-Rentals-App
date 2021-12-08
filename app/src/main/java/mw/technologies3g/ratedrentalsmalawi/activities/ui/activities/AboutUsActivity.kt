package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about_us.*
import mw.technologies3g.ratedrentalsmalawi.R

class AboutUsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        setupActionBar()

        button_tcs_activity.setOnClickListener(this)
        button_developers.setOnClickListener(this)
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_about_us_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_about_us_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {

        if (view != null){

            when (view.id){

                R.id.button_tcs_activity -> {
                    startActivity(Intent(this, TermsAndConditionsActivity::class.java))
                }

                R.id.button_developers -> {
                    startActivity(Intent(this@AboutUsActivity, DevelopersInfoActivity::class.java))
                }

            }
        }
    }


}