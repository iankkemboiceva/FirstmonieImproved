package firstmob.firstbank.com.firstagent.presenter

import android.content.Context
import android.text.Html
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.CommisionContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.MinistatData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

class MinstatementPresenter : CommisionContract.PresenterMinista, MainContract.GetDataIntractor.OnFinishedListener {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var iView: CommisionContract.IViewMinistatement? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var planetsList: MutableList<MinistatData> = ArrayList<MinistatData>()
    internal var context: Context? = null

    constructor(context: Context, iView: CommisionContract.IViewMinistatement, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context

    }

    override fun requestCallMinistat(flag: String?, extraparam: String?) {
        iView!!.showProgress()
        Prefs.putString(SharedPrefConstants.MINIST_TRANS_FLAG,flag)
        var endpoint: String
        val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
        val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
        val mobnoo = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
        var urlparams = ""
        endpoint = "core/stmt.action"
            val params = "1/$usid/$agentid/$mobnoo/$extraparam"
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
    override fun requestCallGetBalnce(flag : String?,extraparam: String?) {
        iView!!.showProgress()
        Prefs.putString(SharedPrefConstants.MINIST_TRANS_FLAG,flag)
        val endpoint = "core/balenquirey.action"
        val usid = Prefs.getString(SharedPrefConstants.KEY_USERID, "NA")
        val agentid = Prefs.getString(SharedPrefConstants.AGENTID, "NA")
        val mobileno = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
        val params = "1/$usid/$agentid/$mobileno"
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
            if(Prefs.getString(SharedPrefConstants.MINIST_TRANS_FLAG,"NA") == "mistate"){


                val comperf = obj.optJSONArray("data")
                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (response!= null) {

                    SecurityLayer.Log("Response Message", responsemessage)

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!Utility.checkUserLocked(respcode)) {
                            SecurityLayer.Log("Response Message", responsemessage)

                            if (respcode == "00") {
                                SecurityLayer.Log("JSON Aray", comperf.toString())
                                if (comperf.length() > 0) {
                                    planetsList.clear()

                                    var json_data: JSONObject? = null
                                    for (i in 0 until comperf.length()) {
                                        json_data = comperf.getJSONObject(i)
                                        //String accid = json_data.getString("benacid");


                                        val tran_date = json_data!!.optString("date")
                                        val tran_remark = json_data.optString("narration")
                                        val credit_debit = json_data.optString("transType")
                                        val tran_amt = json_data.optString("amount")
                                        val time = json_data.optString("time")
                                        planetsList.add(MinistatData("$tran_date $time", tran_remark, credit_debit, tran_amt))


                                    }
                                    iView!!.PopulateRecyclerView(planetsList)
                                } else {
                                    iView!!.onProcessingError("There are no records to display")
                                    if (context != null) {
                                        iView!!.onProcessingError("There are no records to display")
                                    }
                                }

                            } else {
                                if (context != null) {
                                    iView!!.onProcessingError(responsemessage)
                                }
                            }
                        } else {
                            iView!!.NavigateToSognIn()
                            iView!!.onProcessingError("You have been locked out of the app.Please call customer care for further details")

                        }
                    } else {
                        if (context != null) {
                            iView!!.onProcessingError("There was an error on your request")
                        }

                    }
                } else {
                    if (context != null) {
                        iView!!.onProcessingError("There was an error on your request")
                    }

                }
            }else {

                val plan = obj.optJSONObject("data")
                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (context != null) {
                    if (response != null) {
                        if (respcode == "00") {

                            SecurityLayer.Log("Response Message", responsemessage)

                            //                                    SecurityLayer.Log("Respnse getResults",datas.toString());

                            if (plan != null) {
                                val balamo = plan.optString("balance")
                                val comamo = plan.optString("commision")


                                var cmbal = Utility.returnNumberFormat(comamo)

                                cmbal = Utility.roundto2dp(cmbal)
                                val bll = Utility.returnNumberFormat(balamo)
                                val fbal = Utility.returnNumberFormat(balamo)
                                iView!!.setBalance("Account Balance: " + Html.fromHtml("&#8358") + " " + fbal)
                                iView!!.setMinistatementStartim()

                                //txaccbal.setText()
                            } else {
                                if (context != null) {
                                    iView!!.onProcessingError("There was an error retrieving your balance")
                                }
                            }
                        } else {
                            if (context != null) {
                                iView!!.onProcessingError("There was an error retrieving your balance")
                            }
                        }
                    } else {
                        if (context != null) {
                            iView!!.onProcessingError("There was an error retrieving your balance")
                        }
                    }
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
     //   if(Prefs.getString(SharedPrefConstants.MINIST_TRANS_FLAG,"NA").equals("getBalnce")){
     //       iView!!.setMinistatementStartim()
       // }
    }

    override fun onFailure(t: Throwable?) {
      if(iView!=null){
          iView!!.hideProgress()
      }
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
}





