package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.textfield.TextInputEditText
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfimCabletvPresenter
import firstmob.firstbank.com.firstagent.presenter.StateCollectacivtyPresenter
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import javax.inject.Inject

class ConfirmCableActivity : AppCompatActivity(),View.OnClickListener,GetBillersContract.IViewbillConfirmCabletv {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var reccustid: TextView? = null
    internal var recamo:TextView? = null
    internal var recnarr:TextView? = null
    internal var recsendnum:TextView? = null
    internal var recsendnam:TextView? = null
    internal var txtfee:TextView? = null
    internal var txtlabel:TextView? = null
    internal var presenter: GetBillersContract.PresenterConfirmCabletv? =null
    internal var acbal:TextView? = null
    internal var txtanarr:TextView? = null
    internal var step2: TextView? = null
    internal var step1:TextView? = null
    internal var step3:TextView? = null
    internal var stt:TextView? = null
    internal var btnsub: Button? = null
    internal var txtcustid: String? = null
    internal var amou:String? = null
    internal var narra:String? = null
    internal var ednamee:String? = null
    internal var ednumbb:String? = null
    internal var serviceid:String? = null
    internal var billid:String? = null
    internal var strlabl:String? = null
    internal var servicename:String? = null
    internal var billname:String? = null
    internal var packid:String? = null
    internal var paymentCode:String? = null
    internal var bs:String? = null
    internal var agbalance:String? = null
    internal var marketnm:String? = null
    internal var finalrespfee: String? = null
   // internal var prgDialog2: ProgressDialog? = null
    var viewDialog:ViewDialog? =null
    internal var rlreceipt: RelativeLayout? = null
    internal var amon: EditText? = null
    internal var edacc:EditText? = null
    internal var pno:EditText? = null
    internal var txtamount:EditText? = null
    internal var txtnarr:EditText? = null
    internal var edname:EditText? = null
    internal var ednumber:EditText? = null
    internal var etpin: TextInputEditText? = null
    internal var chkfee = false
    internal var session: SessionManagement? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_cable)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.theme_paybills)));
        session = SessionManagement(this)
        reccustid = findViewById(R.id.textViewnb2) as TextView
        etpin = findViewById(R.id.pin) as TextInputEditText
        acbal = findViewById(R.id.txtacbal) as TextView
        stt = findViewById(R.id.recip) as TextView
        recamo = findViewById(R.id.textViewrrs) as TextView
        recnarr = findViewById(R.id.textViewrr) as TextView
        presenter = ConfimCabletvPresenter(this, this, FetchServerResponse())
        txtanarr = findViewById(R.id.textViewr) as TextView
        rlreceipt = findViewById(R.id.rlreceipt) as RelativeLayout
        txtlabel = findViewById(R.id.textViewnb) as TextView
        recsendnam = findViewById(R.id.sendnammm) as TextView
        recsendnum = findViewById(R.id.sendno) as TextView
        viewDialog= ViewDialog(this)
//        prgDialog2 = ProgressDialog(this)
//        prgDialog2!!.setMessage("Loading....")
//        prgDialog2!!.setCancelable(false)
        txtfee = findViewById(R.id.txtfee) as TextView
        chkfee = false
        step2 = findViewById(R.id.tv2) as TextView
        step2!!.setOnClickListener(this)
        step1 = findViewById(R.id.tv) as TextView
        step1!!.setOnClickListener(this)
        step3 = findViewById(R.id.tv3) as TextView
        step3!!.setOnClickListener(this)
        btnsub = findViewById(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)
        val intent = intent
        if (intent != null) {
            txtcustid = intent.getStringExtra("custid")
            amou = intent.getStringExtra("amou")
            narra = intent.getStringExtra("narra")
            ednamee = intent.getStringExtra("ednamee")
            ednumbb = intent.getStringExtra("ednumbb")
            billname = intent.getStringExtra("billname")
            billid = intent.getStringExtra("billid")
            serviceid = intent.getStringExtra("serviceid")
            servicename = intent.getStringExtra("servicename")
            strlabl = intent.getStringExtra("label")
            packid = intent.getStringExtra("packId")
            paymentCode = intent.getStringExtra("paymentCode")
            reccustid!!.setText(txtcustid)
            recamo!!.setText(amou)
            recsendnam!!.setText(ednamee)
            recsendnum!!.setText(ednumbb)
            txtlabel!!.setText(strlabl)
            if (Utility.checkStateCollect(serviceid)) {
                txtanarr!!.setText("Market")
                marketnm = intent.getStringExtra("marketname")
                recnarr!!.setText(marketnm)
                stt!!.setText(ednamee)
                rlreceipt!!.setVisibility(View.GONE)
                chkfee = true
            } else {
                recnarr!!.setText(narra)
                rlreceipt!!.setVisibility(View.VISIBLE)
            }
            amou = Utility.convertProperNumber(amou)
            presenter!!.RequestServerfee(amou,serviceid)
          //  getFeeSec()
        }
    }
    override fun onProcessingError(error: String?) {
        Utility.showToast(error)
    }

    override fun setBalance(balance: String?) {
        acbal?.setText(balance)
        agbalance==balance
        if (paymentCode == null || paymentCode == "") {
            paymentCode = billid + "01"
        }
        if (paymentCode == null || paymentCode == "null") {
            paymentCode = "0"
        }
        presenter!!.RequestServervalidate(billid + "/" + txtcustid + "/" + paymentCode)
    }

    override fun setFee(fee: String?) {
        txtfee?.setText(fee)
        finalrespfee==fee


    }

    override fun checkfee(chekfee: Boolean?) {
        chkfee==chekfee
    }

    override fun setFullNames(fullnames: String?) {
        stt?.setText(fullnames)
    }

    override fun onBackpressed() {
        onBackPressed()
    }

    override fun logoutuser() {
        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
    }

    override fun showProgress() {
        viewDialog!!.showDialog()
    }

    override fun hideProgress() {
        if (viewDialog != null && applicationContext != null) {
            viewDialog!!.hideDialog()
        }
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                val agpin = etpin!!.getText().toString()
                if (Utility.isNotNull(txtcustid)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(narra)) {
                            if (Utility.isNotNull(ednamee)) {
                                if (Utility.isNotNull(ednumbb)) {
                                    if (Utility.isNotNull(agpin)) {
                                        if (chkfee) {

                                            var chkdb = true
                                            try {
                                                val dbamo = java.lang.Double.parseDouble(amou)
                                                var dbagbal: Double? = 0.0

                                                if (Utility.isNotNull(agbalance)) {
                                                    dbagbal = java.lang.Double.parseDouble(agbalance)
                                                    if (dbamo > dbagbal) {
                                                        chkdb = false
                                                    }
                                                }
                                            } catch (e: NumberFormatException) {
                                                e.printStackTrace()
                                            }

                                            //    if(chkdb){
                                            var encrypted: String? = null
                                            encrypted = Utility.b64_sha256(agpin)

                                            val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
                                            val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
                                            val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")

                                            if (!Utility.isNotNull(packid) || packid == "") {
                                                packid = "01"
                                            }
                                            val params = "1/$usid/$agentid/$mobnoo/$billid/$serviceid/$amou/$packid/$ednumbb/$ednamee/$txtcustid/$paymentCode"

                                            val intent = Intent(this@ConfirmCableActivity, TransactionProcessingActivity::class.java)


                                            //    PayBillResp(params);

                                            intent.putExtra("custid", txtcustid)
                                            intent.putExtra("amou", amou)
                                            intent.putExtra("narra", narra)
                                            intent.putExtra("billname", billname)
                                            intent.putExtra("ednamee", ednamee)
                                            intent.putExtra("ednumbb", ednumbb)
                                            intent.putExtra("billid", billid)
                                            intent.putExtra("serviceid", serviceid)
                                            intent.putExtra("label", strlabl)
                                            intent.putExtra("fullname", bs)
                                            intent.putExtra("params", params)
                                            intent.putExtra("fee", finalrespfee)
                                            intent.putExtra("txpin", encrypted)
                                            if (Utility.checkStateCollect(serviceid)) {
                                                intent.putExtra("marketnm", marketnm)
                                            }
                                            intent.putExtra("serv", "CABLETV")
                                            startActivity(intent)
                                            ClearPin()
                                        } else {
                                            Utility.showToast("Please ensure a valid customer has been chosen for the selected biller")
                                        }
                                    } else {
                                        Utility.showToast("Please enter a valid value for Agent PIN")
                                    }
                                } else {
                                    Utility.showToast("Please enter a valid value for Depositor Number")
                                }
                            } else {
                                Utility.showToast("Please enter a valid value for Depositor Name")
                            }
                        } else {
                            Utility.showToast("Please enter a valid value for Narration")
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount")
                    }
                } else {
                    Utility.showToast("Please enter a value for Customer ID")
                }
            }
        }

        if (view.getId() == R.id.tv2) {
            finish()
            val intent = Intent(this@ConfirmCableActivity, BillMenuActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        }
        if (view.getId() == R.id.tv) {
            finish()
            val intent = Intent(this@ConfirmCableActivity, SpecBillerMenuActivity::class.java)
            val bcp = BillMenuParcelable(serviceid, servicename, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
            intent.putExtra("bcp", bcp)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        if (view.getId() == R.id.tv3) {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        presenter!!.ondestroy()
        finish()
        super.onDestroy()
    }

    fun SetForceOutDialog(msg: String, title: String, c: Context?) {
        if (c != null) {
            MaterialDialog.Builder(this)
                    .title(title)
                    .content(msg)

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
                            val i = Intent(c, SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.getItemId() == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun ClearPin() {
        etpin!!.setText("")
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
