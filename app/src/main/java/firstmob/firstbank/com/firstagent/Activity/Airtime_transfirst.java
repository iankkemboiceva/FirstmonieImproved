package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import firstmob.firstbank.com.firstagent.contract.AirtimeContract;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData;
import firstmob.firstbank.com.firstagent.network.FetchServerResponse;
import firstmob.firstbank.com.firstagent.presenter.AirtimeTransFirstPresenter;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.SessionManagement;
import firstmob.firstbank.com.firstagent.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Airtime_transfirst extends Fragment implements View.OnClickListener, AirtimeContract.IviewAirtimeFirst {

    @Inject
    Utility utility;
    public Airtime_transfirst() {
        ApplicationClass.getMyComponent().inject(this);
    }

    List<GetAirtimeBillersData> planetsList2 = new ArrayList<GetAirtimeBillersData>();

    //  RecyclerView lvbann;
    ArrayAdapter<GetAirtimeBillersData> mobadapt;
    TextView tx;
    LinearLayoutManager layoutManager, layoutManager2;
    Button btn2;
    String telcochosen;
    ProgressDialog prgDialog;
    AirtimeContract.PresenterAirtimeFirst presenter;
    SessionManagement session;
    EditText amon, edacc, pno, txtamount, txtnarr, phonenumb;
    Spinner sp1, sp5, sp7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
      View  rootView = inflater.inflate(R.layout.airtime_firstpage, container, false);
        layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        session = new SessionManagement(getActivity());
        //layoutManager.scrollToPosition(currPos);

//        lvbann.setLayoutManager(layoutManager2);
        //  Pop();
        prgDialog = new ProgressDialog(getActivity());

        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        sp1 = (Spinner) rootView.findViewById(R.id.spin1);
        btn2 = (Button) rootView.findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        phonenumb = (EditText) rootView.findViewById(R.id.phonenumb);
        txtamount = (EditText) rootView.findViewById(R.id.amount);
        presenter=new AirtimeTransFirstPresenter(getActivity(),this,new FetchServerResponse());
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        phonenumb.setOnFocusChangeListener(ofcListener);
        txtamount.setOnFocusChangeListener(ofcListener);
        String strservdata = session.getString(SessionManagement.KEY_AIRTIME);
        if (!(strservdata == null)) {
            JSONArray servdata = null;
            try {
                servdata = new JSONArray(strservdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (servdata.length() > 0) {
                presenter.loadCachedAirtimeBillers();
              //  SetAirtimStored();
            } else {
                if (Utility.checkInternetConnection()) {
                    presenter.loadAirtimeBillers();
                    //PopulateAirtime();
                }
            }
        } else {
            if (Utility.checkInternetConnection()) {
                presenter.loadAirtimeBillers();
              //  PopulateAirtime();
            }
        }
        sp1.setOnTouchListener(spinnerOnTouch);
        sp1.setOnKeyListener(spinnerOnKey);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecurityLayer.Log("Good Adapter", "Am I in");
                if (sp1.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter.loadAirtimeBillers();
                       // PopulateAirtime();
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
                SecurityLayer.Log("Good Adapter", "Am I in");
                if (sp1.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter.loadAirtimeBillers();
                       // PopulateAirtime();
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter");
                }

            }
        });
      return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {

            String phoneno = phonenumb.getText().toString();
            String amou = txtamount.getText().toString();

            amou = Utility.returnNumberFormat(amou);
            String mnop = sp1.getSelectedItem().toString();

            int spinpos = sp1.getSelectedItemPosition();
            String billid = planetsList2.get(spinpos).getBillerId();
            String serviceid = planetsList2.get(spinpos).getSId();

            if (Utility.checkInternetConnection()) {


                if (Utility.isNotNull(phoneno)) {
                    if (Utility.isNotNull(amou)) {
                        if (phoneno.length() >= 10) {
                            String nwamo = amou.replace(",", "");
                            SecurityLayer.Log("New Amount", nwamo);
                            double txamou = Double.parseDouble(nwamo);
                            if (txamou >= 100) {
                                // if ((planetsList.size() - 1) == sp1.getSelectedItemPosition()) {
                                if (!(serviceid.equals("0000"))) {

                                    Bundle b = new Bundle();
                                    b.putString("mobno", phoneno);
                                    b.putString("amou", amou);
                                    b.putString("telcoop", mnop);

                                    Log.v("mobile number",phoneno);
                                    b.putString("billid", billid);
                                    b.putString("serviceid", serviceid);
                                    Fragment fragment = new ConfirmAirtime();

                                    fragment.setArguments(b);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //  String tag = Integer.toString(title);
                                    fragmentTransaction.replace(R.id.frameLayout, fragment, "Confirm Airtime");
                                    fragmentTransaction.addToBackStack("Confirm Airtime");
//                                    ((FMobActivity) getActivity())
//                                            .setActionBarTitle("Confirm Airtime");
                                    fragmentTransaction.commit();

                                } else {
                                    Utility.showToast("Please select a valid mobile network operator");
                                }
                            } else {
                                Utility.showToast("Please enter an airtime value more than 100 Naira");
                            }
                        } else {
                            Utility.showToast("Please ensure valid mobile number has been set");
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount");
                    }
                } else {
                    Utility.showToast("Please enter a value for Phone Number");
                }
            }


        }
    }

    @Override
    public void onResult(List<GetAirtimeBillersData> planetsList) {
        SecurityLayer.Log("Get Biller Data Name", planetsList.get(0).getBillerName());
        planetsList2=planetsList;
        Collections.sort(planetsList, new Comparator<GetAirtimeBillersData>() {
            public int compare(GetAirtimeBillersData d1, GetAirtimeBillersData d2) {
                return d1.getBillerName().compareTo(d2.getBillerName());
            }
        });
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

    @Override
    public void logout() {
        session.logoutUser();

        // After logout redirect user to Loing Activity
        getActivity().finish();
        Intent i = new Intent(getActivity(), SignInActivity.class);

        // Staring Login Activity
        startActivity(i);
        Utility.showToast("You have been locked out of the app.Please call customer care for further details");
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }

    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Your code
                Log.v("Spinner on click", "Spinner on click");
                if (sp1.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter.loadAirtimeBillers();
                       // PopulateAirtime();
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter");
                }


            }
            return false;
        }
    };
    private View.OnKeyListener spinnerOnKey = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //your code
                Log.v("Spinner on click", "Spinner on click");
                if (sp1.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter.loadAirtimeBillers();
                        //PopulateAirtime();
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter");
                }


                return true;
            } else {
                return false;
            }
        }
    };



    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.amount && !hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String txt = txtamount.getText().toString();
                String fbal = Utility.returnNumberFormat(txt);
                //  txtamount.setText(fbal);
            }
        }
    }

}
