package firstmob.firstbank.com.firstagent.presenter

import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.InboxContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import firstmob.firstbank.com.firstagent.utils.Utility.isNotNull
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class InboxPresenter(internal var iLoginView: InboxContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : InboxContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    private var reqtype = ""

    var inboxlist = ArrayList<GetCommPerfData>()
    // private TextView emptyView;
    var aAdpt: InboxListAdapter? = null



    override fun Inbox(stdate: String,enddate: String) {


        iLoginView!!.showProgress()

        val endpoint = "report/genrpt.action"



            if (checkInternetConnection()) {


                //   final   String agid = agentid.getText().toString();
                val userid = Prefs.getString(KEY_USERID, "NA")
                val agentid = Prefs.getString(AGENTID, "NA")

                val mobnoo = Prefs.getString(AGMOB, "NA")




                val params = Constants.CH_ID + "/" + userid + "/" + agentid + "/" + mobnoo + "/TXNRPT/" + stdate+"/"+enddate

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

            val comdatas = obj.optJSONObject("data")
            val comperf = comdatas.optJSONArray("transaction")


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                    SecurityLayer.Log("JSON Aray", comperf.toString())
                    if (comperf.length() > 0) {
                        var json_data: JSONObject? = null
                        for (i in 0 until comperf.length()) {
                            json_data = comperf.getJSONObject(i)
                            //String accid = json_data.getString("benacid");
                            var fintoacnum: String? = ""
                            var finfromacnum: String? = ""
                            val txnCode = json_data.optString("txnCode")
                            val agentCmsn = json_data.optDouble("agentCmsn")
                            val txndateTime = json_data.optString("txndateTime")
                            val amount = json_data.optString("amount")
                            val status = json_data.optString("status")
                            var toAcNum = json_data.optString("toAcNum")
                            val refNumber = json_data.optString("refNumber")
                            var fromaccnum = json_data.optString("fromAccountNum")

                            var dbam = 0.0
                            if (amount != null && amount != "null") {

                                dbam = amount.toDouble()
                            } else {
                                dbam = 0.0

                            }
                            if (txnCode == "CASHDEP" || txnCode == "FTINTRABANK" || txnCode == "CWDBYACT" || txnCode == "BILLPAYMENT" || txnCode == "MMO" || txnCode == "FTINTERBANK") {
                                fintoacnum = fromaccnum
                                finfromacnum = toAcNum
                                toAcNum = fintoacnum
                                fromaccnum = finfromacnum
                            }
                            if (dbam > 0) {
                                inboxlist.add(GetCommPerfData(txnCode, txndateTime, agentCmsn, status, amount, toAcNum, refNumber, fromaccnum))
                            }
                        }
                        if (this != null) { //   planetsList.add(new GetCommPerfData("1334", "13 Sep 2012 9:12", 45.00, "N", "450.00", "3123442", "242244432","1239032"));

                            iLoginView!!.setList(inboxlist)

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


}