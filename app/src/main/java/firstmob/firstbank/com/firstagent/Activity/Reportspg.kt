package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import kotlinx.android.synthetic.main.activity_reportspg.*
import kotlinx.android.synthetic.main.activity_reportspg.txfromdate
import kotlinx.android.synthetic.main.activity_reportspg.txtenddat
import kotlinx.android.synthetic.main.inbox.*
import kotlinx.android.synthetic.main.toolbarnewui.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*

class Reportspg : BaseActivity() , View.OnClickListener,DateRangePickerFragment.OnDateRangeSelectedListener, DatePickerDialog.OnDateSetListener{
    internal var format1 = SimpleDateFormat("" + "MMMM dd yyyy")
    internal var format2 = SimpleDateFormat("" + "dd-M-yyyy")
    var txtstrtdt = "NA"
    var txtendt = "NA"
    var frmdate = "NA"
    var enddt = "NA"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportspg)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        inboxbtn.setOnClickListener(this)
        myPermance.setOnClickListener(this)
        monthdtsumm.setOnClickListener(this)
        genrpt.setOnClickListener{
            var i = Intent(this, InboxActivity::class.java)
            i.putExtra("fromdate", frmdate)
            i.putExtra("enddate", enddt)

            i.putExtra("txtfrmdate", txtstrtdt)
            i.putExtra("txtenddate", txtendt)
            startActivity(i)
        }

        calendarbtn.setOnClickListener {

            val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)
            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
        }
        lastweeksumm.setOnClickListener{


            val now = Calendar.getInstance()
            val yest = getDaysAgo(7)
            val year = now.get(Calendar.YEAR)
            var month = now.get(Calendar.MONTH) // Note: zero based!
            val day = now.get(Calendar.DAY_OF_MONTH)



            month += 1

            var frmdymonth = day.toString()
            if (day < 10) {
                frmdymonth = "0$frmdymonth"
            }
            val varendday = day-7
            var todymonth = varendday.toString()
            if (varendday < 10) {
                todymonth = "0$todymonth"
            }
            var frmyear = year.toString()
            frmyear = frmyear.substring(2, 4)
            val tdate = "$frmdymonth-$month-$frmyear"
            val firdate   = format2.format(yest)

            val formattednow = format1.format(now.time)
            val formattedenddt = format1.format(yest)


            var i = Intent(this, InboxActivity::class.java)
            i.putExtra("fromdate", firdate)
            i.putExtra("enddate", tdate)

            i.putExtra("txtfrmdate", formattedenddt)
            i.putExtra("txtenddate", formattednow)
            startActivity(i)
        }
        titlepg.text="Reports"
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        if(v!!.id==R.id.myPermance){
            val i = Intent(this, MyPerfActivity::class.java)
            startActivity(i)
        }
        if(v!!.id==R.id.inboxbtn){


            val now = Calendar.getInstance()
            val yest = getDaysAgo(1)
            val year = now.get(Calendar.YEAR)
            var month = now.get(Calendar.MONTH) // Note: zero based!
            val day = now.get(Calendar.DAY_OF_MONTH)

            month += 1

            var frmdymonth = day.toString()
            if (day < 10) {
                frmdymonth = "0$frmdymonth"
            }
            val varendday = day-1
            var todymonth = varendday.toString()
            if (varendday < 10) {
                todymonth = "0$todymonth"
            }
            var frmyear = year.toString()
            frmyear = frmyear.substring(2, 4)
            val tdate = "$frmdymonth-$month-$frmyear"
            val firdate = "$todymonth-$month-$frmyear"

            val formattednow = format1.format(now.time)
            val formattedenddt = format1.format(yest)




            var i = Intent(this, InboxActivity::class.java)
            i.putExtra("fromdate", firdate)
            i.putExtra("enddate", tdate)

            i.putExtra("txtfrmdate", formattedenddt)
            i.putExtra("txtenddate", formattednow)
            startActivity(i)
        }

        if(v!!.id==R.id.monthdtsumm){


            val now = Calendar.getInstance()
            val strtmnth = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            var month = now.get(Calendar.MONTH) // Note: zero based!
            val day = now.get(Calendar.DAY_OF_MONTH)
            strtmnth.set(year, month, 1)

            month += 1

            var frmdymonth = day.toString()
            if (day < 10) {
                frmdymonth = "0$frmdymonth"
            }
            val varendday = day-1
            var todymonth = varendday.toString()
            if (varendday < 10) {
                todymonth = "0$todymonth"
            }
            var frmyear = year.toString()
            frmyear = frmyear.substring(2, 4)
            val tdate = "$frmdymonth-$month-$frmyear"
            val firdate = "01-$month-$frmyear"

            val formattednow = format1.format(now.time)
            val formattedenddt = format1.format(strtmnth.time)


            var i = Intent(this, InboxActivity::class.java)
            i.putExtra("fromdate", firdate)
            i.putExtra("enddate", tdate)

            i.putExtra("txtfrmdate", formattedenddt)
            i.putExtra("txtenddate", formattednow)
            startActivity(i)
        }

    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }

    fun initcalendar(){
        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        var month = now.get(Calendar.MONTH) // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)
        now.set(year, month, 1)
        val formattednow = format1.format(cal.time)
        val formattedstartdate = format1.format(now.time)
        // Output "2012-09-26"

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


        var i = Intent(this, InboxActivity::class.java)
        i.putExtra("fromdate", firdate)
        i.putExtra("enddate", tdate)

        i.putExtra("txtfrmdate", formattedstartdate)
        i.putExtra("txtenddate", formattednow)
        startActivity(i)



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
            var frmdymonth = dayOfMonth.toString()
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
        txtstrtdt = formattedfrom
        txtendt = formattedto
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
            frmdate = fromd
            enddt = endd

        } else {
            Toast.makeText(
                    applicationContext,
                    "Please ensure the from date is before the after date",
                    Toast.LENGTH_LONG).show()
        }
    }
}


