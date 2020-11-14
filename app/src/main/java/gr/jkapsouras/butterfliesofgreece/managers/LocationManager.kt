package gr.jkapsouras.butterfliesofgreece.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.DisposablesWrapper
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


interface ILocationManager {
    var fusedLocationClient:FusedLocationProviderClient?
    val locationObs: Observable<LocationState>
    var activity: MainActivity

    fun getPermissionStatus() : Observable<LocationState>
    fun askForPermissions()
    fun askForLocation()
    fun subscribe()
    fun unSubscribe()
}

sealed class LocationState{
    class ShowLocation(val location: Location) : LocationState()
    object ShowSettings: LocationState()
    object AskLocation: LocationState()
    object AskPermission: LocationState()
    object LocationErrored: LocationState()
}

class LocationManager : ILocationManager{
    override lateinit var activity: MainActivity
    override var fusedLocationClient: FusedLocationProviderClient? = null
    val emitter: PublishSubject<LocationState> = PublishSubject.create()
    override val locationObs: Observable<LocationState>
     get() = emitter

    private var mLastLocation: Location? = null
    private val disposables = DisposablesWrapper()

    override  fun subscribe(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        activity.emitter.subscribe{
            if(it)
                getLastLocation()
            else
                askForLocation()
        }
            .disposeWith(disposables)
    }

    override  fun unSubscribe(){
        disposables.dispose()
    }

    override fun getPermissionStatus(): Observable<LocationState> {
        var permissionStatus:LocationState
        val permissionState = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        permissionStatus = if (permissionState != PackageManager.PERMISSION_GRANTED)
            LocationState.AskPermission
        else
            LocationState.AskLocation
//        permissionStatus = when(permissionState){
//            PackageManager.PERMISSION_GRANTED ->
//                LocationState.AskLocation
//             else ->
//                LocationState.AskPermission
//        }
        return Observable.just(permissionStatus)
    }

    override fun askForPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

//            showSnackbar(R.string.permission_rationale, android.R.string.ok,
//                View.OnClickListener {
//                    // Request permission
//                    startLocationPermissionRequest()
//                })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            askForLocation()
        }
    }

    override fun askForLocation() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient!!.lastLocation
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                    emitter.onNext(LocationState.ShowLocation(mLastLocation!!))
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    requestNewLocationData()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        var perm = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (perm != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission
        }
        else {
            // Initializing LocationRequest
            // object with appropriate methods
            val mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 50000
            mLocationRequest.fastestInterval = 50000
            mLocationRequest.smallestDisplacement = 170f
            mLocationRequest.numUpdates = 1

            // setting LocationRequest
            // on FusedLocationClient
            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(activity)
            fusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                null
            )
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(
            locationResult: LocationResult
        ) {
            val mLastLocation = locationResult.lastLocation
            emitter.onNext(LocationState.ShowLocation(mLastLocation!!))
        }
    }

    companion object {

         const val TAG = "LocationProvider"

         const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}