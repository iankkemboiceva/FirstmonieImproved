package firstmob.firstbank.com.firstagent.presenter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.Activity.Withdraw
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.WithdrawalsContract
import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject


class WithdrawalfirstPresenter : WithdrawalsContract.PresenterGen, MainContract.GetDataIntractor.OnFinishedListener {


    internal var iView: WithdrawalsContract.IViewWithdrawalFirst? = null
    private var getDataIntractor:
            MainContract.GetDataIntractor? = null
    internal var session : SessionManagement?=null
    internal var context: Context? = null

    constructor(context: Context, iView: WithdrawalsContract.IViewWithdrawalFirst, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session=SessionManagement(context)
    }
    override fun requestCallGetotp(flag: String?,extraparam: String?) {
        session!!.setTranstype(flag)
        iView!!.showProgress()
        val endpoint = "withdrawal/cashbyaccountinit.action"
        val params = extraparam
        var urlparams = ""
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint)
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            SecurityLayer.Log("encryptionerror", e.toString())
        }
        getDataIntractor!!.getResults(this, urlparams)
    }

    override fun requestCallNameInquiry(flag :String?,extraparam: String?) {
        session!!.setTranstype(flag)
        iView!!.showProgress()
        val endpoint = "transfer/nameenq.action"
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobileno = Prefs.getString(AGMOB, "NA")
        val params = "1/$usid/$agentid/$mobileno/0/$extraparam"
        var urlparams = ""
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint)
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            SecurityLayer.Log("encryptionerror", e.toString())
        }
        getDataIntractor!!.getResults(this, urlparams)
    }

    override fun ondestroy() {
        iView = null
    }

    override fun onFinished(response: String?) {
        iView!!.hideProgress()

        try {
            // JSON Object

            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")
            if(session!!.getTransFlag().equals("getnameenq")){
                if (response != null) {
                    if (respcode == "00") {

                        SecurityLayer.Log("Response Message", responsemessage)

                        //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                        val plan = obj.optJSONObject("data")
                        if (plan != null) {
                          val  acname = plan!!.optString("accountName")
                            iView!!.AccountName(acname)
                            iView!!.requestOtp()

                        } else {
                            Utility.showToast("This is not a valid account number.Please check again")
                        }

                    } else {
                        Utility.showToast(responsemessage)
                    }
                } else {
                    Utility.showToast("There was an error processing your request ")
                }
            }else {
                val plan = obj.optString("data")
                //session.setString(SecurityLayer.KEY_APP_ID,appid);
                if (response!= null) {
                   val txref = plan
                    if (Utility.isNotNull(txref)) {
                        iView!!.setReference(txref)
                        //txtref!!.setText(txref)
                    }
                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage)
                        if (respcode == "00") {
                            Utility.showToast("\"Customer can proceed to input their OTP to complete transaction\"")
                            iView!!.setViewVisibility()
                            iView!!.setAccountName();
                           // accountoname!!.setText(acname)
                        } else {
                            iView!!.onProcessingError(responsemessage)
                        }


                    } else {
                        Utility.showToast("There was an error on your request")
                    }

                } else {
                    Utility.showToast("There was an error on your request")
                }
            }


    }catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()


        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // SecurityLayer.Log(e.toString());
        }

    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
}





