package com.example.matchball.usersetting

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matchball.model.User
import com.example.matchball.databinding.ActivityUserInfoBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.firebaseconnection.StorageConnection
import com.example.matchball.home.MainActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userInfoBinding: ActivityUserInfoBinding
    private lateinit var imgUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(userInfoBinding.root)

        getData()
        loadAvatar()
        avatarClick()
        btnSaveClick()

    }

    private fun loadAvatar() {
        val uid = AuthConnection.auth.currentUser!!.uid
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
        val phoneIntent = intent.getStringExtra("phone")

        intent?.let {
            with(userInfoBinding) {
                edtTeamName.setText(nameIntent)
                edtBio.setText(bioIntent)
                edtEmail.setText(emailIntent)
                edtPhone.setText(phoneIntent)
            }
        }
    }

    private fun btnSaveClick() {
        val uid = AuthConnection.auth.currentUser?.uid
        userInfoBinding.btnSave.setOnClickListener {
            val teamName = userInfoBinding.edtTeamName.text.toString()
            val teamBio = userInfoBinding.edtBio.text.toString()
            val teamEmail = userInfoBinding.edtEmail.text.toString()
            val teamPhone = userInfoBinding.edtPhone.text.toString()
            val user = User(teamName, teamBio, teamEmail, teamPhone)

            if (uid != null) {
                DatabaseConnection.databaseReference.getReference("Users").child(uid).setValue(user).addOnCompleteListener {
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
        StorageConnection.storageReference.getReference("Users/" + AuthConnection.auth.currentUser?.uid).putFile(imgUri).addOnSuccessListener {
            Toast.makeText(this, "Save Profile Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to Save Profile", Toast.LENGTH_SHORT).show()
        }
    }
}