package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import firstmob.firstbank.com.firstagent.adapter.MyPerfServAdapt
import firstmob.firstbank.com.firstagent.adapter.TxnAdapter
import firstmob.firstbank.com.firstagent.adapter.ViewPagerMyPerfAdapter
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.contract.CommisionContract
import firstmob.firstbank.com.firstagent.contract.MyPerfActivityContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.TxnList
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.model.GetMyPerfServData
import firstmob.firstbank.com.firstagent.model.GetSummaryData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.MinstatementPresenter
import firstmob.firstbank.com.firstagent.presenter.MyperfActivityPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MyPerfActivity : AppCompatActivity(),View.OnClickListener, DateRangePickerFragment.OnDateRangeSelectedListener, OnChartValueSelectedListener, MyPerfActivityContract.IViewPerfAct {
    @Inject
    internal lateinit var ul: Utility

    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    internal var pager: ViewPager? = null
    internal var Titles: MutableList<String> = ArrayList()
    internal var Numboftabs = 2
    private val SPLASH_TIME_OUT = 3000
    internal var adapter: ViewPagerMyPerfAdapter? = null
    var viewDialg: ViewDialog? =null
    internal var cv: CardView? =null
    internal var sp1: Spinner? = null
    internal var textdate: String? =null
    private val mChart: BarChart? = null
    private var mChart2:BarChart? = null
    private val mLineChart: LineChart? = null
    private val mTf: Typeface? = null
    internal var planetsList: List<TxnList> = ArrayList<TxnList>()
    internal var aAdpt: TxnAdapter? = null
    internal var mypfadapt: MyPerfServAdapt? =null
    internal var lv: ListView? = null
    internal var lvpie:ListView? = null
    internal var lvserv:ListView? =null
    internal var ok: Button? = null
    internal var calendar:Button? =null


    internal var lyselcharttran: LinearLayout? =null
    internal var lyselchart:LinearLayout? =null
    internal var lymapsview:LinearLayout? =null
    internal var lyorderview:LinearLayout? =null
    var finalfx: String? = null
    var finpfrom:String? =null
    var finpto:String? =null
   // internal var prgDialog2: ProgressDialog? = null
    internal var r1: RadioButton? = null
    internal var r2:RadioButton? = null
    internal var r3:RadioButton? = null
    internal var layl: LinearLayout? = null
    internal var ryl: RelativeLayout? = null
    internal var finentry = ArrayList<PieEntry>()
    internal var barent = ArrayList<BarEntry>()
    internal var legendent = ArrayList<LegendEntry>()
    internal var temp: List<GetSummaryData> = ArrayList<GetSummaryData>()
    internal var cperfdata: List<GetCommPerfData> = ArrayList<GetCommPerfData>()
    internal var listentry = ArrayList<Entry>()
    internal var session: SessionManagement? =null
    private val emptyView: TextView? = null
    private var fromdate:TextView? = null
    private var endate: TextView? = null
    private var fromdatetran:TextView? = null
    internal var succtrans: TextView? =null
    internal var fromd: String? =null
    internal var presenter: MyPerfActivityContract.PresenterPerfAct? =null
    internal var endd:String? =null
    internal var dataSet: PieDataSet? = null
    internal var labels = ArrayList<String>()
    internal var labelslnchart = ArrayList<String>()
    internal var myperfdata: MutableList<GetMyPerfServData> = ArrayList<GetMyPerfServData>()
    internal var l: Legend? = null
    internal var l2: Legend? =null
    internal var pieChart: PieChart? =null
   // internal var pro: ProgressDialog? =null
    internal var calnd: Button? = null
    internal var mToolbar: Toolbar? = null
    internal var v1: View? =null
    internal var v2:View? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_perf)
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        //  mToolbar.setTitle("Inbox");
        setSupportActionBar(mToolbar)
        val ab = supportActionBar
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        Titles.add("Transaction")
        Titles.add("Commission")

        fromdate = findViewById(R.id.fromdate) as TextView
        fromdatetran = findViewById(R.id.fromdatetran) as TextView
        presenter = MyperfActivityPresenter(applicationContext, this, FetchServerResponse())

        calendar = findViewById(R.id.button4) as Button

        calendar!!.setOnClickListener(this)
        session = SessionManagement(this)
        viewDialg= ViewDialog(this)
        session = SessionManagement(this)

        //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        InitCharts()

        succtrans = findViewById(R.id.succtrans) as TextView
        lymapsview = findViewById(R.id.lymapsvw) as LinearLayout
        lyorderview = findViewById(R.id.lyorder) as LinearLayout

        lyselcharttran = findViewById(R.id.lyselcharttran) as LinearLayout
        lyselchart = findViewById(R.id.lyselchart) as LinearLayout

        v1 = findViewById(R.id.v1) as View
        v2 = findViewById(R.id.v2) as View

        lyorderview!!.setOnClickListener(View.OnClickListener {
            lyselchart!!.setVisibility(View.GONE)
            lyselcharttran!!.setVisibility(View.VISIBLE)
            v1!!.setVisibility(View.VISIBLE)
            v2!!.setVisibility(View.GONE)
        })

        lymapsview!!.setOnClickListener(View.OnClickListener {
            lyselchart!!.setVisibility(View.VISIBLE)
            lyselcharttran!!.setVisibility(View.GONE)
            v2!!.setVisibility(View.VISIBLE)
            v1!!.setVisibility(View.GONE)
        })

        cv = findViewById(R.id.card_view10) as CardView
        lvserv = findViewById(R.id.lvserv) as ListView


        mChart2 = findViewById(R.id.chart2) as BarChart
        mChart2!!.setOnChartValueSelectedListener(this)

        mChart2!!.setDrawBarShadow(false)
        val ds2 = Description()
        ds2.text = "Transaction amount in NGN"

        mChart2!!.setDescription(ds2)
        mChart2!!.getDescription().setPosition(3f, 3f)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn


        // scaling can now only be done on x- and y-axis separately
        mChart2!!.setPinchZoom(true)

        mChart2!!.setDrawGridBackground(false)
        // mChart.setDrawYLabels(false);


        val xAxis2 = mChart2!!.getXAxis()

        xAxis2.typeface = mTf
        xAxis2.setDrawGridLines(false)
        xAxis2.granularity = 1f // only intervals of 1 day

        xAxis2.position = XAxis.XAxisPosition.BOTTOM
        xAxis2.textSize = 7f
        l2 = mChart2!!.getLegend()


        l2!!.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER)
        l2!!.setForm(Legend.LegendForm.DEFAULT)
        l2!!.setFormSize(6f)
        l2!!.setTextSize(6f)
        l2!!.setXOffset(2f)

        l2!!.setXEntrySpace(4f) // set the space between the legend entries on the x-axis
        l2!!.setYEntrySpace(4f) // set the space between the legend entries on the y-axis


        mChart2!!.getAxisLeft().setDrawLabels(false)
        mChart2!!.getAxisRight().setDrawLabels(false)
        mChart2!!.getXAxis().setDrawLabels(false)


        pieChart = findViewById(R.id.piechart) as PieChart
        pieChart!!.setUsePercentValues(true)
        pieChart!!.getDescription().isEnabled = false
        pieChart!!.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart!!.setDragDecelerationFrictionCoef(0.95f)


        pieChart!!.setCenterText("")

        pieChart!!.setDrawHoleEnabled(true)
        pieChart!!.setHoleColor(Color.WHITE)

        pieChart!!.setTransparentCircleColor(Color.WHITE)
        pieChart!!.setTransparentCircleAlpha(110)

        pieChart!!.setHoleRadius(40f)
        pieChart!!.setTransparentCircleRadius(40f)

        pieChart!!.setDrawCenterText(true)

        pieChart!!.setRotationAngle(0f)
        // enable rotation of the chart by touch
        pieChart!!.setRotationEnabled(true)
        pieChart!!.setHighlightPerTapEnabled(true)

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart!!.setOnChartValueSelectedListener(this)

        //     setPieData(4, 100);

        pieChart!!.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        // mChart.spin(2000, 0, 360);


        val l = pieChart!!.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.yOffset = 0f
        l.formSize = 6f
        l.textSize = 6f
        l.xOffset = 2f
        l.xEntrySpace = 4f // set the space between the legend entries on the x-axis
        l.yEntrySpace = 4f // set the space between the legend entries on the y-axis


        // entry label styling
        pieChart!!.setEntryLabelColor(Color.BLACK)

        pieChart!!.setEntryLabelTextSize(8f)
        val sw = findViewById(R.id.switch1) as Switch
        sw.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                cv!!.setVisibility(View.GONE)
            } else {
                // The toggle is disabled
                cv!!.setVisibility(View.VISIBLE)
            }
        }
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button4) {

            val dateRangePickerFragment = DateRangePickerFragment.newInstance(this, false)

            dateRangePickerFragment.show(supportFragmentManager, "datePicker")
            SecurityLayer.Log("Changed manenoz", "It has")


        }
    }
    override fun setUpActivity(summdata: JSONArray?, comperf: JSONArray?) {
        barent.clear()
        myperfdata.clear()
        session!!.setString(SessionManagement.COMMDATA, comperf.toString())
        session!!.setString(SessionManagement.SUMMDATA, summdata.toString())
        session!!.setString(SessionManagement.MYPERFTEXT, textdate)
        Log.v("Session In", "Session In")
        if (comperf != null && summdata!=null) {
            SetActivityLogic(summdata, comperf)
            SetTranActivityLogic(summdata, comperf)
        }

    }

    override fun startSiginActivity() {
        finish()
        startActivity(Intent(this@MyPerfActivity, SignInActivity::class.java))
    }

    override fun onProcessingMessage(message: String?) {
     Utility.showToast(message)
    }

    override fun showProgress() {
        if (viewDialg != null && applicationContext != null && !this@MyPerfActivity.isFinishing) {
            viewDialg!!.showDialog()
        }
    }

    override fun hideProgress() {
        if (viewDialg != null  && applicationContext != null) {
            viewDialg!!.hideDialog()

        }
    }

    override fun ForceLogout() {

        if (applicationContext != null) {
            MaterialDialog.Builder(this@MyPerfActivity)
                    .title(getString(R.string.forceouterr))
                    .content(getString(R.string.forceout))

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
                            val i = Intent(applicationContext, SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }
    override fun onDateRangeSelected(startDay: Int, startMonth: Int, startYear: Int, endDay: Int, endMonth: Int, endYear: Int) {
        var startMonth = startMonth
        var endMonth = endMonth
        Log.d("range : ", "from: $startDay-$startMonth-$startYear to : $endDay-$endMonth-$endYear")

        val start = Calendar.getInstance()
        start.set(startYear, startMonth, startDay)


        val end = Calendar.getInstance()
        end.set(endYear, endMonth, endDay)

        val now = Calendar.getInstance()

        if (start.compareTo(now) > 0 || end.compareTo(now) > 0) {
            Toast.makeText(
                    applicationContext,
                    "Please ensure either the From Date or the After Date is not after today",
                    Toast.LENGTH_LONG).show()
        } else {

            val format1 = SimpleDateFormat("" + "MMMM dd yyyy")

            val startdatestring = format1.format(start.time)

            val enddatestring = format1.format(end.time)
            //   textdate = "You picked the following date: From- " + startDay + "/" + (++startMonth) + "/" + startYear + " To " + endDay + "/" + (++endMonth) + "/" + endYear;
            textdate = " From- $startdatestring To $enddatestring"

            fromdate!!.setText(textdate)
            fromdatetran!!.setText(textdate)

            startMonth = startMonth + 1
            endMonth = endMonth + 1
            val calfrom = Calendar.getInstance()
            val calto = Calendar.getInstance()
            calto.set(endYear, endMonth, endDay)
            calfrom.set(startYear, startMonth, startDay)

            if (calfrom.before(calto)) {

                var frmdymonth = Integer.toString(startDay)
                if (startDay < 10) {
                    frmdymonth = "0$frmdymonth"
                }
                var frmyear = Integer.toString(startYear)
                frmyear = frmyear.substring(2, 4)
                fromd = "$frmdymonth-$startMonth-$frmyear"
                var frmenddymonth = Integer.toString(endDay)
                if (endDay < 10) {
                    frmenddymonth = "0$frmenddymonth"
                }

                var frmendyr = Integer.toString(endYear)
                frmendyr = frmendyr.substring(2, 4)
                endd = "$frmenddymonth-$endMonth-$frmendyr"

                if (checkInternetConnection()) {
                    if (Utility.isNotNull(fromd) || Utility.isNotNull(endd)) {
                        presenter!!.ServerPullDataCall(fromd+"/"+endd)
                        //loadDataset(fromd, endd)
                    } else {
                        Utility.showToast("Please select appropriate from date and end date")
                    }
                }
            } else {
                Utility.showToast("Please ensure the from date is before the after date")
            }
        }
    }
    override fun onPause() {

        super.onPause()

        val secs = Date().time / 1000
        SecurityLayer.Log("Seconds Loged", java.lang.Long.toString(secs))
        session!!.putCurrTime(secs)
    }

     override fun onResume() {
        super.onResume()
        /* *//* resetDisconnectTimer();*/

        val nurl = session!!.getCurrTime()
        val newurl = nurl[SessionManagement.KEY_TIMEST]
        var blfalse = true
        if (newurl!! > 0) {
            val secs = Date().time / 1000
            var diff: Long = 0
            if (secs >= newurl) {
                diff = secs - newurl
                if (diff > 180) {
                    blfalse = false
                    this.finish()
                    //    session.logoutUser();
                    // After logout redirect user to Loing Activity
                    val i = Intent(this@MyPerfActivity, FMobActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    // Staring Login Activity
                    startActivity(i)
                    //   Toast.makeText(FMobActivity.ths, "Your session has expired. Please login again", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (blfalse) {

        }
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun InitCharts() {

        val dated = Utility.getToday()
        val datew = Utility.getWeek()
        finpfrom = datew
        finpto = dated


        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        var month = now.get(Calendar.MONTH) // Note: zero based!
        val day = now.get(Calendar.DAY_OF_MONTH)
        now.set(year, month, 1)

        val format1 = SimpleDateFormat("" + "MMMM dd yyyy")
        println(cal.time)
        // Output "Wed Sep 26 14:23:28 EST 2012"

        val formattednow = format1.format(cal.time)
        val formattedstartdate = format1.format(now.time)
        // Output "2012-09-26"
        val date = "From- $formattedstartdate To $formattednow"

        fromdate!!.setText(date)
        fromdatetran!!.setText(date)
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
        presenter!!.ServerPullDataCall(firdate+"/"+tdate)
       // loadDataset(firdate, tdate)
    }
    fun SetActivityLogic(summdata: JSONArray, comperf: JSONArray) {
        if (summdata.length() > 0) {

            try {
                var chktxncnt = false
                val dataSets = ArrayList<IBarDataSet>()
                val comdataSets = ArrayList<IBarDataSet>()
                val colors = ArrayList<Int>()
                val entries = ArrayList<PieEntry>()
                val labelsstr = ArrayList<String>()


                for (c in ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.JOYFUL_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.COLORFUL_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.LIBERTY_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.PASTEL_COLORS)
                    colors.add(c)

                colors.add(ColorTemplate.getHoloBlue())
                var json_data: JSONObject? = null
                for (i in 0 until summdata.length()) {
                    json_data = summdata.getJSONObject(i)
                    //String accid = json_data.getString("benacid");
                    val txncode = json_data!!.optString("txnCode")
                    val amon = json_data.optString("amount")
                    val status = json_data.optString("status")
                    val agcmsn = json_data.optString("agentCmsn")
                    val txnname = Utility.convertTxnCodetoServ(txncode)
                    if (amon != "" && agcmsn != "") {
                        val dbagcmsn = java.lang.Float.parseFloat(amon)
                        val agamcmn = java.lang.Float.parseFloat(agcmsn)
                        if (Utility.isNotNull(status)) {
                            if (status == "SUCCESS" && dbagcmsn > 0) {
                                //   finentry.add(new PieEntry(dbagcmsn, txnname));
                                val barnt = ArrayList<BarEntry>()
                                SecurityLayer.Log("Log Amount", java.lang.Double.toString(java.lang.Double.parseDouble(agcmsn)))
                                barnt.add(BarEntry(i.toFloat(), dbagcmsn, txnname))

                                val set1: BarDataSet

                                set1 = BarDataSet(barnt, txnname)

                                set1.color = colors[i]
                                //  }
                                dataSets.add(set1)
                                val bcmnt = ArrayList<BarEntry>()

                                bcmnt.add(BarEntry(i.toFloat(), agamcmn, txnname))
                                labelsstr.add(txnname)

                                chktxncnt = true
                                SecurityLayer.Log("Entries", "$dbagcmsn Code$txnname")
                                val set2: BarDataSet

                                set2 = BarDataSet(bcmnt, txnname)

                                set2.color = colors[i]
                                //  }
                                comdataSets.add(set2)
                                entries.add(PieEntry(agamcmn,
                                        txnname))

                            }
                        }
                    }
                }


                val databar = BarData(dataSets)
                /*BarData data = new BarData(dataSets);

                                        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsstr));
                                        mChart.setData(data);
                                        mChart.invalidate();*/

                val dataSet = PieDataSet(entries, "")

                // dataSet.setDrawIcons(false);

                dataSet.sliceSpace = 3f

                dataSet.selectionShift = 5f

                // add a lot of colors


                colors.add(ColorTemplate.getHoloBlue())

                dataSet.colors = colors
                //dataSet.setSelectionShift(0f);

                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter())
                data.setValueTextSize(7f)
                data.setValueTextColor(Color.BLACK)

                pieChart!!.setData(data)

                // undo all highlights
                pieChart!!.highlightValues(null)

                pieChart!!.invalidate()


                val comdata = BarData(comdataSets)

                mChart2!!.getXAxis().valueFormatter = IndexAxisValueFormatter(labelsstr)
                mChart2!!.setData(databar)
                mChart2!!.invalidate()
                var json_cperfdata: JSONObject? = null
                var cntr = 0
                for (i in 0 until comperf.length()) {
                    json_cperfdata = comperf.getJSONObject(i)
                    //String accid = json_data.getString("benacid");


                    val txnCode = json_cperfdata!!.optString("txnCode")
                    val agentCmsn = json_cperfdata.optDouble("agentCmsn")
                    var txndateTime = json_cperfdata.optString("txndateTime")
                    val amount = json_cperfdata.optString("amount")
                    val status = json_cperfdata.optString("status")
                    val toAcNum = json_cperfdata.optString("toAcNum")
                    val refNumber = json_cperfdata.optString("refNumber")
                    txndateTime = Utility.convertPerfDate(txndateTime)
                    if (status == "SUCCESS" && agentCmsn > 0) {
                        listentry.add(Entry(cntr.toFloat(), java.lang.Float.parseFloat(java.lang.Double.toString(agentCmsn))))

                        labelslnchart.add(cntr, txndateTime)
                        cntr++
                        SecurityLayer.Log("Counter", Integer.toString(cntr))
                    }


                }


                if (cntr > 0) {
                    //  setData(listentry);
                }
                SecurityLayer.Log("Transaction counter", Integer.toString(cntr))
                succtrans!!.setText(Integer.toString(cntr))
                if (chktxncnt == false) {
                    Toast.makeText(
                            applicationContext,
                            "There are no transaction records to display",
                            Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(
                    applicationContext,
                    "There are no records to display",
                    Toast.LENGTH_LONG).show()
        }


    }


    fun SetTranActivityLogic(summdata: JSONArray, comperf: JSONArray) {

        if (summdata.length() > 0) {
            try {

                val dataSets = ArrayList<IBarDataSet>()
                val comdataSets = ArrayList<IBarDataSet>()
                val colors = ArrayList<Int>()
                val entries = ArrayList<PieEntry>()
                val labelsstr = ArrayList<String>()

                var chktxncnt = false
                for (c in ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.JOYFUL_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.COLORFUL_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.LIBERTY_COLORS)
                    colors.add(c)

                for (c in ColorTemplate.PASTEL_COLORS)
                    colors.add(c)

                colors.add(ColorTemplate.getHoloBlue())
                var json_data: JSONObject? = null
                for (i in 0 until summdata.length()) {

                    json_data = summdata.getJSONObject(i)

                    //String accid = json_data.getString("benacid");


                    val txncode = json_data!!.optString("txnCode")

                    val amon = json_data.optString("amount")
                    val status = json_data.optString("status")
                    val agcmsn = json_data.optString("agentCmsn")
                    val txnname = Utility.convertTxnCodetoServ(txncode)
                    if (amon != "" && agcmsn != "") {
                        val dbagcmsn = java.lang.Float.parseFloat(amon)
                        val agamcmn = java.lang.Float.parseFloat(agcmsn)
                        if (Utility.isNotNull(status)) {
                            if (status == "SUCCESS" && dbagcmsn > 0) {
                                //   finentry.add(new PieEntry(dbagcmsn, txnname));
                                val barnt = ArrayList<BarEntry>()
                                SecurityLayer.Log("Log Amount", java.lang.Double.toString(java.lang.Double.parseDouble(agcmsn)))
                                myperfdata.add(GetMyPerfServData(txnname, "", Utility.roundto2dp(java.lang.Double.toString(agamcmn.toDouble())) + Constants.KEY_NAIRA))
                                barnt.add(BarEntry(i.toFloat(), dbagcmsn, txnname))

                                val set1: BarDataSet

                                set1 = BarDataSet(barnt, txnname)

                                set1.color = colors[i]
                                //  }
                                dataSets.add(set1)
                                val bcmnt = ArrayList<BarEntry>()

                                bcmnt.add(BarEntry(i.toFloat(), agamcmn, txnname))
                                labelsstr.add(txnname)

                                chktxncnt = true
                                SecurityLayer.Log("Entries", "$dbagcmsn Code$txnname")
                                val set2: BarDataSet

                                set2 = BarDataSet(bcmnt, txnname)

                                set2.color = colors[i]
                                //  }
                                comdataSets.add(set2)
                                entries.add(PieEntry(agamcmn,
                                        txnname))

                            }
                        }
                    }
                }

                mypfadapt = MyPerfServAdapt(myperfdata, this)
                lvserv!!.setAdapter(mypfadapt)
                val databar = BarData(dataSets)

                val dataSet = PieDataSet(entries, "")

                // dataSet.setDrawIcons(false);

                dataSet.sliceSpace = 3f

                dataSet.selectionShift = 5f

                // add a lot of colors


                colors.add(ColorTemplate.getHoloBlue())

                dataSet.colors = colors
                //dataSet.setSelectionShift(0f);

                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter())
                data.setValueTextSize(7f)
                data.setValueTextColor(Color.BLACK)

                pieChart!!.setData(data)

                // undo all highlights
                pieChart!!.highlightValues(null)

                pieChart!!.invalidate()


                val comdata = BarData(comdataSets)

                mChart2!!.getXAxis().valueFormatter = IndexAxisValueFormatter(labelsstr)
                mChart2!!.setData(databar)
                mChart2!!.invalidate()
                var json_cperfdata: JSONObject? = null
                var cntr = 0
                for (i in 0 until comperf.length()) {
                    json_cperfdata = comperf.getJSONObject(i)
                    //String accid = json_data.getString("benacid");


                    val txnCode = json_cperfdata!!.optString("txnCode")
                    val agentCmsn = json_cperfdata.optDouble("agentCmsn")
                    var txndateTime = json_cperfdata.optString("txndateTime")
                    val amount = json_cperfdata.optString("amount")
                    val status = json_cperfdata.optString("status")
                    val toAcNum = json_cperfdata.optString("toAcNum")
                    val refNumber = json_cperfdata.optString("refNumber")
                    txndateTime = Utility.convertPerfDate(txndateTime)
                    if (status == "SUCCESS" && agentCmsn > 0) {
                        listentry.add(Entry(cntr.toFloat(), java.lang.Float.parseFloat(java.lang.Double.toString(agentCmsn))))

                        labelslnchart.add(cntr, txndateTime)
                        cntr++
                        SecurityLayer.Log("Counter", Integer.toString(cntr))
                    }


                }


                if (cntr > 0) {
                    //  setData(listentry);
                }
                SecurityLayer.Log("Transaction counter", Integer.toString(cntr))
                succtrans!!.setText(Integer.toString(cntr))
                if (chktxncnt == false) {
                    Toast.makeText(
                            applicationContext,
                            "There are no transaction records to display",
                            Toast.LENGTH_LONG).show()
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            /*  SecurityLayer.Log("NewSelChart","Inside");
            NewSelChart fm = (NewSelChart) getFragmentManager().findFragmentById(R.id.pager);
            fm.SetActivityLogic(summdata,comperf);*/
        } else {
            Toast.makeText(
                    applicationContext,
                    "There are no records to display",
                    Toast.LENGTH_LONG).show()
        }


    }
    override fun onDestroy() {

        session!!.setString(SessionManagement.MYPERFTEXT, null)
        if (viewDialg != null && applicationContext != null) {
            viewDialg!!.hideDialog()

        }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
