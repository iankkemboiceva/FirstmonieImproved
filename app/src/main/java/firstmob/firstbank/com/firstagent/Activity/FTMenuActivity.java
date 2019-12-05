package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;
import firstmob.firstbank.com.firstagent.adapter.OTBList;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FTMenuActivity extends AppCompatActivity {
    GridView gridView;
    List<OTBList> planetsList = new ArrayList<OTBList>();
    String ptype;
    ListView lv;
    DepoMenuAdapt aAdpt;
    ProgressDialog prgDialog, prgDialog2;

    String sbpam = "0", pramo = "0";
    boolean blsbp = false, blpr = false, blpf = false, bllr = false, blms = false, blmpesa = false, blcash = false;
    ArrayList<String> ds = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftmenu);


        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");



        prgDialog.setCancelable(false);

        //checkInternetConnection2();
        lv = (ListView) findViewById(R.id.lv);
        SetPop();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Fragment fragment = null;
                String title = null;

                if (position == 0) {
                    /*fragment = new CashDepoTrans();
                    title = "Cash Deposit";*/
                    startActivity(new Intent(FTMenuActivity.this, CashDepoTransActivity.class));
                } else if (position == 1) {
                /*    fragment = new SendOTB();
                    title = "Other Bank";*/

                    startActivity(new Intent(FTMenuActivity.this, SendOTBActivity.class));
                }/* else if (position == 2) {
                    fragment = new SendtoWallet();
                    title = "Send to Wallet";
                } */
                else if (position == 2) {
                    startActivity(new Intent(FTMenuActivity.this, SendOtherWalletActivity.class));
                }

                if (fragment != null) {
                   /* FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //  String tag = Integer.toString(title);
                    fragmentTransaction.replace(R.id.container_body, fragment, title);
                    fragmentTransaction.addToBackStack(null);
                    ((FMobActivity)getActivity())
                            .setActionBarTitle(title);
                    fragmentTransaction.commit();*/
                }


            }
        });

        session.setString("bankname",null);
        session.setString("bankcode",null);
        session.setString("recanno",null);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void SetPop(){
        planetsList.clear();
       /* planetsList.add(new Dashboard("FirstBank", R.drawable.ic_ftblue));
        planetsList.add(new Dashboard("Other Banks", R.drawable.ic_ftblue));
        planetsList.add(new Dashboard("FirstMonie",R.drawable.ic_ftblue));


        planetsList.add(new Dashboard("Other Wallets",R.drawable.ic_ftblue));



       *//* planetsList.add(new Dashboard("My Profile",R.drawable.icons40));*//*

        aAdpt = new DashboardAdapter( planetsList,getActivity());
        gridView.setAdapter(aAdpt);*/

        planetsList.add(new OTBList("FirstBank","057"));
        planetsList.add(new OTBList("Other Banks","058"));
        //    planetsList.add(new OTBList("FirstMonie","059"));
        planetsList.add(new OTBList("Mobile Money Wallet","059"));
        aAdpt = new DepoMenuAdapt(planetsList, this);
        lv.setAdapter(aAdpt);
    }
}
