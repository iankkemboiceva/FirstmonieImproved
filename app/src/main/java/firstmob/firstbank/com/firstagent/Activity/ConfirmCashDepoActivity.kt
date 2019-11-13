package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import firstmob.firstbank.com.firstagent.utils.Utility.convertProperNumber
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.Toast
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA
import firstmob.firstbank.com.firstagent.contract.CashDepoContract
import firstmob.firstbank.com.firstagent.contract.ConfirmCashDepoContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.CashDepoPresenter
import firstmob.firstbank.com.firstagent.presenter.ConfirmCashDepoPresenter
import firstmob.firstbank.com.firstagent.utils.Utility

import kotlinx.android.synthetic.main.activity_confirm_cash_depo.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ConfirmCashDepoActivity : AppCompatActivity(), ConfirmCashDepoContract.ILoginView {

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
    var finalfee: String? = null
    var agbalance: String? = null

    internal lateinit var presenter: ConfirmCashDepoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_cash_depo)

        viewDialog = ViewDialog(this)

        presenter = ConfirmCashDepoPresenter(this, FetchServerResponse())
        val intent = intent
        if (intent != null) {

            recanno = intent.getStringExtra("recanno")
            amou = intent.getStringExtra("amou")
            narra = intent.getStringExtra("narra")
            ednamee = intent.getStringExtra("ednamee")
            ednumbb = intent.getStringExtra("ednumbb")
            txtname = intent.getStringExtra("txtname")


            recacno.text = recanno
            recname.text = txtname

            recamo.text = KEY_NAIRA + amou
            recnarr.text = narra
            amou = convertProperNumber(amou)

            presenter.getFeeSec(amou)
            //   getFeeSec()

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

    override fun settextfee(name: String?) {

        txtfee.text = name

    }

    override fun hidebutton() {
        button2.visibility = View.GONE
    }

}
