package firstmob.firstbank.com.firstagent.presenter


import android.content.Context
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetBillersData
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject


class GetBillersSpecMenuPresenter : GetBillersContract.Presenterloadbillerspc, MainContract.GetDataIntractor.OnFinishedListener {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var iView: GetBillersContract.IViewbillersSpec? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var context: Context? = null
    internal var serviceid: String? =null
    internal var session: SessionManagement? = null
    internal var planetsList: MutableList<GetBillersData> = ArrayList()
    constructor(context: Context, iView: GetBillersContract.IViewbillersSpec, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }

    override fun loadbiller(extraparams :String?) {
        iView!!.showProgress()
        serviceid==extraparams
        val endpoint = "billpayment/getbillers.action"
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
            val bsid = session!!.getString("bllservid$extraparams")
            val servdata = JSONArray(bsid)
            if (servdata.length() > 0) {


                var json_data: JSONObject? = null
                for (i in 0 until servdata.length()) {
                    json_data = servdata.getJSONObject(i)
                    val id = json_data!!.optString("id")
                    val billerId = json_data.optString("billerId")
                    val billerDesc = json_data.optString("billerDesc")
                    val billerName = json_data.optString("billerName")
                    val accnumber = json_data.optString("acountNumber")
                    val customerField = json_data.optString("customerField")
                    val charge = json_data.optString("charge")
                    planetsList.add(GetBillersData(id, billerId, billerDesc, billerName, customerField, charge, accnumber))

                }
                if (context != null) {
                    Collections.sort(planetsList) { d1, d2 -> d1.billerName.compareTo(d2.billerName) }
                    iView!!.onResult(planetsList)
                }


            } else {
                Toast.makeText(
                        context,
                        "No billers available  ",
                        Toast.LENGTH_LONG).show()
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
            SecurityLayer.Log("Cable TV Resp", response)
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())
            val servdata = obj.optJSONArray("data")
            if (response != null) {
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


                                    val id = json_data!!.optString("id")

                                    val billerId = json_data.optString("billerId")
                                    val billerDesc = json_data.optString("billerDesc")
                                    val billerName = json_data.optString("billerName")
                                    val accnumber = json_data.optString("acountNumber")
                                    val customerField = json_data.optString("customerField")
                                    val charge = json_data.optString("charge")
                                    planetsList.add(GetBillersData(id, billerId, billerDesc, billerName, customerField, charge, accnumber))
                                    session!!.setString("bllservid$serviceid", servdata.toString())


                                }
                                if (context != null) {
                                    Collections.sort(planetsList) { d1, d2 -> d1.getBillerName().compareTo(d2.getBillerName()) }
                                   iView!!.onResult(planetsList)
                                }
                            } else {
                                Toast.makeText(
                                        context,
                                        "No billers available  ",
                                        Toast.LENGTH_LONG).show()
                            }

                        } else {
                            Toast.makeText(
                                    context,
                                    "" + responsemessage,
                                    Toast.LENGTH_LONG).show()
                        }
                    } else {

                        iView!!.logoutuser()
                       // LogOut()


                    }
                } else {

                    Toast.makeText(
                            context,
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show()


                }
            } else {

                Toast.makeText(
                        context,
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show()


            }
            // prgDialog2.dismiss();


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            Utility.errornexttoken()
            // TODO Auto-generated catch block
            // SecurityLayer.Log(e.toString());

            if (context != null) {
                Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
                iView!!.logoutuser()
            }

        } catch (e: Exception) {
            Utility.errornexttoken()
            SecurityLayer.Log("encryptionJSONException", e.toString())
            if (context != null) {
                iView!!.logoutuser()

            }
        }

    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }

}







