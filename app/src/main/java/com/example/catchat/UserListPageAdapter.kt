package com.example.catchat

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.GnssAntennaInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

private lateinit var DBref : DatabaseReference
private lateinit var mDbRef : DatabaseReference
private lateinit var mAuth: FirebaseAuth
private lateinit var storRef : StorageReference

class UserListPageAdapter(val context: Context, val userList: ArrayList<user>):
    RecyclerView.Adapter<UserListPageAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }



    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        //----PROFILE PIC FETCH
        val imageName = currentUser.uid
        storRef = FirebaseStorage.getInstance().getReference("profilePic/$imageName")
        val localFile = File.createTempFile("tempIMG","jpg")
        storRef.getFile(localFile).addOnCompleteListener{

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            if (bitmap != null){
                holder.profilePic.setImageBitmap(bitmap)
            }
        }.addOnFailureListener{
        }
        //-------------------------------

        //-----INDICATOR CODE----------//
        mAuth = FirebaseAuth.getInstance()
        val currid = mAuth.currentUser?.uid
        val new_frnd_id = currentUser.uid
//        val reference: DatabaseReference = mDbRef.reference
        mDbRef = FirebaseDatabase.getInstance().getReference("user").child("$currid").child("Friends").child("$new_frnd_id")
        mDbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    holder.indicator.setVisibility(View.VISIBLE)
                }
                else{
                    holder.indicator.setVisibility(View.INVISIBLE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //-----INDICATOR CODE----------//
        holder.textname.text = currentUser.name
        holder.textEmail.text = currentUser.email
        holder.textEmail.setVisibility(View.INVISIBLE)


        holder.itemView.setOnClickListener{
//            val intent = Intent(context,chatPage::class.java)
//
//            intent.putExtra("name",currentUser.name)
//            intent.putExtra("uid",currentUser.uid)
//            context.startActivity(intent)

            val new_frnd_id = currentUser.uid
            mAuth = FirebaseAuth.getInstance()
            val currid = mAuth.currentUser?.uid
            DBref = FirebaseDatabase.getInstance().getReference("user").child("$currid").child("Friends")
            val new_Friend_info = user(currentUser.name, currentUser.email, currentUser.uid)
            DBref.child("$new_frnd_id").setValue(new_Friend_info).addOnSuccessListener {
                
                val intent = Intent(context,chatPage::class.java)
                intent.putExtra("name",currentUser.name)
                intent.putExtra("uid",currentUser.uid)
                (context as UserListPage).finish()
                context.startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(context, "ERROR: UNABLE TO ADD FRIEND", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)
        val profilePic = itemView.findViewById<ImageView>(R.id.profilePic)
        val indicator = itemView.findViewById<ImageView>(R.id.green_indicator)
        val textEmail = itemView.findViewById<TextView>(R.id.txt_email)
    }


}
