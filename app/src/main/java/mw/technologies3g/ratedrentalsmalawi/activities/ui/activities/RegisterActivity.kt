package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.User

class RegisterActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECIATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        tv_login.setOnClickListener(this)
        button_register.setOnClickListener(this)
        tv_ts_and_cs_register.setOnClickListener(this)

    }


    override fun onClick(view: View?) {

        if(view != null){

            when (view.id){

                R.id.button_register->{
                    registerUser()
                }

                R.id.tv_login->{
                    onBackPressed()
                }

                R.id.tv_ts_and_cs_register->{
                    //startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    showTermsAndConditions()
                }
            }
        }
    }

    /**Back button on the screen**/
    private fun setupActionBar(){

        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_arrow_back_ios_24)
        }

        toolbar_register_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**Validate the entries of the new user**/
    private fun validateRegisterDetails(): Boolean{

        return when{

            TextUtils.isEmpty(et_first_name.text.toString().trim() {it <= ' '}) || et_first_name.length() <= 2->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim() {it <= ' '}) || et_last_name.length() <= 2 ->{ //Modified validation
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim() {it <= ' '})  ->{
                //Find a better solution for validating email address such as the @ sign on the email
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim() {it <= ' '}) || et_password.length() <= 3 ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim() {it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_enter_password),true)
                false
            }

            //Comparing the passwords
            et_password.text.toString().trim{it <=' '} != et_confirm_password.text.toString().trim { it <= ' '} ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_password_does_not_match),true)
                false
            }
            !cb_terms_and_conditions.isChecked ->{
                showErrorSnackBar(resources.getString(R.string.error_msg_agree_terms_and_conditions),errorMessage = true)
                false
            }
            else->{
                //showErrorSnackBar(resources.getString(R.string.registry_successful),false)
                true
            }

        }
    }


    private fun registerUser() {

        //Checking if the details of the system were valid
        if(validateRegisterDetails()){

            /**Show the loading thing at this point since the system has verified that all of the fields
            have been entered and filled in successfully
            You can make a custom string here by manually writing the message to be displayed**/

            showProgressDialog()

            val email: String = et_email.text.toString().trim{ it <= ' '}
            val password: String = et_password.text.toString().trim{ it <= ' '}

            //Here you create an instance of registering a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener <AuthResult>{ task ->
                        /**Doesn't matter if the app the registration is a success it just needs to go fam lol**/

                        //if the registration is a success then
                        if (task.isSuccessful){
                            //Firebase should register the user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            //new user entry, our own user not the firebase user
                            val user  = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim(){ it <= ' '},
                                et_last_name.text.toString().trim(){ it <= ' '},
                                et_email.text.toString().trim(){ it <= ' '}
                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)
                            //FirebaseAuth.getInstance().signOut()//Sign the user out
                            //finish()//So that the activity stack is empty
                        }
                        else{
                            hideProgressDialog()
                            //if the registration has an error, then the system should write the exact error on screen
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    })

        }
    }

    fun userRegistrationSuccessful(){
        //Hide progress dialog
        hideProgressDialog()
        Toast.makeText(this,
            resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
    }

   
    private fun showTermsAndConditions(){
        val builder = AlertDialog.Builder(this@RegisterActivity)
        //set the title for the alert dialog
        builder.setTitle(resources.getString(R.string.ts_and_cs))
        //set message for the dialog
        builder.setMessage(resources.getString(R.string.both_ts_and_cs))
        builder.setIcon(R.drawable.ic_verctor_warning)

        //perform positive action
        builder.setPositiveButton(resources.getString(R.string.ok)){
                dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        //create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        //set other AlertDialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}