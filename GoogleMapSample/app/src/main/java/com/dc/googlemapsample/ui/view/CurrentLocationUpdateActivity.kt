package com.dc.googlemapsample.ui.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.googlemapsample.R
import com.dc.googlemapsample.databinding.ActivityCurrentLocationUpdateBinding
import com.dc.googlemapsample.utils.LocationFetchListener
import com.dc.googlemapsample.utils.LocationUtils
import com.dc.googlemapsample.utils.LocationUtils.GPS_REQUEST_CODE
import com.dc.googlemapsample.utils.LocationUtils.LOCATION_REQUEST_CODE
import com.dc.googlemapsample.utils.gone
import com.dc.googlemapsample.utils.show
import com.dc.googlemapsample.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class CurrentLocationUpdateActivity : AppCompatActivity() {
    private val binding: ActivityCurrentLocationUpdateBinding by lazy {
        ActivityCurrentLocationUpdateBinding.inflate(layoutInflater)
    }
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var locationCallBack: LocationCallback
    private lateinit var userMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeMap()
        initializeLocationUpdate()

    }

    private fun initializeLocationUpdate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    updateMap(location)
                }
            }
        }

    }

    private fun updateMap(location: Location) {
        setMarker(location)
        showHideViews(showMap = true)
    }

    private fun setMarker(location: Location) {
        val currentLocation = LatLng(location.latitude, location.longitude)
        if (::userMarker.isInitialized) {
            userMarker.position = currentLocation
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
        } else {
            val markerOptions = MarkerOptions()
            markerOptions.position(currentLocation)
            googleMap.addMarker(markerOptions)?.let {
                userMarker = it
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
        }
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
        }
    }

    private fun startLocationUpdate() {
        showHideViews(showProgress = true)
        LocationUtils.startLocationUpdate(
            this,
            fusedLocationClient,
            locationRequest,
            locationCallBack, object : LocationFetchListener {
                override fun onSuccessListener(location: Location?) {

                }

                override fun onFailureListener(message: String) {
                    when (message) {
                        LocationUtils.REQUEST_LOCATION_PERMISSION -> {
                            showHideViews()
                        }
                        LocationUtils.REQUEST_GPS_ENABLE -> {
                            showHideViews()
                        }
                        else -> {
                            showHideViews(showButton = true)
                        }
                    }
                }
            }
        )
    }


    private fun showHideViews(
        showProgress: Boolean = false,
        showMap: Boolean = false,
        showButton: Boolean = false
    ) {
        if (showProgress) {
            binding.progress.show()
        } else {
            binding.progress.gone()
        }

        if (showButton) {
            binding.button.show()
        } else {
            binding.button.gone()
        }

        if (showMap) {
            binding.mapLayout.show()
        } else {
            binding.mapLayout.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        LocationUtils.stopLocationUpdate(fusedLocationClient, locationCallBack)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdate()
            } else {
                showToast("Permission Denied")
                showHideViews(showButton = true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GPS_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    startLocationUpdate()
                }
                Activity.RESULT_CANCELED -> {
                    showToast("Permission Denied")
                    showHideViews(showButton = true)
                }
            }
        }
    }

}