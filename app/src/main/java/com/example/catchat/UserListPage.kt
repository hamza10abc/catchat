package com.example.catchat

import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class UserListPage : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<user>
    private lateinit var tempArrayList: ArrayList<user>
    private lateinit var adapter: UserListPageAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private lateinit var Back_btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list_page)

        Back_btn = findViewById(R.id.back_btn)
        Back_btn.setOnClickListener {
            val intent = Intent(this, home_page::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        tempArrayList = ArrayList()
//        adapter = UserListPageAdapter(this@UserListPage,userList)
        adapter = UserListPageAdapter(this@UserListPage,tempArrayList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(user::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)

                    }
                }
                tempArrayList.addAll(userList)
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        val item = menu?.findItem(R.id.search_txt)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                tempArrayList.clear()
                val SearchText = p0!!.toLowerCase(Locale.getDefault())
                if(SearchText.isNotEmpty()){
                    userList.forEach{
                        if (it.name?.toLowerCase(Locale.getDefault())!!.contains(SearchText) || it.email?.toLowerCase(Locale.getDefault())!!.contains(SearchText)){
                            tempArrayList.add(it)
                        }
                    }
                    userRecyclerView.adapter!!.notifyDataSetChanged()
                }

                else{
                    tempArrayList.clear()
                    tempArrayList.addAll(userList)
                    userRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    fun onFinish() {
        // Call finish() to finish the activity
        finish()
    }

}