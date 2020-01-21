package firstmob.firstbank.com.firstagent.presenter


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.MyPerfActivityContract
import firstmob.firstbank.com.firstagent.contract.PinChangesContract
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject
import java.lang.NumberFormatException
import java.util.ArrayList


class MyperfActivityPresenter : MyPerfActivityContract.PresenterPerfAct, MainContract.GetDataIntractor.OnFinishedListener {


    internal var iView: MyPerfActivityContract.IViewPerfAct? = null
    private var getDataIntractor:
            MainContract.GetDataIntractor? = null
    internal var session: SessionManagement? = null
    internal var context: Context? = null
    constructor(context: Context, iView: MyPerfActivityContract.IViewPerfAct, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }
    override fun ServerPullDataCall(extraparam: String?) {
        iView!!.showProgress()
        val endpoint = "report/genrpt.action";
        SecurityLayer.Log("From Date/To date", extraparam)
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobnoo = Prefs.getString(AGMOB, "NA")
        val params = "1/$usid/$agentid/$mobnoo/CMSNRPT/$extraparam"
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
                //obj = Utility.onresp(obj,getActivity());
                obj = SecurityLayer.decryptTransaction(obj)
                SecurityLayer.Log("decrypted_response", obj.toString())


                val comdatas = obj.optJSONObject("data")
                val comperf = comdatas.optJSONArray("transaction")
                val summdata = comdatas.optJSONArray("summary")
                val chktxncnt = false
                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (response != null) {
                    val respcode = obj.optString("responseCode")
                    val responsemessage = obj.optString("message")

                    SecurityLayer.Log("Response Message", responsemessage)
                    val labelsstr = ArrayList<String>()

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!Utility.checkUserLocked(respcode)) {

                            if (respcode == "00") {
                                iView!!.setUpActivity(summdata, comperf)
                            } else {
                                iView!!.onProcessingMessage(responsemessage)
                            }
                        } else {
                            iView!!.startSiginActivity()
                            iView!!.onProcessingMessage("You have been locked out of the app.Please call customer care for further details")
                        }
                    } else {
                        iView!!.onProcessingMessage("There was an error on your request")


                    }
                } else {
                    iView!!.onProcessingMessage("There was an error on your request")


                }

        } catch (e:NumberFormatException) {
        SecurityLayer.Log("numberformatexception", e.toString());
        //  ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr),getActivity());
        // SecurityLayer.Log(e.toString());
    } catch (e: JSONException) {
        SecurityLayer.Log("encryptionJSONException", e.toString());
        // TODO Auto-generated catch block

        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                iView!!.ForceLogout()

        // SecurityLayer.Log(e.toString());

    } catch (e: java.lang.Exception) {
        SecurityLayer.Log("generalexception", e.toString());
      iView!!.ForceLogout()
        // SecurityLayer.Log(e.toString());
    }
    }
    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
}





