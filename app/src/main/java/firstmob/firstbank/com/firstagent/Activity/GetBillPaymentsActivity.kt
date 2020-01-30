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
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.adapter.BillerPayMenuAdapt
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.model.GetBillPayData
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetBillPaymentsActPresenter
import firstmob.firstbank.com.firstagent.presenter.GetBillersSpecMenuPresenter
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.ArrayList

class GetBillPaymentsActivity : AppCompatActivity(), GetBillersContract.IViewbillPaymentsAct {


    //internal var planetsList: MutableList<GetBillPayData> = ArrayList()
    //internal var planetsList: MutableList<GetBillPayData> = ArrayList<GetBillPayData>()
    internal var ptype: String? = null
    val KEY_TOKEN = "token"
    internal var lv: ListView? =null
  //  internal var txtservice: TextView? =null
    //internal var step2:TextView? =null
    internal var serviceid: String? =null
    internal var servicename:String? =null
    internal var billid:String? =null
    internal var billname:String? =null
    internal var label:String? =null
    internal var idd:String? =null
    internal var acnumber:String? =null
    internal var aAdpt: BillerPayMenuAdapt? =null
    //internal var prgDialog: ProgressDialog? = null
    var viewDialog:ViewDialog? =null
    internal var session: SessionManagement? =null
    internal var presenter: GetBillersContract.Presenterloadbillerspc? =null
    internal var sbpam = "0"
    internal var pramo = "0"
    internal var ds = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_bill_payments)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.theme_paybills)));
        session = SessionManagement(this)
        viewDialog= ViewDialog(this);
        presenter = GetBillPaymentsActPresenter(this, this, FetchServerResponse())
        val intent = intent
        if (intent != null) {
            val bcp = intent.getParcelableExtra<BillMenuParcelable>("bcp")
            serviceid = bcp.getserviceid()
            servicename = bcp.getservicename()
            billid = bcp.getbillid()
            billname = bcp.getbillname()
            label = bcp.getservlabel()
            acnumber = bcp.getstracnumber()
            idd = bcp.getidd()
         //   txtservice!!.setText(billname)
        }
        lv = findViewById(R.id.lv) as ListView
        val bsid = session!!.getString("getbillpay$idd")
        if (bsid == null) {
            presenter!!.loadbiller(idd)
           // SetPop()
        } else {
            presenter!!.loadcachedbiilersData(idd)
           // SetBillersStored()
        }

    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onResult(BillerList: MutableList<GetBillPayData>?) {
        if (applicationContext != null) {
            aAdpt = BillerPayMenuAdapt(BillerList, this@GetBillPaymentsActivity)
            lv!!.setAdapter(aAdpt)
            lv!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                val title: String? = null
                val billid = BillerList!![position].billerId
                //    String billname = planetsList.get(position).getdisplayName();
                val packid = BillerList!![position].getpackid()
                val paycode = BillerList!![position].getpaymentCode()
                val charge = BillerList!![position].charge
                val dispname = BillerList!![position].getdisplayName()
                var intent: Intent? = null
                Log.v("Idd", idd)

                if (serviceid == "3") {
                    intent = Intent(this@GetBillPaymentsActivity, StateCollectActivity::class.java)
                } else {
                    intent = Intent(this@GetBillPaymentsActivity, CableTVActivity::class.java)
                }
                val bcp = BillMenuParcelable(serviceid, servicename, billid, billname, idd, label, null, paycode, packid, charge, dispname, null, null, null, null, null)
                intent.putExtra("bcp", bcp)
                startActivity(intent)
            })
        }
    }

    override fun onProcessingError(error: String?) {
    Utility.showToast(error)
    }
    override fun navigatetoNextpage() {

        var intent: Intent? = null

        if (serviceid == "3") {
            intent = Intent(this@GetBillPaymentsActivity, StateCollectActivity::class.java)
        } else {
            intent = Intent(this@GetBillPaymentsActivity, CableTVActivity::class.java)
        }
        val bcp = BillMenuParcelable(serviceid, servicename, billid, billname, idd, label, null, acnumber, "0", "N", null, null, null, null, null, null)
        intent.putExtra("bcp", bcp)


        startActivity(intent)
    }

    override fun logoutuser() {
        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
    }

    override fun showProgress() {
        viewDialog!!.showDialog()
        //prgDialog!!.show()
    }

    override fun hideProgress() {
        if(viewDialog!=null){
            viewDialog!!.hideDialog()
        }
    // prgDialog!!.dismiss()
    }
    override fun onDestroy() {
        finish()
        presenter!!.ondestroy()
        super.onDestroy()
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
