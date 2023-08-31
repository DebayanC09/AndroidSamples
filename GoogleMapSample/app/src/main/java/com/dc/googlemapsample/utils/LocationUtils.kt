package com.dc.googlemapsample.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task

object LocationUtils {
    const val REQUEST_LOCATION_PERMISSION = "REQUEST_LOCATION_PERMISSION"
    const val REQUEST_GPS_ENABLE = "REQUEST_GPS_ENABLE"
    const val LOCATION_REQUEST_CODE = 1
    const val GPS_REQUEST_CODE = 2

    fun getCurrentLocation(
        activity: Activity,
        fusedLocationClient: FusedLocationProviderClient,
        locationFetchListener: LocationFetchListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (activity.isLocationEnabled()) {
                val location = fusedLocationClient.lastLocation
                location.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null) {
                            locationFetchListener.onSuccessListener(task.result)
                        } else {
                            getLocationUpdateOnce(
                                activity,
                                fusedLocationClient,
                                object : LocationFetchListener {
                                    override fun onSuccessListener(location: Location?) {
                                        locationFetchListener.onSuccessListener(location)
                                    }

                                    override fun onFailureListener(message: String) {
                                        locationFetchListener.onFailureListener(message)
                                    }
                                })
                        }

                    } else {
                        locationFetchListener.onFailureListener("Failed")
                    }
                }
            } else {
                activity.enableGps(GPS_REQUEST_CODE)
                locationFetchListener.onFailureListener(REQUEST_GPS_ENABLE)
            }
        } else {
            activity.requestLocationPermissions(LOCATION_REQUEST_CODE)
            locationFetchListener.onFailureListener(REQUEST_LOCATION_PERMISSION)
        }
    }


    private fun getLocationUpdateOnce(
        activity: Activity,
        fusedLocationClient: FusedLocationProviderClient,
        locationFetchListener: LocationFetchListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 2000
                fastestInterval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            lateinit var locationCallback: LocationCallback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            locationFetchListener.onSuccessListener(location)
                            stopLocationUpdate(fusedLocationClient, locationCallback)
                            break
                        }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            activity.requestLocationPermissions(LOCATION_REQUEST_CODE)
            locationFetchListener.onFailureListener(REQUEST_LOCATION_PERMISSION)
        }
    }


    fun startLocationUpdate(
        activity: Activity,
        fusedLocationClient: FusedLocationProviderClient,
        locationRequest: LocationRequest,
        locationCallBack: LocationCallback,
        locationFetchListener: LocationFetchListener
    ) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (activity.isLocationEnabled()) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallBack,
                    Looper.getMainLooper()
                )
                locationFetchListener.onSuccessListener(null)
            }else{
                activity.enableGps(GPS_REQUEST_CODE)
                locationFetchListener.onFailureListener(REQUEST_GPS_ENABLE)
            }
        }else{
            activity.requestLocationPermissions(LOCATION_REQUEST_CODE)
            locationFetchListener.onFailureListener(REQUEST_LOCATION_PERMISSION)
        }
    }

    fun stopLocationUpdate(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallBack: LocationCallback
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallBack)
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

interface LocationFetchListener {
    fun onSuccessListener(location: Location?)
    fun onFailureListener(message: String)
}

///////////////////////////////////////////////////////////////////////////////////////////////////////

fun Activity.checkLocationPermissions(): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.shouldRequestLocationPermissions(): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}

fun Activity.goToSettings(){
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Activity.requestLocationPermissions(requestCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        requestCode
    )
}

fun Activity.enableGps(requestCode: Int) {
    val task: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(this)
        .checkLocationSettings(
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create())
                .build()
        )
    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(
                    this,
                    requestCode
                )
            } catch (sendEx: IntentSender.SendIntentException) {

            }
        }
    }
}

fun Activity.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}