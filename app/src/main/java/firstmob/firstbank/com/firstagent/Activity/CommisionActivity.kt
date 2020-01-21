package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import firstmob.firstbank.com.firstagent.adapter.NewCommListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.contract.CommisionContract
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.BalanceEnquirePresenter
import firstmob.firstbank.com.firstagent.presenter.CommisiondatPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*

class CommisionActivity : AppCompatActivity(), CommisionContract.IViewCommission, View.OnClickListener, DatePickerDialog.OnDateSetListener, DateRangePickerFragment.OnDateRangeSelectedListener {


    internal var signup: Button? = null
    // Spinner sp1;
    internal var acc: List<String> = ArrayList()
    internal var temp: List<GetCommPerfData> = ArrayList<GetCommPerfData>()
    internal var data1: Hashtable<String, String>? = null
    var instidacc = ArrayList<String>()
    internal var paramdata = ""
    internal var mArrayAdapter: ArrayAdapter<String>? = null
    internal var phoneContactList = ArrayList<String>()
    internal var planetsList: MutableList<GetCommPerfData> = ArrayList<GetCommPerfData>()
    private var emptyView: TextView? = null
    internal var aAdpt: NewCommListAdapter? = null
    internal var lv: ListView? = null
    internal var ok: Button? = null
    internal  var presenterbalnce: CommisionContract.Presenter? =null
    internal  var presenter: CommisionContract.Presenter? =null
    internal var selacc: String? = null
    internal var prgDialog2: ProgressDialog? = null
    internal var acno: TextView? = null
    internal var txtitle: TextView? = null
    internal var txcomrepo: TextView? = null
    internal var txtcomrepo: TextView? = null
    internal var commamo: TextView? = null
    internal var txfrom: TextView? = null
    internal var accno: EditText? = null
    internal var mobno: EditText? = null
    internal var fnam: EditText? = null
    internal var session: SessionManagement? = null
    internal var calendar: Button? = null
    internal var lstmt: LinearLayout? = null
    private var mToolbar: Toolbar? = null

    internal var format1 = SimpleDateFormat("" + "MMMM dd yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commision)
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        //  mToolbar.setTitle("Inbox");
        setSupportActionBar(mToolbar)
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab!!.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)


        lv = findViewById(R.id.lv) as ListView
        txtitle = findViewById(R.id.bname) as TextView
        txcomrepo = findViewById(R.id.textViewweryu) as TextView
        txtcomrepo = findViewById(R.id.textViewwaq) as TextView
        commamo = findViewById(R.id.txtagcomm) as TextView
        txtitle = findViewById(R.id.bname) as TextView
        txfrom = findViewById(R.id.from) as TextView
        //   sp1 = (Spinner) rootView.findViewById(R.id.accno);
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading ....")
        lstmt = findViewById(R.id.stmtly) as LinearLayout
        lstmt!!.setOnClickListener(this)
        presenterbalnce= BalanceEnquirePresenter(this, this, FetchServerResponse())
        presenter = CommisiondatPresenter(this, this, FetchServerResponse())
        // Set Cancelable as False
        session = SessionManagement(this)
        calendar = findViewById(R.id.button4) as Button
        calendar!!.setOnClickListener(this)
        prgDialog2!!.setCancelable(false)
        emptyView = findViewById(R.id.empty_view) as TextView
        val usid = Utility.gettUtilUserId(this)
        txtcomrepo!!.setText("Commission By User $usid")
        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)

        now.set(year, month, 1)

        println(cal.time)
// Output "Wed Sep 26 14:23:28 EST 2012"

        val formattednow = format1.format(cal.time)
        val formattedstartdate = format1.format(now.time)
// Output "2012-09-26"
        //   txtitle.setText("Commission Report for "+formattedstartdate+" to "+formattednow);

        txtitle!!.setText(formattednow)
        txfrom!!.setText(formattedstartdate)
        presenterbalnce!!.requestCall("getbalnce", null)
        //setBalInquSec()

        lv!!.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, position, l ->
            val p = planetsList[position]
            val txncode = p.txnCode
            val toAcnum = p.gettoAcNum()
            val fromacnum = p.fromAcnum
            val txtrfno = p.getrefNumber()
            val servtype = Utility.convertTxnCodetoServ(txncode)
            val txtdatetime = p.txndateTime
            val dbagcsmn = p.agentCmsn
            val agcsmn = java.lang.Double.toString(dbagcsmn)
            val fbal = Utility.returnNumberFormat(agcsmn)
            val amoo = Constants.KEY_NAIRA + fbal
            val fm = supportFragmentManager
            val editNameDialog = CommReceipt()
            val bundle = Bundle()
            bundle.putString("narr", fromacnum)
            bundle.putString("narrtor", toAcnum)
            bundle.putString("refno", txtrfno)
            bundle.putString("datetime", txtdatetime)
            bundle.putString("servtype", servtype)
            bundle.putString("amo", amoo)
            editNameDialog.setArguments(bundle)
            editNameDialog.show(fm, "fragment_edit_name")
        })

    }

    override fun onClick(v: View?) {
        if (v!!.getId() == R.id.button4) {
            val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)
            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
        }
    }

    override fun onResult(flag : String?,response: String?) {
        if(flag.equals("getbalnce")){
            reponseBalanceEnqury(response)
        }else
        {
        responseComiision(response)
         }

    }

    override fun onProcessingError(error: String?) {
        Utility.showToast(error)
    }

    override fun showProgress() {
        if (prgDialog2 != null && applicationContext != null && !this@CommisionActivity.isFinishing()) {
            prgDialog2!!.show()
        }
    }

    override fun hideProgress() {
        if (prgDialog2 != null && applicationContext != null && !this@CommisionActivity.isFinishing()) {
            prgDialog2!!.hide()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        /* String date = " From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        txtitle.setText(date);*/

        val clfrom = Calendar.getInstance()
        val clto = Calendar.getInstance()
        clfrom.set(year, monthOfYear, dayOfMonth)
        clto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)

        val formattedfrom = format1.format(clfrom.time)
        val formattedto = format1.format(clto.time)
        txtitle!!.setText(formattedto)
        txfrom!!.setText(formattedfrom)
        ++monthOfYear
        ++monthOfYearEnd
        val calfrom = Calendar.getInstance()
        val calto = Calendar.getInstance()
        calto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)
        calfrom.set(year, monthOfYear, dayOfMonth)

        if (calfrom.before(calto)) {
            //   fromdate.setText(date);
            var frmdymonth = Integer.toString(dayOfMonth)
            if (dayOfMonth < 10) {
                frmdymonth = "0$frmdymonth"
            }
            var frmyear = Integer.toString(year)
            frmyear = frmyear.substring(2, 4)
            val fromd = "$frmdymonth-$monthOfYear-$frmyear"
            var frmenddymonth = Integer.toString(dayOfMonthEnd)
            if (dayOfMonthEnd < 10) {
                frmenddymonth = "0$frmenddymonth"
            }

            var frmendyr = Integer.toString(yearEnd)
            frmendyr = frmendyr.substring(2, 4)
            val endd = "$frmenddymonth-$monthOfYearEnd-$frmendyr"


            presenter!!.requestCall("minstatemnt", fromd + "/" + endd)
            //SetMinist(fromd, endd)
        } else {
            Utility.showToast("Please ensure the from date is before the after date")
        }
    }

    override fun onDateRangeSelected(dayOfMonth: Int, monthOfYear: Int, year: Int, dayOfMonthEnd: Int, monthOfYearEnd: Int, yearEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        /*  String date = " From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        txtitle.setText(date);*/

        val clfrom = Calendar.getInstance()
        val clto = Calendar.getInstance()
        clfrom.set(year, monthOfYear, dayOfMonth)
        clto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)

        val formattedfrom = format1.format(clfrom.time)
        val formattedto = format1.format(clto.time)
        txtitle!!.setText(formattedto)
        txfrom!!.setText(formattedfrom)
        ++monthOfYear
        ++monthOfYearEnd
        val calfrom = Calendar.getInstance()
        val calto = Calendar.getInstance()
        calto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)
        calfrom.set(year, monthOfYear, dayOfMonth)

        if (calfrom.before(calto)) {
            //   fromdate.setText(date);
            var frmdymonth = Integer.toString(dayOfMonth)
            if (dayOfMonth < 10) {
                frmdymonth = "0$frmdymonth"
            }
            var frmyear = Integer.toString(year)
            frmyear = frmyear.substring(2, 4)
            val fromd = "$frmdymonth-$monthOfYear-$frmyear"
            var frmenddymonth = Integer.toString(dayOfMonthEnd)
            if (dayOfMonthEnd < 10) {
                frmenddymonth = "0$frmenddymonth"
            }

            var frmendyr = Integer.toString(yearEnd)
            frmendyr = frmendyr.substring(2, 4)
            val endd = "$frmenddymonth-$monthOfYearEnd-$frmendyr"

            presenter!!.requestCall("minstatemnt", fromd + "/" + endd)
            //SetMinist(fromd, endd)
        } else {
            Toast.makeText(
                    applicationContext,
                    "Please ensure the from date is before the after date",
                    Toast.LENGTH_LONG).show()


        }
    }

    override fun onPause() {
        super.onPause()
        val secs = Date().time / 1000
        SecurityLayer.Log("Seconds Loged", java.lang.Long.toString(secs))
        session!!.putCurrTime(secs)
    }


    override fun onDestroy() {
        dismissProgressDialog()
        presenter!!.ondestroy()
        finish()
        super.onDestroy()

    }

    fun dismissProgressDialog() {
        if (prgDialog2 != null && prgDialog2!!.isShowing() && !this@CommisionActivity.isFinishing()) {
            prgDialog2!!.dismiss()
        }
    }

    fun SetForceOutDialog(msg: String, title: String, c: Context?) {
        if (c != null) {
            MaterialDialog.Builder(this@CommisionActivity)
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

    fun reponseBalanceEnqury(response: String?) {
        try {
            // JSON Object

            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")


            val plan = obj.optJSONObject("data")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);


            if (response != null) {
                if (respcode == "00") {

                    SecurityLayer.Log("Response Message", responsemessage)

                    //                                     SecurityLayer.Log("Respnse getResults",datas.toString());
                    if (plan != null) {
                        val balamo = plan!!.optString("balance")
                        val comamo = plan!!.optString("commission")
                        val cmbal = Utility.returnNumberFormat(comamo)
                        //   cmbal = Utility.roundto2dp(cmbal);
                        commamo!!.setText(Constants.KEY_NAIRA + cmbal)
                    } else {
                        Toast.makeText(
                                applicationContext,
                                "There was an error retrieving your balance ",
                                Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(
                            applicationContext,
                            "There was an error retrieving your balance ",
                            Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                        applicationContext,
                        "There was an error retrieving your balance ",
                        Toast.LENGTH_LONG).show()
            }


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(applicationContext, getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // SecurityLayer.Log(e.toString());
        }
        if (applicationContext != null) {
            if (Utility.checkInternetConnection()) {

                val now = Calendar.getInstance()
                val year = now.get(Calendar.YEAR)
                var month = now.get(Calendar.MONTH) // Note: zero based!
                val day = now.get(Calendar.DAY_OF_MONTH)

                month = month + 1

                var frmdymonth = Integer.toString(day)
                if (day < 10) {
                    frmdymonth = "0$frmdymonth"
                }
                var frmyear = Integer.toString(year)
                frmyear = frmyear.substring(2, 4)
                val tdate = "$frmdymonth-$month-$frmyear"
                val firdate = "01-$month-$frmyear"

                val calfrom = Calendar.getInstance()
                calfrom.set(year, month, 1)
                presenter!!.requestCall("minstatemnt", firdate + "/" + tdate)
                // SetMinist(firdate, tdate)
            }
        }
    }

    fun responseComiision(response: String?) {
        try {
            // JSON Object


            SecurityLayer.Log("Cable TV Resp", response)
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())


            val comdatas = obj.optJSONObject("data")
            val comperf = comdatas.optJSONArray("transaction")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);
            var tott = 0.0
            if (response != null) {
                val respcode = obj.optString("responseCode")
                val responsemessage = obj.optString("message")

                SecurityLayer.Log("Response Message", responsemessage)

                if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                    if (!Utility.checkUserLocked(respcode)) {
                        SecurityLayer.Log("Response Message", responsemessage)

                        if (respcode == "00") {
                            SecurityLayer.Log("JSON Aray", comperf.toString())
                            if (comperf.length() > 0) {


                                var json_data: JSONObject? = null
                                for (i in 0 until comperf.length()) {
                                    json_data = comperf.getJSONObject(i)
                                    //String accid = json_data.getString("benacid");

                                    var fintoacnum = ""
                                    var finfromacnum = ""
                                    val txnCode = json_data!!.optString("txnCode")
                                    val agentCmsn = json_data.optDouble("agentCmsn")
                                    val txndateTime = json_data.optString("txndateTime")
                                    val amount = json_data.optString("amount")
                                    val status = json_data.optString("status")
                                    var toAcNum = json_data.optString("toAcNum")
                                    val refNumber = json_data.optString("refNumber")
                                    var fromaccnum = json_data.optString("fromAccountNum")
                                    if (txnCode == "CASHDEP" || txnCode == "FTINTRABANK") {
                                        fintoacnum = fromaccnum
                                        finfromacnum = toAcNum
                                        toAcNum = fintoacnum
                                        fromaccnum = finfromacnum
                                    }
                                    if (status == "SUCCESS" && agentCmsn > 0) {
                                        tott += agentCmsn
                                        planetsList.add(GetCommPerfData(txnCode, txndateTime, agentCmsn, status, amount, toAcNum, refNumber, fromaccnum))

                                    }


                                }
                                if (applicationContext != null) {
                                    aAdpt = NewCommListAdapter(planetsList, this@CommisionActivity)


                                    lv!!.setAdapter(aAdpt)
                                }


                            }

                        } else {
                            if (applicationContext != null) {
                                Toast.makeText(
                                        applicationContext,
                                        "" + responsemessage,
                                        Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        finish()
                        startActivity(Intent(this@CommisionActivity, SignInActivity::class.java))
                        Toast.makeText(
                                applicationContext,
                                "You have been locked out of the app.Please call customer care for further details",
                                Toast.LENGTH_LONG).show()

                    }
                } else {
                    if (applicationContext != null) {
                        Toast.makeText(
                                applicationContext,
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show()
                    }

                }
                var fincommrpt = java.lang.Double.toString(tott)
                fincommrpt = Utility.returnNumberFormat(fincommrpt)
                txcomrepo!!.setText(Constants.KEY_NAIRA + fincommrpt)
            } else {
                if (applicationContext != null) {
                    Toast.makeText(
                            applicationContext,
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show()
                }

            }
            // prgDialog2.dismiss();


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(applicationContext, getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)

            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)

            // SecurityLayer.Log(e.toString());
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}
