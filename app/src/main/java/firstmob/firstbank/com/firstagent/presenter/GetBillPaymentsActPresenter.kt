package firstmob.firstbank.com.firstagent.presenter


import android.content.Context
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetBillPayData
import firstmob.firstbank.com.firstagent.model.GetBillersData
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class GetBillPaymentsActPresenter : GetBillersContract.Presenterloadbillerspc, MainContract.GetDataIntractor.OnFinishedListener {

    internal var iView: GetBillersContract.IViewbillPaymentsAct? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    val KEY_TOKEN = "token"
    internal var context: Context? = null
    internal var serviceid: String? =null
    internal var session: SessionManagement? = null
    internal var planetsList: MutableList<GetBillPayData> = ArrayList()
    constructor(context: Context, iView: GetBillersContract.IViewbillPaymentsAct, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }

    override fun loadbiller(extraparams :String?) {
        iView!!.showProgress()
        serviceid==extraparams
        val endpoint = "billpayment/billpaymentitems.action"
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobnoo = Prefs.getString(AGMOB, "NA")
        val params = "1/$usid/$agentid/$mobnoo/$extraparams"
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
    override fun loadcachedbiilersData(extraparams: String?) {
        planetsList.clear()
        try {
            val bsid = session!!.getString("getbillpay$extraparams")
            val servdata = JSONArray(bsid)
            if (servdata.length() > 0) {


                var json_data: JSONObject? = null
                for (i in 0 until servdata.length()) {
                    json_data = servdata.getJSONObject(i)
                    //String accid = json_data.getString("benacid");


                    val id = json_data!!.optString("packId")

                    val billerId = json_data.optString("billerId")

                    val displayName = json_data.optString("displayName")
                    var charge = json_data.optString("charge")
                    charge = java.lang.Double.toString(java.lang.Double.parseDouble(charge))
                    val paymentCode = json_data.optString("paymentCode")


                    planetsList.add(GetBillPayData(id, displayName, billerId, paymentCode, charge))


                }

                iView!!.onResult(planetsList)
//                    aAdpt = BillerPayMenuAdapt(planetsList, this@GetBillPaymentsActivity)
//                    lv.setAdapter(aAdpt)



            } else {

                iView!!.navigatetoNextpage()
            }
        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            // SecurityLayer.Log(e.toString());

        }

    }

    override fun ondestroy() {
        iView = null
    }

    override fun onFinished(response: String?) {
        iView!!.hideProgress()

        try {
            // JSON Object


            SecurityLayer.Log("Cable TV Resp", response)
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getApplicationContext());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())


            val servdata = obj.optJSONArray("data")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (response!= null) {
                val respcode = obj.optString("responseCode")
                val responsemessage = obj.optString("message")
                SecurityLayer.Log("Response Message", responsemessage)

                if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                    if (!Utility.checkUserLocked(respcode)) {
                        SecurityLayer.Log("Response Message", responsemessage)

                        if (respcode == "00") {
                            SecurityLayer.Log("JSON Aray", servdata.toString())
                            if (servdata.length() > 0) {


                                var json_data: JSONObject? = null
                                for (i in 0 until servdata.length()) {
                                    json_data = servdata.getJSONObject(i)
                                    //String accid = json_data.getString("benacid");


                                    val id = json_data!!.optString("packId")
                                    val billerId = json_data.optString("billerId")
                                    val displayName = json_data.optString("displayName")
                                    var charge = json_data.optString("charge")
                                    charge = java.lang.Double.toString(java.lang.Double.parseDouble(charge))
                                    val paymentCode = json_data.optString("paymentCode")
                                    planetsList.add(GetBillPayData(id, displayName, billerId, paymentCode, charge))


                                }
                                session!!.setString("getbillpay$serviceid", servdata.toString())
                               iView!!.onResult(planetsList)


                            } else {
                                iView!!.navigatetoNextpage()
                            }

                        } else {
                            Utility.showToast(responsemessage)
                        }
                    } else {
                        /* getApplicationContext().finish();
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();*/

                        iView!!.logoutuser()
                        //LogOut()

                    }
                } else {

                    Utility.showToast("There was an error on your request")


                }
            } else {
              Utility.showToast("There was an error on your request")
            }
            // prgDialog2.dismiss();


        } catch (e: JSONException) {
            Utility.errornexttoken()
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            if (context != null) {
                Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
                iView!!.logoutuser()
                //  SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext())
                // SecurityLayer.Log(e.toString());
            }

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            Utility.errornexttoken()
            if (context != null) {
                iView!!.logoutuser()
               // SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext())
            }
            // SecurityLayer.Log(e.toString());
        }

        SecurityLayer.Log("After Req Tok", session!!.getString(KEY_TOKEN))


    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }

}









