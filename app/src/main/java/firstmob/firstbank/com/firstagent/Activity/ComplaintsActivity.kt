package firstmob.firstbank.com.firstagent.Activity





import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import firstmob.firstbank.com.firstagent.adapter.ComplaintsAdapter
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA
import firstmob.firstbank.com.firstagent.contract.ComplaintsContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.CommReceipt
import firstmob.firstbank.com.firstagent.fragments.ComplaintsReceipt
import firstmob.firstbank.com.firstagent.fragments.DateRangePickerFragment
import firstmob.firstbank.com.firstagent.model.ChargebackList
import firstmob.firstbank.com.firstagent.model.CommisionsJSON
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ComplaintsPresenter
import firstmob.firstbank.com.firstagent.utils.Utility.convertTxnCodetoServ
import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat
import kotlinx.android.synthetic.main.inbox.*
import kotlinx.android.synthetic.main.toolbarnewui.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.util.*


class ComplaintsActivity : AppCompatActivity(),   ComplaintsContract.ILoginView {


    internal var signup: Button? = null
    internal var sp1: Spinner? = null
    internal var phoneContactList = ArrayList<String>()
    internal var complist = ArrayList<ChargebackList>()
    // private TextView emptyView;
    internal var aAdpt: ComplaintsAdapter? = null



    internal lateinit var txtitle: TextView
    internal lateinit var txfrom: TextView



    internal var tdate: String? = null
    internal var firdate: String? = null
    internal var editsearch: EditText? = null

    internal var format1 = SimpleDateFormat("" + "MMMM dd yyyy")

    var viewDialog: ViewDialog? = null
    internal lateinit var presenter: ComplaintsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complact)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.colorPrimary)));

        // Set Cancelable as False

        viewDialog = ViewDialog(this)
        titlepg.text="Chargeback"
        presenter = ComplaintsPresenter(this, FetchServerResponse())



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


        presenter.Complaints()
        lv!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, l ->
            val p = complist[position]


            val idd = p.id
            val intent = Intent(this@ComplaintsActivity, ChargebackDetails::class.java)


            intent.putExtra("id", idd)
            startActivity(intent)
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
        inflater.inflate(R.menu.complistmenu, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val index = info.position //Use this for getting the list item value
        val view = info.targetView
        when (item.itemId) {
            R.id.logcomp -> {
                Log.d("onContextItemSelected", "Remove Pressed")


                return true
            }


            else -> return super.onContextItemSelected(item)
        }
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
    }

    override fun setList(complistpar: ArrayList<ChargebackList>) {
        complist = complistpar
        aAdpt = ComplaintsAdapter(complist, this@ComplaintsActivity)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}



