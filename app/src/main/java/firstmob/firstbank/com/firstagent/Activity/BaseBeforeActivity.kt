package firstmob.firstbank.com.firstagent.Activity

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import android.util.Log
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import firstmob.firstbank.com.firstagent.Activity.backgroundlocation.LocationRequestHelper
import firstmob.firstbank.com.firstagent.Activity.backgroundlocation.LocationUpdatesBroadcastReceiver


/**
 * Created by ian on 7/23/2018.
 */

open class BaseBeforeActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    private val pendingIntent: PendingIntent
        get() {
            val intent = Intent(this, LocationUpdatesBroadcastReceiver::class.java)
            intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
            return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {

                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                buildGoogleApiClient()
                            }
                        }
                    }
                })
                .withErrorListener {
                    showToast(it.name)
                }.check()


    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest!!.interval = UPDATE_INTERVAL

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest!!.maxWaitTime = MAX_WAIT_TIME
    }

    /**
     * Builds [GoogleApiClient], enabling automatic lifecycle management using
     * . I.e., GoogleApiClient connects in
     * [AppCompatActivity.onStart], or if onStart() has already happened, it connects
     * immediately, and disconnects automatically in [AppCompatActivity.onStop].
     */
    private fun buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build()
        createLocationRequest()
    }

    override fun onConnected(bundle: Bundle?) {
        Log.i(TAG, "GoogleApiClient connected")
        requestLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {
        val text = "Connection suspended"
        Log.w(TAG, "$text: Error code: $i")

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val text = "Exception while connecting to Google Play services"
        Log.w(TAG, text + ": " + connectionResult.errorMessage)

    }


    /**
     * Return the current state of the permissions needed.
     */

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    fun requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates")
            LocationRequestHelper.setRequesting(this, true)
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, pendingIntent)
        } catch (e: SecurityException) {
            LocationRequestHelper.setRequesting(this, false)
            e.printStackTrace()
        }

    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        LocationRequestHelper.setRequesting(this, false)
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                pendingIntent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {

    }


     fun showToast(text: String) {

        Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG).show()


    }
    companion object {
        val TIMEOUT_IN_MILLI = (1000 * 1).toLong()
        val PREF_FILE = "App_Pref"
        val KEY_SP_LAST_INTERACTION_TIME = "KEY_SP_LAST_INTERACTION_TIME"


        private val TAG = BaseBeforeActivity::class.java.simpleName
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        // FIXME: 5/16/17
        private val UPDATE_INTERVAL = (10 * 1000).toLong()

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value, but they may be less frequent.
         */
        // FIXME: 5/14/17
        private val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2

        /**
         * The max time before batched results are delivered by location services. Results may be
         * delivered sooner than this interval.
         */
        private val MAX_WAIT_TIME = UPDATE_INTERVAL * 3

        /**
         * The entry point to Google Play Services.
         */
        private var mGoogleApiClient: GoogleApiClient? = null
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
}