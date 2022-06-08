package com.example.matchball.joinmatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matchball.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.matchball.databinding.ActivityJoinMapsBinding

class JoinMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityJoinMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

//        intent?.let { bundle ->
//            val requestData = bundle.getParcelableExtra<MatchRequest>(KEY_MAP_DATA)
//            val pitchLatitude = requestData?.pitchLatitude!!.toDouble()
//            val pitchLongitude = requestData.pitchLongitude!!.toDouble()
//            val pitchName = requestData.pitch.toString()
//        }

        mMap = googleMap

        val pitchName = intent.getStringExtra("pitchName")
        val pitchLatitude = intent.getDoubleExtra("pitchLatitude", 0.1)
        val pitchLongitude = intent.getDoubleExtra("pitchLongitude", 0.1)

        val location = LatLng(pitchLatitude, pitchLongitude)
        mMap.addMarker(MarkerOptions().position(location).title("Marker in $pitchName"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

    }
}