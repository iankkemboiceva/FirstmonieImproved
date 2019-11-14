package firstmob.firstbank.com.firstagent.Activity

import FinalConfDepoActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.contract.ConfirmCashDepoContract
import firstmob.firstbank.com.firstagent.contract.TransactionProcessingContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfirmCashDepoPresenter
import firstmob.firstbank.com.firstagent.presenter.TransactionProcPresenter
import kotlinx.android.synthetic.main.activity_transaction_processing.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class TransactionProcessingActivity : AppCompatActivity(), TransactionProcessingContract.ILoginView {

    override fun setstatus(name: String?) {
        txstatus.text = name
    }


    override fun setdesc(name: String?) {
        txdesc.text = name
    }

    var viewDialog: ViewDialog? = null
    var recanno: String? = null
    var amou: String? = null
    var narra: String? = null
    var ednamee: String? = null
    var ednumbb: String? = null
    var txtname: String? = null
    var strfee: String? = null
    var stragcms: String? = null
    var bankname: String? = null
    var bankcode: String? = null
    var txpin: String? = null
    var newparams: String? = null
    var serv: String? = null
    var txtcustid: String? = null
    var serviceid: String? = null
    var billid: String? = null
    var txtfee: String? = null
    var strtref: String? = null
    var strlabel: String? = null
    var strbillnm: String? = null
    var fullname: String? = null
    var telcoop: String? = null
    var marketnm: String? = null
    var txtrfc: String? = null
    var txref: String? = null


    internal lateinit var presenter: TransactionProcessingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_processing)

        viewDialog = ViewDialog(this)


        val intent = intent

        presenter = TransactionProcPresenter(this, FetchServerResponse())
        if (intent != null) {
            val serv = intent.getStringExtra("serv")
            if (serv == "CASHDEPO") {
                recanno = intent.getStringExtra("recanno")
                amou = intent.getStringExtra("amou")
                narra = intent.getStringExtra("narra")
                ednamee = intent.getStringExtra("ednamee")
                ednumbb = intent.getStringExtra("ednumbb")
                txtname = intent.getStringExtra("txtname")
                txtrfc = intent.getStringExtra("refcode")
                val params = intent.getStringExtra("params")
                stragcms = returnNumberFormat(intent.getStringExtra("agcmsn"))
                val trantype = intent.getStringExtra("trantype")
                strfee = intent.getStringExtra("fee")
                txpin = intent.getStringExtra("txpin")
                newparams = "$params/$txpin"

                presenter.IntraDepoBankResp(newparams)

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


    override fun showProgress() {
        viewDialog?.showDialog()
    }


    override fun CashDepoResult(refcodee: String?, datetime: String?, agcmsn: String?, totfee: String?) {

        val intent = Intent(this, FinalConfDepoActivity::class.java)

        intent.putExtra("recanno", recanno)
        intent.putExtra("amou", amou)
        intent.putExtra("narra", narra)
        //    String refcodee = datas.optString("referenceCode");
        intent.putExtra("refcode", refcodee)
        intent.putExtra("ednamee", ednamee)
        intent.putExtra("ednumbb", ednumbb)
        intent.putExtra("txtname", txtname)
        intent.putExtra("datetime", datetime)
        intent.putExtra("trantype", "D")
        intent.putExtra("agcmsn", agcmsn)
        intent.putExtra("fee", totfee)
        startActivity(intent)
    }

    override fun onErrorResult(errormsg: String?) {


        MaterialDialog(this)


                .show {
                    title(text = "Error")
                    message(text = errormsg)

                    negativeButton(R.string.dismiss) { dialog ->
                        dialog.dismiss()
                    }
                }

        txstatus.text = "TRANSACTION FAILURE"
        txdesc.text = errormsg
    }


}
