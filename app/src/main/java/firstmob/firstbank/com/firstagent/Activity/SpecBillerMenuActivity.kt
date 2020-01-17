package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.adapter.BillerMenuAdapt
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.model.GetBillersData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetBillersPresenter
import firstmob.firstbank.com.firstagent.presenter.GetBillersSpecMenuPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.ArrayList

class SpecBillerMenuActivity : AppCompatActivity(),GetBillersContract.IViewbillersSpec {

    val KEY_TOKEN = "token"
    internal var planetsList: MutableList<GetBillersData> = ArrayList<GetBillersData>()
    internal var ptype: String? = null
    internal var lv: ListView? = null
    internal var txtservice: TextView? = null
    //internal var step2:TextView? =null
    internal var presenter: GetBillersContract.Presenterloadbillerspc? =null
    internal var serviceid: String? = null
    internal var servicename: String? = null
    internal var servlabel: String? = null
    internal var aAdpt: BillerMenuAdapt? = null
    internal var prgDialog: ProgressDialog? = null
    internal var session: SessionManagement? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spec_biller_menu)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
        ab.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.theme_paybills)));
        session = SessionManagement(this)
        prgDialog = ProgressDialog(this)
        prgDialog!!.setMessage("Please wait...")
        txtservice = findViewById(R.id.titlte) as TextView
        presenter = GetBillersSpecMenuPresenter(this, this, FetchServerResponse())
        val intent = intent
        if (intent != null) {
            val bcp = intent.getParcelableExtra<BillMenuParcelable>("bcp");
            serviceid = bcp.getserviceid()
            servicename = bcp.getservicename()
            txtservice!!.setText(servicename)
        }

        prgDialog!!.setCancelable(false)

        lv = findViewById(R.id.lv) as ListView
        val bsid = session!!.getString("bllservid$serviceid")
        if (bsid == null) {
            presenter!!.loadbiller(serviceid)
            //SetPop()
        } else {
            presenter!!.loadcachedbiilersData(serviceid)
           // SetBillersStored()
        }

    }

    override fun onResult(BillerList: MutableList<GetBillersData>?) {
        aAdpt = BillerMenuAdapt(BillerList, this@SpecBillerMenuActivity)
        lv!!.setAdapter(aAdpt)
        lv!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val idd = BillerList!![position].id
            val billid = BillerList!![position].billerId
            val billname = BillerList!![position].billerName
            val stracnumber = BillerList!![position].acnumber
            servlabel = BillerList!![position].getcustomerField()
            var intent: Intent? = null
            SecurityLayer.Log("StrAcNumber", stracnumber)
            if (stracnumber == null || stracnumber == null || stracnumber == "null") {
                intent = Intent(this@SpecBillerMenuActivity, GetBillPaymentsActivity::class.java)
                val bcp = BillMenuParcelable(serviceid, servicename, billid, billname, idd, servlabel, stracnumber, null, null, null, null, null, null, null, null, null)
                intent.putExtra("bcp", bcp)
                startActivity(intent)
            } else {
                if (serviceid == "3") {
                    intent = Intent(this@SpecBillerMenuActivity, StateCollectActivity::class.java)
                } else {
                    intent = Intent(this@SpecBillerMenuActivity, CableTVActivity::class.java)
                }
                val bcp = BillMenuParcelable(serviceid, servicename, billid, billname, idd, servlabel, stracnumber, stracnumber, "0", "", null, null, null, null, null, null)
                intent.putExtra("bcp", bcp)
                startActivity(intent)

            }
        })
    }

    override fun onProcessingError(error: String?) {
     Utility.showToast(error)
    }

    override fun logoutuser() {
        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
    }

    override fun showProgress() {
        if (prgDialog != null && prgDialog!!.isShowing()) {
            prgDialog!!.show()
        }
    }

    override fun hideProgress() {
        if (prgDialog != null && prgDialog!!.isShowing()) {
            prgDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        finish()
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
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun SetForceOutDialog(msg : String,title : String,c : Context)
    {
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
        }}
}
