package com.abcd.catchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class VerificationPage : AppCompatActivity() {

    private lateinit var verified : TextView
    private lateinit var notVerified : TextView
    private lateinit var check_btn : Button
    private lateinit var loginPG_btn: Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var resend_btn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_page)

        supportActionBar?.hide()

        verified = findViewById(R.id.eIsV)
        notVerified = findViewById(R.id.eNotV)
        check_btn = findViewById(R.id.check_btn)
        loginPG_btn = findViewById(R.id.loginPAGE_btn)
        mAuth = FirebaseAuth.getInstance()
        resend_btn = findViewById(R.id.resend_btn)
        var back = findViewById<Button>(R.id.BACK)

        verified.setVisibility(View.INVISIBLE)
        loginPG_btn.setVisibility(View.INVISIBLE)
        resend_btn.setVisibility(View.VISIBLE)
        notVerified.setVisibility(View.VISIBLE)
        check_btn.setVisibility(View.VISIBLE)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        check_btn.setOnClickListener{

            if(mAuth.currentUser?.isEmailVerified == true){
                verified.setVisibility(View.VISIBLE)
                loginPG_btn.setVisibility(View.VISIBLE)
                notVerified.setVisibility(View.INVISIBLE)
                check_btn.setVisibility(View.INVISIBLE)
                resend_btn.setVisibility(View.INVISIBLE)
            }
        }

        loginPG_btn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        resend_btn.setOnClickListener {

            mAuth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(applicationContext, "VERIFY YOUR EMAIL", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "FAILED TO SEND VERIFICATION EMAIL", Toast.LENGTH_LONG).show()
                    }

                }

        }


    }
}