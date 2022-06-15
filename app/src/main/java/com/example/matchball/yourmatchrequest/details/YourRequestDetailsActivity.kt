package com.example.matchball.yourmatchrequest.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.matchball.databinding.ActivityYourRequestDetailsBinding
import com.example.matchball.model.MatchRequest
import com.example.matchball.yourmatchrequest.list.YourRequestActivity

class YourRequestDetailsActivity : AppCompatActivity() {

    private lateinit var requestDetailsBinding : ActivityYourRequestDetailsBinding
    private val yourRequestDetailsViewModel : YourRequestDetailsViewModel by viewModels()
    companion object {
        private const val KEY_DATA = "match_data"
        fun startDetails(context : Context, data : MatchRequest) {
            context.startActivity(Intent(context, YourRequestDetailsActivity::class.java).apply {
                putExtra(KEY_DATA, data)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestDetailsBinding = ActivityYourRequestDetailsBinding.inflate(layoutInflater)
        setContentView(requestDetailsBinding.root)

        initEvents()
//        initObserve()
//        yourRequestDetailsViewModel.handleLoadData()

    }

//    private fun initObserve() {
//        yourRequestDetailsViewModel.loadData.observe(this, { result ->
//            when (result) {
//                is YourRequestDetailsViewModel.LoadData.ResultOk -> {
//                    requestDetailsBinding.timeEt.setText(result.time)
//                    requestDetailsBinding.pitchEt.setText(result.location)
//                    requestDetailsBinding.noteEt.setText(result.note)
//                    requestDetailsBinding.peopleSelect.setText(result.people)
//                }
//            }
//        })
//    }

    private fun initEvents() {
        back()
        getIntentData()
    }

    private fun getIntentData() {
        intent?.let { bundle ->
            val matchData = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)
            with(requestDetailsBinding) {
                timeEt.setText(matchData?.time)
                pitchEt.setText(matchData?.pitch)
                noteEt.setText(matchData?.note)
                peopleSelect.setText(matchData?.people)
            }
        }
    }

    private fun back() {
        requestDetailsBinding.back.setOnClickListener {
            finish()
        }
    }

//    private fun selectMap() {
//        requestDetailsBinding.btnPitchSelect.setOnClickListener {
//            val intent = Intent(this, ShowRequestMapActivity::class.java)
//            val locationReceived = intent.getStringExtra("location")
//            intent.let {
//                requestDetailsBinding.tvPitch.text = locationReceived
//            }
//            startActivity(intent)
//        }
//    }
//
//    private fun spnPeopleClick() {
//        requestDetailsBinding.spnPeople.adapter = ArrayAdapter<Int>(this, R.layout.simple_list_item_1, peopleOptions)
//        requestDetailsBinding.spnPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//        }
//    }
//
//    private fun tvDoneClick() {
//
//    }
//
//    private fun ivBackClick() {
//        requestDetailsBinding.ivBack.setOnClickListener {
//            val intent = Intent(this, YourRequestActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun openPitchMap() {
//        requestDetailsBinding.tvPitch.setOnClickListener {
//            intent?.let { bundle ->
//                val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)
//
//                val pitchLatitude = requests?.pitchLatitude?.toDouble()
//                val pitchLongitude = requests?.pitchLongitude?.toDouble()
//                val pitchName = requests?.pitch.toString()
//
//                val intent = Intent(this, JoinMapsActivity::class.java)
//                intent.putExtra("pitchLatitude", pitchLatitude)
//                intent.putExtra("pitchLongitude", pitchLongitude)
//                intent.putExtra("pitchName", pitchName)
//                startActivity(intent)
//            }
//        }
//    }

}