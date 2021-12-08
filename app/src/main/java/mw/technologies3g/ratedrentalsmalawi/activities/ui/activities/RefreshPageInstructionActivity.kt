package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_contact_rental_property_owners_instruction.*
import kotlinx.android.synthetic.main.activity_refresh_page_instruction.*
import mw.technologies3g.ratedrentalsmalawi.R

class RefreshPageInstructionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_page_instruction)

        setupActionBar()
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_how_to_refresh)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_how_to_refresh.setNavigationOnClickListener { onBackPressed() }
    }
}