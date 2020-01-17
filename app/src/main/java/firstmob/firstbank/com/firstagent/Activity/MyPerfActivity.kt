package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import firstmob.firstbank.com.firstagent.adapter.MyPerfServAdapt
import firstmob.firstbank.com.firstagent.adapter.TxnAdapter
import firstmob.firstbank.com.firstagent.adapter.ViewPagerMyPerfAdapter
import firstmob.firstbank.com.firstagent.model.TxnList
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.model.GetMyPerfServData
import firstmob.firstbank.com.firstagent.model.GetSummaryData
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import java.util.ArrayList

class MyPerfActivity : AppCompatActivity() {
    internal var pager: ViewPager? = null
    internal var Titles: MutableList<String> = ArrayList()
    internal var Numboftabs = 2
    private val SPLASH_TIME_OUT = 3000
    internal var adapter: ViewPagerMyPerfAdapter? = null
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
    internal var prgDialog2: ProgressDialog? = null
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
    internal var endd:String? =null
    internal var dataSet: PieDataSet? = null
    internal var labels = ArrayList<String>()
    internal var labelslnchart = ArrayList<String>()
    internal var myperfdata: MutableList<GetMyPerfServData> = ArrayList<GetMyPerfServData>()
    internal var l: Legend? = null
    internal var l2: Legend? =null
    internal var pieChart: PieChart? =null
    internal var pro: ProgressDialog? =null
    internal var calnd: Button? = null

    internal var v1: View? =null
    internal var v2:View? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_perf)
    }
}
