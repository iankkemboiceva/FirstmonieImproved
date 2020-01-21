package firstmob.firstbank.com.firstagent.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.contract.WithdrawalsContract;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

    public class ConfirmWithdrwalPresenter implements WithdrawalsContract.ConfirmWithdralPresenter, MainContract.GetDataIntractor.OnFinishedListener {
        WithdrawalsContract.IViewConfirmWithdrawal iView;
        private MainContract.GetDataIntractor getDataIntractor;
        Context context;

        public ConfirmWithdrwalPresenter(Context context, WithdrawalsContract.IViewConfirmWithdrawal iView, MainContract.GetDataIntractor getDataIntractor) {
            this.iView = iView;
            this.getDataIntractor = getDataIntractor;
            this.context = context;
        }

        @Override
        public void loaddata(String extraparam) {
            iView.showProgress();
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
        public void ondestroy() {
            iView = null;
        }

        @Override
        public void onFinished(String response) {
           // iView.onfetchResult("getfee",response);
            iView.hideProgress();

            try {
                // JSON Object

                SecurityLayer.Log("response..:", response);
                JSONObject obj = new JSONObject(response);
                //obj = Utility.onresp(obj,getActivity());
                obj = SecurityLayer.decryptTransaction(obj);
                SecurityLayer.Log("decrypted_response", obj.toString());

                String respcode = obj.optString("responseCode");
                String responsemessage = obj.optString("message");
                String respfee = obj.optString("fee");
               String agbalance = obj.optString("data");
                if(Utility.isNotNull(agbalance)) {
                    iView.setBalance(Utility.returnNumberFormat(agbalance)+ Constants.KEY_NAIRA);
                   // acbal.setText(Utility.returnNumberFormat(agbalance)+ Constants.KEY_NAIRA);
                }

                //session.setString(SecurityLayer.KEY_APP_ID,appid);

                if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                    if (!(Utility.checkUserLocked(respcode))) {
                        if (!(response == null)) {
                            if (respcode.equals("00")) {

                                SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                                if (respfee == null || respfee.equals("")) {
                                    iView.setFee("N/A");
                                   // txtfee.setText("N/A");
                                } else {
                                    respfee = Utility.returnNumberFormat(respfee);
                                    iView.setFee(Constants.KEY_NAIRA + respfee);
                                    //txtfee.setText(Constants.KEY_NAIRA + respfee);
                                }

                            } else if (respcode.equals("93")) {
                                iView.onError(responsemessage);
                                iView.launchWithdrawFrag();
                                iView.onError("Please ensure amount set is below the set limit");


                            } else {
                                iView.setViewVisibillity();
                                iView.onError(responsemessage);

                            }
                        } else {
                            iView.setFee("N/A");
                           // txtfee.setText("N/A");
                        }
                    }else{
                        iView.setLogout();

                    }
                }


            } catch (JSONException e) {
                SecurityLayer.Log("encryptionJSONException", e.toString());
                // TODO Auto-generated catch block
                if(!(context == null)) {
                    Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());
                    iView.setForcedLogout();
                  //  ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                }

            } catch (Exception e) {
                SecurityLayer.Log("encryptionJSONException", e.toString());
                if(!(context == null)) {
                    iView.setForcedLogout();
                  //  ((FMobActivity) getActivity()).SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                }
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





