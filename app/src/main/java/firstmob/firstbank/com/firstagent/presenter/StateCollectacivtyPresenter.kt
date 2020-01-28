package firstmob.firstbank.com.firstagent.presenter



import android.content.Context
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.BenList
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


class StateCollectacivtyPresenter : GetBillersContract.PresenterLoadMarket, MainContract.GetDataIntractor.OnFinishedListener {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var iView: GetBillersContract.IViewbillStateCollect? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var marketslist: MutableList<BenList>? = ArrayList<BenList>()
    internal var context: Context? = null
    internal var serviceid: String? =null
    internal var session: SessionManagement? = null
    internal var planetsList: MutableList<GetBillersData> = ArrayList()
    constructor(context: Context, iView: GetBillersContract.IViewbillStateCollect, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }

    override fun loadMarketPlaces(extrparams: String?) {
        iView!!.showProgress()
        val endpoint = "billpayment/lpam.action"
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobnoo = Prefs.getString(AGMOB, "NA")
        val params =  "1/"+usid+"/"+agentid+"/"+mobnoo+"/"+extrparams
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
            SecurityLayer.Log("Cable TV Resp", response)
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,getApplicationContext());
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())
            val newobj = obj.getJSONObject("data")
            val servdata = newobj.optJSONArray("makets")
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


                                    val marketName = json_data!!.optString("marketName")

                                    val id = json_data.optString("id")

                                    marketslist!!.add(BenList(id, marketName))


                                }
                                iView!!.onResult(marketslist)


                            } else {

                            }

                        } else {
                            iView!!.onProcessingError(responsemessage)
                        }
                    } else {
                        iView!!.logoutuser()
                       // LogOut()

                    }
                } else {
                    iView!!.onProcessingError("There was an error on your request")



                }
            } else {

                        iView!!.onProcessingError("There was an error on your request")



            }
            // prgDialog2.dismiss();


        } catch (e: JSONException) {
            Utility.errornexttoken()
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            if (context!= null) {
                iView!!.onProcessingError(context!!.getText(R.string.conn_error).toString())
                iView!!.logoutuser()
                // SecurityLayer.Log(e.toString());
            }

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            Utility.errornexttoken()
            if (context != null) {
                iView!!.logoutuser()
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







