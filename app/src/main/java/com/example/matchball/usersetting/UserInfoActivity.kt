package com.example.matchball.usersetting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityUserInfoBinding
import com.example.matchball.home.MainActivity
import com.google.firebase.auth.UserInfo

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userInfoBinding: ActivityUserInfoBinding
    private lateinit var imgUri : Uri
    private val userInfoViewModel : UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfoBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(userInfoBinding.root)

        initEvent()
        initLoadUserDataObserve()
        initSaveProfileObserve()
        userInfoViewModel.handleLoadUserData()
    }

    private fun initLoadUserDataObserve() {
        userInfoViewModel.loadUserData.observe(this, { loadResult ->
            when (loadResult) {
                is UserInfoViewModel.LoadUserData.LoadUserAvatarOk -> {
                    userInfoBinding.avatar.setImageBitmap(loadResult.avatar)
                }
                is UserInfoViewModel.LoadUserData.LoadUserAvatarFail -> {
                    //
                }
                is UserInfoViewModel.LoadUserData.LoadUserInfoSuccess -> {
                    userInfoBinding.teamNameEt.setText(loadResult.name)
                    userInfoBinding.teamBioEt.setText(loadResult.bio)
                    userInfoBinding.teamBirthdayEt.setText(loadResult.email)
                    userInfoBinding.teamPhoneEt.setText(loadResult.phone)
                }
                is UserInfoViewModel.LoadUserData.LoadUserInfoFail -> {
                    //
                }
            }
        })
    }

    private fun initEvent() {
        changeAvatar()
        saveProfile()
        back()
    }

    private fun initSaveProfileObserve() {
        userInfoViewModel.saveUserData.observe(this, {result ->
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

    private fun saveProfile() {
        userInfoBinding.btnSave.setOnClickListener {

//            if (userInfoBinding.teamNameEt.text.isNullOrEmpty()
//                || userInfoBinding.teamBioEt.text.isNullOrEmpty()
//                || userInfoBinding.teamBirthdayEt.text.isNullOrEmpty()
//                || userInfoBinding.teamPhoneEt.text.isNullOrEmpty())
//            {
//                Toast.makeText(this, "Please Enter all Fields", Toast.LENGTH_SHORT).show()
//            } else {
                val teamName = userInfoBinding.teamNameEt.text.toString()
                val teamBio = userInfoBinding.teamBioEt.text.toString()
                val teamBirthday = userInfoBinding.teamBirthdayEt.text.toString()
                val teamPhone = userInfoBinding.teamPhoneEt.text.toString()

                userInfoViewModel.handleSaveUserData(teamName, teamBio, teamBirthday, teamPhone)
        }
    }

    private fun changeAvatar() {
        userInfoBinding.avatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)

        }
    }

    private fun back() {
        userInfoBinding.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            userInfoViewModel.setUri(imgUri)
            userInfoBinding.avatar.setImageURI(imgUri)
        }
    }
}