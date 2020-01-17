package firstmob.firstbank.com.firstagent.presenter

import android.os.Handler
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import org.json.JSONException
import org.json.JSONObject

import javax.inject.Inject

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.MainContract

import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.*

class ActivateAgentPresenter(internal var iLoginView: ActivateAgentContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : ActivateAgentContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility
    private var reqtype = ""


    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }


    override fun DevReg(agpin: String, otp: String) {




        iLoginView!!.showProgress()

        val endpoint = "reg/devReg.action/"
        val ip = getIP()
        val mac = getMacAddress()
        val serial = getSerial()
        val version = getDevVersion()
        val devtype = getDevModel()
        val imei = getDevImei()
        if (isNotNull(agpin)) {
            if (isNotNull(otp)) {
        if (checkInternetConnection()) {
            if (isNotNull(imei) && isNotNull(serial)) {


                //   final   String agid = agentid.getText().toString();
                val agid = Prefs.getString(KEY_USERID,"NA")

                val regId = "sss"
                val encrypted = b64_sha256(agpin)
                SecurityLayer.Log("Encrypted Pin", encrypted)
                val params = Constants.CH_ID + "/" + agid + "/" + otp + "/" + encrypted + "/secans1/" + "secans2" + "/secans3/" + mac + "/" + ip + "/" + imei + "/" + serial + "/" + version + "/" + devtype + "/" + regId

                val urlparams = ul.firsttimelogin(params, endpoint)
                reqtype = "DEVREG"
                getDataIntractor.getResults(this, urlparams)

            } else {


                iLoginView!!.showToast("Please ensure this device has an IMEI number")

                iLoginView!!.hideProgress()

            }
        }
            } else {
                iLoginView!!.showToast("Please enter a valid value for OTP")
            }
        } else {
            iLoginView!!.showToast("Please enter a valid value for activation key")
        }

    }


    override fun onFinished(responsebody: String) {

        try {
            // JSON Object
            SecurityLayer.Log("response..:", responsebody)


            var obj = JSONObject(responsebody)
            /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
            obj = SecurityLayer.decryptFirstTimeLogin(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                 if(reqtype.equals("RESENDOTP")) {


                     iLoginView!!.showToast("OTP has been successfully resent")
                 }else{
                     iLoginView?.onLoginResult()
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




    override fun ResendOTP(agid: String) {
        //   ul.checkpermissions();

        iLoginView?.showProgress()

        val endpoint = "otp/generateotp.action/"
        val params = Constants.CH_ID+"/"+agid
        var urlparams = ""
        try {
            urlparams = ul.firsttimelogin(params, endpoint)
            //SecurityLayerStanbic.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            SecurityLayer.Log("encryptionerror", e.toString())
        }

        reqtype = "RESENDOTP"
        getDataIntractor.getResults(this, urlparams)
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