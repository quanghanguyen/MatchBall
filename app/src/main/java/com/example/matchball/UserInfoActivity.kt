package com.example.matchball

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matchball.model.User
import com.example.matchball.databinding.ActivityUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.net.URI

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userInfoBinding: ActivityUserInfoBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var storageReference : StorageReference
    private lateinit var imgUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(userInfoBinding.root)


        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        getData()
        loadAvatar()
        avatarClick()
        btnSaveClick()

    }

    private fun loadAvatar() {
        val uid = auth.currentUser!!.uid
        val firebaseStorage = FirebaseStorage.getInstance().getReference("Users").child(uid)
        val localFile = File.createTempFile("tempImage", "jpg")
        firebaseStorage.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            userInfoBinding.civAvatar.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Toast.makeText(this, "Load Avatar Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getData() {
        val nameIntent = intent.getStringExtra("name")
        val bioIntent = intent.getStringExtra("bio")
        val emailIntent = intent.getStringExtra("email")

        intent?.let {
            with(userInfoBinding) {
                edtTeamName.setText(nameIntent)
                edtBio.setText(bioIntent)
                edtEmail.setText(emailIntent)
            }
        }
    }

    private fun btnSaveClick() {
        val uid = auth.currentUser?.uid
        userInfoBinding.btnSave.setOnClickListener {
            val teamName = userInfoBinding.edtTeamName.text.toString()
            val teamBio = userInfoBinding.edtBio.text.toString()
            val teamEmail = userInfoBinding.edtEmail.text.toString()
            val user = User(teamName, teamBio, teamEmail)

            if (uid != null) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        uploadProfile()
                    } else {
                        Toast.makeText(this, "Failed to update Profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun avatarClick() {
        userInfoBinding.civAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 100)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            userInfoBinding.civAvatar.setImageURI(imgUri)
        }
    }

    private fun uploadProfile() {
//        imgUri = Uri.parse("android.resource://$packageName/${R.drawable.ic_launcher_background}")
//        imgUri = Uri.parse("android.resource://$packageName")
        storageReference = FirebaseStorage.getInstance().getReference("Users/" + auth.currentUser?.uid)
        storageReference.putFile(imgUri).addOnSuccessListener {
            Toast.makeText(this, "Save Profile Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to Save Profile", Toast.LENGTH_SHORT).show()
        }
    }
}