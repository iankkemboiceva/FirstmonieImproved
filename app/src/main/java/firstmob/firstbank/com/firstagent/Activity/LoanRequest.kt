package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.model.GetStores
import firstmob.firstbank.com.firstagent.model.ObservableWebView
import firstmob.firstbank.com.firstagent.model.ObservableWebView.OnScrollChangedCallback
import firstmob.firstbank.com.firstagent.network.ApiInterface
import firstmob.firstbank.com.firstagent.network.RetrofitInstance
import firstmob.firstbank.com.firstagent.security.SecurityLayer

import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.loanreq.*
import kotlinx.android.synthetic.main.toolbarnewui.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class LoanRequest : BaseActivity(), View.OnClickListener {
    var imageView1: ImageView? = null
    var amon: EditText? = null
    var edacc: EditText? = null
    var pno: EditText? = null
    var txtamount: EditText? = null
    var txtnarr: EditText? = null
    var pin: EditText? = null
    var btnsub: Button? = null

    var storelist: MutableList<GetStores>? = ArrayList()
    var maradapt: ArrayAdapter<GetStores>? = null
    var accountoname: TextView? = null
    var depositid: String? = null
    var acname: String? = null
    var storeidd: String? = null
    var amolimit = "NA"


    var prgDialog: ProgressDialog? = null
    var txelig: TextView? = null
    var txstorename: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loanreq)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.nbkyellow)));

        titlepg.text="Agent Credit"


        txstorename = findViewById<View>(R.id.storid) as TextView
        txelig = findViewById<View>(R.id.txtelig) as TextView
        amon = findViewById<View>(R.id.amount) as EditText
        prgDialog = ProgressDialog(this)
        prgDialog!!.setMessage("Loading ....")
        prgDialog!!.setCancelable(false)
        txtamount = findViewById<View>(R.id.amount) as EditText


        val ofcListener: OnFocusChangeListener = MyFocusChangeListener()
        txtamount!!.onFocusChangeListener = ofcListener
        SetStores()
        btnsub = findViewById<View>(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)
        NameInquirySec()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private inner class MyFocusChangeListener : OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (v.id == R.id.amount && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val txt = txtamount!!.text.toString()
                val fbal = Utility.returnNumberFormat(txt)
                Log.v("Amount", txt)
                txtamount!!.setText(fbal)
            }
            if (v.id == R.id.ednarr && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            if (v.id == R.id.sendname && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            if (v.id == R.id.sendnumber && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            if (v.id == R.id.input_payacc && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button4) {

            //   checkInternetConnection2();
        }
        if (view.id == R.id.button2) {
            val amont = amon!!.text.toString()
            val storeid = storeidd
            if (Utility.isNotNull(amont)) {
                val inpamo = amont.toDouble()
                if (inpamo >= 100) {
                    val dbamolimit = amolimit.toDouble()
                    if (inpamo <= dbamolimit) {
                        val blchkend = false
                        val webView = ObservableWebView(applicationContext)
                        webView.loadUrl("file:///android_asset/agentcredittc.html")
                        val builder = AlertDialog.Builder(this@LoanRequest)
                        builder.setTitle("Terms and Conditions")
                                .setView(webView)
                                .setNeutralButton("Reject") { dialog, which ->
                                    Toast.makeText(applicationContext, "Sorry, the application cannot continue", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                .setPositiveButton("Accept") { dialog, which ->
                                    /*   Intent intent = new Intent(LoanRequest.this, ConfirmLoanRequest.class);
                                                                intent.putExtra("amount", amont);
                                                                intent.putExtra("storeid", storeid);
                                                                startActivity(intent);*/
                                }
                        val dialog = builder.show()
                        val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        button.visibility = View.GONE
                        webView.onScrollChangedCallback = object : OnScrollChangedCallback {
                            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
                                if (t > oldt) { //Do stuff
                                    println("Swipe UP")
                                    //Do stuff
                                } else if (t < oldt) {
                                    println("Swipe Down")
                                }
                                val height = Math.floor(webView.contentHeight * webView.scale.toDouble()).toInt()
                                val webViewHeight = webView.height
                                val cutoff = height - webViewHeight - 10 // Don't be too strict on the cutoff point
                                if (t >= cutoff) {
                                    button.visibility = View.VISIBLE
                                }
                            }

                            override fun onScroll(l: Int, t: Int) {}
                        }
                    } else {
                        Toast.makeText(applicationContext, "Kindly enter an amount below your loan limit", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Please enter credit amount greater than 100" + Constants.KEY_NAIRA, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun SetDialog(msg: String?, title: String?) {
        MaterialDialog.Builder(applicationContext)
                .title(title!!)
                .content(msg!!)
                .negativeText("Close")
                .show()
    }

    private fun NameInquirySec() {
        prgDialog!!.show()
        val adminid = Prefs.getString("SUPERID","NA")
        val apiService = RetrofitInstance.getAgentCreditInstance().create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("userId", adminid)
            paramObject.put("channel", "1")
            paramObject.put("storeId", storeidd)
            val call = apiService.loaneligibility(paramObject.toString())
            call.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    try { // JSON Object
                        SecurityLayer.Log("response..:", response.body())
                        val obj = JSONObject(response.body())
                        //obj = Utility.onresp(obj,getApplicationContext());
                        SecurityLayer.Log("decrypted_response", obj.toString())
                        val respcode = obj.optString("responseCode")
                        val responsemessage = obj.optString("message")
                        val plan = obj.optJSONObject("data")
                        //Prefs.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (Utility.checkUserLocked(respcode)) {
                                LogOut()
                            }
                            if (response.body() != null) {
                                if (respcode == "00" || respcode == "Success") {
                                    SecurityLayer.Log("Response Message", responsemessage)
                                    amolimit = plan.optString("creditLimit")
                                    amolimit = Utility.roundto2dp(amolimit)
                                    txelig!!.text = "Congratulations,you are eligible for a loan upto " + amolimit + Constants.KEY_NAIRA
                                    lyamo!!.visibility = View.VISIBLE
                                    lybut!!.visibility = View.VISIBLE
                                    //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                } else {
                                    txelig!!.text = responsemessage
                                    txelig!!.setTextColor(resources.getColor(R.color.black))
                                    Toast.makeText(
                                            applicationContext,
                                            responsemessage,
                                            Toast.LENGTH_LONG).show()
                                    lyamo!!.visibility = View.GONE
                                    lybut!!.visibility = View.GONE
                                }
                            } else {
                                txelig!!.text = "There was an error processing your loan eligibility request"
                                Toast.makeText(
                                        applicationContext,
                                        "There was an error processing your request ",
                                        Toast.LENGTH_LONG).show()
                                lyamo!!.visibility = View.GONE
                                lybut!!.visibility = View.GONE
                            }
                        }
                    } catch (e: JSONException) {
                        SecurityLayer.Log("encryptionJSONException", e.toString())
                        // TODO Auto-generated catch block
                        if (applicationContext != null) {
                            Toast.makeText(applicationContext, applicationContext.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
                            // SecurityLayer.Log(e.toString());
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
                        }
                    } catch (e: Exception) {
                        SecurityLayer.Log("encryptionJSONException", e.toString())
                        if (applicationContext != null) {
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    try {
                        if (prgDialog != null && prgDialog!!.isShowing && applicationContext != null) {
                            prgDialog!!.dismiss()
                        }
                    } catch (e: IllegalArgumentException) { // Handle or log or ignore
                    } catch (e: Exception) { // Handle or log or ignore
                    } finally { //   prgDialog = null;
                    }
                    //   prgDialog.dismiss();
                }

                override fun onFailure(call: Call<String?>, t: Throwable) { // Log error here since request failed
                    SecurityLayer.Log("Throwable error", t.toString())
                    if (prgDialog != null && prgDialog!!.isShowing && applicationContext != null) {
                        prgDialog!!.dismiss()
                    }
                    if (applicationContext != null) {
                        Toast.makeText(
                                applicationContext,
                                "There was an error processing your request",
                                Toast.LENGTH_LONG).show()
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
                    }
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun SetForceOutDialog(msg: String?, title: String?, c: Context?) {
        if (c != null) {
            MaterialDialog.Builder(this)
                    .title(title!!)
                    .content(msg!!)
                    .negativeText("CONTINUE")
                    .callback(object : ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog) {
                            dialog.dismiss()
                        }

                        override fun onNegative(dialog: MaterialDialog) {
                            dialog.dismiss()
                            finish()

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

    private fun dismissProgressDialog() {
        if (prgDialog != null && prgDialog!!.isShowing) {
            prgDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun LogOut() {

        // After logout redirect user to Loing Activity
        finish()
        val i = Intent(applicationContext, SignInActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        // Staring Login Activity
        startActivity(i)
        Toast.makeText(
                applicationContext,
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show()
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();
    }

    fun SetStores() {
        val strjsarray = Prefs.getString("STORES","NA")
        SecurityLayer.Log("stores", strjsarray)
        var servdata: JSONArray? = null
        try {
            servdata = JSONArray(strjsarray)
            if (servdata.length() > 0) {
                var json_data: JSONObject? = null
                for (i in 0 until servdata.length()) {
                    json_data = servdata.getJSONObject(i)
                    val storeid = json_data.optString("id")
                    val storename = json_data.optString("name")
                    storelist!!.add(GetStores(storeid, storename))
                }
                if (storelist != null) {
                    if (storelist!!.size > 0) {
                        storeidd = storelist!![0].getstoreid()
                        val storename = storelist!![0].getstorename()
                        txstorename!!.text = storename
                    } else {
                        Toast.makeText(
                                applicationContext,
                                "No stores available  ",
                                Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}