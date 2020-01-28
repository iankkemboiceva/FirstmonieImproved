package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import firstmob.firstbank.com.firstagent.utils.Utility.convertProperNumber
import android.content.Intent
import android.graphics.drawable.ColorDrawable

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
import firstmob.firstbank.com.firstagent.network.ApiInterface
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*


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
    private var finalfee: String? = null
    private var boolchkfee: Boolean? = false
    var agbalance: String? = null
    internal lateinit var presenter: ConfirmCashDepoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_cash_depo)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.nbkyellow)));
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

        button2.setOnClickListener {
            if (Utility.checkInternetConnection()) {
                val agpin = pin.text.toString()


                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(narra)) {
                            if (Utility.isNotNull(ednamee)) {
                                if (Utility.isNotNull(ednumbb)) {
                                    if (Utility.isNotNull(agpin)) {
                                        if (boolchkfee == true) {

                                            var encrypted: String? = null
                                            encrypted = Utility.b64_sha256(agpin)


                                            val usid = Prefs.getString(KEY_USERID,"NA")
                                            val agentid = Prefs.getString(AGENTID,"NA")
                                            val mobnoo = Prefs.getString(AGMOB,"NA")
                                            // "0000"
                                            val params = "1/$usid/$agentid/$mobnoo/2/$amou/$recanno/$txtname/$narra"

                                            val intent = Intent(this, TransactionProcessingActivity::class.java)

                                            intent.putExtra("params", params)
                                            intent.putExtra("serv", "CASHDEPO")
                                            intent.putExtra("recanno", recanno)
                                            intent.putExtra("amou", amou)

                                            intent.putExtra("narra", narra)
                                            intent.putExtra("ednamee", ednamee)
                                            intent.putExtra("ednumbb", ednumbb)
                                            intent.putExtra("txtname", txtname)
                                            intent.putExtra("txpin", encrypted)
                                            startActivity(intent)



                                        } else {
                                            Toast.makeText(
                                                    applicationContext,
                                                    "Please ensure fee modules are set up appropiately",
                                                    Toast.LENGTH_LONG).show()
                                        }

                                    } else {
                                        Toast.makeText(
                                                applicationContext,
                                                "Please enter a valid value for Agent PIN",
                                                Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(
                                            applicationContext,
                                            "Please enter a valid value for Depositor Number",
                                            Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(
                                        applicationContext,
                                        "Please enter a valid value for Depositor Name",
                                        Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(
                                    applicationContext,
                                    "Please enter a valid value for Narration",
                                    Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(
                                applicationContext,
                                "Please enter a valid value for Amount",
                                Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Please enter a value for Account Number",
                            Toast.LENGTH_LONG).show()
                }
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

    override fun settextfee(name: String?,chkfee: Boolean) {

        txtfee.text = name
        boolchkfee = chkfee

    }

    override fun hidebutton() {
        button2.visibility = View.GONE
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.getItemId() == android.R.id.home) {
            finish()
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
