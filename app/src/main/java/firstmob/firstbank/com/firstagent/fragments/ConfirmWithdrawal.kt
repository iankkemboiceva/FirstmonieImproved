package firstmob.firstbank.com.firstagent.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.Activity.TransactingProcessing
import firstmob.firstbank.com.firstagent.Activity.Withdraw
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.GetFeePresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject

class ConfirmWithdrawal : Fragment(), View.OnClickListener,MainContract.IViewFee {


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

    internal var agbalance :String? = null
    internal var prgDialog2:ProgressDialog? = null
    internal var etpin: EditText? =null
    internal var presenter: MainContract.PresenterGetFee? =null
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
        prgDialog2 = ProgressDialog(activity)
        prgDialog2!!.setMessage("Loading....")
        prgDialog2!!.setCancelable(false)
        presenter = GetFeePresenter(activity, this, FetchServerResponse())
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
    override fun onfetchResult(flag: String?, response: String?) {
     if(flag.equals("getfee")){
     respgetfee(response)
     }else{

     }
    }

    override fun onError(error: String?) {
        Utility.showToast(error)
    }

    override fun showProgress() {
        prgDialog2!!.show()
    }

    override fun hideProgress() {
        prgDialog2!!.dismiss()
    }

    override fun onClick(v: View?) {

        if (v!!.getId() == R.id.button2) {
            if (Utility.checkInternetConnection()) {
                val agpin = etpin!!.getText().toString()
                if (Utility.isNotNull(recanno)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            if (prgDialog2 != null && activity != null) {
                                // prgDialog2.show();
                            }
                            val encrypted: String? = null


                            val usid = Utility.gettUtilUserId(activity)
                            val agentid = Utility.gettUtilAgentId(activity)
                            val mobnoo = Utility.gettUtilMobno(activity)
                            val params = "1/$usid/$agentid/$mobnoo/$amou/$txref/$recanno/$txtname/Narr/$otp"


                            val b = Bundle()
                            b.putString("recanno", recanno)
                            b.putString("amou", amou)
                            b.putString("otp", otp)
                            b.putString("txtname", txtname)
                            b.putString("txref", txref)
                            b.putString("params", params)
                            b.putString("txpin", encrypted)
                            b.putString("serv", "WDRAW")
                            val fragment = TransactingProcessing()

                            fragment.setArguments(b)
                            val fragmentManager = fragmentManager
                            val fragmentTransaction = fragmentManager!!.beginTransaction()
                            //  String tag = Integer.toString(title);
                            fragmentTransaction.replace(R.id.container_body, fragment, "Final Confirm Withdrawal")
                            fragmentTransaction.commit()
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
    fun respgetfee(response: String?){
        try {
            // JSON Object

            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")
            var respfee: String? = obj.optString("fee")
            agbalance = obj.optString("data")
            if (Utility.isNotNull(agbalance)) {
                acbal!!.setText(Utility.returnNumberFormat(agbalance) + Constants.KEY_NAIRA)
            }

            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                if (!Utility.checkUserLocked(respcode)) {
                    if (response!= null) {
                        if (respcode == "00") {
                            SecurityLayer.Log("Response Message", responsemessage)
                            if (respfee == null || respfee == "") {
                                txtfee!!.setText("N/A")
                            } else {
                                respfee = Utility.returnNumberFormat(respfee)
                                txtfee!!.setText(Constants.KEY_NAIRA + respfee!!)
                            }

                        } else if (respcode == "93") {
                            Utility.showToast(responsemessage)
                            val fragment = Withdraw_Firsts()
                            val fragmentManager = fragmentManager
                            val fragmentTransaction = fragmentManager!!.beginTransaction()
                            fragmentTransaction.replace(R.id.container_body, fragment)
                            fragmentTransaction.commit()
                            Utility.showToast("Please ensure amount set is below the set limit")
                        } else {
                            btnsub!!.setVisibility(View.GONE)
                            Utility.showToast(responsemessage)
                        }
                    } else {
                        txtfee!!.setText("N/A")
                    }
                } else {
                    (activity as Withdraw).LogOut()
                }
            }


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            if (activity != null) {
                Utility.showToast(activity!!.getText(R.string.conn_error).toString())
                (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            }

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            if (activity != null) {
                (activity as Withdraw).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), activity)
            }
            // SecurityLayer.Log(e.toString());
        }
    }
}
