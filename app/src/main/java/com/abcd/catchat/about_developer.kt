package com.abcd.catchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class about_developer : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_developer)

        supportActionBar?.title = "ABOUT DEVELOPER"

        mAuth = FirebaseAuth.getInstance()

        var curruid = mAuth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user")
        database.child("$curruid").get().addOnSuccessListener {

            val user = it.child("name").value

            val tv = findViewById<TextView>(R.id.text)
            tv.setText("Hi $user\n" +
                    "I am Hamza, the developer of CATCHAT.\n" +
                    "\n" +
                    "My interest to develop CATCHAT arised in the early days of March 2022.\n" +
                    "\n" +
                    "It wasn't easy at all, infact I should mention it was extremely hard as I had no help to get to this stage and moreover I had no support while developing this app. The best part was I was too dedicated and determined to develop this application that even people demotivating me couldn't stop me from my work.\n" +
                    "\n" +
                    "I still managed it somehow and developed something kinda useful.\n" +
                    "\n" +
                    "The amount of energy and time spent on this was the most in all the types of projects I have made.\n" +
                    "\n" +
                    "I literally can't make credits / cast of this application as I will just have to copy paste my own name again and again \uD83D\uDE02.\n" +
                    "\n" +
                    "I feel like there is only one developer on EARTH and that's me cause I tried searching but found none except ETERNAL DARKNESS.\n" +
                    "\n" +
                    "IMAGINE HOW HARD IT WAS TO RESOLVE ERRORS \uD83D\uDE14\n" +
                    "\n" +
                    "I am looking for people who can give me proper feedback and provide me support ( for first time in my life ) to help me take this application to ADVANCED level.\n" +
                    "\n" +
                    "PLEASE TRY GIVING 5 STAR RATING\n" +
                    "Now this is the biggest obstacle for me to overcome.\n" +
                    "That is to make my $user HAPPY.\n" +
                    "\n" +
                    "ALL THE BEST \uD83D\uDE42\n" +
                    "ENJOY AND PROVIDE ME FEEDBACK\n" +
                    "WISH YOU A HAPPY CATCHAT LIFE")


        }


    }
}