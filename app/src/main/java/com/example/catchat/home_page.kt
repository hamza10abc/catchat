package com.example.catchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class home_page : AppCompatActivity() {




    lateinit var  toogle : ActionBarDrawerToggle

    private lateinit var nav_username : TextView
    private lateinit var nav_email : TextView

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<user>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this@home_page,userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter








        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toogle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.isDrawerSlideAnimationEnabled
        toogle.syncState()
        val navView : NavigationView = findViewById(R.id.nav_view)/////////




        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_settings -> Toast.makeText(applicationContext,"SETTINGS",Toast.LENGTH_SHORT).show()
               // R.id.nav_trash -> Toast.makeText(applicationContext,"TRASH",Toast.LENGTH_SHORT).show()
               // R.id.nav_theme -> Toast.makeText(applicationContext,"THEMES",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> Toast.makeText(applicationContext,"LOGOUT",Toast.LENGTH_SHORT).show()
                R.id.nav_reset_pass -> Toast.makeText(applicationContext,"RESET PASSWORD",Toast.LENGTH_SHORT).show()
                R.id.nav_rate -> Toast.makeText(applicationContext,"RATE US",Toast.LENGTH_SHORT).show()
                R.id.nav_feedback -> Toast.makeText(applicationContext,"FEEDBACK",Toast.LENGTH_SHORT).show()


            }
            true
        }

        // NAV HEADER PROFILE
        val curruid = mAuth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("user")
        database.child("$curruid").get().addOnSuccessListener {

            val user_name = it.child("name").value
            val user_email = it.child("email").value

            findViewById<TextView>(R.id.crnt_username).setText("$user_name")
            findViewById<TextView>(R.id.crnt_email).setText("$user_email")

        }

        // logout from nav menu

        navView.setNavigationItemSelectedListener { item ->

            // SETTINGS FROM NEV MENU

            when(item.itemId) {
                R.id.nav_settings ->{

                    val intent = Intent(this, nav_settings::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    return@setNavigationItemSelectedListener true
                }
            }

            when(item.itemId) {
                R.id.nav_developer ->{

                    val intent = Intent(this, about_developer::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    return@setNavigationItemSelectedListener true
                }
            }

            when(item.itemId){
                R.id.nav_logout ->{

                    mAuth.signOut()
                    val intent = Intent(this,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    return@setNavigationItemSelectedListener true
                }


            };false


            when(item.itemId){
                R.id.nav_reset_pass -> {

                    val curruid = mAuth.currentUser?.uid
                    database = FirebaseDatabase.getInstance().getReference("user")
                    database.child("$curruid").get().addOnSuccessListener {

                        val user_email = it.child("email").value
                        val email1 = user_email.toString()

                        mAuth.sendPasswordResetEmail(email1)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "SENDING...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(
                                        applicationContext,
                                        "RESET EMAIL SENT",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "FAILED!!! SEND RESET EMAIL",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    };return@setNavigationItemSelectedListener true
                }
            };false


        }









        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(user::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)

                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout){

            mAuth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0,0)

            return true
        }
        return true
    }
}