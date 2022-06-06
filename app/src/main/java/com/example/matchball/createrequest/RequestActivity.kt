package com.example.matchball.createrequest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isEmpty
import com.example.matchball.home.MainActivity
import com.example.matchball.databinding.ActivityRequestBinding
import java.util.*

class RequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var requestBinding: ActivityRequestBinding
    private val requestViewModel : RequestViewModel by viewModels()

    private var teamName : String? = null
    private var teamPhone : String? = null

    private val peopleOptions = arrayOf(4, 5, 6, 7, 8, 9, 10, 11)
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay: Int = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBinding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(requestBinding.root)

        initEvents()
        initSendRequestObserve()
    }

    private fun initEvents() {
        timeSelect()
        pitchSelect()
        peopleSelect()
        sendRequest()
    }

    private fun initSendRequestObserve() {
        requestViewModel.sendRequest.observe(this, {sendRequestResult ->
            when (sendRequestResult) {
                is RequestViewModel.SendRequestResult.GetResultOk -> {
                    teamName = sendRequestResult.teamName
                    teamPhone = sendRequestResult.teamPhone
                }
                is RequestViewModel.SendRequestResult.GetResultError -> {
                    Toast.makeText(this, sendRequestResult.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is RequestViewModel.SendRequestResult.SendResultOk -> {
                    Toast.makeText(this, sendRequestResult.successMessage, Toast.LENGTH_SHORT).show()
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is RequestViewModel.SendRequestResult.SendResultError -> {
                    Toast.makeText(this, sendRequestResult.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun sendRequest() {
        val locationReceived = intent.getStringExtra("location")
        val latitudeReceived = intent.getStringExtra("latitude")
        val longitudeReceived = intent.getStringExtra("longitude")

        requestBinding.btnSend.setOnClickListener {
            val matchTime = requestBinding.timeEt.text.toString()
            val matchPeople = requestBinding.spnPeople.selectedItem.toString()
            val matchNote = requestBinding.noteEt.text.toString()

            teamName?.let { it1 ->
                teamPhone?.let { it2 ->
                    requestViewModel.handleSendRequest(
                        it1, matchTime, locationReceived,
                        latitudeReceived, longitudeReceived, matchPeople, matchNote, it2
                    )
                }
            }
        }
    }

    private fun locationReceived() {
        val locationReceived = intent.getStringExtra("location")
        locationReceived?.let {
            with(requestBinding) {
                pitchEt.setText("$locationReceived")
            }
        }
    }

    private fun pitchSelect() {
        requestBinding.pitchSelect.setOnClickListener {
            intent = Intent(this, RequestMapsActivity::class.java)
            startActivity(intent)
        }
        locationReceived()
    }

    private fun peopleSelect() {
        requestBinding.spnPeople.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, peopleOptions)
//        requestBinding.spnPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
    }

    private fun timeSelect() {
        requestBinding.timeSelect.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DATE)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        requestBinding.timeEt.setText("$myHour:$myMinute ($myDay/$myMonth/$myYear)")
    }
}