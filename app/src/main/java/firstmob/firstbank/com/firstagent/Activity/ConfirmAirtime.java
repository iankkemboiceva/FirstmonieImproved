package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.contract.AirtimeContract;
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog;
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
    @Inject
    Utility utility;
    public ConfirmAirtime() {
        ApplicationClass.getMyComponent().inject(this);
    }
    AirtimeContract.PresenterConfirmAirtime presenterairtime;
    TextView reccustid, recamo, rectelco, step2, txtfee,acbal;
    Button btnsub;
    String txtcustid, amou, serviceid, billid,agbalance;
   // ProgressDialog prgDialog, prgDialog2;
    String telcoop;
    EditText amon, edacc, pno, txtamount, txtnarr, edname, ednumber;
    EditText etpin;
    public static final String KEY_TOKEN = "token";
    SessionManagement session;
    ViewDialog viewDialog;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.confim_airtime, container, false);
        presenterairtime=new ConfirmAirtimePresenter(getActivity(),this,new FetchServerResponse());
        session = new SessionManagement(getActivity());
        reccustid = (TextView) root.findViewById(R.id.textViewnb2);
        etpin = (EditText) root.findViewById(R.id.pin);
        acbal = (TextView) root.findViewById(R.id.txtacbal);
        recamo = (TextView) root.findViewById(R.id.textViewrrs);
        rectelco = (TextView) root.findViewById(R.id.textViewrr);
        viewDialog=new ViewDialog(getActivity());
//        prgDialog2 = new ProgressDialog(getActivity());
//        prgDialog2.setMessage("Loading....");
//        prgDialog2.setCancelable(false);
//
//
//        prgDialog = new ProgressDialog(getActivity());
//        prgDialog.setMessage("Loading....");
//        prgDialog.setCancelable(false);

      //  step2 = (TextView) root.findViewById(R.id.tv);
//        step2.setOnClickListener(this);
        txtfee = (TextView) root.findViewById(R.id.txtfee);
        btnsub = (Button) root.findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            txtcustid = bundle.getString("mobno");


            Log.v("mobile number",txtcustid);

            amou = bundle.getString("amou");
            telcoop = bundle.getString("telcoop");
            String newamo = amou.replace(",", "");
            String txtamou = Utility.returnNumberFormat(newamo);
            if (txtamou.equals("0.00")) {
                txtamou = amou;
            }
            billid = bundle.getString("billid");
            serviceid = bundle.getString("serviceid");
            reccustid.setText(txtcustid);


            recamo.setText(Constants.KEY_NAIRA + txtamou);
            rectelco.setText(telcoop);
            amou = Utility.convertProperNumber(amou);
            presenterairtime.fetchServerfee("getFee","/MMO/"+amou);
        }

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            txtcustid = Utility.convertMobNumber(txtcustid);
            if (Utility.checkInternetConnection()) {
                String agpin = etpin.getText().toString();
                if (Utility.isNotNull(txtcustid)) {
                    if (Utility.isNotNull(amou)) {
                        if (Utility.isNotNull(agpin)) {
                            String encrypted = null;
                            String usid = Prefs.getString(KEY_USERID, "NA");
                            String agentid = Prefs.getString(AGENTID, "NA");

                            final String mobnoo = "0" + Prefs.getString(AGMOB, "NA");
                            String emaill = Prefs.getString(KEY_EMAIL, "NA");

                            String params = "1/" + usid + "/" + agentid + "/" + mobnoo + "/" + billid + "/" + serviceid + "/" + amou + "/01/" + txtcustid + "/" + emaill + "/" + txtcustid + "/" + billid + "01";

                            Intent i=new Intent(getActivity(),TransactionProcessingActivity.class);
                            i.putExtra("mobno", txtcustid);
                            i.putExtra("amou", amou);
                            i.putExtra("telcoop", telcoop);

                            i.putExtra("billid", billid);
                            i.putExtra("serviceid", serviceid);
                            i.putExtra("txpin", encrypted);
                            i.putExtra("serv", "AIRT");
                            i.putExtra("params", params);
                            startActivity(i);
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
    public void setBalance(String blance) {

    }

    @Override
    public void logout() {
        session.logoutUser();
        getActivity().finish();
        Intent i = new Intent(getActivity(), SignInActivity.class);
        startActivity(i);
        Utility.showToast("You have been locked out of the app.Please call customer care for further details");
    }



    @Override
    public void showProgress() {
      //  prgDialog2.show();
        viewDialog.showDialog();
    }

    @Override
    public void hideProgress() {
        if(viewDialog!=null){
            viewDialog.hideDialog();
        }
    // prgDialog2.dismiss();
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
