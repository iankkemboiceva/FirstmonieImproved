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
import firstmob.firstbank.com.firstagent.contract.TransactionProcessingContract

import firstmob.firstbank.com.firstagent.Activity.TransactionProcessingActivity
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent




class TransactionProcPresenter(internal var iLoginView: TransactionProcessingContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : TransactionProcessingContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility
    private var regtype = ""



    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }

    override fun CallActivity(params: String?,endpoint: String?,serv: String) {
        iLoginView!!.showProgress()

        regtype = serv





        if (checkInternetConnection()) {


            val urlparams = ul?.genURLCBC(params, endpoint)

            getDataIntractor.getResults(this, urlparams)

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



                SecurityLayer.Log("Response Message", responsemessage)
                if (!(checkUserLocked(respcode))) {
                    if (responsebody != null) {
                        val respcode = obj.optString("responseCode")
                        val responsemessage = obj.optString("message")
                        val agcmsn = obj.optString("fee")
                        val refcodee = obj.optString("commission")
                        SecurityLayer.Log("Response Message", responsemessage)


                        val datas = obj.optJSONObject("data")

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!Utility.checkUserLocked(respcode)) {
                                if (respcode == "00") {

                                    if(regtype == "CASHDEPO") {
                                        var totfee = "0.00"
                                        var datetimee = ""
                                        if (datas != null) {
                                            totfee = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                        }


                                        iLoginView!!.CashDepoResult(refcodee, datetimee, agcmsn, totfee)

                                    }

                                    else if(regtype == "CASHTRAN") {
                                        var totfee = "0.00"
                                        var datetimee = ""
                                        if (datas != null) {
                                            totfee = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                        }


                                        iLoginView!!.CashDepoTranResult(refcodee, datetimee, agcmsn, totfee)


                                    }else if(regtype=="AIRT"){

                                        if (datas != null) {

                                            var totfee: String? = "0.00"
                                            var datetimee = ""
                                            val ttf = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                            if (ttf == null || ttf == "") {

                                            } else {
                                                totfee = ttf
                                            }

                                            val tref = datas.optString("refNumber")

                                            iLoginView!!.CashAirtimeResult(refcodee, datetimee, agcmsn, totfee,tref)
                                        }


                                    }else if(regtype=="WDRAW"){
                                        var totfee = "0.00"
                                        var datetimee = ""
                                        if (datas != null) {
                                            totfee = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                        }
                                        iLoginView!!.CashWithdrawalResult(refcodee, datetimee, agcmsn, totfee)
                                    }
                                    else if(regtype=="WDRAW"){

                                        var totfee: String? = "0.00"
                                        var tref = "N/A"
                                        if (datas != null) {

                                            var datetimee = ""
                                            val ttf = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                            if (ttf == null || ttf == "") {

                                            } else {
                                                totfee = ttf
                                            }
                                            tref = datas.optString("refNumber")
                                            iLoginView!!.CashCabletvResult(refcodee, datetimee, agcmsn, totfee,tref)
                                        }


                                    }else if(regtype=="SENDOTB"){

                                        var totfee: String? = "0.00"
                                        var tref = "N/A"
                                        if (datas != null) {

                                            var datetimee = ""
                                            val ttf = datas.optString("fee")
                                            datetimee = datas.optString("dateTime")
                                            if (ttf == null || ttf == "") {

                                            } else {
                                                totfee = ttf
                                            }
                                            tref = datas.optString("refNumber")
                                            iLoginView!!.SendOTBResult(refcodee, datetimee, agcmsn, totfee)
                                        }


                                    }
                                } else if (respcode == "002") {
                                    iLoginView!!.onErrorResult(responsemessage)



                                } else {
                                    iLoginView!!.onErrorResult(responsemessage)
                                }
                            } else {
                                iLoginView!!.onErrorResult(responsemessage)

                            }
                        } else {


                            iLoginView!!.showToast("There was an error on your request")
                            iLoginView!!.setstatus("TRANSACTION FAILURE")
                            iLoginView!!.setdesc("There was an error on your request")

                        }
                    } else {
                        iLoginView!!.showToast("There was an error on your request")
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