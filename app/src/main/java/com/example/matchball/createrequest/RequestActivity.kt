package com.example.matchball.createrequest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import com.example.matchball.home.MainActivity
import com.example.matchball.databinding.ActivityRequestBinding
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.model.MatchRequest
import java.util.*

class RequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var requestBinding: ActivityRequestBinding
    private val peopleOptions = arrayOf(4, 5, 6, 7, 8, 9, 10, 11)

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBinding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(requestBinding.root)

        timePick()
        pitchPick()
        locationReceive()
        sendRequest()
        peopleSelect()

    }

    private fun sendRequest() {

        val uid = AuthConnection.auth.currentUser!!.uid

        DatabaseConnection.databaseReference.getReference("Users").child(uid).get().addOnSuccessListener {
            val teamNameReceived =  it.child("teamName").value.toString()
            val teamPhoneReceived = it.child("phone").value.toString()

            requestBinding.btnSend.setOnClickListener {

                if (requestBinding.tvPickTime.text.isEmpty() || requestBinding.tvPickPitch.text.isEmpty()
                    || requestBinding.spnPeople.isEmpty()) {
                    Toast.makeText(this, "Missing Information Request", Toast.LENGTH_SHORT).show()
                } else {

                    val locationReceived = intent.getStringExtra("location")
                    val latitudeReceived = intent.getStringExtra("latitude")
                    val longitudeReceived = intent.getStringExtra("longitude")

                    val matchTime = requestBinding.tvPickTime.text.toString()
                    val matchPeople = requestBinding.spnPeople.selectedItem.toString()
                    val matchNote = requestBinding.edtNote.text.toString()

                    val matchRequest = MatchRequest(teamNameReceived, matchTime, locationReceived, latitudeReceived, longitudeReceived,
                        matchPeople, matchNote, teamPhoneReceived)

                    if (uid != null) {
                        DatabaseConnection.databaseReference.getReference("MatchRequest").push().setValue(matchRequest).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Send Request Success", Toast.LENGTH_SHORT).show()
                                intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Failed to Send Request", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Load TeamName", Toast.LENGTH_SHORT).show()
        }

        }

    private fun locationReceive() {
        val locationReceived = intent.getStringExtra("location")
        intent.let {
            with(requestBinding) {
                tvPickPitch.setText("$locationReceived")
            }
        }
    }

    private fun pitchPick() {
        requestBinding.btnLocationSelect.setOnClickListener {
            intent = Intent(this, RequestMapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun peopleSelect() {
        requestBinding.spnPeople.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, peopleOptions)
        requestBinding.spnPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val peopleSelected : String = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun timePick() {
        requestBinding.btnSelect.setOnClickListener {
//            getDateTimeCalendar()
//
//            DatePickerDialog(this, this, year, month, day).show()

            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month,day)
            datePickerDialog.show()

        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
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
//        requestBinding.tvPickTime.text = "Year: " + myYear + "\n" + "Month: " + myMonth + "\n" + "Day: " + myDay + "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        requestBinding.tvPickTime.text = "$myHour:$myMinute ($myDay/$myMonth/$myYear)"
    }
}