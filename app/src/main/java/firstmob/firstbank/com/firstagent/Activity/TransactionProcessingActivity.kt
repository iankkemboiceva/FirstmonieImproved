package firstmob.firstbank.com.firstagent.Activity



import android.util.Log
import android.widget.Toast

import androidx.core.content.ContextCompat


import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog


import firstmob.firstbank.com.firstagent.contract.TransactionProcessingContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.TransactionProcPresenter

import firstmob.firstbank.com.firstagent.utils.Utility

import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat
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
            if (serv == "CASHDEPO" || serv == "CASHTRAN") {
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
                val endpoint = "transfer/intrabank.action"

                presenter.CallActivity(newparams,endpoint,serv)


            }else if(serv == "AIRT"){

                strfee = intent.getStringExtra("fee")
                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"))

                strfee = intent.getStringExtra("fee")

                txtcustid = intent.getStringExtra("mobno")
                amou = intent.getStringExtra("amou")
                telcoop = intent.getStringExtra("telcoop")
                val params = intent.getStringExtra("params")
                val txtamou = Utility.returnNumberFormat(amou)
                if (txtamou == "0.00") {
                    amou = txtamou
                }

                billid = intent.getStringExtra("billid")
                serviceid = intent.getStringExtra("serviceid")
                txpin = intent.getStringExtra("txpin")
                newparams = params
                Log.v("Params", "$newparams/$txpin")
                val endpoint = "billpayment/mobileRecharge.action"
                presenter.CallActivity(newparams+"/"+txpin,endpoint,serv)

                //AirtimeResp("$newparams/$txpin")
            }else if(serv == "WDRAW"){
                recanno = intent.getStringExtra("recanno")
                amou = intent.getStringExtra("amou")
                strfee = intent.getStringExtra("fee")
                txtname = intent.getStringExtra("txtname")
                txref = intent.getStringExtra("txref")
                txtrfc = intent.getStringExtra("refcode")

                val params = intent.getStringExtra("params")
                stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"))

                strfee = intent.getStringExtra("fee")

                txpin = intent.getStringExtra("txpin")
                newparams = params
                Log.v("Params", "$newparams/$txpin")
                val endpoint = "billpayment/mobileRecharge.action"
                presenter.CallActivity(newparams+"/"+txpin,endpoint,serv)
             //   WithdrawResp("$newparams/$txpin")
            }
            else if (serv == "CABLETV") {
                txtcustid = intent.getStringExtra("custid")
                amou = intent.getStringExtra("amou")
                narra = intent.getStringExtra("narra")
                ednamee = intent.getStringExtra("ednamee")
                ednumbb = intent.getStringExtra("ednumbb")
                strlabel = intent.getStringExtra("label")
                billid = intent.getStringExtra("billid")
                strbillnm = intent.getStringExtra("billname")
                serviceid = intent.getStringExtra("serviceid")
                strfee = intent.getStringExtra("fee")
                strtref = intent.getStringExtra("tref")
                fullname = intent.getStringExtra("fullname")
                val params = intent.getStringExtra("params")
                txpin = intent.getStringExtra("txpin")
                if (Utility.checkStateCollect(serviceid)) {

                }
                newparams = params
                Log.v("Params", "$newparams/$txpin")
                val endpoint = "billpayment/billpayment.action"
                presenter.CallActivity(newparams+"/"+txpin,endpoint,serv)
              //  PayBillResp("$newparams/$txpin")

            }

            }





        button2.setOnClickListener(){
            finish()
            val intent = Intent(applicationContext, FMobActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

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

    override fun CashWithdrawalResult(refcodee: String?, datetime: String?, agcmsn: String?, totfee: String?) {
        val intent = Intent(this@TransactionProcessingActivity, FinalConfWithdrawActivity::class.java)


        intent.putExtra("recanno", recanno)

        intent.putExtra("amou", amou)

        intent.putExtra("datetime", datetime)
        intent.putExtra("txtname", txtname)
        intent.putExtra("txref", txref)
        intent.putExtra("agcmsn", agcmsn)
        //    String refcodee = datas.optString("referenceCode");
        intent.putExtra("refcode", refcodee)
        intent.putExtra("fee", totfee)

        startActivity(intent)
    }

    override fun CashAirtimeResult(refcodee: String?, datetime: String?, agcmsn: String?, totfee: String?,tref: String?) {
        val intent = Intent(this@TransactionProcessingActivity, FinalConfAirtimeActivity::class.java)


        intent.putExtra("mobno", txtcustid)
        intent.putExtra("amou", amou)
        intent.putExtra("telcoop", telcoop)
        //  String refcodee = datas.optString("refNumber");
        intent.putExtra("refcode", refcodee)
        intent.putExtra("billid", billid)
        intent.putExtra("datetime", datetime)
        intent.putExtra("serviceid", serviceid)
        intent.putExtra("agcmsn", agcmsn)
        intent.putExtra("fee", totfee)
        intent.putExtra("tref", tref)

        startActivity(intent)
    }


    override fun CashDepoTranResult(refcodee: String?, datetime: String?, agcmsn: String?, totfee: String?) {

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
        intent.putExtra("trantype", "T")
        intent.putExtra("agcmsn", agcmsn)
        intent.putExtra("fee", totfee)
        startActivity(intent)
    }

    override fun CashCabletvResult(refcodee: String?, datetime: String?, agcmsn: String?, totfee: String?, tref: String?) {
        val intent = Intent(this@TransactionProcessingActivity, FinalConfirmCableTVActivity::class.java)
        intent.putExtra("custid", txtcustid)
        intent.putExtra("amou", amou)
        intent.putExtra("narra", narra)
        intent.putExtra("billname", strbillnm)
        intent.putExtra("ednamee", ednamee)
        intent.putExtra("ednumbb", ednumbb)
        intent.putExtra("billid", billid)
        intent.putExtra("serviceid", serviceid)
        intent.putExtra("label", strlabel)
        intent.putExtra("fullname", fullname)
        intent.putExtra("datetime", datetime)
        //  String refcodee = datas.optString("refNumber");
        intent.putExtra("refcode", refcodee)
        intent.putExtra("tref", tref)
        intent.putExtra("agcmsn", agcmsn)
        intent.putExtra("fee", strfee)


        startActivity(intent)
    }


    override fun onErrorResult(errormsg: String?) {


        txstatus.text = "TRANSACTION FAILURE"
        txdesc.text = errormsg
    }




}

