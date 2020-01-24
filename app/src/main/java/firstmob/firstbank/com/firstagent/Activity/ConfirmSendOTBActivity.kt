package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.ConfirmSendOTBContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfirmSendOTBPresenter
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.button2
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.pin
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.recacno
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.recamo
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.recname
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.recnarr
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.txtfee
import kotlinx.android.synthetic.main.activity_confirm_cash_depo_trans.*
import kotlinx.android.synthetic.main.activity_confirm_cash_depo_trans.recsendname
import kotlinx.android.synthetic.main.activity_confirm_cash_depo_trans.recsendnumber
import kotlinx.android.synthetic.main.activity_confirm_send_otb.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ConfirmSendOTBActivity : AppCompatActivity(), ConfirmSendOTBContract.ILoginView  {


    private var recanno: String? = null
    var viewDialog: ViewDialog? = null
    var amou: String? = null
    var narra: String? = null
    lateinit var ednamee: String
    var ednumbb: String? = null
    var txtname: String? = null
    var bankname: String? = null
    var bankcode: String? = null
    private var finalfee: String? = null
    private var boolchkfee: Boolean? = false
    var agbalance: String? = null

    internal lateinit var presenter: ConfirmSendOTBContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_send_otb)

        viewDialog = ViewDialog(this)

        presenter = ConfirmSendOTBPresenter(this, FetchServerResponse())
        val intent = intent
        if (intent != null) {

            recanno = intent.getStringExtra("recanno")
            amou = intent.getStringExtra("amou")
            narra = intent.getStringExtra("narra")
            ednamee = intent.getStringExtra("ednamee")
            ednumbb = intent.getStringExtra("ednumbb")
            txtname = intent.getStringExtra("txtname")
            bankname = intent.getStringExtra("bankname");
            bankcode = intent.getStringExtra("bankcode");


            recacno.text = recanno
            recname.text = txtname
            recbank.text = bankname

            recamo.text = Constants.KEY_NAIRA + amou
            recnarr.text = narra

            recsendname.text = ednamee
            recsendnumber.text = ednumbb
            amou = Utility.convertProperNumber(amou)

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


                                            val usid = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA")
                                            val agentid = Prefs.getString(SharedPrefConstants.AGENTID,"NA")
                                            val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB,"NA")
                                            val params = "1/$usid/$agentid/$mobnoo/1/$amou/$bankcode/$recanno/$txtname/$narra"

                                         //   val params = "1/$usid/$agentid/$mobnoo/1/$amou/$recanno/$txtname/$narra"

                                            val intent = Intent(this, TransactionProcessingActivity::class.java)




                                            intent.putExtra("recanno", recanno);
                                            intent.putExtra("amou", amou);
                                            intent.putExtra("narra", narra);
                                            intent.putExtra("ednamee", ednamee);
                                            intent.putExtra("ednumbb", ednumbb);
                                            intent.putExtra("txtname", txtname);
                                            intent.putExtra("bankname", bankname);
                                            intent.putExtra("bankcode", bankcode);

                                            intent.putExtra("params",params);
                                            intent.putExtra("txpin", encrypted);
                                            intent.putExtra("serv","SENDOTB");
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

    override fun onLoginResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}
