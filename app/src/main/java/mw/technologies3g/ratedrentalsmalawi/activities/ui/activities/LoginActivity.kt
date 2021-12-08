package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.User
import mw.technologies3g.ratedrentalsmalawi.utils.Constants


class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        @Suppress("DEPRECIATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        /**OnClickListeners**/
        txt_view_forgot_password.setOnClickListener(this)
        button_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }


    /**So on the login screen, you have about three clickable, these clickable can all be implemented
     * here**/
    override fun onClick(view: View?){

        if (view != null){

            when (view.id){

                R.id.txt_view_forgot_password -> {
                   startActivity(Intent(this, ForgotPasswordActivity::class.java))
                }

                R.id.button_login -> {
                    //Calling the validate function
                    loginRegisteredUser()
                }

                R.id.tv_register ->{
                    //Launch the register screen when the user click on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

    private fun validateLoginDetails(): Boolean{
        return when{
            TextUtils.isEmpty(edit_text_email.text.toString().trim() {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(edit_text_password.text.toString().trim() {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_password),true)
                false
            }
            else->{
                //showErrorSnackBar("Your details are valid.", false)
                true
            }

        }
    }


    private fun loginRegisteredUser(){

        if(validateLoginDetails()){

            //show progress dialog
            showProgressDialog()

            //Get text from edit text and trim the space

            val email = edit_text_email.text.toString().trim(){ it <= ' '}
            val password = edit_text_password.text.toString().trim(){it <= ' '}

            //Login Firebase Auth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task->

                    if (task.isSuccessful){
                        //Send user to the main activity
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }

                    else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                        true
                    }

                }
        }
    }

    fun userLoggedInSuccess(user: User){

        //Hide the progress dialog
        hideProgressDialog()

        //if the user has completed the profile they will go to the main activity which is the dashboard activity
        //if the user has not yet completed the profile they will go to the user profile activity

        if(user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)//UserProfileActivity,used test image too here
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }

        finish()

    }


}

