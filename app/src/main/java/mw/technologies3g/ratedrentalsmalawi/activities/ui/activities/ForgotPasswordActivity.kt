package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import mw.technologies3g.ratedrentalsmalawi.R

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar()
    }

    /**Back button on the screen**/
    private fun setupActionBar() {

        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24_white)
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }

        button_submit_forgot_password.setOnClickListener {

            val email: String = et_email_forgot_password.text.toString().trim() { it <= ' ' }
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_email), true)
            } else {
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {

                            Toast.makeText(
                                this,
                                resources.getString(R.string.email_sent_successfully),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()

                        } else {

                            showErrorSnackBar(
                                task.exception!!.message.toString(),
                                errorMessage = true
                            )
                        }

                    }
            }


        }
    }
}

