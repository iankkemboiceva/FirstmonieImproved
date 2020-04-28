package firstmob.firstbank.com.firstagent.Activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import firstmob.firstbank.com.firstagent.model.GetCitiesData;
import firstmob.firstbank.com.firstagent.model.GetStatesData;
import firstmob.firstbank.com.firstagent.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OpenAccBVNConfirm extends BaseActivity implements View.OnClickListener {
    String strfname, strlname, strmidnm, stryob, stremail, strhmdd, strmobn, strsalut, strmarst, strcity, strstate, strgender, straddr;
    String strcode,strcitycode = "N/A";
    TextView txtstrfname, txtstrlname, txtstrmidnm, txtstryob, txtstremail, txtstrhmdd, txtstrmobn, txtstrsalut, txtstrmarst, txtstrcity, txtstrstate, txtstrgender, txtstraddr;
    List<GetStatesData> planetsList = new ArrayList<GetStatesData>();
    List<GetStatesData> arrangelist = new ArrayList<GetStatesData>();
    List<GetCitiesData> citylist = new ArrayList<GetCitiesData>();
    Button btnconfirm;
    Spinner sp1;
    RelativeLayout rlbvn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openaccbvnconfirm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnconfirm = (Button) findViewById(R.id.button2);
        btnconfirm.setOnClickListener(this);
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


        txtstrfname = (TextView)findViewById(R.id.fname);
        txtstryob = (TextView)findViewById(R.id.txtstryob);
        txtstremail = (TextView)findViewById(R.id.txtstremail);
        txtstrmobn = (TextView)findViewById(R.id.txtstrmobn);
        txtstrmarst = (TextView)findViewById(R.id.txtstrmarst);
        txtstrstate = (TextView)findViewById(R.id.txtstrstate);
        txtstrgender = (TextView)findViewById(R.id.txtstrgender);
        txtstraddr = (TextView)findViewById(R.id.txtstraddr);

        rlbvn = (RelativeLayout) findViewById(R.id.title);





        Intent intent = getIntent();
        if (intent != null) {
            strfname = intent.getStringExtra("fname");
            strlname = intent.getStringExtra("lname");
            strmidnm = intent.getStringExtra("midname");
           String txstringryob = intent.getStringExtra("yob");
            stremail = intent.getStringExtra("email");
            strhmdd = intent.getStringExtra("hmadd");
            strmobn = intent.getStringExtra("mobn");
            strsalut = intent.getStringExtra("salut");
            strmarst = intent.getStringExtra("marstatus");
            strcity = intent.getStringExtra("city");
            strstate = intent.getStringExtra("state");
            strgender = intent.getStringExtra("gender");
            straddr = intent.getStringExtra("straddr");


          //  Toast.makeText(getApplicationContext(),stryob,  Toast.LENGTH_LONG).show();

            txtstrfname.setText(strfname+" "+strlname);
            txtstryob.setText(txstringryob);

            stryob = Utility.convertBVNdate(txstringryob);
            txtstremail.setText(stremail);
            txtstrmobn.setText(strmobn);
            txtstrmarst.setText(strmarst);
            txtstrgender.setText(strgender);

            txtstrstate.setText(strstate);
            txtstraddr.setText(straddr);

            if(strgender.equals("Male")){
                strgender = "M";
            }
            if(strgender.equals("Female")){
                strgender = "F";
            }


            if(Utility.isNotNull(strsalut) || strsalut.equals("")){
                rlbvn.setVisibility(View.VISIBLE);
            }
            sp1 = (Spinner)findViewById(R.id.spinsal);

            ArrayAdapter<CharSequence> adapter = null;
            if(Utility.isNotNull(strgender)) {

                if (strgender.equals("M")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.malesalut, R.layout.my_spinner);
                } else if (strgender.equals("F")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.femsalut, R.layout.my_spinner);
                }else{
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.salut, R.layout.my_spinner);
                }
            }else{
                adapter = ArrayAdapter.createFromResource(
                        OpenAccBVNConfirm.this, R.array.salut, R.layout.my_spinner);
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(adapter);





        }
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button2) {
            Intent intent  = new Intent(OpenAccBVNConfirm.this,OpenAccUpPicActivity.class);




            if(strmarst.equals("Single")){
                strmarst = "UNMARR";
            }
            if(strmarst.equals("Married")){
                strmarst = "MARR";
            }
            intent.putExtra("fname", strfname);
            intent.putExtra("lname", strlname);
            intent.putExtra("midname", strmidnm);
            intent.putExtra("yob", stryob);
            intent.putExtra("gender", strgender);
            intent.putExtra("city", strcity);
            intent.putExtra("state", strstate);
            intent.putExtra("straddr", straddr);
            intent.putExtra("email", stremail);
            intent.putExtra("hmadd", strhmdd);
            intent.putExtra("mobn", strmobn);

            intent.putExtra("marstatus", strmarst);

            if(Utility.isNotNull(strsalut) || strsalut.equals("")){
                if(!(sp1.getSelectedItemPosition() == 0)) {
                    strsalut = sp1.getSelectedItem().toString();
                    intent.putExtra("salut", strsalut);


                    startActivity(intent);
                }else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please select a valid Title",
                            Toast.LENGTH_LONG).show();
                }

            }else {

                intent.putExtra("salut", strsalut);


                startActivity(intent);
            }
        }
    }





}
