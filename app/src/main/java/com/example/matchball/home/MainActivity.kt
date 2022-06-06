package com.example.matchball.home


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.matchball.R
import com.example.matchball.createrequest.RequestActivity
import com.example.matchball.databinding.ActivityMainBinding
import com.example.matchball.dashboard.MatchListFragment
import com.example.matchball.usersetting.UserFragment

// chưa lấy đưuọc teamname + phonename
// sửa lại màu trong request activity
// get email và ảnh của current User
// Check Intent trên Google Maps
// Xác thực email
// Chủ sân
// Check lại cái lưu ảnh
// Click on Map
// Check lại Time Request List
// MVVM
// Object

//------------

// Setting Activity
// Lên ý tưởng về phân quyền User
// Xây dựng Database
// Notification
// Search


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initEvents()
        initUI()
    }

    private fun initUI() {
        mainBinding.bottomNavigationView.background = null
        mainBinding.bottomNavigationView.menu.getItem(2).isEnabled = true
        loadFragment(MatchListFragment.newInstance())
    }

    private fun initEvents() {
        createRequest()
        selectBottomNavigation()
    }

    private fun selectBottomNavigation() {
        mainBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment : Fragment
            when (item.itemId) {
                R.id.home -> {
                    fragment = MatchListFragment()
                    loadFragment(fragment)
                    true
                }
                R.id.person -> {
                    fragment = UserFragment()
                    loadFragment(fragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun createRequest() {
        mainBinding.fab.setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

// Print KeyHash Syntax
//
//    private fun printKeyHash() {
//        try {
//            val info : PackageInfo = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md : MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val hashKey = String(Base64.encode(md.digest(), 0))
//                Log.d(TAG, "printHaskKey() Hask Key: $hashKey")
//            }
//        } catch (e : NoSuchAlgorithmException) {
//            Log.d(TAG, "printHashKey()", e)
//        } catch (e: Exception) {
//            Log.d(TAG, "printHaskKey", e)
//        }
//    }

}