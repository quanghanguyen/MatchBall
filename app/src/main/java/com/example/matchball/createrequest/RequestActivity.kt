package com.example.matchball.createrequest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
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
//    private var locationReceived : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBinding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(requestBinding.root)

        initEvents()
        initGetNameAndPhone()
        initSendRequestObserve()
        requestViewModel.handleNameAndPhone()
    }

    private fun initEvents() {
        timeSelect()
        pitchSelect()
        peopleSelect()
        sendRequest()
        back()
    }

    private fun initGetNameAndPhone() {
        requestViewModel.getNameAndPhone.observe(this, {getResult ->
            when (getResult) {
                is RequestViewModel.GetNameAndPhone.GetResultOk -> {
                    teamName = getResult.name
                    teamPhone = getResult.phone
                }
                is RequestViewModel.GetNameAndPhone.GetResultError -> {
                    Toast.makeText(this, getResult.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initSendRequestObserve() {
        requestViewModel.sendRequest.observe(this, {sendRequestResult ->
            when (sendRequestResult) {
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
            if (requestBinding.timeEt.text.isNullOrEmpty()
                || requestBinding.pitchEt.text.isNullOrEmpty()
                || requestBinding.peopleSelect.text.isNullOrEmpty()
                || latitudeReceived == null
                || longitudeReceived == null
                    ) {
                Toast.makeText(this, "Please Check Your Request Again", Toast.LENGTH_SHORT).show()
            } else {
                val matchTime = requestBinding.timeEt.text.toString()
                val matchPeople = requestBinding.peopleSelect.text.toString()
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
    }

    private fun back() {
        requestBinding.back.setOnClickListener {
            finish()
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
//            startActivity(intent)
            startActivityForResult(intent, 1)
        }
        locationReceived()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                var locationReceived = data?.getStringExtra("location")
                val latitudeReceived = data?.getStringExtra("latitude")
                val longitudeReceived = data?.getStringExtra("longitude")
                requestBinding.pitchEt.setText((data?.getStringExtra("location")))
            }
        }
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
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        requestViewModel.myDay = day
        requestViewModel.myYear = year
        requestViewModel.myMonth = month + 1
        val calendar : Calendar = Calendar.getInstance()
        requestViewModel.hour = calendar.get(Calendar.HOUR)
        requestViewModel.minute = calendar.get(Calendar.MINUTE)
        var timePickerDialog = TimePickerDialog(this, this, requestViewModel.hour,
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