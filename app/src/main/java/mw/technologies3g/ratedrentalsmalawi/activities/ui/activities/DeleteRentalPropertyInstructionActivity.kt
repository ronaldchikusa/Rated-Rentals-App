package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_delete_rental_property_instruction.*
import mw.technologies3g.ratedrentalsmalawi.R

class DeleteRentalPropertyInstructionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_rental_property_instruction)

        setupActionBar()
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_delete_prop_instruction_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_delete_prop_instruction_activity.setNavigationOnClickListener { onBackPressed() }
    }
}