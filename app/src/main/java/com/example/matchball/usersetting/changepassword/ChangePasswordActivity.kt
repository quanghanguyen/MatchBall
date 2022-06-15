package com.example.matchball.usersetting.changepassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityChangePasswordBinding
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.signin.password.SignInActivity
import com.example.matchball.usersetting.account.UserAccountActivity

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private val changePasswordViewModel : ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        initEvents()
        initObserve()
    }

    private fun initObserve() {
        changePasswordViewModel.changePasswordResult.observe(this, {result ->
            when (result) {
                is ChangePasswordViewModel.ChangePassWordResult.ResultOk -> {
                    Toast.makeText(this, result.successMessage, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }
                is ChangePasswordViewModel.ChangePassWordResult.ResultError -> {
                    Toast.makeText(this, result.errorError, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initEvents() {
        back()
        changePassword()
    }

    private fun changePassword() {
        changePasswordBinding.save.setOnClickListener {
            if (changePasswordBinding.oldPassEt.text!!.isEmpty()&&
                    changePasswordBinding.newPassEt.text!!.isEmpty()&&
                    changePasswordBinding.confirmNewPassEt.text!!.isEmpty()) {
                Toast.makeText(this, "Please Enter all Fields", Toast.LENGTH_SHORT).show()
            } else {
                val email = authUser?.email
                val oldPassword = changePasswordBinding.oldPassEt.text.toString()
                val newPassword = changePasswordBinding.newPassEt.text.toString()

                changePasswordViewModel.handleChangePassWord(email!!, oldPassword, newPassword)
                }
            }
        }

    private fun back() {
        changePasswordBinding.back.setOnClickListener {
            finish()
        }
    }
}