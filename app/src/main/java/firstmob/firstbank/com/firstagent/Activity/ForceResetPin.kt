package firstmob.firstbank.com.firstagent.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID
import firstmob.firstbank.com.firstagent.contract.PinChangesContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ForceResetPinPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

class ForceResetPin : AppCompatActivity(), View.OnClickListener, PinChangesContract.IViewPinChange {
    @Inject
    internal lateinit var ul: Utility

    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    //internal var pDialog: ProgressDialog? = null
    var viewDialog: ViewDialog? =null
    internal var et: EditText? = null
    internal var et2: EditText? = null
    internal var oldpin: EditText? = null
    internal var btnok: Button? = null
    internal var session: SessionManagement? = null
    internal var value: String? = null
    internal var blchk: Boolean = false
    internal var npin: String? = null
    internal var chgpinparams: String? = null
    internal lateinit var presenter: PinChangesContract.PresenterPinChange
    val AGMOB = "agmobno"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)));
        oldpin = findViewById(R.id.oldpin) as EditText
        et = findViewById(R.id.pin) as EditText
        et2 = findViewById(R.id.cpin) as EditText
        btnok = findViewById(R.id.button2) as Button
        btnok!!.setOnClickListener(this)
        session = SessionManagement(applicationContext)
        presenter = ForceResetPinPresenter(this, this, FetchServerResponse())

        viewDialog= ViewDialog(this)
//        pDialog = ProgressDialog(this)
//       // pDialog!!.setTitle("Loading")
//        pDialog!!.setMessage("Loading Request....")
//        pDialog!!.setCancelable(false)
        updateAndroidSecurityProvider(this)
        if (intent != null) {
            value = intent.extras.getString("pinna")

            val type = intent.extras.getString("type")
            if (type != null || type == "") {
                if (type == "SUP") {
                    blchk = true
                }
            }
        }


    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            SecurityLayer.Log("SecurityException", "Google Play Services not available.")
        }

    }

    override fun onClick(view: View) {

        if (view.id == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                 npin = et!!.getText().toString()
                val confnpin = et2!!.getText().toString()
                val oldpinn = oldpin!!.getText().toString()
                if (Utility.isNotNull(oldpinn)) {
                    if (Utility.isNotNull(npin)) {
                        if (confnpin == npin) {
                            if (npin!!.length == 5 && oldpinn.length == 5) {
                                //   pDialog.show();
                                if (Utility.findweakPin(npin)) {


                                    var encrypted1: String? = null
                                    var encrypted2: String? = null

                                    var usid: String? = null
                                    if (blchk) {
                                        usid = session!!.getString("SUPERVID")
                                    } else {
                                        usid = Prefs.getString(KEY_USERID, "NA")
                                    }

                                    val agentid = Prefs.getString(AGENTID, "NA")
                                    val mobnoo = Prefs.getString(AGMOB, "NA")

                                    encrypted1 = Utility.b64_sha256(oldpinn)
                                    encrypted2 = Utility.b64_sha256(npin)

                                    SecurityLayer.Log("Encrypted Pin", encrypted1)

                                     chgpinparams = "1/$usid/$agentid/$mobnoo/$encrypted1/"


                                    val lgparams = "1/$usid/$value/$mobnoo"
                                    presenter!!.ServerLogRetroCall("LogRetro",lgparams)
                                   /// LogRetro(lgparams, params, npin)
                                } else {
                                    Utility.showToast("You have set a weak PIN for New Pin Value.Please ensure you have selected a strong PIN")
                                }
                            } else {
                                Utility.showToast("Please ensure the Confirm New Pin and New Pin values are 5 digit length")
                            }
                        } else {
                            Utility.showToast("Please ensure the Confirm New Pin and New Pin values are the same")
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for New pin")
                    }
                } else {
                    Utility.showToast("Please enter a valid value for Current pin")
                }
            }

        }
    }

    override fun invokeRetroDevRegCall(pubkey: String?) {
        val encryptednewpin = Utility.getencryptedpin(npin, pubkey)
        Log.v("Public Key", pubkey)
        val finalparams = chgpinparams + encryptednewpin
        presenter!!.ServerRetroDevRegCall("RetroDevReg",finalparams)
    }

    override fun startSiginActivity() {
        finish()
        val i = Intent(getApplicationContext(), SignInActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Staring Login Activity
        startActivity(i)
    }
    override fun onProcessingMessage(error: String?) {
    Utility.showToast(error)
    }

    override fun showProgress() {
        viewDialog!!.showDialog()
    //pDialog!!.show()
    }

    override fun hideProgress() {
        if(viewDialog!=null){
            viewDialog!!.hideDialog()
        }
     //pDialog!!.dismiss()
    }

    override fun ForceLogout() {
        if (applicationContext != null) {
            MaterialDialog.Builder(applicationContext)
                    .title(resources.getString(R.string.forceouterr))
                    .content(resources.getString(R.string.forceout))
                    .negativeText("CONTINUE")
                    .callback(object : MaterialDialog.ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                        }
                        override fun onNegative(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                            finish()
                            session!!.logoutUser()
                            // After logout redirect user to Loing Activity
                            val i = Intent(applicationContext, SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter!!.ondestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
