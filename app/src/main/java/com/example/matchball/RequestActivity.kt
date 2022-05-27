package com.example.matchball

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.matchball.databinding.ActivityRequestBinding
import com.example.matchball.firebasedatabase.DatabaseConnection
import com.example.matchball.map.MapsActivity
import com.example.matchball.model.MatchRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var requestBinding: ActivityRequestBinding
    private lateinit var auth : FirebaseAuth
//    private lateinit var databaseReference : DatabaseReference
    private lateinit var reference : DatabaseReference
    private val peopleOptions = arrayOf(4, 5, 6, 7, 8, 9, 10, 11)

    //Notification
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAWkPGrQY:APA91bGf56l6anVyOwwUjCH87Jgj4p4whfluhk19N8mqlInCJwUBZnJN1WHQcDEFzbz0G-amPJQ_lBGcFmAZQTf91_4xYmphCGuqzYkGAm9VzGPMbh3bJadv0avp8je0pjokN4MUdm8a"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

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
//        DatabaseConnection.databaseReference
        auth = FirebaseAuth.getInstance()
        //databaseReference = FirebaseDatabase.getInstance().getReference("MatchRequest")

        timePick()
        pitchPick()
        locationReceive()
        sendRequest()
        peopleSelect()

        //Notification
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_topic")

    }

    private fun sendRequest() {

        val uid = auth.currentUser!!.uid

        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(uid).get().addOnSuccessListener {
            val teamNameReceived =  it.child("teamName").value.toString()

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
                        matchPeople, matchNote)

                    if (uid != null) {
                        DatabaseConnection.databaseReference.push().setValue(matchRequest).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Send Request Success", Toast.LENGTH_SHORT).show()
                                intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Failed to Send Request", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    if (!TextUtils.isEmpty(requestBinding.tvPickPitch.text)) {
                        val topic = "/topics/Enter_topic"

                        val notification = JSONObject()
                        val notifcationBody = JSONObject()

                        try {
                            notifcationBody.put("title", "Match Request Notification")
                            notifcationBody.put("message", requestBinding.tvPickPitch.text)
                            notification.put("to", topic)
                            notification.put("data", notifcationBody)
                            Log.e("TAG", "try")
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }

                        sendNotification(notification)
                    }
                }

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Load TeamName", Toast.LENGTH_SHORT).show()
        }

        }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
//                msg.setText("")
            },
            Response.ErrorListener {
                Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
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
            intent = Intent(this, MapsActivity::class.java)
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

//    private fun getDateTimeCalendar() {
//        val cal = Calendar.getInstance()
//        day = cal.get(Calendar.DAY_OF_MONTH)
//        month = cal.get(Calendar.MONTH)
//        year = cal.get(Calendar.YEAR)
//        hour = cal.get(Calendar.HOUR)
//        minute = cal.get(Calendar.MINUTE)
//    }



//    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
//        savedDay = day
//        savedMonth = month
//        savedYear = year
//
//        getDateTimeCalendar()
//
//        TimePickerDialog(this, this, hour, minute, true).show()
//
//    }

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