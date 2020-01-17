package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixplicity.easyprefs.library.Prefs;

import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.contract.AirtimeContract;
import firstmob.firstbank.com.firstagent.network.FetchServerResponse;
import firstmob.firstbank.com.firstagent.presenter.ConfirmAirtimePresenter;
import firstmob.firstbank.com.firstagent.security.EncryptTransactionPin;
import firstmob.firstbank.com.firstagent.utils.SessionManagement;
import firstmob.firstbank.com.firstagent.utils.Utility;

import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGENTID;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.AGMOB;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_EMAIL;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_USERID;

public class ConfirmAirtime extends Fragment implements View.OnClickListener, AirtimeContract.IViewConfirmAirtime {
    TextView reccustid, recamo, rectelco, txtfee;
    AirtimeContract.PresenterConfirmAirtime presenterairtime;
    Button btnsub;
    String txtcustid, amou, narra, ednamee, ednumbb, serviceid, billid;
    ProgressDialog prgDialog, prgDialog2;
    String telcoop;
    EditText amon, edacc, pno, txtamount, txtnarr, edname, ednumber;
    EditText etpin;
    public static final String KEY_TOKEN = "token";
    SessionManagement session;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.confim_airtime, container, false);

        session = new SessionManagement(getActivity());
        reccustid = (TextView) root.findViewById(R.id.textViewnb2);
        etpin = (EditText) root.findViewById(R.id.pin);

        recamo = (TextView) root.findViewById(R.id.textViewrrs);
        rectelco = (TextView) root.findViewById(R.id.textViewrr);
        txtfee = (TextView) root.findViewById(R.id.txtfee);

        prgDialog2 = new ProgressDialog(getActivity());
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);

        //step2 = (TextView) root.findViewById(R.id.tv);

        //step2.setOnClickListener(this);
        presenterairtime=new ConfirmAirtimePresenter(getActivity(),this,new FetchServerResponse());
        txtfee = (TextView) root.findViewById(R.id.txtfee);
        btnsub = (Button) root.findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            txtcustid = bundle.getString("mobno");
            amou = bundle.getString("amou");
            telcoop = bundle.getString("telcoop");

            String txtamou = Utility.returnNumberFormat(amou);
            if (txtamou.equals("0.00")) {
                txtamou = amou;
            }
            billid = bundle.getString("billid");
            serviceid = bundle.getString("serviceid");
            reccustid.setText(txtcustid);
            recamo.setText(Constants.KEY_NAIRA + txtamou);
            rectelco.setText(telcoop);
            Log.v("serviceid",serviceid);
            presenterairtime.fetchServerfee("getFee","/MMO/"+amou);
            //presenter.loaddata("/MMO/"+amou);

    }

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            //    txtcustid = Utility.convertMobNumber(txtcustid);
            if (Utility.checkInternetConnection()) {
                String agpin = etpin.getText().toString();
                if (Utility.isNotNull(txtcustid)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            String encrypted = null;
                            String key = "97206B46CE46376894703ECE161F31F2";
                            try {

                                encrypted = EncryptTransactionPin.encrypt(key, agpin, 'F');
                                System.out.println("Encrypt Pin " + EncryptTransactionPin.encrypt(key, agpin, 'F'));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            prgDialog2.show();



                            String usid = Prefs.getString(KEY_USERID, "NA");
                            String emaill = Prefs.getString(KEY_EMAIL, "NA");
                            String agentid = Prefs.getString(AGENTID, "NA");

                            String mobnoo = Prefs.getString(AGMOB, "NA");

                            String params = "1/" + usid + "/" + agentid + "/" + mobnoo + "/" + billid + "/" + serviceid + "/" + amou + "/01/" + txtcustid + "/" + emaill + "/" + txtcustid + "/" + billid + "/01/" + encrypted;
                            presenterairtime.fetchServerConfirm("confirmairtime",params);
                            //presenterairtime.loaddata(params);
                          //  AirtimeResp(params);
                            ClearPin();
                        } else {
                            Utility.showToast("Please enter a valid value for Agent PIN");
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount");
                    }
                } else {
                    Utility.showToast("Please enter a value for Customer ID");
                }
            }
        }
    }

    @Override
    public void setFee(String fee) {
        txtfee.setText(fee);
    }

    @Override
    public void setviewvisibility() {
        btnsub.setVisibility(View.GONE);
    }

    @Override
    public void onProcessingError(String error) {
        Utility.showToast(error);
    }

    @Override
    public void onBackNavigate() {
        loadFrag(new Airtime_transfirst());
    }

    @Override
    public void openSignIn() {
        getActivity().finish();
        startActivity(new Intent(getActivity(), SignInActivity.class));
    }

    @Override
    public void openFinalConfirmAirtime(String agcmsn, String totfee, String tref) {
        Bundle b = new Bundle();
        b.putString("mobno", txtcustid);
        b.putString("amou", amou);
        b.putString("telcoop", telcoop);

        b.putString("billid", billid);
        b.putString("serviceid", serviceid);
        b.putString("agcmsn", agcmsn);
        b.putString("fee", totfee);
        b.putString("tref", tref);
        Fragment fragment = new FinalConfAirtime();

        fragment.setArguments(b);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showProgress() {
        prgDialog2.show();
    }

    @Override
    public void hideProgress() {
     prgDialog2.dismiss();
    }

    @Override
    public void onDestroy() {
        presenterairtime.ondestroy();
        super.onDestroy();
    }

    public void ClearPin() {
        etpin.setText("");
    }
    public void loadFrag(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
