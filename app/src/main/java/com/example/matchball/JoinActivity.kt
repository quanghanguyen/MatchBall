package com.example.matchball

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.matchball.databinding.ActivityJoinBinding
import com.example.matchball.map.JoinMapsActivity
import com.example.matchball.model.MatchRequest
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

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

    //Notification
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAWkPGrQY:APA91bGf56l6anVyOwwUjCH87Jgj4p4whfluhk19N8mqlInCJwUBZnJN1WHQcDEFzbz0G-amPJQ_lBGcFmAZQTf91_4xYmphCGuqzYkGAm9VzGPMbh3bJadv0avp8je0pjokN4MUdm8a"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        joinBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(joinBinding.root)

        //Notification
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_topic")

        intent?.let { bundle ->
            val requests = bundle.getParcelableExtra<MatchRequest>(KEY_DATA)

//            val pitchLatitude = requests?.pitchLatitude.toString()
//            val pitchLongitude = requests?.pitchLongitude.toString()
//            val pitchName = requests?.pitch.toString()

            with(joinBinding){
                tvJMTeamName.text = requests!!.teamName
                tvJMTime.text = requests.time
                tvJMPitch.text = requests.pitch
                tvJMNote.text = requests.note
            }

        }

        openPitchMap()
        sendJoin()



    }

    private fun sendJoin() {
        joinBinding.btnJoin.setOnClickListener {
            val topic = "/topics/Enter_topic"

            val notification = JSONObject()
            val notifcationBody = JSONObject()

            try {
                notifcationBody.put("title", "Join Team Notification")
                notifcationBody.put("message", "Đã tìm được đội")
                notification.put("to", topic)
                notification.put("data", notifcationBody)
                Log.e("TAG", "try")
            } catch (e: JSONException) {
                Log.e("TAG", "onCreate: " + e.message)
            }

            sendNotification(notification)

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    private fun openPitchMap() {
        joinBinding.tvJMPitch.setOnClickListener {

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
//            val requestData = MatchRequest()
//            JoinMapsActivity.startMapDetails(this, requestData)

        }
    }
}