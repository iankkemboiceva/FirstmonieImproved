package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import firstmob.firstbank.com.firstagent.contract.CashDepoContract
import firstmob.firstbank.com.firstagent.contract.LogComplContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.CashDepoPresenter
import firstmob.firstbank.com.firstagent.presenter.LogComplaintPresenter
import kotlinx.android.synthetic.main.activity_cash_depo.*
import kotlinx.android.synthetic.main.logcomplaint.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class LogComplaint : AppCompatActivity(),LogComplContract.ILoginView{

    var stracno: String? = null;var stramo:String? = null; var strefno:kotlin.String? = null;var strdatee:kotlin.String? = null
    var viewDialog: ViewDialog? = null

    internal lateinit var presenter: LogComplContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logcomplaint)

        viewDialog = ViewDialog(this)


        val intent = intent
        if (intent != null) {
            stracno = intent.getStringExtra("txaco")
            stramo = intent.getStringExtra("txamo")
            strefno = intent.getStringExtra("txref")
            strdatee = intent.getStringExtra("txdate")
            edamo.setText(stramo)
            recacno.setText(stracno)
            edrefno.setText(strefno)
            timestamp.setText(strdatee)

            timestamp.isEnabled = false
            recacno.isEnabled = false
            edrefno.isEnabled = false
            edamo.isEnabled = false
        }

        presenter = LogComplaintPresenter(this, FetchServerResponse())

        btsubmit.setOnClickListener(){
            val desc = desc.text.toString()
            val acno = recacno.text.toString()
            val datetime = timestamp.text.toString()
            val amount = edamo.text.toString()

            presenter.LogComp(acno,amount,datetime, desc)
        }
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

    override fun onlogcompresult() {
        finish()
        val intent = Intent(this, FMobActivity::class.java)
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun showProgress() {
        viewDialog?.showDialog()
    }

}
