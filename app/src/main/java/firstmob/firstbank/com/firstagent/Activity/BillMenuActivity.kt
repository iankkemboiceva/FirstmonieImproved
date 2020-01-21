package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.adapter.ServicesMenuAdapt
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetBillersPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import kotlin.Comparator

class BillMenuActivity : AppCompatActivity(), GetBillersContract.IViewbillers {
    internal var planetsList: MutableList<GetServicesData> = ArrayList<GetServicesData>()
    internal var lv: ListView? =null
   // var planetsList = arrayListOf<GetServicesData>()
    internal var aAdpt: ServicesMenuAdapt? =null
    internal var prgDialog: ProgressDialog? = null
    internal var session: SessionManagement? =null
    internal var presenter: GetBillersContract.Presenterloadbillers? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_menu)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab!!.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.theme_paybills)));
        presenter = GetBillersPresenter(this, this, FetchServerResponse())
        session = SessionManagement(this)

        prgDialog = ProgressDialog(this)

        prgDialog!!.setMessage("Please wait...")


        prgDialog!!.setCancelable(false)
        lv = findViewById(R.id.lv) as ListView
        val strservdata = session!!.getString(SessionManagement.KEY_BILLERS)
        if (strservdata != null) {
            var servdata: JSONArray? = null
            try {
                servdata = JSONArray(strservdata)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            if (servdata!!.length() > 0) {
                presenter!!.loadcachedbiilersData()
                //SetBillersStored()
            } else {
                presenter!!.loadbiller()
                //SetPop()
            }
        } else {
            presenter!!.loadbiller()
        }

        val newarrlist = session!!.getSets("serviceid")
        if (newarrlist != null) {
            for (s in newarrlist!!.indices) {
                SecurityLayer.Log("Service Id", newarrlist!!.get(s))

            }

        }
    }
    override fun onResult(List: MutableList<GetServicesData>?) {
        if (applicationContext != null) {
            aAdpt = ServicesMenuAdapt(List, this@BillMenuActivity)
            lv!!.setAdapter(aAdpt)
            lv!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                val serviceid = List!![position].id
                val servicename = List!![position].serviceName
                val label = List!![position].label
                val intent = Intent(this@BillMenuActivity, SpecBillerMenuActivity::class.java)
                val bcp = BillMenuParcelable(serviceid, servicename, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
                intent.putExtra("bcp", bcp)
                startActivity(intent)
            })
        }
    }
    override  fun logoutuser(){
    SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
}
    override fun onProcessingError(error: String?) {
    Utility.showToast(error)
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

    fun SetForceOutDialog(msg : String,title : String,c :Context)
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
    override fun onDestroy() {
       // dismissProgressDialog()
        finish()
        presenter!!.ondestroy()
        super.onDestroy()
    }
}
