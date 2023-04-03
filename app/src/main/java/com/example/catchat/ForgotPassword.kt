package com.example.catchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var frgt_pass_btn: Button
    private lateinit var  back_btn: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.hide()

        email = findViewById(R.id.email)
        frgt_pass_btn = findViewById(R.id.RESET_LINK)
        back_btn = findViewById(R.id.back_btn)

        mAuth = FirebaseAuth.getInstance()

        val anim_signup1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup1);
        val anim_signup2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup2);
        val signup_btn_anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.btn_signup_page)

        email.animate().apply {
            email.startAnimation(anim_signup1)
        }

        frgt_pass_btn.animate().apply {
            frgt_pass_btn.startAnimation(anim_signup2)
        }

        back_btn.animate().apply {
            back_btn.startAnimation(anim_signup1)
        }

        back_btn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)

        }

        frgt_pass_btn.setOnClickListener {
            val email1 = email.text.toString()



            if(email1.isNullOrEmpty() == false) {
                forgotPass(email1)
            }
            else{
                Toast.makeText(applicationContext,"ERROR : BLANK ENTRY",Toast.LENGTH_SHORT).show()
            }

        }

    }


    private fun forgotPass(email1: String) {
        mAuth.sendPasswordResetEmail(email1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "SENDING...", Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "RESET EMAIL SENT", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext, "RESET EMAIL FAILED", Toast.LENGTH_LONG).show()
                }
            }

    }


}