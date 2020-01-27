package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.WithdrawalsContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfirmWithdrwalPresenter
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

class ConfirmWithdrawalActivity : BaseActivity(), View.OnClickListener,WithdrawalsContract.IViewConfirmWithdrawal {
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    internal var recacno: TextView? =null
    internal var recname:TextView? =null
    internal var recamo:TextView? =null
    var viewDialog: ViewDialog? =null
    internal var recnarr:TextView? =null
    internal var recsendnum:TextView? =null
    internal var recsendnam:TextView? =null
    internal var step2:TextView? =null
    internal var txtfee:TextView? =null
    internal var acbal:TextView? =null
    internal var btnsub: Button? =null
    internal var recanno: String? =null
    internal var amou:String? =null
    internal var txtname:String? =null
    internal var txref:String? =null
    internal var presenter: WithdrawalsContract.ConfirmWithdralPresenter? =null
    internal var otp:String? =null
    internal var agbalance:String? =null
//    internal var prgDialog: ProgressDialog? = null
//    internal var prgDialog2:ProgressDialog? = null
    internal var etpin: EditText? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_withdrawal)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.lightgree_withdrcolor)));
        supportActionBar!!.title = null
        session = SessionManagement(this)
        recacno = findViewById(R.id.textViewnb2) as TextView
        recname = findViewById(R.id.textViewcvv) as TextView
        acbal = findViewById(R.id.txtacbal) as TextView
        etpin = findViewById(R.id.pin) as EditText
        recamo = findViewById(R.id.textViewrrs) as TextView
        recnarr = findViewById(R.id.textViewrr) as TextView
        txtfee = findViewById(R.id.txtfee) as TextView
        recsendnam = findViewById(R.id.sendnammm) as TextView
        recsendnum = findViewById(R.id.sendno) as TextView
        viewDialog= ViewDialog(this)
        btnsub = findViewById(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)
        presenter = ConfirmWithdrwalPresenter(this, this, FetchServerResponse())
        step2 = findViewById(R.id.tv2) as TextView
        step2!!.setOnClickListener(this)

        val intent = intent
        if (intent != null) {
            recanno = intent.getStringExtra("recanno")
            amou = intent.getStringExtra("amou")
            txtname = intent.getStringExtra("txtname")
            txref = intent.getStringExtra("txref")
            otp = intent.getStringExtra("otp")
            recacno!!.setText(recanno)
            recname!!.setText(txtname)
            recamo!!.setText(amou)
            amou = Utility.convertProperNumber(amou)
            presenter!!.loaddata("/CWDBYACT/"+amou)
            //getFeeSec()

        }
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                val agpin = etpin!!.getText().toString()
                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            var encrypted: String? = null
                            encrypted = Utility.b64_sha256(agpin)

                            val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
                            val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
                            val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
                            val params = "1/$usid/$agentid/$mobnoo/$amou/$txref/$recanno/$txtname/Narr/$otp"

                            val intent = Intent(this@ConfirmWithdrawalActivity, TransactionProcessingActivity::class.java)
                            intent.putExtra("recanno", recanno)
                            intent.putExtra("recanno", recanno)
                            intent.putExtra("amou", amou)
                            intent.putExtra("otp", otp)
                            intent.putExtra("txtname", txtname)
                            intent.putExtra("txref", txref)
                            intent.putExtra("params", params)
                            intent.putExtra("txpin", encrypted)
                            intent.putExtra("serv", "WDRAW")

                            startActivity(intent)

                            ClearPin()
                        } else {
                            Utility.showToast("Please enter a valid value for Agent PIN")
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount")
                    }
                } else {
                    Utility.showToast("Please enter a value for Account Number")
                }
            }
        }
        if (view.getId() == R.id.tv2) {
            finish()
            val intent = Intent(this@ConfirmWithdrawalActivity, WithdrawActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    override fun setFee(fee: String?) {
        txtfee!!.setText(fee);
    }

    override fun setBalance(Balance: String?) {
        acbal!!.setText(Utility.returnNumberFormat(Balance));
    }

    override fun launchWithdrawFrag() {
     finish()
    }

    override fun setViewVisibillity() {
        btnsub!!.setVisibility(View.GONE);
    }

    override fun setLogout() {
        session!!.logoutUser()
        finish()
        val i = Intent(this, SignInActivity::class.java)
        startActivity(i)
        Utility.showToast("You have been locked out of the app. Please call customer care for further details")
    }
    override fun onError(error: String?) {
        Utility.showToast(error)
    }
    override fun setForcedLogout() {
        if (applicationContext != null) {
            MaterialDialog.Builder(applicationContext)
                    .title(resources.getString(R.string.forceouterr))
                    .content(resources.getString(R.string.forceout))
                    .negativeText("CONTINUE")
                    .callback(object : MaterialDialog.ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                        }
                        override fun onNegative(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                            finish()
                            session!!.logoutUser()
                            // After logout redirect user to Loing Activity
                            val i = Intent(applicationContext, SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    override fun showProgress() {
        viewDialog?.showDialog()
        //prgDialog2!!.show()
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
        // prgDialog2!!.dismiss()
    }
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun onDestroy() {
        // TODO Auto-generated method stub
        presenter?.ondestroy()
        if (viewDialog != null) {
            viewDialog?.hideDialog()
        }
        super.onDestroy()
    }
    fun ClearPin() {
        etpin!!.setText("")
    }
}
