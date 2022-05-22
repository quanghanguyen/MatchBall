package com.example.matchball

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matchball.databinding.ActivityJoinBinding
import com.example.matchball.model.MatchRequest

class JoinActivity : AppCompatActivity() {

    private lateinit var joinBinding: ActivityJoinBinding

    companion object
    {
        private const val KEY_DATA = "request_data"
        fun startDetails(context: Context, data : MatchRequest)
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

        intent?.let { bundle ->
            val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

            val pitchLatitude = requests?.pitchLatitude.toString()
            val pitchLongitude = requests?.pitchLongitude.toString()
            val pitchName = requests?.pitch.toString()



            with(joinBinding){
                tvJMTime.text = requests!!.time
                tvJMPitch.text = requests.pitch
                tvJMNote.text = requests.note
            }
        }

        openPitchMap()

    }

    private fun openPitchMap() {
        joinBinding.tvJMPitch.setOnClickListener {
            val intent = Intent(this, JoinMapsActivity::class.java)
            intent.putExtra("pitchLatitude", pitch)
            startActivity(intent)
        }
    }
}