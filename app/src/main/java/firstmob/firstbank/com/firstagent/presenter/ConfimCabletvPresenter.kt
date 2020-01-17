package firstmob.firstbank.com.firstagent.presenter


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ConfimCabletvPresenter : GetBillersContract.PresenterConfirmCabletv, MainContract.GetDataIntractor.OnFinishedListener {
    internal var iView: GetBillersContract.IViewbillConfirmCabletv? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var context: Context? = null
    internal var session: SessionManagement? = null
   // internal var type: String? =null
    internal var serviceid: String? =null
    internal var planetsList: MutableList<GetServicesData> = ArrayList()
    constructor(context: Context, iView: GetBillersContract.IViewbillConfirmCabletv, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }

    override fun RequestServerfee(extrparams : String,serviceidp : String) {
        iView!!.showProgress()
        session!!.setTranstype("getfee")
        serviceid=serviceidp
        val endpoint = "fee/getfee.action"
        val userid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val params = "1/$userid/$agentid/BILLPAYMENT/$extrparams"
        var urlparams = ""
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint)
            //Log.d("cbcurl",url);
            Log.v("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            Log.e("encryptionerror", e.toString())
        }
        getDataIntractor!!.getResults(this, urlparams)

    }
    override fun RequestServervalidate(extrparams: String?) {
        iView!!.showProgress()
        session!!.setTranstype("validate")
        val endpoint = "billpayment/validateCustomer.action"
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobileno = Prefs.getString(AGMOB, "NA")
        val params = "1/$usid/$agentid/$mobileno/$extrparams"
        var urlparams = ""
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint)
            //Log.d("cbcurl",url);
            Log.v("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            Log.e("encryptionerror", e.toString())
        }
        getDataIntractor!!.getResults(this, urlparams)
    }
    override fun ondestroy() {
        iView = null
    }
    override fun onFinished(response: String?) {
        iView!!.hideProgress()
        try {
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,context);
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")
            if(session!!.transFlag.equals("getfee")){
                var respfee = obj.optString("fee")
               val agbalance = obj.optString("data")
                if (Utility.isNotNull(agbalance)) {
                    //acbal.setText(agbalance + Constants.KEY_NAIRA)
                    iView!!.setBalance(agbalance + Constants.KEY_NAIRA)
                }

                if (response != null) {
                    if (respcode == "00") {

                        SecurityLayer.Log("Response Message", responsemessage)

                        //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                        if (respfee == null || respfee.equals("")) {
                            iView!!.setFee("N/A")
                           // txtfee.setText("N/A")
                        } else {
                            respfee = Utility.returnNumberFormat(respfee)
                            //finalrespfee = respfee
                            iView!!.setFee(respfee)
                            if (Utility.checkStateCollect(serviceid)) {
                                //txtfee.setText(Constants.KEY_NAIRA + "63.00")
                                iView!!.setFee(Constants.KEY_NAIRA + "63.00")
                            } else {
                                iView!!.setFee(Constants.KEY_NAIRA + respfee)
                                //txtfee.setText()

                            }
                        }


                    } else if (respcode == "93") {
                        iView!!.onProcessingError(responsemessage)
                        iView!!.onBackpressed()
                        //onBackPressed()
                        iView!!.onProcessingError("Please ensure amount set is below the set limit")
                    } else {
                        //   btnsub.setVisibility(View.GONE);
                        iView!!.onProcessingError(responsemessage)
                    }
                } else {
                    iView!!.setFee("N/A")
                    //txtfee.setText("N/A")
                }
            }else{

                if (response != null) {
                    if (respcode == "00") {
                        val respfee = obj.optJSONObject("data")
                        SecurityLayer.Log("Response Message", responsemessage)

                        //                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                        if (respfee == null ) {

                        } else {

                            val bs = respfee.optString("FullName")
                            iView!!.setFullNames(bs)
                            //stt.setText(bs)
                        }
                        iView!!.checkfee(true)
                       // chkfee = true
                    } else if (respcode == "93") {
                        iView!!.onProcessingError(responsemessage)
                        iView!!.onProcessingError(obj.optString("responsemessage"))
                    } else {
                        iView!!.onProcessingError(responsemessage)
                    }
                } else {
                    iView!!.setFee("N/A")
                }

            }


          } catch (e: JSONException) {
        SecurityLayer.Log("encryptionJSONException", e.toString());
        Utility.errornexttoken();
        // TODO Auto-generated catch block
        if (!(context == null)) {
            Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
            iView!!.logoutuser()
           // SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), context);
        }
        // SecurityLayer.Log(e.toString());

    } catch (e: JSONException) {
        SecurityLayer.Log("encryptionJSONException", e.toString());
        Utility.errornexttoken();
        if (!(context == null)) {
            iView!!.logoutuser()
           // SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), context);
        }
        // SecurityLayer.Log(e.toString());
    }
    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }

}





