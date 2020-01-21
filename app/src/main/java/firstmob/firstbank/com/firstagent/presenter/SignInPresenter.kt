package firstmob.firstbank.com.firstagent.presenter

import android.os.Handler
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import org.json.JSONException
import org.json.JSONObject

import javax.inject.Inject

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.SignInContract

import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.*

class SignInPresenter(internal var iLoginView: SignInContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : SignInContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {


    @Inject
    internal lateinit var ul: Utility
    private var reqtype = ""


    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }


    override fun Login(agpin: String) {




        iLoginView!!.showProgress()

        val endpoint = "login/login.action/"

        if (isNotNull(agpin)) {

        if (checkInternetConnection()) {



                //   final   String agid = agentid.getText().toString();
                val userid = Prefs.getString(KEY_USERID,"NA")


                val encrypted = b64_sha256(agpin)
                SecurityLayer.Log("Encrypted Pin", encrypted)
            val mobnoo = "12345"

                val params = Constants.CH_ID + "/" + userid + "/" + encrypted + "/" + mobnoo

                val urlparams = ul?.generalLogin(params, endpoint)

                getDataIntractor.getResults(this, urlparams)

        }

        } else {
            iLoginView!!.showToast("Please enter a valid value for PIN")
        }

    }


    override fun onFinished(responsebody: String) {

        try {
            // JSON Object
            SecurityLayer.Log("response..:", responsebody)


            var obj = JSONObject(responsebody)

            obj = SecurityLayer.decryptGeneralLogin(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")
            val datas = obj.optJSONObject("data")


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {

                    if (!(datas == null)) {

                        val status = datas.optString ("status")
                        if (status == "F") {

                        }else{

                            val agentid = datas.optString("agent")
                            val userid = datas.optString("userId")
                            val username = datas.optString("userName")
                            val email = datas.optString("email")
                            val lastl = datas.optString("lastLoggedIn")
                            val mobno = datas.optString("mobileNo")
                            val accno = datas.optString("acountNumber")
                            val cntopen = datas.optString("canOpenAccount")

                            Prefs.putString(KEY_SETCNTOPEN,cntopen)
                            Prefs.putString(AGENTID,agentid)
                            Prefs.putString(KEY_USERID,userid)
                            Prefs.putString(KEY_EMAIL,email)
                            Prefs.putString(AGMOB,mobno)
                            Prefs.putString(KEY_CUSTNAME,username)
                            Prefs.putString(KEY_ACCO,accno)
                            Prefs.putString(LASTL,lastl)
                            Prefs.putString(CHKLOGIN,"Y")
                            Prefs.putBoolean(ISLOGIN,true);

                            iLoginView?.onLoginResult()

                        }
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

    override fun setAppVersion() {
       val appversion = getAppVersion()
        iLoginView?.setTextApp("Version $appversion")
    }


}