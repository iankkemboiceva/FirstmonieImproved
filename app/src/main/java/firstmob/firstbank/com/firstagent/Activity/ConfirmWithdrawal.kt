package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.WithdrawalsContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ConfirmWithdrwalPresenter
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import javax.inject.Inject

class ConfirmWithdrawal : Fragment(), View.OnClickListener,WithdrawalsContract.IViewConfirmWithdrawal {
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var recacno: TextView? =null
    internal var recname:TextView? =null
    internal var recamo:TextView? =null
    internal var recnarr:TextView? =null
    internal var txtfee:TextView? =null
    internal var acbal:TextView? =null
    internal var btnsub: Button? =null
    internal var recanno: String? = null
    internal var amou:String? = null
    internal var txtname:String? = null
    internal var txref:String? = null
    internal var otp:String? = null
    var session : SessionManagement? =null
    internal var agbalance :String? = null
   // internal var prgDialog2:ProgressDialog? = null
    var viewDialog: ViewDialog? =null
    internal var etpin: EditText? =null
    internal var presenter: WithdrawalsContract.ConfirmWithdralPresenter? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater!!.inflate(R.layout.activity_confirm_withdrawal, container, false)
        recacno = root.findViewById(R.id.textViewnb2)
        recname = root.findViewById(R.id.textViewcvv)
        etpin = root.findViewById(R.id.pin)
        recamo = root.findViewById(R.id.textViewrrs)
        recnarr = root.findViewById(R.id.textViewrr)
        txtfee = root.findViewById(R.id.txtfee)
        session= SessionManagement(requireContext())
        viewDialog= ViewDialog(activity!!);
//        prgDialog2 = ProgressDialog(requireContext())
//        prgDialog2!!.setMessage("Loading....")
//        prgDialog2!!.setCancelable(false)
        presenter = ConfirmWithdrwalPresenter(activity, this, FetchServerResponse())
        btnsub = root.findViewById(R.id.button2)
        btnsub!!.setOnClickListener(this)


        val bundle = this.arguments
        if (bundle != null) {

            recanno = bundle.getString("recanno")
            amou = bundle.getString("amou")
            txtname = bundle.getString("txtname")
            txref = bundle.getString("txref")
            otp = bundle.getString("otp")
            recacno!!.setText(recanno)
            recname!!.setText(txtname)
            recamo!!.setText(amou)
            amou = Utility.convertProperNumber(amou)
            presenter!!.loaddata("/CWDBYACT/"+amou)

        }
        return root
    }
    override fun setFee(fee: String?) {
        txtfee!!.setText(fee);
    }

    override fun setBalance(Balance: String?) {
        acbal!!.setText(Utility.returnNumberFormat(Balance));
    }

    override fun launchWithdrawFrag() {
        val fragment = Withdraw_Firsts()
        val title = "Withdraw"
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.container_body, fragment)
        //fragmentTransaction.addToBackStack(title)
        fragmentTransaction.commit()
    }

    override fun setViewVisibillity() {
        btnsub!!.setVisibility(View.GONE);
    }

    override fun setLogout() {
        session!!.logoutUser()
        activity!!.finish()
        val i = Intent(activity, SignInActivity::class.java)
        startActivity(i)
        Utility.showToast("You have been locked out of the app. Please call customer care for further details")
    }
    override fun onError(error: String?) {
        Utility.showToast(error)
    }

    override fun setForcedLogout() {
        if (requireContext() != null) {
            MaterialDialog.Builder(requireContext())
                    .title(resources.getString(R.string.forceouterr))
                    .content(resources.getString(R.string.forceout))
                    .negativeText("CONTINUE")
                    .callback(object : MaterialDialog.ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                        }
                        override fun onNegative(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                            activity!!.finish()
                            session!!.logoutUser()
                            // After logout redirect user to Loing Activity
                            val i = Intent(requireContext(), SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    override fun showProgress() {
        viewDialog!!.showDialog()
        //prgDialog2!!.show()
    }

    override fun hideProgress() {
        viewDialog!!.hideDialog()
       // prgDialog2!!.dismiss()
    }

    override fun onClick(v: View?) {

        if (v!!.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                val agpin = etpin!!.getText().toString()
                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {

                            val encrypted: String? = null
                            val usid = Utility.gettUtilUserId(activity)
                            val agentid = Utility.gettUtilAgentId(activity)
                            val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
                          //  val b = Bundle()
                            val params = "1/$usid/$agentid/$mobnoo/$amou/$txref/$recanno/$txtname/Narr/$otp"

                            val intent = Intent(activity, TransactionProcessingActivity::class.java)

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

    }
    fun ClearPin() {
        etpin!!.setText("")
    }
}
