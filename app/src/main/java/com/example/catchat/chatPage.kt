package com.example.catchat

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class chatPage : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var send_btn: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var dbref : DatabaseReference
    private lateinit var storRef : StorageReference
    private lateinit var mAuth: FirebaseAuth

    var senderRoom: String? = null
    var receiverRoom: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.catchat_logo_no_bk)
        setContentView(R.layout.activity_chat_page)


        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()
        dbref = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()
        val currid = mAuth.currentUser?.uid

        senderRoom = receiveruid + senderUid
        receiverRoom = senderUid + receiveruid


//--------PROFILE PIC SETUP
//        supportActionBar?.title = name
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setLogo(R.drawable.catchat_logo)
//        supportActionBar?.setDisplayUseLogoEnabled(true)
//        supportActionBar?.setIcon(R.drawable.catchat_logo)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
            setDisplayUseLogoEnabled(false)


            // Set a custom view for the action bar
            setCustomView(R.layout.chatroom_action_bar)

            // Get a reference to the ImageView in the custom layout
            val imageView = customView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.action_bar_image_view)
            val action_name = customView.findViewById<TextView>(R.id.action_name)
            val Remove_Frnd_Btn = customView.findViewById<ImageView>(R.id.Remove_frnd_btn)
            val back_btn = customView.findViewById<ImageButton>(R.id.backbtn)

            back_btn.setOnClickListener {
                val intent = Intent(applicationContext,home_page::class.java)
                finish()
                startActivity(intent)
            }

            Remove_Frnd_Btn.setOnClickListener {
                val builder = AlertDialog.Builder(this@chatPage)
                builder.setTitle("REMOVE FRIEND")
                builder.setMessage("Are you sure you want to UNFRIEND $name from your friend list")
                builder.setPositiveButton("YES",{ dialogInterface: DialogInterface, i: Int ->


                    dbref.child("user").child("$currid").child("Friends").child("$receiveruid").removeValue().addOnSuccessListener{

                        Toast.makeText(applicationContext, "FRIEND REMOVED", Toast.LENGTH_LONG).show()

                        val intent = Intent(applicationContext,home_page::class.java)
                        finish()
                        startActivity(intent)
                    }.addOnFailureListener {

                        Toast.makeText(applicationContext, "FAILED TO UNFRIEND", Toast.LENGTH_LONG).show()

                    }

                })

                builder.setNegativeButton("NO",{ dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }

            // Set a new drawable for the ImageView
//            imageView.setImageResource(R.drawable.my_new_image)
            action_name.setText(name)

//-----------------------------FETCHING PROFILE PIC FROM CLOUD--------------------------------------
            val curruid = receiveruid
            val imageName = curruid
            storRef = FirebaseStorage.getInstance().getReference("profilePic/$imageName")
            val localFile = File.createTempFile("tempIMG","jpg")
            storRef.getFile(localFile).addOnCompleteListener{

                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                val bitmap: Bitmap = bit// your bitmap here
                val drawable = BitmapDrawable(resources, bitmap)

// Set the drawable on the ImageView in the action bar
                if (bitmap != null) {
                    imageView.setImageDrawable(drawable)
                }
//            profile_pic = findViewById(R.id.profile_pic)
//            if (bitmap != null){
//                profile_pic.setImageBitmap(bitmap)
//            }

            }.addOnFailureListener{

            }
//---------------------------------------------------------------------------------------------------


            // Show the custom view in the action bar
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        }




//---------------------------

        messageRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.chatBox)
        send_btn = findViewById(R.id.send_btn)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

            // CODE FOR VIEWING MESSAGE ON CHAT_PAGE
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


            // CODE FOR ADDING MESSAGE TO DATABASE
        send_btn.setOnClickListener{

            val  message = messageBox.text.toString()
            val messageObj = Message(message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObj).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObj)
                }

            messageBox.setText("")



        }
    }
}

