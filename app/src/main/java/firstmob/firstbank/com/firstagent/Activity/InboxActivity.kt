package firstmob.firstbank.com.firstagent.Activity

import android.app.Fragment
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler

import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

import com.afollestad.materialdialogs.MaterialDialog


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.UUID


import android.app.DatePickerDialog
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA
import firstmob.firstbank.com.firstagent.contract.CashDepoContract
import firstmob.firstbank.com.firstagent.contract.InboxContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.utils.Utility.convertTxnCodetoServ
import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class InboxActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener, DateRangePickerFragment.OnDateRangeSelectedListener, CashDepoContract.ILoginView {


    internal var signup: Button? = null
    internal var sp1: Spinner? = null
    internal var phoneContactList = ArrayList<String>()
    internal var inboxlist = ArrayList<GetCommPerfData>()
    // private TextView emptyView;
    internal var aAdpt: InboxListAdapter? = null
    internal lateinit var lv: ListView
    internal var ok: Button? = null
    internal var prgDialog2: ProgressDialog? = null
    internal var acno: TextView? = null
    internal var accno: EditText? = null
    internal var mobno: EditText? = null
    internal var fnam: EditText? = null
    internal lateinit var calendar: Button

    internal lateinit var txtitle: TextView
    internal lateinit var txfrom: TextView
    internal var temp: List<GetCommPerfData> = ArrayList<GetCommPerfData>()


    internal lateinit var pro: ProgressDialog
    internal lateinit var sv: SearchView
    internal var tdate: String? = null
    internal var firdate: String? = null
    internal var editsearch: EditText? = null

    internal var format1 = SimpleDateFormat("" + "MMMM dd yyyy")

    var viewDialog: ViewDialog? = null
    internal lateinit var presenter: InboxContract.Presenter

  override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inboxact)


        //   emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading Inbox....")
        // Set Cancelable as False

      viewDialog = ViewDialog(this)


        calendar.setOnClickListener(this)
        prgDialog2!!.setCancelable(false)
        inboxlist.clear()

        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        var month = now.get(Calendar.MONTH) // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)

        sv = findViewById(R.id.searchView1) as SearchView
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(arg0: String): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // TODO Auto-generated method stub
                if (aAdpt != null) {
                    aAdpt!!.getFilter().filter(query)
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
        txtitle.text = formattednow
        txfrom.text = formattedstartdate
        //  checkInternetConnection2();


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



       // SetMinist(firdate, tdate)

        pro = ProgressDialog(this)
        pro.setMessage("Loading...")
        pro.setTitle("")
        pro.setCancelable(false)



        lv.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            val p = inboxlist[position]
            val txncode = p.getTxnCode()
            val toAcnum = p.gettoAcNum()
            val fromacnum = p.getFromAcnum()
            val txtrfno = p.getrefNumber()

            val servtype = convertTxnCodetoServ(txncode)
            val txtdatetime = p.getTxndateTime()
            val statuss = p.getStatus()


            val amoo = KEY_NAIRA + returnNumberFormat(p.getAmount())
            val fm = getSupportFragmentManager()
          /*  val editNameDialog = CommReceipt()
            val bundle = Bundle()
            bundle.putString("narr", fromacnum)
            bundle.putString("refno", txtrfno)
            bundle.putString("datetime", txtdatetime)
            bundle.putString("amo", amoo)
            bundle.putString("servtype", servtype)
            bundle.putString("status", statuss)
            bundle.putString("narrtor", toAcnum)
            editNameDialog.setArguments(bundle)
            editNameDialog.show(fm, "fragment_edit_name")*/
        }
    }

    protected override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun StartChartAct(i: Int) {}

    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        Log.v("Am i in", "Yes")
        val inflater = getMenuInflater()
     //   inflater.inflate(R.menu.inboxlistmenu, menu)

    }

    override fun onContextItemSelected(item: MenuItem) {
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
            }


            else -> return super.onContextItemSelected(item)

        }


        fun SetMinist(stdate: String, enddate: String) {


            if (prgDialog2 != null && getApplicationContext() != null) {
                prgDialog2!!.show()
                sv.setQuery("", false)
                sv.clearFocus()
                val now = Calendar.getInstance()
                val year = now.get(Calendar.YEAR)
                val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
                val day = now.get(Calendar.DAY_OF_MONTH)


                var frmdymonth = Integer.toString(day)
                if (day < 10) {
                    frmdymonth = "0$frmdymonth"
                }
                var frmyear = Integer.toString(year)
                frmyear = frmyear.substring(2, 4)
                val format1 = SimpleDateFormat("" + "MMMM dd yyyy")
                val fdate = "$frmdymonth-$month-$frmyear"
                val noww = Calendar.getInstance()
                val yearr = now.get(Calendar.YEAR)
                val monthh = now.get(Calendar.MONTH) // Note: zero based!

                noww.set(yearr, monthh, 1)
                val formattedstartdate = "01-$month-$frmyear"


                //    val params = "1/$usid/$agentid/$mobnoo/TXNRPT/$stdate/$enddate"

                //    Inbox(params)

            }
        }


        override fun onClick(view: View) {
            if (view.id == R.id.button4) {
                /*  Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    InboxActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setMaxDate(now);
            dpd.show(getFragmentManager(), "Datepickerdialog");*/


                val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)
                dateRangePickerFragment.show(getSupportFragmentManager(), "datePicker")
            }


        }

        fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
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
                        getApplicationContext(),
                        "Please ensure the from date is before the after date",
                        Toast.LENGTH_LONG).show()
            }
        }


        fun onDateRangeSelected(dayOfMonth: Int, monthOfYear: Int, year: Int, dayOfMonthEnd: Int, monthOfYearEnd: Int, yearEnd: Int) {
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
                        getApplicationContext(),
                        "Please ensure the from date is before the after date",
                        Toast.LENGTH_LONG).show()
            }
        }


    }


}
