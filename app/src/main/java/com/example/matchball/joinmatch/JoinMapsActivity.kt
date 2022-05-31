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

//    companion object
//    {
//        private const val KEY_MAP_DATA = "request_map_data"
//        fun startMapDetails(context: Context, data : MatchRequest)
//        {
//            context.startActivity(Intent(context, JoinMapsActivity::class.java).apply {
//                putExtra(KEY_MAP_DATA, data)
//            })
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJoinMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
        val sydney = LatLng(pitchLatitude, pitchLongitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in $pitchName"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }
}