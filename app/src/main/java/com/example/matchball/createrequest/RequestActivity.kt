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
import com.example.matchball.R
import com.example.matchball.home.MainActivity
import com.example.matchball.databinding.ActivityRequestBinding
import java.util.*

class RequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var requestBinding: ActivityRequestBinding
    private val requestViewModel : RequestViewModel by viewModels()

    private var teamName : String? = null
    private var teamPhone : String? = null

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
                    teamName = sendRequestResult.name
                    teamPhone = sendRequestResult.phone
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
            val matchPeople = requestBinding.peopleSelect.toString()
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
        val peopleOptions = resources.getStringArray(R.array.amount_people)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_items, peopleOptions)
        requestBinding.peopleSelect.setAdapter(arrayAdapter)
    }

    private fun timeSelect() {
        requestBinding.timeSelect.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            requestViewModel.day = calendar.get(Calendar.DATE)
            requestViewModel.month = calendar.get(Calendar.MONTH)
            requestViewModel.year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(this, this, requestViewModel.year,
                requestViewModel.month, requestViewModel.day)
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        requestViewModel.myDay = day
        requestViewModel.myYear = year
        requestViewModel.myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        requestViewModel.hour = calendar.get(Calendar.HOUR)
        requestViewModel.minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, requestViewModel.hour,
            requestViewModel.minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        requestViewModel.myHour = hourOfDay
        requestViewModel.myMinute = minute
        requestBinding.timeEt.setText("${requestViewModel.myHour}:${requestViewModel.myMinute} (${requestViewModel.myDay}/${requestViewModel.myMonth}/${requestViewModel.myYear})")
    }
}