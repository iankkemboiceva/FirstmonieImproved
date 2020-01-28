package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.WithdrawalsContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.WithdrawalfirstPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

class WithdrawActivity : AppCompatActivity(), View.OnClickListener,WithdrawalsContract.IViewWithdrawalFirst{
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
    }

    internal var acno: EditText? = null
    internal var amo:EditText? = null
    internal var accnum: String? = null
    internal var btn_back: Button? = null
    internal var txtref: TextView? =null
    internal var r1: RadioButton? = null
    internal var r2:RadioButton? = null
    internal var r3:RadioButton? = null
    internal var lywithdr: LinearLayout? =null
    internal var lybutt:LinearLayout? =null
    internal var rlid: RelativeLayout? = null
    internal var sp1: Spinner? = null
    internal var sp2:Spinner? = null
    internal var sp3:Spinner? = null
    lateinit var viewDialog: ViewDialog
    internal var btnok: Button? =null
    internal var acname: String? =null
    internal var txref: String? =null
    internal var accountoname: EditText? =null
    internal var cotp:EditText? =null
    private val SPLASH_TIME_OUT = 2500
    internal var amon: EditText? = null
    internal var edacc:EditText? =null
    internal var session: SessionManagement? =null
    internal var edamo:EditText? =null
    internal lateinit var presenter: WithdrawalsContract.PresenterGen
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_withdraw2)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.lightgree_withdrcolor)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        session= SessionManagement(this)
        btnok = findViewById(R.id.button5) as Button
        btn_back = findViewById(R.id.button3) as Button
        session = SessionManagement(this)
        lywithdr = findViewById(R.id.lywithdr) as LinearLayout
        txtref = findViewById(R.id.txref) as TextView
        lybutt = findViewById(R.id.lybutt) as LinearLayout
        edacc = findViewById(R.id.input_payacc) as EditText
        edamo = findViewById(R.id.amount) as EditText
        cotp = findViewById(R.id.cotp) as EditText
        accountoname = findViewById(R.id.cname) as EditText
        btnok!!.setOnClickListener(this)
        btn_back!!.setOnClickListener(this)
        presenter = WithdrawalfirstPresenter(this, this, FetchServerResponse())
        viewDialog= ViewDialog(this)
        val ofcListener = MyFocusChangeListener()
        edamo!!.setOnFocusChangeListener(ofcListener)
        edacc!!.setOnFocusChangeListener(ofcListener)
        edacc!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edacc!!.getText().toString().length == 10) {
                   // prgDialog!!.show()
                  //  viewDialog!!.showDialog()
                    if (Utility.checkInternetConnection()) {

                        Utility.hideKeyboardFrom(applicationContext, edacc)
                       // viewDialog!!.showDialog()
                       // prgDialog!!.show()

                        val acno = edacc!!.getText().toString()
                        presenter.requestCallNameInquiry("getnameenq",acno)
                       // NameInquirySec(acno)

                    }
                    //   accountoname.setText("Test Customer");


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
}
    override fun AccountName(accountName: String?) {
        acname=accountName;
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button5) {
            val recaccno = edacc!!.getText().toString()
            val editamo = edamo!!.getText().toString()
            val ottp = cotp!!.getText().toString()
            if (Utility.isNotNull(recaccno)) {
                if (Utility.isNotNull(editamo)) {
                    val nwamo = editamo.replace(",", "")
                    SecurityLayer.Log("New Amount", nwamo)
                    val txamou = java.lang.Double.parseDouble(nwamo)
                    /*  if (txamou >= 100) {*/
                    if (Utility.isNotNull(ottp)) {
                        if (Utility.isNotNull(acname)) {
                            if (Utility.isNotNull(txref)) {
                                val intent = Intent(this@WithdrawActivity, ConfirmWithdrawalActivity::class.java)
                                intent.putExtra("recanno", recaccno)
                                intent.putExtra("amou", editamo)
                                intent.putExtra("otp", ottp)
                                intent.putExtra("txtname", acname)
                                intent.putExtra("txref", txref)


                                startActivity(intent)

                            } else {
                                Utility.showToast("Please ensure you have generated a withdrawal transaction for the customer")
                            }
                        } else {
                            Utility.showToast("Please ensure you have checked the account's name ")
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for one time pin")
                    }
                } else {
                    Utility.showToast("Please enter a valid value for amount")
                }
            } else {
                Utility.showToast("Please enter a valid value for account number")
            }
        }
        if (view!!.id==R.id.button3){
            finish()
        }
    }


    private inner class MyFocusChangeListener : View.OnFocusChangeListener {

        override fun onFocusChange(v: View, hasFocus: Boolean) {

            if (v.id == R.id.amount && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val txt = edamo!!.getText().toString()
                val fbal = Utility.returnNumberFormat(txt)
                edamo!!.setText(fbal)

            }


        }
    }

    override fun setAccountName() {
        accountoname!!.setText(acname)
    }

    override fun requestOtp() {
        val recaccno = edacc!!.getText().toString()
        MaterialDialog.Builder(this)
                .title("Account Details")
                .content("The following are the recipient account details  \n \n" +
                        " Account Number: " + recaccno + " \n   Account Name:" + acname + "   \n Do you wish to proceed with this transaction?")
                .positiveText("Confirm")
                .negativeText("Cancel")
                .callback(object : MaterialDialog.ButtonCallback() {
                    override fun onPositive(dialog: MaterialDialog) {


                        //prgDialog2!!.show()
                        val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
                        val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
                        val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
                        val params = "1/$usid/$agentid/$mobnoo/$recaccno/$acname"
                        presenter.requestCallGetotp("getotp",params)
                        // presenter2.requestCall(params)

                    }

                    override fun onNegative(dialog: MaterialDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
    }

    override fun setViewVisibility() {
        lybutt!!.setVisibility(View.VISIBLE)
        lywithdr!!.setVisibility(View.VISIBLE)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    override fun setReference(reference: String?) {
        txtref!!.setText(reference)
        txref=reference
    }

    override fun onProcessingError(error: String?) {
        Utility.showToast(error);
    }

    override fun showProgress() {
        viewDialog?.showDialog()
        //prgDialog!!.show()
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
        //  prgDialog!!.hide()
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        // TODO Auto-generated method stub
        presenter!!.ondestroy()
        super.onDestroy()
    }
}