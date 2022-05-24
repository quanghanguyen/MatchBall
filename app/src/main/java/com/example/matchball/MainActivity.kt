package com.example.matchball


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.matchball.databinding.ActivityMainBinding
import com.example.matchball.fragment.ListFragment
import com.example.matchball.fragment.UserFragment

// coi Intern Malaysia
// Lấy Team People
// Lên ý tưởng về phân quyền User
// Xây dựng Database
// Setting Activity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.bottomNavigationView.background = null
        mainBinding.bottomNavigationView.menu.getItem(2).isEnabled = true

        mainBinding.fab.setOnClickListener {
            goRequest()
        }

        //List fragment is first
        loadFragment(ListFragment.newInstance())

        mainBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment : Fragment
            when (item.itemId) {

                R.id.home -> {
                    fragment = ListFragment()
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

    private fun goRequest() {
        val intent = Intent(this, RequestActivity::class.java)
        startActivity(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

// Print KeyHash Syntax

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