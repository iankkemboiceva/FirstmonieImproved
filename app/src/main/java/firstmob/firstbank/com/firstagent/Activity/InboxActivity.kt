package firstmob.firstbank.com.firstagent.Activity




import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA
import firstmob.firstbank.com.firstagent.contract.InboxContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.CommReceipt
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.InboxPresenter
import firstmob.firstbank.com.firstagent.utils.Utility.convertTxnCodetoServ
import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat
import kotlinx.android.synthetic.main.inbox.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*


class InboxActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, DateRangePickerFragment.OnDateRangeSelectedListener, InboxContract.ILoginView {


    internal var signup: Button? = null
    internal var sp1: Spinner? = null
    internal var phoneContactList = ArrayList<String>()
    internal var inboxlist = ArrayList<GetCommPerfData>()
    // private TextView emptyView;
    internal var aAdpt: InboxListAdapter? = null

    internal var ok: Button? = null

    internal var acno: TextView? = null
    internal var accno: EditText? = null
    internal var mobno: EditText? = null
    internal var fnam: EditText? = null


    internal lateinit var txtitle: TextView
    internal lateinit var txfrom: TextView



    internal var tdate: String? = null
    internal var firdate: String? = null
    internal var editsearch: EditText? = null

    internal var format1 = SimpleDateFormat("" + "MMMM dd yyyy")

    var viewDialog: ViewDialog? = null
    internal lateinit var presenter: InboxContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inboxact)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.fbnlightblue)));
        // Set Cancelable as False

        viewDialog = ViewDialog(this)

        presenter = InboxPresenter(this, FetchServerResponse())


        calendar.setOnClickListener {

            val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)
            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
        }


        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        var month = now.get(Calendar.MONTH) // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)


        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(arg0: String): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // TODO Auto-generated method stub
                if (aAdpt != null) {
                    aAdpt!!.filter.filter(query)
                }
                return false
            }
        })
        sv.isIconified = false


        now.set(year, month, 1)


        println(cal.time)
        // Output "Wed Sep 26 14:23:28 EST 2012"

        val formattednow = format1.format(cal.time)
        val formattedstartdate = format1.format(now.time)
        // Output "2012-09-26"
        txtenddat.text = formattednow
        txfromdate.text = formattedstartdate
        //  checkInternetConnection2();


        month += 1

        var frmdymonth = day.toString()
        if (day < 10) {
            frmdymonth = "0$frmdymonth"
        }
        var frmyear = Integer.toString(year)
        frmyear = frmyear.substring(2, 4)
        val tdate = "$frmdymonth-$month-$frmyear"
        val firdate = "01-$month-$frmyear"

        val calfrom = Calendar.getInstance()
        calfrom.set(year, month, 1)



        SetMinist(firdate, tdate)





        lv.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            val p = inboxlist[position]
            val txncode = p.txnCode
            val toAcnum = p.gettoAcNum()
            val fromacnum = p.fromAcnum
            val txtrfno = p.getrefNumber()

            val servtype = convertTxnCodetoServ(txncode)
            val txtdatetime = p.txndateTime
            val statuss = p.status


            val amoo = KEY_NAIRA + returnNumberFormat(p.amount)
            val fm = supportFragmentManager
              val editNameDialog = CommReceipt()
              val bundle = Bundle()
              bundle.putString("narr", fromacnum)
              bundle.putString("refno", txtrfno)
              bundle.putString("datetime", txtdatetime)
              bundle.putString("amo", amoo)
              bundle.putString("servtype", servtype)
              bundle.putString("status", statuss)
              bundle.putString("narrtor", toAcnum)
              editNameDialog.setArguments(bundle)
              editNameDialog.show(fm, "fragment_edit_name")
        }


    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }



    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        Log.v("Am i in", "Yes")
        val inflater = menuInflater
        inflater.inflate(R.menu.inboxlistmenu, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val index = info.position //Use this for getting the list item value
        val view = info.targetView
        when (item.itemId) {
            R.id.logcomp -> {
                Log.d("onContextItemSelected", "Remove Pressed")

                /*

                val txamo = planetsList[index].getAmount()
                val txacno = planetsList[index].gettoAcNum()
                val txrefno = planetsList[index].getrefNumber()
                val txtdate = planetsList[index].getTxndateTime()
                val b = Bundle()
                b.putString("txamo", txamo)
                b.putString("txaco", txacno)
                b.putString("txref", txrefno)
                b.putString("txdate", txtdate)
                val pIntent = Intent(this, LogCompActivity::class.java)


                pIntent.putExtras(b)
                startActivity(pIntent)
                return true*/
                return true
            }


            else -> return super.onContextItemSelected(item)
        }
    }


    private fun SetMinist(stdate: String, enddate: String) {


        sv.setQuery("", false)
        sv.clearFocus()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)


        var frmdymonth = day.toString()
        if (day < 10) {
            frmdymonth = "0$frmdymonth"
        }
        var frmyear = year.toString()
        frmyear = frmyear.substring(2, 4)
        val format1 = SimpleDateFormat("" + "MMMM dd yyyy")
        val fdate = "$frmdymonth-$month-$frmyear"
        val noww = Calendar.getInstance()
        val yearr = now.get(Calendar.YEAR)
        val monthh = now.get(Calendar.MONTH) // Note: zero based!

        noww.set(yearr, monthh, 1)
        val formattedstartdate = "01-$month-$frmyear"

        inboxlist.clear()

        presenter.Inbox(stdate, enddate)


    }


    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        //   String date = "Inbox : From- " + dayOfMonth + "/" + (monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (monthOfYearEnd) + "/" + yearEnd;

        val clfrom = Calendar.getInstance()
        val clto = Calendar.getInstance()
        clfrom.set(year, monthOfYear, dayOfMonth)
        clto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)

        val formattedfrom = format1.format(clfrom.time)
        val formattedto = format1.format(clto.time)
        txtitle.text = formattedto
        txfrom.text = formattedfrom
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
            SetMinist(fromd, endd)
        } else {
            Toast.makeText(
                    applicationContext,
                    "Please ensure the from date is before the after date",
                    Toast.LENGTH_LONG).show()
        }
    }


    override fun onDateRangeSelected(dayOfMonth: Int, monthOfYear: Int, year: Int, dayOfMonthEnd: Int, monthOfYearEnd: Int, yearEnd: Int) {
        var monthOfYear = monthOfYear
        var monthOfYearEnd = monthOfYearEnd
        /*   String date = " From- " + dayOfMonth + "/" + (++monthOfYear) + "/" + year + " To " + dayOfMonthEnd + "/" + (++monthOfYearEnd) + "/" + yearEnd;
//     txtitle.setText(date);
*/
        val clfrom = Calendar.getInstance()
        val clto = Calendar.getInstance()
        clfrom.set(year, monthOfYear, dayOfMonth)
        clto.set(yearEnd, monthOfYearEnd, dayOfMonthEnd)

        val formattedfrom = format1.format(clfrom.time)
        val formattedto = format1.format(clto.time)
        txtenddat.text = formattedto
        txfromdate.text = formattedfrom
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
            SetMinist(fromd, endd)
        } else {
            Toast.makeText(
                    applicationContext,
                    "Please ensure the from date is before the after date",
                    Toast.LENGTH_LONG).show()
        }
    }


    override fun hideProgress() {
        viewDialog?.hideDialog()
    }

    override fun setList(inboxlistpar: ArrayList<GetCommPerfData>) {
        inboxlist = inboxlistpar
        aAdpt = InboxListAdapter(inboxlist, this@InboxActivity)
        lv.adapter = aAdpt
    }

    override fun showToast(text: String) {

        Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG).show()


    }

    override fun setaccname(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoginResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showProgress() {
        viewDialog?.showDialog()
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}



