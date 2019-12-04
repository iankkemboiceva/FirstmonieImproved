package firstmob.firstbank.com.firstagent.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;

public class ConfirmAirtimePresenter implements MainContract.PresenterGetFee, MainContract.GetDataIntractor.OnFinishedListener {
    MainContract.IViewFee iView;
    private MainContract.GetDataIntractor getDataIntractor;
    Context context;

    public ConfirmAirtimePresenter(Context context, MainContract.IViewFee iView, MainContract.GetDataIntractor getDataIntractor) {
        this.iView = iView;
        this.getDataIntractor = getDataIntractor;
        this.context = context;
    }
    @Override
    public void loaddata(String extraparam) {
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
            iView.onfetchResult("confirm",response);
         iView.hideProgress();
    }

    @Override
    public void onFailure(Throwable t) {
        iView.hideProgress();
        SecurityLayer.Log("encryptionJSONException", t.toString());
        Toast.makeText(context, context.getText(R.string.conn_error), Toast.LENGTH_LONG).show();
    }
}
