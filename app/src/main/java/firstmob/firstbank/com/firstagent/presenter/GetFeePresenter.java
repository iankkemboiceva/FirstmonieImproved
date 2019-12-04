package firstmob.firstbank.com.firstagent.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

    public class GetFeePresenter implements MainContract.PresenterGetFee, MainContract.GetDataIntractor.OnFinishedListener {
        MainContract.IViewFee iView;
        private MainContract.GetDataIntractor getDataIntractor;
        Context context;

        public GetFeePresenter(Context context, MainContract.IViewFee iView, MainContract.GetDataIntractor getDataIntractor) {
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
            iView.onfetchResult("getfee",response);
            iView.hideProgress();
        }

        @Override
        public void onFailure(Throwable t) {
            iView.hideProgress();
            SecurityLayer.Log("encryptionJSONException", t.toString());
            Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
        }
    }





