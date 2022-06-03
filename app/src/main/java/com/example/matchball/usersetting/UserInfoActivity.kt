package com.example.matchball.usersetting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityUserInfoBinding
import com.example.matchball.home.MainActivity

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userInfoBinding: ActivityUserInfoBinding
    private lateinit var imgUri : Uri
    private val userInfoViewModel : UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(userInfoBinding.root)

        initEvent()
        initLoadAvatarObserve()
        initSaveProfileObserve()
        userInfoViewModel.handleLoadAvatar()
    }

    private fun initLoadAvatarObserve() {
        userInfoViewModel.loadAvatar.observe(this, { loadResult ->
            when (loadResult) {
                is UserInfoViewModel.LoadAvatar.LoadAvatarOk -> {
                    userInfoBinding.civAvatar.setImageBitmap(loadResult.avatar)
                }
                is UserInfoViewModel.LoadAvatar.LoadAvatarFail -> {
                    Toast.makeText(this, loadResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initEvent() {
        getIntentData()
        changeAvatar()
        saveProfile()
    }

    private fun initSaveProfileObserve() {
        userInfoViewModel.saveUserData.observe(this, { result ->
            when (result) {
                is UserInfoViewModel.SaveUserData.SaveOk -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is UserInfoViewModel.SaveUserData.SaveFail -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getIntentData() {
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

    private fun saveProfile() {
        userInfoBinding.btnSave.setOnClickListener {
            val teamName = userInfoBinding.edtTeamName.text.toString()
            val teamBio = userInfoBinding.edtBio.text.toString()
            val teamEmail = userInfoBinding.edtEmail.text.toString()
            val teamPhone = userInfoBinding.edtPhone.text.toString()

            userInfoViewModel.handleSaveUserData(teamName, teamBio, teamEmail, teamPhone)

        }
    }

    private fun changeAvatar() {
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
}