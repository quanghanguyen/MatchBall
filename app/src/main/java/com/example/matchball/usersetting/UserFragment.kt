package com.example.matchball.usersetting

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.matchball.*
import com.example.matchball.databinding.FragmentUserBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.firebaseconnection.StorageConnection
import com.example.matchball.signin.IntroActivity
import com.example.matchball.yourmatchrequest.YourRequestActivity
import java.io.File

class UserFragment : Fragment() {

    private lateinit var userFragmentBinding : FragmentUserBinding
    private val userFragmentViewModel : UserFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readUserInfo()
        readUserImage()
        goUserInfoActivity()
        yourRequestClick()
        signOut()

    }

    private fun readUserInfo() {

        userFragmentViewModel.readUserInfo.observe(this, Observer { result ->
            when (result) {
                is UserFragmentViewModel.UserInfo.ReadSuccess -> {
                    userFragmentBinding.tvIntroName.text = result.teamName
                    userFragmentBinding.tvIntroEmail.text = result.email
                    userFragmentBinding.tvBio.text = result.teamBio
                    userFragmentBinding.tvPhone.text = result.phone
                }

                is UserFragmentViewModel.UserInfo.ReadError -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        userFragmentViewModel.handleReadUserInfo()
    }

    private fun readUserImage() {
        userFragmentViewModel.readUserImage.observe(this, Observer { result ->

            when (result) {
                is UserFragmentViewModel.UserImage.ReadSuccess -> {
                    userFragmentBinding.civIntroAvatar.setImageBitmap(result.image)
                }
                is UserFragmentViewModel.UserImage.ReadError -> {
                    // Do nothing
                }
            }
        })

        userFragmentViewModel.handleReadUserImage()

    }

    private fun yourRequestClick() {
        userFragmentBinding.cvYourRequest.setOnClickListener {
            val intent = Intent(requireContext(), YourRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        userFragmentBinding.btnSignOut.setOnClickListener {
            AuthConnection.auth.signOut()
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
            val teamPhone = userFragmentBinding.tvPhone.text.toString()

            intent.putExtra("name", teamName)
            intent.putExtra("email", teamEmail)
            intent.putExtra("bio", teamBio)
            intent.putExtra("phone", teamPhone)
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