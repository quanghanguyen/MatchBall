package com.example.matchball.usersetting.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityUserAccountBinding
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.home.MainActivity
import com.example.matchball.usersetting.changepassword.ChangePasswordActivity

class UserAccountActivity : AppCompatActivity() {

    private lateinit var userAccountBinding: ActivityUserAccountBinding
    private val userAccountViewModel : UserAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userAccountBinding = ActivityUserAccountBinding.inflate(layoutInflater)
        setContentView(userAccountBinding.root)

        initEvents()
        initObserve()
        initEmailVerifyObserve()
        userAccountViewModel.handleLoadAvatar()
    }

    private fun initEvents() {
        emailVerifyCheck()
        back()
        changePassword()
        verifyEmail()
    }

    private fun initEmailVerifyObserve() {
        userAccountViewModel.verifyEmail.observe(this, {sendResult ->
            when (sendResult) {
                is UserAccountViewModel.VerifyEmail.EmailVerifySuccess -> {
                    Toast.makeText(this, sendResult.successMessage, Toast.LENGTH_SHORT).show()
                }
                is UserAccountViewModel.VerifyEmail.EmailVerifyFail -> {
                    Toast.makeText(this, sendResult.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initObserve() {
        userAccountViewModel.loadData.observe(this, { result ->
            when (result) {
                is UserAccountViewModel.UserData.LoadEmailSuccess -> {
                    userAccountBinding.email.text = result.email
                }
                is UserAccountViewModel.UserData.LoadAvatarSuccess -> {
                    userAccountBinding.avatar.setImageBitmap(result.image)
                }
                is UserAccountViewModel.UserData.LoadDataFail -> {
                }
            }
        })
    }

    private fun emailVerifyCheck() {
        if (authUser!!.isEmailVerified) {
            userAccountBinding.emailStatus.visibility = View.GONE
            userAccountBinding.verifyEmail.visibility = View.GONE
        }
    }

    private fun verifyEmail() {
        userAccountBinding.verifyEmail.setOnClickListener {
            userAccountViewModel.handleVerifyEmail()
        }
    }

    private fun changePassword() {
        userAccountBinding.changePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun back() {
        userAccountBinding.back.setOnClickListener {
            finish()
        }
    }
}