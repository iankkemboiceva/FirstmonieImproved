package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import firstmob.firstbank.com.firstagent.adapter.DepoMenuAdapt;
import firstmob.firstbank.com.firstagent.adapter.OTBList;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseBVNorNoBVN extends BaseActivity {

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
        setContentView(R.layout.activity_choosebvn);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
       TextView accopeningttle=findViewById(R.id.titlepg);
        accopeningttle.setText("Account Opening");
        ab.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.nbkyellow)));
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

                String title = null;

                if (position == 0) {
                    startActivity(new Intent(ChooseBVNorNoBVN.this, OpenAccBVN.class));
                    Prefs.putString("ISBVN", "Y");
                } else if (position == 1) {
                    startActivity(new Intent(ChooseBVNorNoBVN.this, OpenAccActivity.class));
                    Prefs.putString("ISBVN","N");
                }



            }
        });


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


        planetsList.add(new OTBList("Account Opening with BVN","057"));
        planetsList.add(new OTBList("Open Wallet","058"));

        aAdpt = new DepoMenuAdapt(planetsList, this);
        lv.setAdapter(aAdpt);
    }
}
