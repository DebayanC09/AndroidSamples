package com.dc.googlemapsample.ui.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.googlemapsample.R
import com.dc.googlemapsample.databinding.ActivityCurrentLocationBinding
import com.dc.googlemapsample.utils.LocationFetchListener
import com.dc.googlemapsample.utils.LocationUtils
import com.dc.googlemapsample.utils.LocationUtils.GPS_REQUEST_CODE
import com.dc.googlemapsample.utils.LocationUtils.LOCATION_REQUEST_CODE
import com.dc.googlemapsample.utils.gone
import com.dc.googlemapsample.utils.show
import com.dc.googlemapsample.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CurrentLocationActivity : AppCompatActivity() {
    private val binding: ActivityCurrentLocationBinding by lazy {
        ActivityCurrentLocationBinding.inflate(layoutInflater)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initializeMap()
        onClickListener()
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            getCurrentLocation()
        }
    }

    private fun onClickListener() {
        binding.button.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        showHideViews(showProgress = true)
        LocationUtils.getCurrentLocation(
            this,
            fusedLocationClient,
            object : LocationFetchListener {
                override fun onSuccessListener(location: Location?) {
                    location?.let {
                        updateMap(it.latitude, it.longitude)
                    }
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
            })
    }

    private fun updateMap(latitude: Double, longitude: Double) {
        val currentLocation = LatLng(latitude, longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(currentLocation)
                .title("Current Location")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
        showHideViews(showMap = true)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
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
                    getCurrentLocation()
                }
                Activity.RESULT_CANCELED -> {
                    showToast("Permission Denied")
                    showHideViews(showButton = true)
                }
            }
        }
    }
}