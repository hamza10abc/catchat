package com.example.catchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp_PAGE : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var signup_btn: Button
    private lateinit var old_user : Button
    private lateinit var main_box : CheckBox

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        val anim_signup1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup1);
        val anim_signup2 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.anim_signup2);
        val signup_btn_anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.btn_signup_page)

        edtName = findViewById(R.id.name)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        signup_btn = findViewById(R.id.signup_pg_btn)
        old_user = findViewById(R.id.alrdy_user)
        main_box = findViewById(R.id.main_box)
        main_box.isChecked = false
        signup_btn.isClickable = false

        edtName.animate().apply {
            edtName.startAnimation(anim_signup1)
        }

        username.animate().apply {
            username.startAnimation(anim_signup2)
        }

        password.animate().apply {
            password.startAnimation(anim_signup1)
        }

        signup_btn.animate().apply {
            signup_btn.startAnimation(signup_btn_anim)
        }

        old_user.animate().apply{
            old_user.startAnimation(signup_btn_anim)
        }

        old_user.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)

        }

        main_box.setOnClickListener {
            if (main_box.isChecked == false){
                signup_btn.isClickable = false
            }
            if (main_box.isChecked == true){
                signup_btn.isClickable = true

                signup_btn.setOnClickListener {
                    val name = edtName.text.toString()
                    val email = username.text.toString()
                    val password = password.text.toString()

                    if(email.isNullOrEmpty() == false && password.isNullOrEmpty() == false && name.isNullOrEmpty() == false) {
                        signup(name, email, password)
                        //   emailVerify(email)
                    }
                    else{
                        Toast.makeText(applicationContext,"ERROR : BLANK ENTRY",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }



    }
/// down...
   // private fun emailVerify(email: String) {
   //     val user = mAuth.currentUser
     //   if(user != null){
     //       log.d(TAG,)
    //    }

//    }

    private fun signup(
        name: String,
        email: String,
        password: String, ){

        // CREATING NEW USER USING EMAIL AND PASSWORD ONLY
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserDatabase(name,email,mAuth.currentUser?.uid!!)

                    mAuth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener(this) { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(applicationContext, "ACCOUNT CREATED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                                Toast.makeText(applicationContext, "VERIFICATION EMAIL SENT", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(applicationContext, "FAILED TO SEND VERIFICATION EMAIL", Toast.LENGTH_LONG).show()
                            }

                        }
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0,0)



                } else {

                    Toast.makeText(this, "UNSUCCESSFUL SIGNUP", Toast.LENGTH_SHORT).show()

                }
            }

    }



    private fun addUserDatabase(name: String,email: String,uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(user(name,email,uid))

    }


}