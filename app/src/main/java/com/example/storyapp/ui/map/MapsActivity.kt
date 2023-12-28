package com.example.storyapp.ui.map

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.storyapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.ViewModelFactory
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val storyViewModel by viewModels<StoryViewModel> { ViewModelFactory.getInstance(this) }
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupSession()
        setMapStyle()
        addManyMarker()
        getMyLocation()
        
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

            if (!success) {
                Log.d(TAG, "Style Parsing Failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setupSession() {
        storyViewModel.getSession().observe(this) {
            token = it.token
            getLocationStories(token)
        }
    }

    private fun addManyMarker() {
        storyViewModel.list.observe(this) {
            Log.d(TAG, "Data dari ViewModel: $it")
            it?.listStory?.forEach { data ->
                val latLng = LatLng(data.lat, data.lon)
                Log.d(TAG, "addManyMarker: $data")
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(data.name)
                        .snippet(data.description)
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }

            if (it.listStory.isNotEmpty()) {
                val random = (0 until it.listStory.size).random()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.listStory[random].lat,
                            it.listStory[random].lon
                        ), 15f
                    )
                )
            } else {
                val dicodingSpace = LatLng(-6.8957643, 107.6338462)
                mMap.addMarker(
                    MarkerOptions()
                        .position(dicodingSpace)
                        .title("Dicoding Space")
                        .snippet("Batik Kumeli No.50")
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

            }
        }
    }

    private fun getLocationStories(token: String) {
        storyViewModel.listStoryLocation(token)
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}