package firstmob.firstbank.com.firstagent.presenter

import android.os.Handler
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import org.json.JSONException
import org.json.JSONObject

import javax.inject.Inject

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract

import firstmob.firstbank.com.firstagent.contract.ConfirmCashDepoContract
import firstmob.firstbank.com.firstagent.contract.MainContract

import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.*
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA



class ConfirmCashDepoPresenter(internal var iLoginView: ConfirmCashDepoContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : ConfirmCashDepoContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {


    @Inject
    internal lateinit var ul: Utility
    private var reqtype = ""


    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    override fun getFeeSec(amou: String?) {
        iLoginView!!.showProgress()

        val endpoint = "fee/getfee.action"

        if (isNotNull(amou)) {

            if (checkInternetConnection()) {


                //   final   String agid = agentid.getText().toString();
                val userid = Prefs.getString(KEY_USERID, "NA")
                val agentid = Prefs.getString(AGENTID, "NA")




                val params = Constants.CH_ID + "/" + userid + "/" + agentid + "/CASHDEP/" + amou

                val urlparams = ul?.genURLCBC(params, endpoint)

                getDataIntractor.getResults(this, urlparams)

            }

        } else {
            iLoginView!!.showToast("Please enter a valid value for amount")
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
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())

            val respcode = obj.optString("responseCode")
            val responsemessage = obj.optString("message")

            val plan = obj.optJSONObject("data")


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                var respfee = obj.optString("fee")
                val agbalance = obj.optString("data")



                SecurityLayer.Log("Response Message", responsemessage)
                if (!(checkUserLocked(respcode))) {
                    if (responsebody != null) {
                        if (respcode == "00") {

                            SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                            if (respfee == null || respfee.equals("")) {

                                iLoginView!!.settextfee("N/A")
                            } else {

                                respfee = returnNumberFormat(respfee);
                                iLoginView!!.settextfee(KEY_NAIRA + respfee)

                            }

                        } else if (respcode == "93") {


                            iLoginView!!.showToast(responsemessage)





                        } else {
                            if (!(checkUserLocked(respcode))) {




                                iLoginView!!.hidebutton()
                                iLoginView!!.showToast(responsemessage)
                            } else {

                            }
                        }
                    } else {
                        iLoginView!!.settextfee("N/A")
                    }

                }else {

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