package firstmob.firstbank.com.firstagent.Activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

import butterknife.BindView
import butterknife.OnClick
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

import butterknife.ButterKnife
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.SESS_REG
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.notifications.FirebaseService
import firstmob.firstbank.com.firstagent.notifications.RegistrationIntentService
import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl
import kotlinx.android.synthetic.main.content_activate_agent.*


class ActivateAgent : AppCompatActivity(), ActivateAgentContract.ILoginView {


    var viewDialog: ViewDialog? = null

    internal lateinit var presenter: ActivateAgentContract.Presenter
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val TAG = "Notifications"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activate_agent)
        viewDialog = ViewDialog(this)



        button2.setOnClickListener {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.READ_PHONE_STATE
                    ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {

                            token?.continuePermissionRequest()
                        }

                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            report?.let {
                                if (report.areAllPermissionsGranted()) {
                                    if(checkPlayServices()){
                                        val agpin = agentpin.text.toString()
                                        val otp = otp.text.toString()

                                        presenter.DevReg(agpin, otp)
                                    }
                                }
                            }
                        }
                    })
                    .withErrorListener {
                        showToast(it.name)
                    }.check()
        }

        resendbutt.setOnClickListener {
            /* val i = Intent(this,ActivateAgentBefore::class.java)
             startActivity(i)*/

            val agid = Prefs.getString(KEY_USERID,"NA")

            presenter.ResendOTP(agid)
        }



        presenter = ActivateAgentPresenter(this, FetchServerResponse())

        if(checkPlayServices()){
            Log.v(TAG,"am in")
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
            FirebaseService.createChannelAndHandleNotifications(applicationContext);
        }
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }



    override fun showToast(text: String) {

        Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG).show()


    }




    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.")

            }
            return false
        }
        return true
    }





    override fun showProgress() {
        viewDialog?.showDialog()
    }

    override fun onLoginError(error: String) {
        showToast(error)
    }


    override fun hideProgress() {
        viewDialog?.hideDialog()
    }



    override fun onLoginResult() {
        Prefs.putString(SESS_REG,"Y")
        val i = Intent(this,SignInActivity::class.java)
        startActivity(i)

    }


}
