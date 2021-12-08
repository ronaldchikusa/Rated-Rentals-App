package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_developers.*
import mw.technologies3g.ratedrentalsmalawi.R

class DevelopersInfoActivity : AppCompatActivity() {

    private lateinit var emailIntent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developers)

        setupActionBar()

        val textView: TextView = findViewById(R.id.act_linked)
        textView.movementMethod = LinkMovementMethod.getInstance()

        /**Email intent to email app on device**/
        emailIntent = findViewById(R.id.act_email)

        emailIntent.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO,
            Uri.fromParts("mailto","chikusaronald1@gmail.com",null))
            startActivity(Intent.createChooser(emailIntent, "Send email to Ronald via..."))
        }


    }


    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_developers_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_developers_activity.setNavigationOnClickListener { onBackPressed() }
    }



}