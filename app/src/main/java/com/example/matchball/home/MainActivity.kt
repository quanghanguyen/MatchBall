package com.example.matchball.home


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.matchball.R
import com.example.matchball.createrequest.RequestActivity
import com.example.matchball.databinding.ActivityMainBinding
import com.example.matchball.dashboard.matchlist.MatchListFragment
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.usersetting.useroverview.UserFragment

/* Sửa UI :
 5/ activity_user_account : avatar
 6/ activity_user_info : avatar
 7/ fragment_user */

// phải sign out ra vài lần email mới xác thực được
// khi đăng nhập bằng account mới thì vẫn load lại info account cũ
// check ảnh khi tạo user
// Xác thực email
// get ảnh của current User

//------------------------------

// Sửa các function deprecated : Map, UserInfoActivity
// Search
// Sort
// Notification

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val mainActivityViewModel : MainActivityViewModel by viewModels()
    private var name : String? = null
    private var phone : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initEvents()
        initObserve()
        initUI()
        mainActivityViewModel.handleReadUserData()
    }

    private fun initObserve() {
        mainActivityViewModel.userInfo.observe(this, {result ->
            when (result) {
                is MainActivityViewModel.UserData.LoadDataOk -> {
                    name = result.name
                    phone = result.phone
                }
            }
        })
    }

    private fun initUI() {
        mainBinding.bottomNavigationView.background = null
        mainBinding.bottomNavigationView.menu.getItem(2).isEnabled = true
        loadFragment(MatchListFragment.newInstance())
    }

    private fun initEvents() {
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
                R.id.request -> {
                    if (authUser!!.isEmailVerified && name != null && phone != null) {
                        startActivity(Intent(applicationContext, RequestActivity::class.java))
                        overridePendingTransition(0, 0)
                    } else {
                        Toast.makeText(this, "Please complete your Profile", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
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