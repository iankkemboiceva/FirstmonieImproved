package firstmob.firstbank.com.firstagent.presenter;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import firstmob.firstbank.com.firstagent.Activity.ApplicationClass;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.contract.OpenAccCustContract;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

public class OpenAccCustPicPresenter implements OpenAccCustContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {
    OpenAccCustContract.ILoginView iLoginView;
    private MainContract.GetDataIntractor getDataIntractor;
    String mobno;

    @Inject
    SecurityLayer sl;

    @Inject
    Utility ul;

    public OpenAccCustPicPresenter(OpenAccCustContract.ILoginView iLoginView, MainContract.GetDataIntractor getDataIntractor) {
        this.iLoginView = iLoginView;
        this.getDataIntractor = getDataIntractor;

        ApplicationClass.getMyComponent().inject(this);
        // initUser();
    }


    @Override
    public void GenCustOTP(String mobno) {

        //   ul.checkpermissions();

        iLoginView.showProgress();
        this.mobno = mobno;

        String userid = Prefs.getString(KEY_USERID, "NA");
        String agentid = Prefs.getString(AGENTID, "NA");

        String endpoint = "otp/generatecustomerotp.action";
        String params = "1/" + userid + "/" + agentid + "/" + mobno;
        String urlparams = "";
        try {
            this.mobno = mobno;
            urlparams = SecurityLayer.genURLCBC(params, endpoint);
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
            obj = SecurityLayer.decryptFirstTimeLogin(obj);
            SecurityLayer.Log("decrypted_response", obj.toString());

            String respcode = obj.optString("responseCode");
            String responsemessage = obj.optString("message");


            //session.setString(SecurityLayer.KEY_APP_ID,appid);

            if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                SecurityLayer.Log("Response Message", responsemessage);

                if (respcode.equals("00")) {



                    iLoginView.onfetchResult();


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
        iLoginView.onError("There was an error processing your request");

    }




}