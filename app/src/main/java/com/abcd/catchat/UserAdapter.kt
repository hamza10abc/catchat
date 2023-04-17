package com.abcd.catchat

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

private lateinit var storRef : StorageReference
private lateinit var mAuth: FirebaseAuth
private lateinit var mDbRef : DatabaseReference

class UserAdapter(val context: Context, val userList: ArrayList<user>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.fall_down))

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
//        mDbRef = FirebaseDatabase.getInstance().getReference("user").child("$currid").child("Friends").child("$new_frnd_id")
        mDbRef = FirebaseDatabase.getInstance().getReference("user").child("$new_frnd_id").child("Friends").child("$currid")
        mDbRef.addListenerForSingleValueEvent(object: ValueEventListener {
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

        if(holder.indicator.isVisible == true)
        //-----INDICATOR CODE----------//

        holder.textname.text = currentUser.name


        holder.itemView.setOnClickListener{

            val intent = Intent(context,chatPage::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)
        val profilePic = itemView.findViewById<ImageView>(R.id.profilePic)
        val indicator = itemView.findViewById<ImageView>(R.id.orange_indicator)
        val cardView = itemView.findViewById<CardView>(R.id.userLayout_view)
    }


}