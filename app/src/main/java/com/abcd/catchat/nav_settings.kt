package com.abcd.catchat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File

class nav_settings : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var update_DB: DatabaseReference
    private lateinit var update_DB2: DatabaseReference

    private lateinit var imagePreview : ImageView
    private lateinit var Img_URI : Uri
    private lateinit var Fbstorage: FirebaseStorage
    private lateinit var storREF: StorageReference
    private var x = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_settings)

        imagePreview = findViewById(R.id.profile_pic)
        Fbstorage = FirebaseStorage.getInstance()
        storREF = FirebaseStorage.getInstance().getReference()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()


        var new_usrnme_btn = findViewById<Button>(R.id.new_usrnme_btn)
        var change_usrnme_btn = findViewById<Button>(R.id.username_rename_btn)
        var new_usrname_txt = findViewById<EditText>(R.id.new_usrnme)

        var back_btn = findViewById<Button>(R.id.back_btn)

        var pic_upload_btn = findViewById<Button>(R.id.pic_upload_btn)
        var select_img_btn = findViewById<Button>(R.id.select_img_btn)
        var upload_img_btn = findViewById<Button>(R.id.upload_img_btn)

        var main_back_btn = findViewById<Button>(R.id.main_back_btn)

        // CURRENT UID FOR CURRENT USERNAME AND EMAIL
        val curruid = mAuth.currentUser?.uid

//----------FETCHING PROFILE PIC FROM FIREBASE CLOUD STORAGE----------------
        val imageName = curruid
        storREF = FirebaseStorage.getInstance().getReference("profilePic/$imageName")
        val localFile = File.createTempFile("tempIMG","jpg")
        storREF.getFile(localFile).addOnCompleteListener{

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imagePreview = findViewById(R.id.profile_pic)
            if (bitmap != null){
                imagePreview.setImageBitmap(bitmap)
            }

        }.addOnFailureListener{

        }
//------------------------------------------------------------------------

        pic_upload_btn.setVisibility(View.VISIBLE)
        change_usrnme_btn.setVisibility(View.VISIBLE)
        main_back_btn.setVisibility(View.VISIBLE)

        new_usrnme_btn.setVisibility(View.INVISIBLE)
        new_usrname_txt.setVisibility(View.INVISIBLE)
        back_btn.setVisibility(View.INVISIBLE)
        select_img_btn.setVisibility(View.INVISIBLE)
        upload_img_btn.setVisibility(View.INVISIBLE)



        main_back_btn.setOnClickListener {

            val intent = Intent(this, home_page::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)

        }

        pic_upload_btn.setOnClickListener {

            pic_upload_btn.setVisibility(View.INVISIBLE)
            change_usrnme_btn.setVisibility(View.INVISIBLE)
            new_usrnme_btn.setVisibility(View.INVISIBLE)
            new_usrname_txt.setVisibility(View.INVISIBLE)
            main_back_btn.setVisibility(View.INVISIBLE)

            back_btn.setVisibility(View.VISIBLE)
            select_img_btn.setVisibility(View.VISIBLE)
            upload_img_btn.setVisibility(View.VISIBLE)


        }

        select_img_btn.setOnClickListener {

           selectImage()

//            Toast.makeText(applicationContext, "THIS FEATURE IS CURRENTLY UNAVAILABLE", Toast.LENGTH_SHORT).show()
        }

        upload_img_btn.setOnClickListener {

            if(x>0){
                uploadImage() 
            }
            else{
                Toast.makeText(applicationContext, "PLEASE SELECT AN IMAGE", Toast.LENGTH_SHORT).show()
            }

//            Toast.makeText(applicationContext, "THIS FEATURE IS CURRENTLY UNAVAILABLE", Toast.LENGTH_SHORT).show()

        }





        change_usrnme_btn.setOnClickListener {

            select_img_btn.setVisibility(View.INVISIBLE)
            upload_img_btn.setVisibility(View.INVISIBLE)
            pic_upload_btn.setVisibility(View.INVISIBLE)
            change_usrnme_btn.setVisibility(View.INVISIBLE)
            main_back_btn.setVisibility(View.INVISIBLE)

            new_usrnme_btn.setVisibility(View.VISIBLE)
            new_usrname_txt.setVisibility(View.VISIBLE)
            back_btn.setVisibility(View.VISIBLE)


        }

        back_btn.setOnClickListener {

            select_img_btn.setVisibility(View.INVISIBLE)
            upload_img_btn.setVisibility(View.INVISIBLE)
            pic_upload_btn.setVisibility(View.VISIBLE)
            change_usrnme_btn.setVisibility(View.VISIBLE)
            new_usrnme_btn.setVisibility(View.INVISIBLE)
            main_back_btn.setVisibility(View.VISIBLE)

            new_usrname_txt.setVisibility(View.INVISIBLE)
            back_btn.setVisibility(View.INVISIBLE)

        }

        new_usrnme_btn.setOnClickListener {



            val username = new_usrname_txt.text.toString()

            if(username.isNullOrEmpty() == false){

                update_DB = FirebaseDatabase.getInstance().getReference("user").child("$curruid")
                update_DB2 = FirebaseDatabase.getInstance().getReference("user")
                val user = mapOf<String, String>("name" to username)

                update_DB.updateChildren(user).addOnSuccessListener {
                    new_usrname_txt.setText("")
                    Toast.makeText(
                        applicationContext,
                        "USERNAME CHANGED SUCCESSFULLY",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "UNSUCCESSFUL : UPDATION FAILED",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //-------------------------
                update_DB2.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (topuidSnapshot in snapshot.children){
                            val topuid = topuidSnapshot.key

                            if (topuidSnapshot.child("Friends").hasChild("$curruid")){

                                topuidSnapshot.child("Friends").child("$curruid").child("name").ref.setValue(username).addOnSuccessListener {  }.addOnFailureListener {
                                    Toast.makeText(applicationContext, "FAILED TO UPDATE", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
                //-------------------------

            }else{
                Toast.makeText(applicationContext, "ERROR: BLANK ENTRY", Toast.LENGTH_SHORT).show()
            }


        }


        //CURRUID USED FOR CURRENT USER UID
        database = FirebaseDatabase.getInstance().getReference("user")
        database.child("$curruid").get().addOnSuccessListener {

            val user_name = it.child("name").value
            val user_email = it.child("email").value

            findViewById<TextView>(R.id.crnt_username).setText("$user_name")
            findViewById<TextView>(R.id.crnt_email).setText("$user_email")

        }

    }

    private fun uploadImage() {

        val fileName = mAuth.currentUser?.uid
        storREF = FirebaseStorage.getInstance().getReference("profilePic/$fileName")
        storREF.putFile(Img_URI).addOnSuccessListener {
            Toast.makeText(applicationContext, "PROFILE PIC UPLOADED", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(applicationContext, "ERROR: UNABLE TO UPLOAD", Toast.LENGTH_SHORT).show()
        }
        
    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
//            Img_URI = data?.data!!
//            val resized = Bitmap.createScaledBitmap(Img_URI, 400, 400, true)
//            imagePreview.setImageURI(Img_URI)
            x = 1

            val inputStream = contentResolver.openInputStream(data?.data!!)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // Calculate the image's new dimensions
            val width = options.outWidth
            val height = options.outHeight
            val scaleFactor = Math.min(width / 400, height / 400)

            // Load the image into memory with the calculated dimensions
            options.inJustDecodeBounds = false
            options.inSampleSize = scaleFactor
            val resizedBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.data!!), null, options)
            imagePreview.setImageBitmap(resizedBitmap)
            Img_URI = resizedBitmap?.let { getImageUri(it) }!!

        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image",
            null
        )
        return Uri.parse(path)
    }

}