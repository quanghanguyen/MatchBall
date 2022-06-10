package com.example.matchball.usersetting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.matchball.*
import com.example.matchball.databinding.FragmentUserBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.AuthConnection.authUser
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
        checkEmailVerified()
        signOut()
        goUserInfoActivity()
        goYourRequestActivity()
        goUserAccount()
    }

    private fun initObserve() {
        userFragmentViewModel.readUser.observe(this, {result ->
            when (result) {
                is UserFragmentViewModel.UserData.ReadAvatarSuccess -> {
                    userFragmentBinding.civIntroAvatar.setImageBitmap(result.image)
                }
                is UserFragmentViewModel.UserData.LoadUserInfo -> {
                    userFragmentBinding.tvIntroEmail.text = result.email
                }
                is UserFragmentViewModel.UserData.ReadAvatarError -> {
                }
                is UserFragmentViewModel.UserData.ReadInfoSuccess -> {
                    if (result.teamName == "") {
                        userFragmentBinding.tvIntroName.text = "Unnamed"
                    } else {
                        userFragmentBinding.tvIntroName.text = result.teamName
                        userFragmentBinding.tvIntroName.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                    if (result.teamBio == "") {
                        userFragmentBinding.bio.text = "No Bio"
                    } else {
                        userFragmentBinding.bio.text = result.teamBio
                        userFragmentBinding.bio.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                    if (result.phone == "") {
                        userFragmentBinding.phone.text = "No phone number"
                    } else {
                        userFragmentBinding.phone.text = result.phone
                        userFragmentBinding.phone.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                    if (result.birthday == "") {
                        userFragmentBinding.birthday.text = "No date of birth"
                    } else {
                        userFragmentBinding.birthday.text = result.birthday
                        userFragmentBinding.birthday.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
                is UserFragmentViewModel.UserData.ReadInfoError -> {
                }
            }
        })
    }

    private fun checkEmailVerified() {
        if (authUser!!.isEmailVerified) {
            userFragmentBinding.emailVerified.visibility = View.GONE
        }
    }

    private fun goYourRequestActivity() {
        userFragmentBinding.requestsManager.setOnClickListener {
            val intent = Intent(requireContext(), YourRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        userFragmentBinding.signOut.setOnClickListener {
            AuthConnection.auth.signOut()
            val intent = Intent(context, GoogleSignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun goUserInfoActivity() {
        userFragmentBinding.editInformation.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goUserAccount() {
        userFragmentBinding.userAccount.setOnClickListener {
            val intent = Intent(context, UserAccountActivity::class.java)
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