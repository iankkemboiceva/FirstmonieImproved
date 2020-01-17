package firstmob.firstbank.com.firstagent.presenter


import android.content.Context
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.GetBillersContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.model.GetServicesData
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class GetBillersPresenter : GetBillersContract.Presenterloadbillers, MainContract.GetDataIntractor.OnFinishedListener {

    internal var iView: GetBillersContract.IViewbillers? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var context: Context? = null
    internal var session: SessionManagement? = null
    internal var planetsList: MutableList<GetServicesData> = ArrayList()
    constructor(context: Context, iView: GetBillersContract.IViewbillers, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
        session= SessionManagement(context)
    }

    override fun loadbiller() {
        iView!!.showProgress()
        val endpoint = "billpayment/services.action"
        val usid = Prefs.getString(KEY_USERID, "NA")
        val agentid = Prefs.getString(AGENTID, "NA")
        val mobnoo = Prefs.getString(AGMOB, "NA")

        val params = "1/$usid/$agentid/$mobnoo"
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
    override fun loadcachedbiilersData() {
        planetsList.clear()
        try {
            val strservdata = session!!.getString(SessionManagement.KEY_BILLERS)
            val servdata = JSONArray(strservdata)
            if (servdata.length() > 0) {


                var json_data: JSONObject? = null
                for (i in 0 until servdata.length()) {
                    json_data = servdata.getJSONObject(i)
                    //String accid = json_data.getString("benacid");


                    val id = json_data!!.optString("id")

                    val label = json_data.optString("label")
                    val serviceName = json_data.optString("serviceName")

                    planetsList.add(GetServicesData(id, label, serviceName))


                }
                if (context != null) {
                    Collections.sort(planetsList) { d1, d2 -> d1.serviceName.compareTo(d2.serviceName) }
                    iView!!.onResult(planetsList)
                }


            } else {
                if (context != null) {
                    Toast.makeText(
                            context,
                            "No services available  ",
                            Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            if (context != null) {
                Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            }
            // SecurityLayer.Log(e.toString());

        }

    }

    override fun ondestroy() {
        iView = null
    }

    override fun onFinished(response: String?) {
        iView!!.hideProgress()
        try {
            // JSON Object


            SecurityLayer.Log("Cable TV Resp", response)
            SecurityLayer.Log("response..:", response)
            var obj = JSONObject(response)
            //obj = Utility.onresp(obj,context);
            obj = SecurityLayer.decryptTransaction(obj)
            SecurityLayer.Log("decrypted_response", obj.toString())


            val servdata = obj.optJSONArray("data")
            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (response != null) {
                val respcode = obj.optString("responseCode")
                val responsemessage = obj.optString("message")

                SecurityLayer.Log("Response Message", responsemessage)

                if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                    if (!Utility.checkUserLocked(respcode)) {
                        SecurityLayer.Log("Response Message", responsemessage)

                        if (respcode == "00") {
                            SecurityLayer.Log("JSON Aray", servdata.toString())

                            if (servdata.length() > 0) {
                                session!!.setString(SessionManagement.KEY_SETBILLERS, "Y")
                                session!!.setString(SessionManagement.KEY_BILLERS, servdata.toString())

                                var json_data: JSONObject? = null
                                for (i in 0 until servdata.length()) {
                                    json_data = servdata.getJSONObject(i)
                                    //String accid = json_data.getString("benacid");


                                    val id = json_data!!.optString("id")

                                    val label = json_data.optString("label")
                                    val serviceName = json_data.optString("serviceName")



                                    planetsList.add(GetServicesData(id, label, serviceName))


                                }
                                if (context != null) {
                                    Collections.sort(planetsList) { d1, d2 -> d1.serviceName.compareTo(d2.serviceName) }
                                   iView!!.onResult(planetsList)
                                }


                            } else {
                                if (context != null) {
                                    Toast.makeText(
                                            context,
                                            "No services available  ",
                                            Toast.LENGTH_LONG).show()
                                }
                            }

                        } else {
                            if (context != null) {
                                Toast.makeText(
                                        context,
                                        "" + responsemessage,
                                        Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {

                     iView!!.logoutuser()


                    }
                } else {
                    if (context != null) {
                        Toast.makeText(
                                context,
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show()

                    }
                }
            } else {
                if (context != null) {
                    Toast.makeText(
                            context,
                            "There was an error on your request",
                            Toast.LENGTH_LONG).show()
                }

            }
            // prgDialog2.dismiss();


        } catch (e: JSONException) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            // TODO Auto-generated catch block
            if (context != null) {
                Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
                // SecurityLayer.Log(e.toString());
               // SetForceOutDialog(context!!.getText(R.string.forceout), context!!.getText(R.string.forceouterr), context)
               iView!!.logoutuser()
            }

        } catch (e: Exception) {
            SecurityLayer.Log("encryptionJSONException", e.toString())
            if (context != null) {
                iView!!.logoutuser()
                //SetForceOutDialog(context!!.getText(R.string.forceout), context!!.getText(R.string.forceouterr), context)
            }
            // SecurityLayer.Log(e.toString());
        }

        try {
          iView!!.hideProgress()
        } catch (e: IllegalArgumentException) {
            // Handle or log or ignore
        } catch (e: Exception) {
            // Handle or log or ignore
        } finally {

        }

        }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
//
//    fun SetForceOutDialog(msg: String, title: String, c: Context?) {
//        if (c != null) {
//            MaterialDialog.Builder(c)
//                    .title(title)
//                    .content(msg)
//
//                    .negativeText("CONTINUE")
//                    .callback(object : MaterialDialog.ButtonCallback() {
//                        override fun onPositive(dialog: MaterialDialog?) {
//                            dialog!!.dismiss()
//                        }
//
//                        override fun onNegative(dialog: MaterialDialog?) {
//
//                            dialog!!.dismiss()
//                            c.finish()
//                            session!!.logoutUser()
//
//                            // After logout redirect user to Loing Activity
//                            val i = Intent(c, SignInActivity::class.java)
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            // Staring Login Activity
//                            c.startActivity(i)
//
//                        }
//                    })
//                    .show()
//        }
//    }
}





