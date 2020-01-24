package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
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
import firstmob.firstbank.com.firstagent.utils.Utility.*
import javax.inject.Inject

class Withdraw_Firsts : Fragment(), View.OnClickListener, WithdrawalsContract.IViewWithdrawalFirst {
    @Inject
    internal lateinit var ul: Utility
    init {
        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var session: SessionManagement? = null
    internal var acno: EditText? = null
    internal var amo: EditText? = null
    internal var accnum: String? = null
   // internal var prgDialog: ProgressDialog? = null
    var viewDialog: ViewDialog? = null
    //internal var prgDialog2: ProgressDialog? = null
    internal var sigin: Button? = null
    internal var txtref: TextView? = null
    internal var r1: RadioButton? = null
    internal var r2: RadioButton? = null
    internal var r3: RadioButton? = null
    internal var lywithdr: LinearLayout? = null
    internal var lybutt: LinearLayout? = null
    internal var rlid: RelativeLayout? = null
    internal var sp1: Spinner? = null
    internal var sp2: Spinner? = null
    internal var sp3: Spinner? = null
    internal var btnok: Button? = null
    internal var acname: String? = null
    internal var txref: String? = null
    internal var accountoname: EditText? = null
    internal var cotp: EditText? = null
    private val SPLASH_TIME_OUT = 2500
    internal var amon: EditText? = null
    internal var edacc: EditText? = null
    internal var edamo: EditText? = null
    internal lateinit var presenter: WithdrawalsContract.PresenterGen
   // internal lateinit var presenter2: MainContract.PresenterGen
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.activity_withdraw__firsts, container, false)
        btnok = rootView.findViewById(R.id.button5)
        lywithdr = rootView.findViewById(R.id.lywithdr)
        txtref = rootView.findViewById(R.id.txref)
        lybutt = rootView.findViewById(R.id.lybutt)
        edacc = rootView.findViewById(R.id.input_payacc)
        edamo = rootView.findViewById(R.id.amount)
        cotp = rootView.findViewById(R.id.cotp)
        accountoname = rootView.findViewById(R.id.cname)
        presenter = WithdrawalfirstPresenter(requireContext(), this, FetchServerResponse())
     //   presenter2 = GetOtpPresenter(requireContext(), this, FetchServerResponse())
        btnok!!.setOnClickListener(this)
        viewDialog = ViewDialog(activity!!)
        val ofcListener = MyFocusChangeListener()
        edamo!!.setOnFocusChangeListener(ofcListener)
        edacc!!.setOnFocusChangeListener(ofcListener)
//        prgDialog!!.setCancelable(false)

        edtTextListener(edacc!!)
        return rootView
    }
    override fun AccountName(accountName: String?) {
        acname=accountName;
    }
    override fun onClick(v: View?) {

        if (v!!.getId() == R.id.button5) {
            val recaccno = edacc!!.getText().toString()
            val editamo = edamo!!.getText().toString()
            val ottp = cotp!!.getText().toString()
            if (isNotNull(recaccno)) {
                if (isNotNull(editamo)) {
                    val nwamo = editamo.replace(",", "")
                    SecurityLayer.Log("New Amount", nwamo)
                    if (isNotNull(ottp)) {
                        if (isNotNull(acname)) {
                            if (isNotNull(txref)) {
                                val b = Bundle()
                                b.putString("recanno", recaccno)
                                b.putString("amou", editamo)
                                b.putString("otp", ottp)
                                b.putString("txtname", acname)
                                b.putString("txref", txref)
                                val fragment = ConfirmWithdrawal()
                                fragment.setArguments(b)
                                val fragmentManager = fragmentManager
                                val fragmentTransaction = fragmentManager!!.beginTransaction()
                                //  String tag = Integer.toString(title);
                                fragmentTransaction.replace(R.id.container_body, fragment)
                                fragmentTransaction.commit()

                            } else {
                                showToast("Please ensure you have generated a withdrawal transaction for the customer")
                            }
                        } else {
                            showToast("Please ensure you have checked the account's name ")
                        }
                    } else {
                        showToast("Please enter a valid value for one time pin")
                    }
                } else {
                   showToast("Please enter a valid value for amount")
                }
            } else {
               showToast("Please enter a valid value for account number")
            }
        }

    }


    override fun setAccountName() {
        accountoname!!.setText(acname)
    }

    override fun requestOtp() {
        val recaccno = edacc!!.getText().toString()

        MaterialDialog.Builder(requireContext())
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

    override fun setReference(reference: String?) {
        txtref!!.setText(reference)
        txref=reference
    }

    override fun onProcessingError(error: String?) {
      showToast(error);
    }

    override fun showProgress() {
        viewDialog!!.showDialog()
        //prgDialog!!.show()
    }

    override fun hideProgress() {
        viewDialog!!.hideDialog()
      //  prgDialog!!.hide()
    }


    fun edtTextListener(edt: EditText) {
        edt!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edacc!!.getText().toString().length == 10) {
//                    prgDialog!!.show()
                    if (checkInternetConnection()) {
                        hideKeyboardFrom(activity, edacc)
                        viewDialog!!.showDialog()
                       // prgDialog!!.show()
                        val acno = edacc!!.getText().toString()
                        presenter.requestCallNameInquiry("getnameenq",acno)
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

    private inner class MyFocusChangeListener : View.OnFocusChangeListener {

        override fun onFocusChange(v: View, hasFocus: Boolean) {

            if (v.id == R.id.amount && !hasFocus) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val txt = edamo!!.getText().toString()
                val fbal = Utility.returnNumberFormat(txt)
                edamo!!.setText(fbal)

            }


        }
    }

    override fun onDestroy() {
        presenter.ondestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

}
