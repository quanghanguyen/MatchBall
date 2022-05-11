package com.example.matchball

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64.getEncoder
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printKeyHash()


    }

    private fun printKeyHash() {
        try {
            val info : PackageInfo = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md : MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.d(TAG, "printHaskKey() Hask Key: $hashKey")
            }
        } catch (e : NoSuchAlgorithmException) {
            Log.d(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.d(TAG, "printHaskKey", e)
        }
    }
}