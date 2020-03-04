package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.ChargebackContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.model.ChargebackList
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ChargebackPresenter
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.activity_chargeback_details.*

import kotlinx.android.synthetic.main.toolbarnewui.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ChargebackDetails : AppCompatActivity(), ChargebackContract.ILoginView {

    override fun onLoginResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var recanno: String? = null
    var viewDialog: ViewDialog? = null
    var amou: String? = null
    var narra: String? = null
    lateinit var ednamee: String
    var ednumbb: String? = null
    var txtname: String? = null
    private var finalfee: String? = null
    private var boolchkfee: Boolean? = false
    var agbalance: String? = null
    internal lateinit var presenter: ChargebackContract.Presenter
    var refnum: String? = null
    var chgbckid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chargeback_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.nbkyellow)))
        titlepg.text="Chargeback Details"
        viewDialog = ViewDialog(this)

        presenter = ChargebackPresenter(this, FetchServerResponse())
        val intent = intent
        if (intent != null) {
            val chgbckid = intent.getIntExtra("id",0)

            presenter.getChargebackDetails(chgbckid)


        }
gobutton.setOnClickListener{
    if(Utility.isNotNull(refnum)) {
        val intent = Intent(this, ChargebackComments::class.java)
        intent.putExtra("id", chgbckid)
        intent.putExtra("ref", refnum)
        intent.putExtra("iscashgiv", "1")
        startActivity(intent)
    }
}


        ispaid.setOnClickListener{
            if(Utility.isNotNull(refnum)) {
                val intent = Intent(this, ChargebackComments::class.java)
                intent.putExtra("id", chgbckid)
                intent.putExtra("ref", refnum)
                intent.putExtra("iscashgiv", "0")
                startActivity(intent)
            }
        }

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun hideProgress() {
        viewDialog?.hideDialog()
    }

    override fun showToast(text: String) {

        Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG).show()


    }

    override fun setResultText(cglist: ChargebackList?) {
        accountno.text = cglist?.accno
        reccode.text = cglist?.code
        txtpan.text = cglist?.pan
        txtamo.text = cglist?.amount
        txdate.text = cglist?.txnDate
        txref.text = cglist?.refNum
        chgbckid = cglist?.id!!
        refnum = cglist?.refNum
        txcard.text = cglist?.catdType
        var statuss = cglist?.status
        if(statuss == "0"){
            txstatus.text = "Not Paid"
            txstatus.setTextColor(resources.getColor(R.color.fab_material_red_900))
        }
        else if(statuss == "1"){
            txstatus.text = "Paid"
            txstatus.setTextColor(resources.getColor(R.color.fab_material_light_green_900))

        }


    }


    override fun showProgress() {
        viewDialog?.showDialog()
    }



    override fun hidebutton() {

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
