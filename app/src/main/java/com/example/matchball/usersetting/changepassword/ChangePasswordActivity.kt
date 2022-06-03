package com.example.matchball.usersetting.changepassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matchball.R
import com.example.matchball.databinding.ActivityChangePasswordBinding
import com.example.matchball.usersetting.UserAccountActivity

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        initEvents()
    }

    private fun initEvents() {
        back()
    }

    private fun back() {
        changePasswordBinding.back.setOnClickListener {
            val intent = Intent(this, UserAccountActivity::class.java)
            startActivity(intent)
        }
    }
}