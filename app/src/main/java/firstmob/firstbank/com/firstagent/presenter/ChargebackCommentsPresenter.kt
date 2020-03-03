package firstmob.firstbank.com.firstagent.presenter

import android.util.Log
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.ChargebackCommentsContract
import firstmob.firstbank.com.firstagent.contract.ChargebackContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.ChargebackList
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import firstmob.firstbank.com.firstagent.utils.Utility.isNotNull
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class ChargebackCommentsPresenter(internal var iLoginView: ChargebackCommentsContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : ChargebackCommentsContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility




    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }



    override  fun saveChargeback(pin: String?, comments: String?, iscashgiven: String?, txrefnum: String?, chgbckid: Int, receipt: String?){


        iLoginView!!.showProgress()

        val endpoint = "chargeback/save.action"



            if (checkInternetConnection()) {




                val paramObject = JSONObject()



                val usidd = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA")
                val agidd = Prefs.getString(SharedPrefConstants.AGENTID,"NA")

                paramObject.put("channel", Constants.CH_ID)
                paramObject.put("userId", usidd)
                paramObject.put("merchantId", agidd)
                paramObject.put("chargeBackId", chgbckid)

                paramObject.put("pin", pin)
                paramObject.put("isCashGiven", iscashgiven)

                paramObject.put("comments", comments)
                paramObject.put("txnRefNum", txrefnum)
                paramObject.put("receipt", receipt)

                getDataIntractor.getJsonBodyResults(this, paramObject.toString(),endpoint)

            }



    }

    override fun onFinished(responsebody: String) {

        try {
            // JSON Object
            SecurityLayer.Log("response..:", responsebody)


            var obj = JSONObject(responsebody)

            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")

            val comdatas = obj.optJSONObject("data")



            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                    SecurityLayer.Log("JSON Aray", comdatas.toString())
                    if (comdatas.length() > 0) {

                        }






                } else {

                    iLoginView!!.showToast(responsemessage)


                }

            } else {


                iLoginView!!.showToast("There was an error on your request")


            }

        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())

            iLoginView!!.showToast("There was an error on your request")
            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            iLoginView!!.showToast("There was an error on your request")
            // SecurityLayer.Log(e.toString());
        }


        //   iLoginView.onLoginResult(response);
        iLoginView!!.hideProgress()
    }


    override fun onFailure(t: Throwable) {
        iLoginView!!.hideProgress()
        iLoginView!!.showToast("There was an error on your request")
        //    iLoginView!!.onLoginError(t.toString())

    }


    override fun ondestroy() {
        iLoginView = null
    }


}