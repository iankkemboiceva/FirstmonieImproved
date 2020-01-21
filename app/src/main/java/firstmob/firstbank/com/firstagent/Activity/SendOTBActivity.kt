package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import firstmob.firstbank.com.firstagent.contract.CashDepoTransContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.fragments.OtherBankPage
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.CashDepoTransPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.hideKeyboardFrom
import kotlinx.android.synthetic.main.activity_cash_depo.button2
import kotlinx.android.synthetic.main.activity_cash_depo.cname
import kotlinx.android.synthetic.main.activity_cash_depo.edacc
import kotlinx.android.synthetic.main.activity_send_otb.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class SendOTBActivity : AppCompatActivity(), CashDepoTransContract.ILoginView {
    override fun onLoginResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var viewDialog: ViewDialog? = null
    internal lateinit var presenter: CashDepoTransContract.Presenter
    lateinit var acno: String
    lateinit var acname: String
    var bankname: String? = null;var bankcode:kotlin.String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otb)

        viewDialog = ViewDialog(this)

        selbank.setOnClickListener {
            val titlefrag = "Sasa"
            val fragment: Fragment = OtherBankPage()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, titlefrag)



            fragmentTransaction.commit()
        }



        edacc.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edacc.text.toString().length === 10) {


                    hideKeyboardFrom(applicationContext, edacc)


                    acno = edacc.text.toString()
                    presenter.NameEnquiry(acno)


                }
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        })

        button2.setOnClickListener {
            if (Utility.isNotNull(bankname)) {
                val recanno = edacc.text.toString()
                val amou: String = amount.getText().toString()
                val narra: String = ednarr.getText().toString()
                val ednamee: String = sendername.getText().toString()
                val ednumbb: String = sendno.getText().toString()
                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        val nwamo = amou.replace(",", "")
                        SecurityLayer.Log("New Amount", nwamo)
                        val txamou = nwamo.toDouble()
                        /* if (txamou >= 100) {*/if (Utility.isNotNull(narra)) {
                            if (Utility.isNotNull(ednamee)) {
                                if (Utility.isNotNull(ednumbb)) {
                                    if (Utility.isNotNull(bankcode)) {
                                        if (Utility.isNotNull(acname)) {
                                    /*        val intent = Intent(this@SendOTBActivity, ConfirmSendOTBActivity::class.java)
                                            intent.putExtra("recanno", recanno)
                                            intent.putExtra("amou", amou)
                                            intent.putExtra("narra", narra)
                                            intent.putExtra("ednamee", ednamee)
                                            intent.putExtra("ednumbb", ednumbb)
                                            intent.putExtra("txtname", acname)
                                            intent.putExtra("bankname", bankname)
                                            intent.putExtra("bankcode", bankcode)
                                            startActivity(intent)
                                    */    } else {
                                            Toast.makeText(
                                                    applicationContext,
                                                    "Please enter a valid account number",
                                                    Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                                applicationContext,
                                                "Please select a bank",
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
                        /*   } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please enter a valid amount more than 100 Naira",
                                    Toast.LENGTH_LONG).show();
                        }*/
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
            } else {
                Toast.makeText(applicationContext, "Please select a bank", Toast.LENGTH_LONG).show()
            }

        }

        presenter = CashDepoTransPresenter(this, FetchServerResponse())
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun showProgress() {
        viewDialog?.showDialog()
    }

    override fun setaccname(name: String) {
        acname = name
        cname.setText(name)

    }

    override fun onResume() {
        super.onResume()//DETERMINE WHO STARTED THIS ACTIVITY
        val sender=this.intent.extras.getString("SENDER_KEY")

        //IF ITS THE FRAGMENT THEN RECEIVE DATA
        if(sender != null)
        {
            val i = intent
            val bankname = i.getStringExtra("bankname")
            val bankcode = i.getStringExtra("bankcode")
            Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show()

        }
    }


}
