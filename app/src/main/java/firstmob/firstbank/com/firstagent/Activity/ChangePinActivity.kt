package firstmob.firstbank.com.firstagent.Activity

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
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.PinChangesContract
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ChangePinActivityPresenter
import firstmob.firstbank.com.firstagent.presenter.ForceChangePinPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import okhttp3.OkHttpClient
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ChangePinActivity : AppCompatActivity(), View.OnClickListener, PinChangesContract.IViewPinChangeActivity {

    internal var pDialog: ProgressDialog? =null
    internal var et: EditText? =null
    internal var et2:EditText? =null
    internal var oldpin:EditText? =null
    internal var btnok: Button? =null
    internal var session: SessionManagement? =null
    internal var prgDialog2: ProgressDialog? = null
    internal var npin: String? = null
    internal var chgpinparams: String? = null
    internal lateinit var presenter: PinChangesContract.PresenterPinChange
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.nbkyellow)));
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading....")
        prgDialog2!!.setCancelable(false)
        session = SessionManagement(this)
        oldpin = findViewById(R.id.oldpin) as EditText
        presenter = ChangePinActivityPresenter(this, this, FetchServerResponse())
        et = findViewById(R.id.pin) as EditText
        et2 = findViewById(R.id.cpin) as EditText
        btnok = findViewById(R.id.button2) as Button
        btnok!!.setOnClickListener(this)
        pDialog = ProgressDialog(this)
        pDialog!!.setTitle("Loading")
        pDialog!!.setCancelable(false)
    }
    override fun onClick(v: View?) {
        if (v!!.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                 npin = et!!.getText().toString()
                val confnpin = et2!!.getText().toString()
                val oldpinn = oldpin!!.getText().toString()
                if (Utility.isNotNull(oldpinn)) {
                    if (Utility.isNotNull(npin)) {
                        if (confnpin == npin) {
                            if (oldpinn != npin) {
                                if (npin!!.length == 5 && oldpinn.length == 5) {

                                    //    pDialog.show();
                                    if (Utility.findweakPin(npin)) {
                                        val client = OkHttpClient()


                                        var encrypted1: String? = null
                                        var encrypted2: String? = null

                                        encrypted1 = Utility.b64_sha256(oldpinn)
                                        encrypted2 = Utility.b64_sha256(npin)


                                        val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
                                        val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
                                        val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")

                                        SecurityLayer.Log("Chg Pin URL", "1/$usid/$agentid/0000/$encrypted1/$encrypted2")
                                         chgpinparams = "1/$usid/$agentid/$mobnoo/$encrypted1/"


                                        val lgparams = "1/$usid/$encrypted1/$mobnoo"
                                        //  invokeCheckRef(params);
                                        presenter!!.ServerLogRetroCall("LogRetro",lgparams)
                                        //LogRetro(lgparams, params, npin)


                                    } else {
                                        Utility.showToast("You have set a weak PIN for New Pin Value.Please ensure you have selected a strong PIN")
                                    }
                                } else {
                                    Utility.showToast("Please ensure the Confirm New Pin and New Pin values are 5 digit length")
                                }
                            } else {
                                Utility.showToast("Please ensure Current Pin and New Pin values are not the same")
                            }
                        } else {
                            Utility.showToast("Please ensure the Confirm New Pin and New Pin values are  the same")
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
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
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
        pDialog!!.show()
    }

    override fun hideProgress() {
        pDialog!!.dismiss()
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
    override fun onBackpressd() {
    onBackPressed()
    }
    override fun onDestroy() {
        presenter!!.ondestroy()
        super.onDestroy()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
