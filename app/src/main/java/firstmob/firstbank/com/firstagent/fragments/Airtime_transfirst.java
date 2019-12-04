package firstmob.firstbank.com.firstagent.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;
import firstmob.firstbank.com.firstagent.network.FetchServerResponse;
import firstmob.firstbank.com.firstagent.presenter.LoadMmbillersPresenter;
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;
import firstmob.firstbank.com.firstagent.utils.Utility;

public class Airtime_transfirst extends Fragment implements View.OnClickListener, MainContract.IViewbillers {
    List<GetAirtimeBillersData> planetsList2 = new ArrayList<GetAirtimeBillersData>();
    EditText amon, edacc, pno, txtamount, txtnarr, phonenumb;
    Spinner sp1, sp5, sp7;
    //  RecyclerView lvbann;
    ArrayAdapter<GetAirtimeBillersData> mobadapt;
    TextView tx;
    LinearLayoutManager layoutManager, layoutManager2;
    Button btn2;
    String telcochosen;
    ProgressDialog prgDialog;
    MainContract.Presenterloadbillers presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
      View  rootView = inflater.inflate(R.layout.airtime_firstpage, container, false);
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        sp1 = (Spinner) rootView.findViewById(R.id.spin1);
        btn2 = (Button) rootView.findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        phonenumb = (EditText) rootView.findViewById(R.id.phonenumb);
        txtamount = (EditText) rootView.findViewById(R.id.amount);
        presenter = new LoadMmbillersPresenter(getActivity(),this,new FetchServerResponse());
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        phonenumb.setOnFocusChangeListener(ofcListener);
        txtamount.setOnFocusChangeListener(ofcListener);
        presenter.loadbiller();
      return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            String phoneno = phonenumb.getText().toString();
            String amou = txtamount.getText().toString();
            String mnop = sp1.getSelectedItem().toString();
            int spinpos = sp1.getSelectedItemPosition();
            String billid = planetsList2.get(spinpos).getBillerId();
            String serviceid = planetsList2.get(spinpos).getSId();

            if (Utility.checkInternetConnection()) {
                if (Utility.isNotNull(phoneno)) {
                    if (Utility.isNotNull(amou)) {
                        // if ((planetsList.size() - 1) == sp1.getSelectedItemPosition()) {
                        if (!(serviceid.equals("0000"))) {
                            Bundle b = new Bundle();
                            b.putString("mobno", phoneno);
                            b.putString("amou", amou);
                            b.putString("telcoop", mnop);
                            b.putString("billid", billid);
                            b.putString("serviceid", serviceid);
                            Fragment fragment = new ConfirmAirtime();

                            fragment.setArguments(b);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //  String tag = Integer.toString(title);
                            fragmentTransaction.replace(R.id.frameLayout, fragment);
                           // fragmentTransaction.addToBackStack("Confirm Airtime");
                            fragmentTransaction.commit();

                        } else {
                         Utility.showToast("Please select a valid mobile network operator");

                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount");
                    }
                } else {
                    Utility.showToast("Please enter a value for Account Number");
                }
            }


        }
    }

    @Override
    public void onLoginResult(List<GetAirtimeBillersData> planetsList) {
        Log.v("Get Biller Data Name", planetsList.get(0).getBillerName());

        Collections.sort(planetsList, new Comparator<GetAirtimeBillersData>() {
            public int compare(GetAirtimeBillersData d1, GetAirtimeBillersData d2) {
                return d1.getBillerName().compareTo(d2.getBillerName());
            }
        });
        planetsList2=planetsList;
        //  Collections.swap(planetsList,0,planetsList.size()-1);
        mobadapt = new ArrayAdapter<GetAirtimeBillersData>(getActivity(), android.R.layout.simple_spinner_item, planetsList);
        mobadapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(mobadapt);
        sp1.setSelection(planetsList.size() - 1);
    }

    @Override
    public void onProcessingError(String error) {
       Utility.showToast(error);
    }


    @Override
    public void showProgress() {
        prgDialog.show();
    }

    @Override
    public void hideProgress() {
        prgDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        presenter.ondestroy();
        super.onDestroy();
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.amount && !hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String txt = txtamount.getText().toString();
                String fbal = Utility.returnNumberFormat(txt);
                txtamount.setText(fbal);

            }


        }
    }
}
