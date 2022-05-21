package com.example.matchball

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.matchball.databinding.ActivityRequestBinding
import java.util.*

class RequestActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var requestBinding: ActivityRequestBinding
    private val pitchOptions = arrayOf("Pitch 1", "Pitch 2", "Pitch 3", "Pitch 4", "Pitch 5")
    private val peopleOptions = arrayOf(4, 5, 6, 7, 8, 9, 10, 11)

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBinding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(requestBinding.root)

        timePick()
//        pitchSelect()
        peopleSelect()

    }

    private fun peopleSelect() {
        requestBinding.spnPeople.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, peopleOptions)
//        requestBinding.spnPeople.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }
    }

//    private fun pitchSelect() {
//        requestBinding.spnPitch.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pitchOptions)
//    }

    private fun timePick() {
        requestBinding.btnSelect.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }



    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = hour
        savedMinute = minute

        requestBinding.tvPickTime.text = ("$savedHour:$savedMinute ($savedDay/$savedMonth/$savedYear)")
    }
}