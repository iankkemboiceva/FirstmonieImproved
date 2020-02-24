package firstmob.firstbank.com.firstagent.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.adapter.NewMinListAdapter
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.CommisionContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.MinistatData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.BalanceEnquirePresenter
import firstmob.firstbank.com.firstagent.presenter.MinstatementPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.toolbarnewui.*
import org.json.JSONException
import org.json.JSONObject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MinistatActivity : AppCompatActivity(),View.OnClickListener,DatePickerDialog.OnDateSetListener,DateRangePickerFragment.OnDateRangeSelectedListener,CommisionContract.IViewMinistatement {
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    private var mToolbar: Toolbar? = null
    internal var planetsList: MutableList<MinistatData> = ArrayList<MinistatData>()
    private var emptyView: TextView? = null
    internal var aAdpt: NewMinListAdapter? = null
    internal var lv: ListView? =null
    internal var ok: Button? = null
    var session :SessionManagement? =null
    internal var selacc: String? = null
    //internal var prgDialog2: ProgressDialog? = null
    var viewDialg: ViewDialog? =null
    internal var acno: TextView? = null
    internal var txaco:TextView? =null
    internal var txaccbal:TextView? =null
    internal var accno: EditText? = null
    internal var mobno:EditText? = null
    internal var fnam:EditText? = null
    internal var calendar: Button? =null
    internal var lstmt: LinearLayout? =null
    internal var txtitle: TextView? =null
    internal var txfrom:TextView? =null
    internal var presenter: CommisionContract.PresenterMinista? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ministat)
        mToolbar = findViewById<Toolbar>(R.id.toolbar)
        //  mToolbar.setTitle("Inbox");
        setSupportActionBar(mToolbar)
        val ab = supportActionBar
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)))
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true)
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true)
        ab.setDisplayShowTitleEnabled(false)
        titlepg.text="Ministatement"
        txtitle = findViewById<TextView>(R.id.enddate)
        txfrom = findViewById<TextView>(R.id.from)
        lv = findViewById<ListView>(R.id.lv)
        txaco = findViewById<TextView>(R.id.bname)
        txaccbal = findViewById<TextView>(R.id.accountbalance)
        lstmt = findViewById<LinearLayout>(R.id.stmtrlyy)
        lstmt!!.setOnClickListener(this)
        //   sp1 = (Spinner) rootView.findViewById(R.id.accno);
        viewDialg= ViewDialog(this)
        session = SessionManagement(this)
        calendar = findViewById<Button>(R.id.button4)
        calendar!!.setOnClickListener(this)
        presenter = MinstatementPresenter(applicationContext, this, FetchServerResponse())
        emptyView = findViewById<TextView>(R.id.empty_view)
        val accnoo = Prefs.getString(SharedPrefConstants.KEY_ACCO,"NA")
        txaco!!.text = "Statement for Account Number -$accnoo"
        presenter!!.requestCallGetBalnce("getblance",null)
       // presenterbalance!!.requestCall("getblance",null)
        //   setBalInquSec()
    }
    override fun onClick(v: View?) {
        if (v!!.id == R.id.button4) {
            val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)
            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
        }
    }

    override fun NavigateToSognIn() {
        finish()
        startActivity(Intent(applicationContext, SignInActivity::class.java))
    }

    override fun PopulateRecyclerView(planetsList: MutableList<MinistatData>?) {
        if (applicationContext != null) {
            aAdpt = NewMinListAdapter(planetsList, this@MinistatActivity)
            lv!!.adapter = aAdpt
        }
    }

    override fun ForceLogout() {
        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), applicationContext)
    }

    override fun setBalance(balance: String?) {
        txaccbal!!.text = balance
    }
    override fun onProcessingError(error: String?) {
        Utility.showToast(error)
    }

    override fun showProgress() {
        if (viewDialg != null && applicationContext != null && !this@MinistatActivity.isFinishing) {
            viewDialg!!.showDialog()
        }
    }

    override fun hideProgress() {
        if (viewDialg != null && applicationContext != null && !this@MinistatActivity.isFinishing) {
            viewDialg!!.hideDialog()
           // prgDialog2!!.dismiss()
        }
    }

    override fun setMinistatementStartim() {
        setMinistatementStart()
    }
    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        val date = "From- " + dayOfMonth + "/" + ++monthOfYear + "/" + year + " To " + dayOfMonthEnd + "/" + ++monthOfYearEnd + "/" + yearEnd
        //txtitle.setText(date);
        val calfrom = Calendar.getInstance()
        val calto = Calendar.getInstance()
        calto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)
        calfrom.set(year, monthOfYear, dayOfMonth)
        var strmnthyearend: String? = null
        if (calfrom.before(calto)) {
            //   fromdate.setText(date);
            var frmdymonth = Integer.toString(dayOfMonth)
            var strmnthyear = Integer.toString(monthOfYear)
            strmnthyearend = Integer.toString(monthOfYearEnd)
            if (dayOfMonth < 10) {
                frmdymonth = "0$frmdymonth"
            }
            if (monthOfYear < 10) {
                strmnthyear = "0$strmnthyear"
            }
            if (monthOfYearEnd < 10) {
                strmnthyearend = "0" + strmnthyearend!!
            }
            var frmyear = Integer.toString(year)
            frmyear = frmyear.substring(0, 4)

            val fromd = "$frmyear-$strmnthyear-$frmdymonth"
            var frmenddymonth = Integer.toString(dayOfMonthEnd)
            if (dayOfMonthEnd < 10) {
                frmenddymonth = "0$frmenddymonth"
            }

            var frmendyr = Integer.toString(yearEnd)
            frmendyr = frmendyr.substring(0, 4)
            val endd = "$frmendyr-$strmnthyearend-$frmenddymonth"
            presenter!!.requestCallMinistat("mistate",fromd+"/"+endd)
            //SetMinist(fromd, endd)

        } else {
            Utility.showToast("Please ensure the from date is before the after date")

        }
    }

   override fun onDateRangeSelected(dayOfMonth: Int, monthOfYear: Int, year: Int, dayOfMonthEnd: Int, monthOfYearEnd: Int, yearEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        // String date = "From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
        //txtitle.setText(date);

        val clfrom = Calendar.getInstance()
        val clto = Calendar.getInstance()
        clfrom.set(year, monthOfYear, dayOfMonth)
        clto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)
        val format2 = SimpleDateFormat("" + "MMMM dd yyyy")
        val formattedfrom = format2.format(clfrom.time)
        val formattedto = format2.format(clto.time)
       txtitle!!.text = formattedto
       txfrom!!.text = formattedfrom
        ++monthOfYear
        ++monthOfYearEnd
        val calfrom = Calendar.getInstance()
        val calto = Calendar.getInstance()
        calto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)
        calfrom.set(year, monthOfYear, dayOfMonth)
        var strmnthyearend: String? = null
        if (calfrom.before(calto)) {
            //   fromdate.setText(date);
            var frmdymonth = Integer.toString(dayOfMonth)
            var strmnthyear = Integer.toString(monthOfYear)
            strmnthyearend = Integer.toString(monthOfYearEnd)
            if (dayOfMonth < 10) {
                frmdymonth = "0$frmdymonth"
            }
            if (monthOfYear < 10) {
                strmnthyear = "0$strmnthyear"
            }
            if (monthOfYearEnd < 10) {
                strmnthyearend = "0" + strmnthyearend!!
            }
            var frmyear = Integer.toString(year)
            frmyear = frmyear.substring(0, 4)

            val fromd = "$frmyear-$strmnthyear-$frmdymonth"
            var frmenddymonth = Integer.toString(dayOfMonthEnd)
            if (dayOfMonthEnd < 10) {
                frmenddymonth = "0$frmenddymonth"
            }

            var frmendyr = Integer.toString(yearEnd)
            frmendyr = frmendyr.substring(0, 4)
            val endd = "$frmendyr-$strmnthyearend-$frmenddymonth"
            presenter!!.requestCallMinistat("mistate",fromd+"/"+endd)
        } else {
            Utility.showToast("Please ensure the from date is before the after date")
        }
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    fun setMinistatementStart(){
        if (applicationContext != null) {
            if (Utility.checkInternetConnection()) {
                if (Utility.checkInternetConnection()) {
                    val format2 = SimpleDateFormat("" + "MMMM dd yyyy")

                    val cal = Calendar.getInstance()
                    val format1 = SimpleDateFormat("" + "yyyy-MM-dd")
                    val formattednow = format1.format(cal.time)
                    //    SetMinist("2017-01-01", formattednow);


                    val now = Calendar.getInstance()
                    val year = now.get(Calendar.YEAR)
                    var month = now.get(Calendar.MONTH) // Note: zero based!
                    val day = now.get(Calendar.DAY_OF_MONTH)
                    now.set(year, month, 1)


                    println(cal.time)


                    val formattednowa = format1.format(cal.time)
                    val formattedstartdate = format1.format(now.time)

                    Log.v("Formatet Date", formattedstartdate)


                    val formatteduserend = format2.format(cal.time)
                    val formatteduserstart = format2.format(now.time)
                    txtitle!!.text = formatteduserend
                    txfrom!!.text = formatteduserstart
                    //  checkInternetConnection2();


                    month = month + 1

                    var frmdymonth = Integer.toString(day)
                    if (day < 10) {
                        frmdymonth = "0$frmdymonth"
                    }
                    var frmyear = Integer.toString(year)
                    frmyear = frmyear.substring(2, 4)
                    val calfrom = Calendar.getInstance()
                    calfrom.set(year, month, 1)

                   // SetMinist(formattedstartdate, formattednow)
                    presenter!!.requestCallMinistat("mistate",formattedstartdate+"/"+formattednow)

              //  presenter!!.requestCall("mistate",formattedstartdate+"/"+formattednow)
                //SetMinist(formattedstartdate, formattednow)


            }
        }
    }}

    fun SetForceOutDialog(msg: String, title: String, c: Context?) {
        if (c != null) {
            MaterialDialog.Builder(c)
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.ondestroy()
        finish()
    }

    override fun onPause() {
        super.onPause()

        val secs = Date().time / 1000
        SecurityLayer.Log("Seconds Loged", java.lang.Long.toString(secs))
        session!!.putCurrTime(secs)
    }
}

