package firstmob.firstbank.com.firstagent.presenter

import android.content.Context
import android.widget.Toast
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.PinChangesContract
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class ForceChangePinPresenter : PinChangesContract.PresenterPinChange, MainContract.GetDataIntractor.OnFinishedListener {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }

    internal var iView: PinChangesContract.IViewPinChange? = null
    private var getDataIntractor:
            MainContract.GetDataIntractor? = null
    internal var session : SessionManagement?=null
    internal var context: Context? = null
    constructor(context: Context, iView: PinChangesContract.IViewPinChange, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session=SessionManagement(context)
    }
    override fun ServerLogRetroCall(flag: String?,extraparam: String?) {
        session!!.setTranstype(flag)
        iView!!.showProgress()
        val endpoint = "login/login.action/"
        val params = extraparam
        var urlparams = ""
        try {
            urlparams = SecurityLayer.generalLogin(params, endpoint)
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams)
            SecurityLayer.Log("refurl", urlparams)
            SecurityLayer.Log("params", params)
        } catch (e: Exception) {
            SecurityLayer.Log("encryptionerror", e.toString())
        }
        getDataIntractor!!.getResults(this, urlparams)
    }

    override fun ServerRetroDevRegCall(flag: String?,extraparam: String?) {
        session!!.setTranstype(flag)
        iView!!.showProgress()
        val endpoint = "login/changepin.action"
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
            if(session!!.getTransFlag().equals("LogRetro")){
                val datas = obj.optJSONObject("data")

                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                    SecurityLayer.Log("Response Message", responsemessage)

                    if (respcode == "00") {
                        if (datas != null) {
                            session!!.setString(SessionManagement.CHKLOGIN, "Y")

                            val agentid = datas.optString("agent")
                            val userid = datas.optString("userId")
                            val username = datas.optString("userName")
                            val email = datas.optString("email")
                            val lastl = datas.optString("lastLoggedIn")
                            val mobno = datas.optString("mobileNo")
                            val accno = datas.optString("acountNumber")
                            val pubkey = datas.optString("publicKey")

                            session!!.SetAgentID(agentid)
                            session!!.SetUserID(userid)
                            session!!.putCustName(username)
                            session!!.putEmail(email)
                            session!!.putLastl(lastl)
                            session!!.setString(AGMOB, mobno)
                            session!!.putAccountno(accno)

                            session!!.setString("Base64image", "N")
                            session!!.setString(SessionManagement.KEY_SETBANKS, "N")
                            session!!.setString(SessionManagement.KEY_SETBILLERS, "N")
                            session!!.setString(SessionManagement.KEY_SETWALLETS, "N")
                            session!!.setString(SessionManagement.KEY_SETAIRTIME, "N")

//                            val encryptednewpin = Utility.getencryptedpin(npin, pubkey)
//                            Log.v("Public Key", pubkey)
//                            val checknewast = session.checkAst()
//
//                            val finalparams = chgpinparams + encryptednewpin
//                            RetroDevReg(finalparams)

                            iView!!.invokeRetroDevRegCall(pubkey)
                            // RetroDevReg(finalparams)
                        }
                    } else {
                        iView!!.onProcessingMessage(responsemessage)
                    }

                } else {
                    iView!!.onProcessingMessage("There was an error on your request")
                }
            }else {

                if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                    SecurityLayer.Log("Response Message", responsemessage)

                    if (respcode == "00") {
                        session!!.setString(SessionManagement.SESS_REG, "Y")
                        iView!!.onProcessingMessage("Pin successfully changed.Please login")
                        iView!!.startSiginActivity()
                    } else {
                        iView!!.onProcessingMessage(responsemessage)
                    }

                } else {
                    iView!!.onProcessingMessage("There was an error on your request")

                }
            }


        }catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            iView!!.ForceLogout()
            //(context as Withdraw).SetForceOutDialog(context!!.getString(R.string.forceout), context!!.getString(R.string.forceouterr), context)
            // SecurityLayer.Log(e.toString());

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            iView!!.ForceLogout()
            // (context as Withdraw).SetForceOutDialog(context!!.getString(R.string.forceout), context!!.getString(R.string.forceouterr), context)
            // SecurityLayer.Log(e.toString());
        }

    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
}
