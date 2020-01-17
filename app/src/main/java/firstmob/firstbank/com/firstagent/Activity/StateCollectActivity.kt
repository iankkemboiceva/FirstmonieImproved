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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.model.BenList
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetBillersSpecMenuPresenter
import firstmob.firstbank.com.firstagent.presenter.StateCollectacivtyPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import java.util.*

class StateCollectActivity : AppCompatActivity(),View.OnClickListener,GetBillersContract.IViewbillStateCollect {


    internal var imageView1: ImageView? = null
    internal var marketslist: MutableList<BenList>? = ArrayList<BenList>()
    internal var presenter: GetBillersContract.PresenterLoadMarket? =null
    internal var btnsub: Button? =null
    internal var session: SessionManagement? =null
    internal var prgDialog2: ProgressDialog? = null
    internal var amon: EditText? =null
    internal var edacc:EditText? =null
    internal var pno:EditText? = null
    internal var txtamount:EditText? =null
    internal var edname:EditText? =null
    internal var ednumber:EditText? =null
    internal var billid: String? =null
    internal var serviceid:String? =null
    internal var servlabel:String? =null
    internal var servicename:String? =null
    internal var blname:String? =null
    internal var packid:String? =null
    internal var paycode:String? =null
    internal var charge:String? =null
    internal var billname: TextView? =null
    internal var smcno:TextView? =null
    internal var step2:TextView? =null
    internal var step1:TextView? =null

    internal var mobadapt: ArrayAdapter<BenList>? =null

    internal var sp3: Spinner? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_collect)
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
        amon = findViewById(R.id.amount) as EditText
        edacc = findViewById(R.id.acc) as EditText
        billname = findViewById(R.id.textView1) as TextView
        smcno = findViewById(R.id.smcno) as TextView
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading Request....")
        prgDialog2!!.setCancelable(false)


        sp3 = findViewById(R.id.spin3) as Spinner
        btnsub = findViewById(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)

        step2 = findViewById(R.id.tv2) as TextView
        step2!!.setOnClickListener(this)

        step1 = findViewById(R.id.tv) as TextView
        step1!!.setOnClickListener(this)

        txtamount = findViewById(R.id.amount) as EditText
        presenter = StateCollectacivtyPresenter(this, this, FetchServerResponse())
        edname = findViewById(R.id.sendname) as EditText
        ednumber = findViewById(R.id.sendnumber) as EditText


        val ofcListener = MyFocusChangeListener()
        txtamount!!.setOnFocusChangeListener(ofcListener)

        edname!!.setOnFocusChangeListener(ofcListener)
        ednumber!!.setOnFocusChangeListener(ofcListener)


        val intent = intent
        if (intent != null) {

            val bcp = intent.getParcelableExtra<BillMenuParcelable>("bcp")
            billid = bcp.getbillid()
            serviceid = bcp.getserviceid()
            servicename = bcp.getservicename()
            servlabel = bcp.getservlabel()
            blname = bcp.getbillname()

            packid = bcp.getpackid()
            paycode = bcp.getpaymentCode()
            charge = bcp.getcharge()

            val idd = bcp.getidd()

            val dispname = bcp.getdispname()
            if (Utility.isNotNull(charge)) {
                if (charge != "N") {
                    Log.v("Charge", charge)
                    if (charge != "0.0") {
                        txtamount!!.setText(charge)
                    }
                }
            }
            billname!!.setText(dispname)
            smcno!!.setText(servlabel)
            edacc!!.setHint(servlabel)


     presenter!!.loadMarketPlaces(idd)        }
    }
    override fun onResult(BillerList: MutableList<BenList>?) {
        marketslist==BillerList
        if (BillerList != null) {
            if (BillerList!!.size > 0) {
                Collections.sort(BillerList) { d1, d2 -> d1.benmob.compareTo(d2.benmob) }
                val sa = BenList("0000", "Select Market")

                BillerList!!.add(sa)
                mobadapt = ArrayAdapter(this@StateCollectActivity, R.layout.my_spinner, BillerList)
                mobadapt!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp3!!.setAdapter(mobadapt)

                sp3!!.setSelection(BillerList!!.size - 1)
            } else {
                Utility.showToast("No states available  ")
            }
        } else {

        }
    }

    override fun onProcessingError(error: String?) {
        Utility.showToast(error)
    }

    override fun logoutuser() {
        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
    }

    override fun showProgress() {
        prgDialog2!!.show()
    }

    override fun hideProgress() {

            prgDialog2!!.dismiss()

    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {

            if (Utility.checkInternetConnection()) {
                val recanno = edacc!!.getText().toString()
                val amou = txtamount!!.getText().toString()
                val mktindex = sp3!!.getSelectedItemPosition()
                if (mktindex >= 0) {
                    val narra = marketslist!!.get(sp3!!.getSelectedItemPosition()).benName
                    val ednamee = edname!!.getText().toString()
                    val ednumbb = ednumber?.getText().toString()
                    if (Utility.isNotNull(recanno)) {
                        if (Utility.isNotNull(amou)) {
                            val nwamo = amou.replace(",", "")
                            SecurityLayer.Log("New Amount", nwamo)
                            val txamou = java.lang.Double.parseDouble(nwamo)
                            if (txamou >= 100) {
                                if (Utility.isNotNull(narra)) {
                                    if (Utility.isNotNull(ednamee)) {
                                        if (Utility.isNotNull(ednumbb)) {
                                            if (narra != "0000") {

                                                val intent = Intent(this@StateCollectActivity, ConfirmCableActivity::class.java)


                                                //    PayBillResp(params);

                                                val marketname = marketslist?.get(sp3!!.getSelectedItemPosition())!!.benmob
                                                intent.putExtra("custid", recanno)
                                                intent.putExtra("amou", amou)
                                                intent.putExtra("narra", narra)
                                                intent.putExtra("ednamee", ednamee)
                                                intent.putExtra("ednumbb", ednumbb)
                                                intent.putExtra("billid", billid)
                                                intent.putExtra("billname", blname)
                                                intent.putExtra("serviceid", serviceid)
                                                intent.putExtra("servicename", servicename)
                                                intent.putExtra("label", servlabel)
                                                intent.putExtra("packId", narra)
                                                intent.putExtra("paymentCode", paycode)
                                                intent.putExtra("marketname", marketname)

                                                Log.v("new packid", narra)
                                                startActivity(intent)

                                            } else {
                                                Utility.showToast("Please enter a valid value for Market")

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
                                Utility.showToast("Please enter an amount value more than 100 Naira")
                            }
                        } else {
                            Utility.showToast("Please enter a valid value for Amount")
                        }
                    } else {
                        Utility.showToast("Please enter a value for $servlabel")

                    }
                } else {
                    Utility.showToast("Please enter a valid value for Market")
                }
            }


        }
        if (view.getId() == R.id.tv2) {
            finish()
            val intent = Intent(this@StateCollectActivity, BillMenuActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        if (view.getId() == R.id.tv) {
            onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    private inner class MyFocusChangeListener : View.OnFocusChangeListener {

        override fun onFocusChange(v: View, hasFocus: Boolean) {

            if (v.id == R.id.amount && !hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val txt = txtamount!!.getText().toString()
                val fbal = Utility.returnNumberFormat(txt)
                txtamount!!.setText(fbal)

            }

            if (v.id == R.id.ednarr && !hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)


            }
            if (v.id == R.id.sendname && !hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)


            }
            if (v.id == R.id.sendnumber && !hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)


            }

        }
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
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    override fun onDestroy() {
        finish()
        presenter!!.ondestroy()
        super.onDestroy()
    }
}
