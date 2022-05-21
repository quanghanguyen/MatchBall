package com.example.matchball.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.matchball.IntroActivity
import com.example.matchball.R
import com.example.matchball.SignUpActivity
import com.example.matchball.UserInfoActivity
import com.example.matchball.databinding.FragmentUserBinding
import com.example.matchball.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class UserFragment : Fragment() {

    private lateinit var userFragmentBinding : FragmentUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readUserData()
        goUserInfoActivity()
        signOut()

    }

    private fun readUserData() {
        val uid = firebaseAuth.currentUser!!.uid

        database.child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val teamName = it.child("teamName").value
                val teamBio = it.child("teamBio").value
                val email = it.child("email").value

                userFragmentBinding.tvIntroName.text = teamName.toString()
                userFragmentBinding.tvIntroEmail.text = email.toString()
                userFragmentBinding.tvBio.text = teamBio.toString()

            } else {
                Toast.makeText(context, "User does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }

        val firebaseStorage = FirebaseStorage.getInstance().getReference("Users").child(uid)
        val localFile = File.createTempFile("tempImage", "jpg")
        firebaseStorage.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            userFragmentBinding.civIntroAvatar.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Toast.makeText(context, "Load Avatar Failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun signOut() {
        userFragmentBinding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(context, IntroActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun goUserInfoActivity() {
        userFragmentBinding.cvUserInfo.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            val teamName = userFragmentBinding.tvIntroName.text.toString()
            val teamEmail = userFragmentBinding.tvIntroEmail.text.toString()
            val teamBio = userFragmentBinding.tvBio.text.toString()
            intent.putExtra("name", teamName)
            intent.putExtra("email", teamEmail)
            intent.putExtra("bio", teamBio)
            startActivity(intent)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userFragmentBinding = FragmentUserBinding.inflate(inflater, container, false)
        return userFragmentBinding.root
    }

}