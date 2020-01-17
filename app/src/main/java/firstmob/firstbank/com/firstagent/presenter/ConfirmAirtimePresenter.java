package firstmob.firstbank.com.firstagent.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.contract.AirtimeContract;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.SessionManagement;
import firstmob.firstbank.com.firstagent.utils.Utility;
import okhttp3.internal.Internal;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

public class ConfirmAirtimePresenter implements AirtimeContract.PresenterConfirmAirtime, MainContract.GetDataIntractor.OnFinishedListener {
    AirtimeContract.IViewConfirmAirtime iView;
    private MainContract.GetDataIntractor getDataIntractor;
    Context context;
   // String type=null;
    SessionManagement session;


    public ConfirmAirtimePresenter(Context context, AirtimeContract.IViewConfirmAirtime iView, MainContract.GetDataIntractor getDataIntractor) {
        this.iView = iView;
        this.getDataIntractor = getDataIntractor;
        this.context = context;
        session=new SessionManagement(context);
    }
    @Override
    public void fetchServerfee(String flag,String extraparam) {
        iView.showProgress();
        session.setTranstype(flag);
        String endpoint = "fee/getfee.action";
        String userid = Prefs.getString(KEY_USERID, "NA");
        String agentid = Prefs.getString(AGENTID, "NA");

        String params = "1/" + userid + "/" + agentid + extraparam;
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params, endpoint);
            //Log.d("cbcurl",url);
            Log.v("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            Log.e("encryptionerror", e.toString());
        }
        getDataIntractor.getResults(this, urlparams);
    }
    @Override
    public void fetchServerConfirm(String flag,String extraparam) {
        iView.showProgress();
        session.setTranstype(flag);
        String endpoint = "billpayment/mobileRecharge.action";
       // Log.v("Before Req Tok", session.getString(KEY_TOKEN));

        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(extraparam, endpoint);
            //Log.d("cbcurl",url);
            Log.v("RefURL", urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", extraparam);
        } catch (Exception e) {
            Log.e("encryptionerror", e.toString());
        }
        getDataIntractor.getResults(this, urlparams);
    }

    @Override
    public void ondestroy() {
        iView = null;
    }

    @Override
    public void onFinished(String response) {
            iView.hideProgress();
        try {
            // JSON Object

            Log.v("response..:", response);
            JSONObject obj = new JSONObject(response);
            //obj = Utility.onresp(obj,getActivity());
            obj = SecurityLayer.decryptTransaction(obj);
            SecurityLayer.Log("decrypted_response", obj.toString());

            String respcode = obj.optString("responseCode");
            String responsemessage = obj.optString("message");

            if(session.getTransFlag().equals("getFee")){
                if (!(response == null)) {
                String resfee = obj.optString("fee");
                if (respcode.equals("00")) {

                    if (resfee == null || resfee.equals("")) {
                        iView.setFee("N/A");
                       // txtfee.setText("N/A");
                    } else {
                        resfee = Utility.returnNumberFormat(resfee);
                        iView.setFee(Constants.KEY_NAIRA + resfee);
                       // txtfee.setText(Constants.KEY_NAIRA + resfee);
                    }

                } else if (respcode.equals("93")) {
                    Utility.showToast(responsemessage);
                    iView.onBackNavigate();
                    //loadFrag(new Airtime_transfirst());
                    Utility.showToast("Please ensure amount set is below the set limit");
                } else {
                    iView.setviewvisibility();
                   // btnsub.setVisibility(View.GONE);
                    Utility.showToast(responsemessage);
                }
            }else{
                  iView.setFee("N/A");
                }

            }else{
                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (!(response == null)) {
                    JSONObject datas = obj.optJSONObject("data");
                    String agcmsn = obj.optString("fee");
                    Log.v("Response Message", responsemessage);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if (!(Utility.checkUserLocked(respcode))) {
                            Log.v("Response Message", responsemessage);
                            iView.onProcessingError(responsemessage);
                            if (respcode.equals("00")) {

//                                    Log.v("Respnse getResults",datas.toString());
                                if (!(datas == null)) {

                                    String totfee = "0.00";

                                    String ttf = datas.optString("fee");
                                    if (ttf == null || ttf.equals("")) {

                                    } else {
                                        totfee = ttf;
                                    }

                                    String tref = datas.optString("refNumber");

                                    iView.openFinalConfirmAirtime(agcmsn,totfee,tref);

                                }
                            } else {
                                Utility.showToast("" + responsemessage);
                            }
                        } else {
                            iView.openSignIn();
//                            getActivity().finish();
//                            startActivity(new Intent(context, SignInActivity.class));
                            Utility.showToast("You have been locked out of the app.Please call customer care for further details");


                        }
                    } else {
                        Utility.showToast("There was an error on your request");
                    }
                } else {
                    Utility.showToast("There was an error on your request");
                }

            }



        }catch (JSONException e) {
            Utility.errornexttoken();
            SecurityLayer.Log("encryptionJSONException", e.toString());
            // TODO Auto-generated catch block
            iView.onProcessingError(context.getText(R.string.conn_error).toString());
          //  Utility.showToast(getActivity().getText(R.string.conn_error).toString());
            // SecurityLayer.Log(e.toString());

        } catch (Exception e) {
            Utility.errornexttoken();
            SecurityLayer.Log("encryptionJSONException", e.toString());
            // SecurityLayer.Log(e.toString());
        }



    }

    @Override
    public void onFailure(Throwable t) {
        iView.hideProgress();
        SecurityLayer.Log("encryptionJSONException", t.toString());
        Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
    }

}
