package firstmob.firstbank.com.firstagent.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import firstmob.firstbank.com.firstagent.Activity.AirtimeTransf
import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.Activity.Withdraw
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.SignInContract
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetFeePresenter
import firstmob.firstbank.com.firstagent.presenter.GetOtpPresenter
import firstmob.firstbank.com.firstagent.presenter.NameEnquirryPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject

class Withdraw_Firsts : Fragment(), View.OnClickListener, MainContract.IViewGen {

    internal var session: SessionManagement? = null
    internal var acno: EditText? = null
    internal var amo: EditText? = null
    internal var accnum: String? = null
    internal var prgDialog: ProgressDialog? = null
    internal var prgDialog2: ProgressDialog? = null
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
    internal lateinit var presenter: MainContract.PresenterGen
    internal lateinit var presenter2: MainContract.PresenterGen


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
        presenter = NameEnquirryPresenter(requireContext(), this, FetchServerResponse())
        presenter2 = GetOtpPresenter(requireContext(), this, FetchServerResponse())
        btnok!!.setOnClickListener(this)
        prgDialog = ProgressDialog(activity)
        prgDialog!!.setMessage("Loading....")

//        prgDialog2 = ProgressDialog(activity)
//        prgDialog2!!.setMessage("Loading....")
//        prgDialog2!!.setCancelable(false)
        // Set Cancelable as False
        val ofcListener = MyFocusChangeListener()
        edamo!!.setOnFocusChangeListener(ofcListener)

        edacc!!.setOnFocusChangeListener(ofcListener)

        prgDialog!!.setCancelable(false)

        edtTextListener(edacc!!)
        return rootView
    }
    override fun onClick(v: View?) {

        if (v!!.getId() == R.id.button5) {
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

    }
    override fun onfetchResult(flag: String?, response: String?) {
        if (flag.equals("nameinquiery")) {
            respnameenquiry(response)
        } else {
          responsegetotp(response)
        }

    }

    override fun onError(error: String?) {
        Utility.showToast(error)

    }

    override fun showProgress() {
        prgDialog!!.show()
    }

    override fun hideProgress() {
        prgDialog!!.hide()
    }


    fun edtTextListener(edt: EditText) {
        edt!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edacc!!.getText().toString().length == 10) {
//                    prgDialog!!.show()
                    if (Utility.checkInternetConnection()) {

                        Utility.hideKeyboardFrom(activity, edacc)
                        prgDialog!!.show()

                        val acno = edacc!!.getText().toString()
                        presenter.requestCall(acno)


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

    fun respnameenquiry(response: String?) {
        try {
            // JSON Object

            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")


            val plan = obj.optJSONObject("data")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);


            if (response != null) {
                if (respcode == "00") {

                    SecurityLayer.Log("Response Message", responsemessage)

                    //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                    if (plan != null) {
                        acname = plan!!.optString("accountName")

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

                                        presenter2.requestCall(params)

                                    }

                                    override fun onNegative(dialog: MaterialDialog) {
                                        dialog.dismiss()
                                    }
                                })
                                .show()
                    } else {
                     Utility.showToast("This is not a valid account number.Please check again")
                    }

                } else {
                    Utility.showToast(responsemessage)
                }
            } else {
                Utility.showToast("There was an error processing your request ")
            }


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(activity, activity!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            // SecurityLayer.Log(e.toString());
        }

    }
    fun responsegetotp(response: String?) {


        try {
            // JSON Object

            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")


            val plan = obj.optString("data")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);


            if (response!= null) {
                txref = plan
                if (Utility.isNotNull(txref)) {
                    txtref!!.setText(txref)
                }
                if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                    SecurityLayer.Log("Response Message", responsemessage)
                    if (respcode == "00") {
                        Utility.showToast("\"Customer can proceed to input their OTP to complete transaction\"")
                        lybutt!!.setVisibility(View.VISIBLE)
                        lywithdr!!.setVisibility(View.VISIBLE)
                        accountoname!!.setText(acname)
                    } else {
                        Toast.makeText(
                                activity,
                                responsemessage,
                                Toast.LENGTH_LONG).show()
                    }


                } else {
                    Utility.showToast("There was an error on your request")
                }

            } else {
                Utility.showToast("There was an error on your request")
            }


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(activity, activity!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            // SecurityLayer.Log(e.toString());
        }


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

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDestroy() {
        presenter.ondestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

}
