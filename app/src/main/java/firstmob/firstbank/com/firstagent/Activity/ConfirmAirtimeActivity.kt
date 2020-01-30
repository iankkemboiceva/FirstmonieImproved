package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.AirtimeContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfirmAirtimePresenter
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class ConfirmAirtimeActivity : AppCompatActivity(), View.OnClickListener,AirtimeContract.IViewConfirmAirtime{
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    internal var reccustid: TextView? =null
    internal var recamo:TextView? =null
    internal var rectelco:TextView? =null
    internal var step2:Button? =null
    internal var txtfee:TextView? =null
    internal var acbal:TextView? =null
    var viewDialog: ViewDialog? =null
    internal var session: SessionManagement? =null
    internal var btnsub: Button? =null
    internal var btn_back: Button? =null
    internal var txtcustid: String? =null
    internal var presenterairtime: AirtimeContract.PresenterConfirmAirtime? =null
    internal var amou:String? =null
    internal var narra:String? = null
    internal var ednamee:String? = null
    internal var ednumbb:String? = null
    internal var serviceid:String? =null
    internal var billid:String? =null
    internal var agbalance:String? =null
    internal var telcoop: String? =null
    internal var amon: EditText? = null
    internal var edacc:EditText? = null
    internal var etpin: TextInputEditText? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confim_airtime)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.pink_btncolor)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        session = SessionManagement(this)
        reccustid = findViewById(R.id.textViewnb2) as TextView
        etpin = findViewById(R.id.pin) as TextInputEditText
        acbal = findViewById(R.id.txtacbal) as TextView
        recamo = findViewById(R.id.textViewrrs) as TextView
        rectelco = findViewById(R.id.textViewrr) as TextView
        txtfee = findViewById(R.id.txtfee) as TextView
        viewDialog= ViewDialog(this)
        presenterairtime = ConfirmAirtimePresenter(this, this, FetchServerResponse())
        step2 = findViewById(R.id.tv) as Button
        step2!!.setOnClickListener(this)
        txtfee = findViewById(R.id.txtfee) as TextView
        btnsub = findViewById(R.id.button2) as Button
        btn_back = findViewById(R.id.button3) as Button
        btnsub!!.setOnClickListener(this)
        btn_back!!.setOnClickListener(this)
        val intent = intent
        if (intent != null) {
            txtcustid = intent.getStringExtra("mobno")
            amou = intent.getStringExtra("amou")
            telcoop = intent.getStringExtra("telcoop")
            val newamo = amou!!.replace(",", "")
            var txtamou = Utility.returnNumberFormat(newamo)
            if (txtamou == "0.00") {
                txtamou = amou
            }
            billid = intent.getStringExtra("billid")
            serviceid = intent.getStringExtra("serviceid")
            reccustid!!.setText(txtcustid)


            recamo!!.setText(Constants.KEY_NAIRA + txtamou)
            rectelco!!.setText(telcoop)
            amou = Utility.convertProperNumber(amou)
            presenterairtime!!.fetchServerfee("getFee", "/MMO/$amou")
            //getFeeSec()

        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {
            txtcustid = Utility.convertMobNumber(txtcustid)
            if (Utility.checkInternetConnection()) {
                val agpin = etpin!!.getText().toString()
                if (Utility.isNotNull(txtcustid)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            var encrypted: String? = null
                            encrypted = Utility.b64_sha256(agpin)
                            val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
                            val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
                            val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
                            val emaill = Prefs.getString(SharedPrefConstants.KEY_EMAIL, "NA")
                            val params = "1/" + usid + "/" + agentid + "/" + mobnoo + "/" + billid + "/" + serviceid + "/" + amou + "/01/" + txtcustid + "/" + emaill + "/" + txtcustid + "/" + billid + "01"
                            val intent = Intent(this@ConfirmAirtimeActivity, TransactionProcessingActivity::class.java)

                            intent.putExtra("mobno", txtcustid)
                            intent.putExtra("amou", amou)
                            intent.putExtra("telcoop", telcoop)

                            intent.putExtra("res", billid)
                            intent.putExtra("billid", billid)
                            intent.putExtra("serviceid", serviceid)
                            intent.putExtra("txpin", encrypted)
                            intent.putExtra("serv", "AIRT")
                            intent.putExtra("params", params)
                            startActivity(intent)
                            ClearPin()

                        } else {
                            Utility.showToast("Please enter a valid value for Agent PIN")

                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount")
                    }
                } else {
                    Utility.showToast("Please enter a value for Customer ID")
                }
            }
        }

        if (view.getId() == R.id.tv) {


            finish()


            val intent = Intent(this@ConfirmAirtimeActivity, AirtimeTransfActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        }
        if(view.id==R.id.button3){
            finish()

            val intent = Intent(this@ConfirmAirtimeActivity, AirtimeTransfActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    fun ClearPin() {
        etpin!!.setText("")
    }

    override fun setFee(fee: String) {
        txtfee!!.setText(fee)
    }

    override fun setviewvisibility() {
        btnsub!!.setVisibility(View.GONE)
    }

    override fun onProcessingError(error: String) {
        Utility.showToast(error)
    }

    override fun onBackNavigate() {
        onBackPressed()
    }

    override fun setBalance(blance: String) {
        acbal!!.setText(blance)
    }

    override fun logout() {
        session!!.logoutUser()
        finish()
        val i = Intent(this, SignInActivity::class.java)
        startActivity(i)
        Utility.showToast("You have been locked out of the app.Please call customer care for further details")
    }


    override fun showProgress() {
        //  prgDialog2.show();
        viewDialog?.showDialog()
    }

    override fun hideProgress() {
        if (viewDialog != null) {
            viewDialog?.hideDialog()
        }
        // prgDialog2.dismiss();
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    public override fun onDestroy() {
        presenterairtime?.ondestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
