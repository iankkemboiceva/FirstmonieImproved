package firstmob.firstbank.com.firstagent.Activity

import android.Manifest
//import android.app.ProgressDialog
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
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl
import kotlinx.android.synthetic.main.content_activate_agent.*


class ActivateAgent : AppCompatActivity(), ActivateAgentContract.ILoginView {


    var viewDialog: ViewDialog? = null

    internal lateinit var presenter: ActivateAgentContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activate_agent)
        viewDialog = ViewDialog(this);

        ButterKnife.bind(this)

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
                                    checkPlayServices()
                                }
                            }
                        }
                    })
                    .withErrorListener {
                        showToast(it.name)
                    }.check()
        }

        resendbutt.setOnClickListener {
            val i = Intent(this,ActivateAgentBefore::class.java)
            startActivity(i)
        }



        presenter = ActivateAgentPresenter(this, FetchServerResponse());
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

    private fun checkPlayServices() {
        val api = GoogleApiAvailability.getInstance()
        val code = api.isGooglePlayServicesAvailable(applicationContext)
        if (code == ConnectionResult.SUCCESS) {
            // Do Your Stuff Here


            val agpin = agentpin.text.toString()
            val otp = otp.text.toString()

            presenter.DevReg(agpin, otp);


        } else {
            Toast.makeText(
                    applicationContext,
                    "Please ensure you have installed Google Play Services", Toast.LENGTH_LONG).show()

        }
    }


    override fun showProgress() {
        viewDialog?.showDialog()
    }

    override fun onLoginError(error: String) {
        showToast(error)
   }

    override fun onLoginResult(result: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
    }

}
