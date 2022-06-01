package com.example.matchball.joinmatch

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.matchball.databinding.ActivityJoinBinding
import com.example.matchball.home.MainActivity
import com.example.matchball.model.MatchRequest

class JoinActivity : AppCompatActivity() {

    private lateinit var joinBinding: ActivityJoinBinding

    companion object
    {
        private const val KEY_DATA = "request_data"
        fun startDetails(context: Context, data : MatchRequest?)
        {
            context.startActivity(Intent(context, JoinActivity::class.java).apply {
                putExtra(KEY_DATA, data)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        joinBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(joinBinding.root)

        initIntentData()
        initEvent()
    }

    private fun initIntentData() {
        intent?.let { bundle ->
            val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

            with(joinBinding){
                tvJMTeamName.text = requests!!.teamName
                tvJMTime.text = requests.time
                tvJMPitch.text = requests.pitch
                tvJMNote.text = requests.note
                tvJMPhone.text = requests.phone
            }
        }
    }

    private fun initEvent() {
        sendJoinRequest()
        openPitchMap()
    }

    private fun sendJoinRequest() {
        joinBinding.btnJoin.setOnClickListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPitchMap() {
        joinBinding.tvJMPitch.setOnClickListener {

            intent?.let { bundle ->
                val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

                val pitchLatitude = requests?.pitchLatitude?.toDouble()
                val pitchLongitude = requests?.pitchLongitude?.toDouble()
                val pitchName = requests?.pitch.toString()

//                val intent = Intent(this, JoinMapsActivity::class.java)
//                intent.putExtra("pitchLatitude", pitchLatitude)
//                intent.putExtra("pitchLongitude", pitchLongitude)
//                intent.putExtra("pitchName", pitchName)
//                startActivity(intent)

                val gmmIntentUri = Uri.parse("geo:$pitchLatitude, $pitchLongitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }
}