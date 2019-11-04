package firstmob.firstbank.com.firstagent.presenter;

import android.content.Intent;
import android.os.Handler;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.model.IUser;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

public class LoginPresenterCompl implements MainContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {
    MainContract.ILoginView iLoginView;
    IUser user;
    Handler handler;
    private MainContract.GetDataIntractor getDataIntractor;
    String userid;

    @Inject
    SecurityLayer sl;

    @Inject
    Utility ul;

    public LoginPresenterCompl(MainContract.ILoginView iLoginView, MainContract.GetDataIntractor getDataIntractor) {
        this.iLoginView = iLoginView;
        this.getDataIntractor = getDataIntractor;

        ApplicationClass.getMyComponent().inject(this);
        // initUser();
    }


    @Override
    public void doLogin(String name) {

        //   ul.checkpermissions();

        iLoginView.showProgress();
        this.userid = name;

        String endpoint = "otp/generateotp.action/";
        String params = "1/" + name;
        String urlparams = "";
        try {
            this.userid = name;
            urlparams = SecurityLayer.firstLogin(params, endpoint);
            //SecurityLayerStanbic.Log("cbcurl",url);
            SecurityLayer.Log("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror", e.toString());
        }

        getDataIntractor.getResults(this, urlparams);

    }


    @Override
    public void onFinished(String responsebody) {

        try {
            // JSON Object
            SecurityLayer.Log("response..:", responsebody);


            JSONObject obj = new JSONObject(responsebody);
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
            obj = SecurityLayer.decryptFirstTimeLogin(obj);
            SecurityLayer.Log("decrypted_response", obj.toString());

            String respcode = obj.optString("responseCode");
            String responsemessage = obj.optString("message");


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                SecurityLayer.Log("Response Message", responsemessage);

                if (respcode.equals("00")) {

                    Prefs.putString(KEY_USERID, userid);
                    //  iLoginView.showToast(userid);

                    iLoginView.onLoginResult();


                } else {

                    iLoginView.showToast(responsemessage);


                }

            } else {


                iLoginView.showToast("There was an error on your request");


            }

        } catch (JSONException e) {
            SecurityLayer.Log("encryptionJSONException", e.toString());
            // TODO Auto-generated catch block
            iLoginView.showToast("There was an error on your request");
            // SecurityLayer.Log(e.toString());

        } catch (Exception e) {
            SecurityLayer.Log("encryptionJSONException", e.toString());
            iLoginView.showToast("There was an error on your request");
            // SecurityLayer.Log(e.toString());
        }


        //   iLoginView.onLoginResult(response);
        iLoginView.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        iLoginView.hideProgress();
        SecurityLayer.Log(t.toString());
        iLoginView.onLoginError("There was an error processing your request");

    }

    @Override
    public void ondestroy() {
        iLoginView = null;
    }


}