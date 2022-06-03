package com.example.matchball.usersetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.R
import com.example.matchball.databinding.ActivityUserAccountBinding
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
        userAccountViewModel.handleLoadAvatar()
    }

    private fun initObserve() {
        userAccountViewModel.loadAvatar.observe(this, { result ->
            when (result) {
                is UserAccountViewModel.UserAvatar.LoadAvatarSuccess -> {
                    userAccountBinding.avatar.setImageBitmap(result.image)
                }
                is UserAccountViewModel.UserAvatar.LoadAvatarFail -> {
                }
            }

        })
    }

    private fun initEvents() {
        back()
        changePassword()
    }

    private fun changePassword() {
        userAccountBinding.changePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun back() {
        userAccountBinding.back.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}