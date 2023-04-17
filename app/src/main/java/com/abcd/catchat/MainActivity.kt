package com.abcd.catchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var frgt_pass: TextView
    private lateinit var login: Button
    private lateinit var signup: Button
    private lateinit var text101: TextView
    private lateinit var image101: ImageView
    private lateinit var resendlinkbtn : Button
    private lateinit var textnotverified : TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()


        val anim_logo = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.catchat_logo_anim);
        val anim_user = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim2);
        val anim_btn1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_btn1)
        val anim_btn2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_btn2);

        val catchat_logo: ImageView = findViewById(R.id.catchat_logo)
        val text: TextView = findViewById(R.id.text)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login_btn)
        signup = findViewById(R.id.signup_pg_btn)
        frgt_pass = findViewById(R.id.forgot_password)
        text101 = findViewById(R.id.text101)
        image101 = findViewById(R.id.img101)
        resendlinkbtn = findViewById(R.id.resend_btn)
        textnotverified = findViewById(R.id.notVerifiedtext)

        var x = 0;

        username.setVisibility(View.INVISIBLE)
        password.setVisibility(View.INVISIBLE)
        login.setVisibility(View.INVISIBLE)
        signup.setVisibility(View.INVISIBLE)
        frgt_pass.setVisibility(View.INVISIBLE)
        text101.setVisibility(View.INVISIBLE)
        image101.setVisibility(View.INVISIBLE)
        resendlinkbtn.setVisibility(View.INVISIBLE)
        textnotverified.setVisibility(View.INVISIBLE)

        catchat_logo.startAnimation(anim_logo)
        text.startAnimation(anim_logo)

        catchat_logo.setOnClickListener{
            text.setVisibility(View.INVISIBLE)
            x = x + 1;


            if(x<=1)
            {

            catchat_logo.animate().apply {

                duration = 1300
                alpha(1f)
                scaleXBy(-0.5f)
                scaleYBy(-0.5f)
                translationYBy(-500f)
            }



                username.animate().apply {

                    username.setVisibility(View.VISIBLE)
                    username.startAnimation(anim_user)

                }


                password.animate().apply {

                    password.setVisibility(View.VISIBLE)
                    password.startAnimation(anim_user)

                }


                frgt_pass.animate().apply {

                    frgt_pass.setVisibility(View.VISIBLE)
                    frgt_pass.startAnimation(anim_user)

                }


                login.animate().apply{

                    login.setVisibility(View.VISIBLE)
                    login.startAnimation(anim_btn1)
                }

                signup.animate().apply {

                    signup.setVisibility(View.VISIBLE)
                    signup.startAnimation(anim_btn2)
                }

            }

            //--- SAMPLE CODE FOR INTENT -----------------------------//
           //--- val intent = Intent(this, LOGIN_PAGE::class.java) --//
          //--- startActivity(intent) ------------------------------//
        }

        signup.setOnClickListener {
            val intent = Intent(this, SignUp_PAGE::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)

        }

        frgt_pass.setOnClickListener{
            val intent = Intent(this,ForgotPassword::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        // EMPTY FIELD
        //COPIED TEXT FROM DOWN

        // EMPTY FIELD
        //COPIED TEXT FROM DOWN


        login.setOnClickListener {
            val email = username.text.toString()
            val password1 = password.text.toString()

            if (email.isNullOrEmpty() == false && password1.isNullOrEmpty() == false) {
                login(email, password1)


            }else {
                Toast.makeText(applicationContext, "ERROR : BLANK ENTRY", Toast.LENGTH_SHORT).show()
            }


        }

    }



    private fun login(email: String, password: String){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkVerification()

                } else {

                    Toast.makeText(this, "UNSUCCESSFUL LOGIN", Toast.LENGTH_SHORT).show()

                }
            }}

    private fun checkVerification() {

        val signup_btn_anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.btn_signup_page)
        val anim_signup1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup1);
        val anim_signup2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup2);
        if (mAuth.currentUser?.isEmailVerified == true) {

            val intent = Intent(this,home_page::class.java)
            startActivity(intent)

        }else if(mAuth.currentUser?.isEmailVerified == false) {

            text101.setVisibility(View.VISIBLE)
            image101.setVisibility(View.VISIBLE)
            resendlinkbtn.setVisibility(View.VISIBLE)
            textnotverified.setVisibility(View.VISIBLE)

// ANIMATIONS ->

            image101.animate().apply {
                image101.startAnimation(anim_signup1)
            }

            text101.animate().apply {
                text101.startAnimation(anim_signup2)
            }

            resendlinkbtn.animate().apply {
                resendlinkbtn.startAnimation(signup_btn_anim)
            }

            textnotverified.animate().apply {
                textnotverified.startAnimation(signup_btn_anim)
            }

            resendlinkbtn.setOnClickListener {

                mAuth.currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener(this) { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(applicationContext, "VERIFICATION LINK SENT", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(applicationContext, "FAILED TO SEND VERIFICATION EMAIL", Toast.LENGTH_LONG).show()
                        }

                    }

            }

        }else{
            Toast.makeText(applicationContext, "UNSUCCESSFUL LOGIN", Toast.LENGTH_SHORT).show()
        }

    }
}
