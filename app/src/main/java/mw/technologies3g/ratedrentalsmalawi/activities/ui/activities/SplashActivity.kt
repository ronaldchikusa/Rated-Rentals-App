package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass


class SplashActivity : AppCompatActivity() {
    var sharedPreference: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //This is deprecated tho
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        if (restorePrefData()){
            @Suppress("DEPRECATION")
            Handler().postDelayed( {

                // If the user is logged in once and did not logged out manually from the app.
                // So, next time when the user is coming into the app user will be redirected to MainScreen.
                // If user is not logged in or logout manually then user will  be redirected to the Login screen as usual.
                // Get the current logged in user id
                val currentUserID = FirestoreClass().getCurrentUserId()

                if (currentUserID.isNotEmpty()) {
                    // Launch dashboard screen.
                    startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                } else {
                    // Launch the Login Activity
                    //If it first time run, then show onBoardingTing, if not go login
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

                }
                finish() // Call this when your activity is done and should be closed.
            },2500) //Milliseconds of course

        }else{
            savePrefData()
            @Suppress("DEPRECATION")
            Handler().postDelayed({
                val i = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(i)
                finish()
            },2500)

        }


    }
    //Shared Preferences
    private fun savePrefData(){
        sharedPreference = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreference!!.edit()
        editor?.putBoolean("FirstTimeRun", true)
        editor?.apply()
    }
    private fun restorePrefData(): Boolean{
        sharedPreference = applicationContext.getSharedPreferences("pref", MODE_PRIVATE)
        return sharedPreference!!.getBoolean("FirstTimeRun",false)
    }

}