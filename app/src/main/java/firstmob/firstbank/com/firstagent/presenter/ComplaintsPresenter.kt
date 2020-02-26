package firstmob.firstbank.com.firstagent.presenter

import android.util.Log
import firstmob.firstbank.com.firstagent.Activity.ApplicationClass
import firstmob.firstbank.com.firstagent.adapter.ComplaintsAdapter
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.contract.ComplaintsContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.ChargebackList
import firstmob.firstbank.com.firstagent.model.CommisionsJSON
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import firstmob.firstbank.com.firstagent.utils.Utility.isNotNull
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class ComplaintsPresenter(internal var iLoginView: ComplaintsContract.ILoginView?, private val getDataIntractor: MainContract.GetDataIntractor) : ComplaintsContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {

    @Inject
    internal lateinit var ul: Utility
    private var reqtype = ""

    var chglist = ArrayList<ChargebackList>()
    // private TextView emptyView;
    var aAdpt: ComplaintsAdapter? = null


    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }


    override fun Complaints() {


        iLoginView!!.showProgress()

        val endpoint = "chargeback/getall.action"



            if (checkInternetConnection()) {




                val paramObject = JSONObject()

                paramObject.put("channel", "1")
                paramObject.put("userId", "CEVA")
                paramObject.put("merchantId", "1119040102")
                paramObject.put("status", "0")



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

            val comdatas = obj.optJSONArray("data")



            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (isNotNull(respcode) && isNotNull(responsemessage)) {

                SecurityLayer.Log("Response Message", responsemessage)

                if (respcode == "00") {
                    SecurityLayer.Log("JSON Aray", comdatas.toString())
                    if (comdatas.length() > 0) {
                        var json_data: JSONObject? = null
                        for (i in 0 until comdatas.length()) {
                            json_data = comdatas.getJSONObject(i)



                            val txnCode = json_data.optString("code")
                            Log.v("txncode",txnCode)
                            val refNum = json_data.optString("refNum")
                            val txndateTime = json_data.optString("txnDate")
                            val amount = json_data.optString("amount")
                            val status = json_data.optString("status")
                            var id = json_data.optString("id")
                            val catdType = json_data.optString("catdType")
                            var pan = json_data.optString("pan")


                                chglist.add(ChargebackList(amount, txnCode, refNum, catdType, id, pan,txndateTime,status))
                            }
                        }
                        if (this != null) { //   planetsList.add(new GetCommPerfData("1334", "13 Sep 2012 9:12", 45.00, "N", "450.00", "3123442", "242244432","1239032"));

                            iLoginView!!.setList(chglist)

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