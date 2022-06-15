package com.example.matchball.usersetting.userinfo

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityUserInfoBinding
import com.example.matchball.home.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userInfoBinding: ActivityUserInfoBinding
    private lateinit var imgUri : Uri
    private val userInfoViewModel : UserInfoViewModel by viewModels()
    val cal: Calendar = Calendar.getInstance()

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
        selectBirthday()
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

    private fun selectBirthday() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        userInfoBinding.birthdaySelect.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        userInfoBinding.teamBirthdayEt.setText(sdf.format(cal.time))
    }

    private fun saveProfile() {
        userInfoBinding.btnSave.setOnClickListener {
                val teamName = userInfoBinding.teamNameEt.text.toString()
                val teamBio = userInfoBinding.teamBioEt.text.toString()
                val teamBirthday = userInfoBinding.teamBirthdayEt.text.toString()
                val teamPhone = userInfoBinding.teamPhoneEt.text.toString()

                userInfoViewModel.handleSaveUserData(teamName, teamBio, teamBirthday, teamPhone)
        }
    }

    private fun changeAvatar() {
        userInfoBinding.avatar.setOnClickListener {
//            val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
//                when (result.resultCode) {
//                    RESULT_OK -> {
//                        val intent = Intent()
//                        intent.type = "image/*"
//                        intent.action = Intent.ACTION_GET_CONTENT
//                    }
//                }
//            }

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)
        }
    }

    private fun back() {
        userInfoBinding.back.setOnClickListener {
            finish()
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