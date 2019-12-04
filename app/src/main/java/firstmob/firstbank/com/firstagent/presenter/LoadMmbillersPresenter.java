package firstmob.firstbank.com.firstagent.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass;
import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGMOB;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

public class LoadMmbillersPresenter implements MainContract.Presenterloadbillers, MainContract.GetDataIntractor.OnFinishedListener {
    MainContract.IViewbillers ibillerView;
    private MainContract.GetDataIntractor getDataIntractor;
    List<GetAirtimeBillersData> planetsList = new ArrayList<GetAirtimeBillersData>();
    Context context;

    public LoadMmbillersPresenter(Context context, MainContract.IViewbillers ibillerView, MainContract.GetDataIntractor getDataIntractor) {
        this.ibillerView = ibillerView;
        this.getDataIntractor = getDataIntractor;
        this.context = context;
    }

    @Override
    public void loadbiller() {
        ibillerView.showProgress();
        String endpoint = "billpayment/MMObillers.action";


        String userid = Prefs.getString(KEY_USERID, "NA");
        String agentid = Prefs.getString(AGENTID, "NA");

        String mobnoo = Prefs.getString(AGMOB, "NA");


        String params = "1/" + userid + "/" + agentid + "/" + mobnoo + "/1";
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint);
            //Log.d("cbcurl",url);
            Log.v("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror", e.toString());
        }
        getDataIntractor.getResults(this, urlparams);
    }

    @Override
    public void ondestroy() {
        ibillerView = null;
    }

    @Override
    public void onFinished(String response) {
        try {
            // JSON Object

            Log.v("response..:", response);
            JSONObject obj = new JSONObject(response);
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj);
            SecurityLayer.Log("decrypted_response", obj.toString());

            String respcode = obj.optString("responseCode");
            String responsemessage = obj.optString("message");


            JSONArray plan = obj.optJSONArray("data");
            //session.setString(SecurityLayer.KEY_APP_ID,appid);


            if (!(response== null)) {
                if (respcode.equals("00")) {
                    Log.v("Response Message", responsemessage);
                    if (plan.length() > 0) {


                        JSONObject json_data = null;
                        for (int i = 0; i < plan.length(); i++) {
                            json_data = plan.getJSONObject(i);
                            //String accid = json_data.getString("benacid");
                            String sid = json_data.optString("sid");
                            String id = json_data.optString("id");
                            String billerName = json_data.optString("billerName");
                            planetsList.add(new GetAirtimeBillersData(sid, id, billerName));
                        }
                        if (!(planetsList == null)) {
                            if (planetsList.size() > 0) {
                                ibillerView.onLoginResult(planetsList);
                            } else {
                             Utility.showToast("No airtime billers available");
                            }
                        }
                        ibillerView.hideProgress();
                    }

                } else {
                    Toast.makeText(
                            context,
                            responsemessage,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Utility.showToast("There was an error processing your request ");
            }


        } catch (JSONException e) {
            SecurityLayer.Log("encryptionJSONException", e.toString());
            Utility.errornexttoken();
            // TODO Auto-generated catch block
            Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
            // SecurityLayer.Log(e.toString());

        } catch (Exception e) {
            Utility.errornexttoken();
            SecurityLayer.Log("encryptionJSONException", e.toString());
            // SecurityLayer.Log(e.toString());
        }
        ibillerView.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        ibillerView.hideProgress();
        SecurityLayer.Log("encryptionJSONException", t.toString());
        Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
    }
}
