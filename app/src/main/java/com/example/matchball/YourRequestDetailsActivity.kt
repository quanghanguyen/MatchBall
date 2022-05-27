package com.example.matchball

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.matchball.databinding.ActivityYourRequestDetailsBinding
import com.example.matchball.map.EditMapsActivity
import com.example.matchball.map.JoinMapsActivity
import com.example.matchball.model.MatchRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class YourRequestDetailsActivity : AppCompatActivity() {

    private lateinit var requestDetailsBinding: ActivityYourRequestDetailsBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var reference : DatabaseReference
    private val peopleOptions = arrayOf(4, 5, 6, 7, 8, 9, 10, 11)

    companion object
    {
        private const val KEY_DATA = "request_details_data"
        fun startRequestDetails(context: Context, data : MatchRequest)
        {
            context.startActivity(Intent(context, YourRequestDetailsActivity::class.java).apply {
                putExtra(KEY_DATA, data)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestDetailsBinding = ActivityYourRequestDetailsBinding.inflate(layoutInflater)
        setContentView(requestDetailsBinding.root)

        intent?.let { bundle ->
            val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

            with(requestDetailsBinding){
                tvTeamName.text = requests!!.teamName
                tvTime.text = requests.time
                tvPitch.text = requests.pitch
                tvNote.text = requests.note
                tvPeople.text = requests.people
            }

        }

        spnPeopleClick()
        openPitchMap()
        selectMap()
//        btnEditClick()
        ivBackClick()
        tvDoneClick()

    }

    private fun selectMap() {
        requestDetailsBinding.btnPitchSelect.setOnClickListener {
            val intent = Intent(this, EditMapsActivity::class.java)
            val locationReceived = intent.getStringExtra("location")
            intent.let {
                requestDetailsBinding.tvPitch.text = locationReceived
            }
            startActivity(intent)
        }
    }

    private fun spnPeopleClick() {
        requestDetailsBinding.spnPeople.adapter = ArrayAdapter<Int>(this, R.layout.simple_list_item_1, peopleOptions)
        requestDetailsBinding.spnPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun tvDoneClick() {

    }

    private fun ivBackClick() {
        requestDetailsBinding.ivBack.setOnClickListener {
            val intent = Intent(this, YourRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPitchMap() {
        requestDetailsBinding.tvPitch.setOnClickListener {
            intent?.let { bundle ->
                val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

                val pitchLatitude = requests?.pitchLatitude?.toDouble()
                val pitchLongitude = requests?.pitchLongitude?.toDouble()
                val pitchName = requests?.pitch.toString()

                val intent = Intent(this, JoinMapsActivity::class.java)
                intent.putExtra("pitchLatitude", pitchLatitude)
                intent.putExtra("pitchLongitude", pitchLongitude)
                intent.putExtra("pitchName", pitchName)
                startActivity(intent)
            }
        }
    }

//    private fun btnEditClick() {
//        requestDetailsBinding.btnEdit.setOnClickListener {
//            with(requestDetailsBinding){
//                btnPitchSelect.visibility = View.VISIBLE
//                btnTimeSelect.visibility = View.VISIBLE
//                tvNote.visibility = View.GONE
//                tvPeople.visibility = View.GONE
//                edtNote.visibility = View.VISIBLE
//                spnPeople.visibility = View.VISIBLE
//                // finish()
//            }
//        }
//    }
}