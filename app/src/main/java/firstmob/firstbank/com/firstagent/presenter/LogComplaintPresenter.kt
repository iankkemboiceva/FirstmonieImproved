package firstmob.firstbank.com.firstagent.presenter

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.Activity.FMobActivity
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.LogComplContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import firstmob.firstbank.com.firstagent.utils.Utility.isNotNull
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class LogComplaintPresenter(internal var iLoginView: LogComplContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : LogComplContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility
    private var reqtype = ""


    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }


    override fun LogComp(acno: String,amount: String,datetime: String,desc: String) {


        iLoginView!!.showProgress()

        val endpoint = "complaint/log.action"

        if (isNotNull(desc)) {

            if (checkInternetConnection()) {


                //   final   String agid = agentid.getText().toString();
                val userid = Prefs.getString(KEY_USERID, "NA")
                val agentid = Prefs.getString(AGENTID, "NA")

                val mobnoo = Prefs.getString(AGMOB, "NA")


                val params = Constants.CH_ID + "/" + userid + "/" + agentid + "/"  + acno +"/N/N/"+desc

                val urlparams = ul?.genURLCBC(params, endpoint)

                getDataIntractor.getResults(this, urlparams)

            }

        } else {
            iLoginView!!.showToast("Please enter a valid value for description")
        }

    }

    override fun onFinished(responsebody: String) {

        try {
            // JSON Object
            SecurityLayer.Log("response..:", responsebody)


            var obj = JSONObject(responsebody)

            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")

            val plan = obj.optJSONObject("data")


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                    SecurityLayer.Log("Response Message", responsemessage);



                    iLoginView!!.showToast("Complaint has been successfully logged ")


                   iLoginView!!.onlogcompresult()

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