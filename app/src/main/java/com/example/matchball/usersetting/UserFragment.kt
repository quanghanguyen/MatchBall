package com.example.matchball.usersetting

import android.content.Intent
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
import com.example.matchball.signin.GoogleSignInActivity
import com.example.matchball.yourmatchrequest.YourRequestActivity

class UserFragment : Fragment() {

    private lateinit var userFragmentBinding : FragmentUserBinding
    private val userFragmentViewModel : UserFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        initEvent()
        userFragmentViewModel.handleReadUser()

    }

    private fun initEvent() {
        signOut()
        goUserInfoActivity()
        goYourRequestActivity()
    }

    private fun initObserve() {
        userFragmentViewModel.readUser.observe(this, {result ->
            when (result) {
                is UserFragmentViewModel.UserData.Loading -> {
                    userFragmentBinding.progressBar.visibility = View.GONE
                    userFragmentBinding.cvUserInfo.visibility = View.VISIBLE
                    userFragmentBinding.cvYourRequest.visibility = View.VISIBLE
                    userFragmentBinding.cvSetting.visibility = View.VISIBLE
                    userFragmentBinding.btnSignOut.visibility = View.VISIBLE
                }
                is UserFragmentViewModel.UserData.ReadAvatarSuccess -> {
                    userFragmentBinding.civIntroAvatar.setImageBitmap(result.image)
                }
                is UserFragmentViewModel.UserData.ReadAvatarError -> {
                }
                is UserFragmentViewModel.UserData.ReadInfoSuccess -> {
                    userFragmentBinding.tvIntroName.text = result.teamName
                    userFragmentBinding.tvIntroEmail.text = result.email
                    userFragmentBinding.tvBio.text = result.teamBio
                    userFragmentBinding.tvPhone.text = result.phone
                }
                is UserFragmentViewModel.UserData.ReadInfoError -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun goYourRequestActivity() {
        userFragmentBinding.cvYourRequest.setOnClickListener {
            val intent = Intent(requireContext(), YourRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        userFragmentBinding.btnSignOut.setOnClickListener {
            AuthConnection.auth.signOut()
            val intent = Intent(context, GoogleSignInActivity::class.java)
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
    ): View {
        userFragmentBinding = FragmentUserBinding.inflate(inflater, container, false)
        return userFragmentBinding.root
    }
}