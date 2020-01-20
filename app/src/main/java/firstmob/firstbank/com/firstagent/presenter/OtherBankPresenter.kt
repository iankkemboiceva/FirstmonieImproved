package firstmob.firstbank.com.firstagent.presenter

import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.adapter.GetBanksData
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.contract.OtherBankContract
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.security.SecurityLayer.genURLCBC
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import firstmob.firstbank.com.firstagent.utils.Utility.isNotNull
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class OtherBankPresenter(internal var iLoginView: OtherBankContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : OtherBankContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {


    private var reqtype = ""

    var bnklist = ArrayList<GetBanksData>()
    // private TextView emptyView;
    var aAdpt: InboxListAdapter? = null




    override fun BanksList() {


        iLoginView!!.showProgress()

        val endpoint = "transfer/getbanks.action"



            if (checkInternetConnection()) {


                //   final   String agid = agentid.getText().toString();
                val userid = Prefs.getString(KEY_USERID, "NA")
                val agentid = Prefs.getString(AGENTID, "NA")



                val params = Constants.CH_ID + "/" + userid + "/" + agentid + "/3939393"

                val urlparams = genURLCBC(params, endpoint)

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

            val servdata = obj.optJSONArray("data")



            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                    SecurityLayer.Log("JSON Aray", servdata.toString())
                    if (servdata.length() > 0) {
                        var json_data: JSONObject? = null
                        for (inti in 0 until servdata.length()) {
                            Prefs.putString(KEY_SETBANKS, "Y");
                            Prefs.putString(KEY_BANKS, servdata.toString());
                            json_data = servdata.optJSONObject(inti)


                            val instName = json_data.optString("instName")

                            val bankCode = json_data.optString("bankCode")
                                bnklist.add(GetBanksData(instName, bankCode))

                        }
                        if (this != null) { //   planetsList.add(new GetCommPerfData("1334", "13 Sep 2012 9:12", 45.00, "N", "450.00", "3123442", "242244432","1239032"));

                            iLoginView!!.setList(bnklist)

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