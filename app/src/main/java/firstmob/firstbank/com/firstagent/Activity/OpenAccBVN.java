package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;


import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import firstmob.firstbank.com.firstagent.network.ApiInterface;
import firstmob.firstbank.com.firstagent.network.RetrofitInstance;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection;

public class OpenAccBVN extends BaseActivity implements View.OnClickListener {
    ProgressDialog pro ;
    Button btnnext,btnopenacc;
    EditText agentid;
    @Inject
    Utility ul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_bvn);

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

        pro = new ProgressDialog(this);
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);


        agentid  = (EditText) findViewById(R.id.agentid);

        btnnext = (Button) findViewById(R.id.button2);
        btnnext.setOnClickListener(this);

        btnopenacc = (Button) findViewById(R.id.button5);
        btnopenacc.setOnClickListener(this);

        SecurityLayer.Log("plain appid", Utility.getPlainAppid(getApplicationContext()));
        Log.v("plain appid",Utility.getPlainAppid(getApplicationContext()));
      //  Toast.makeText(getApplicationContext(),Utility.getPlainAppid(getApplicationContext()),Toast.LENGTH_LONG).show();

    }




    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button2) {
            String agid = agentid.getText().toString();

            if (Utility.isNotNull(agid)) {

       //         if (ul.checkInternetConnection()) {
                    GetBVNMicro(agid);

            //    }


            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Please enter a valid value for BVN",
                        Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId() == R.id.button5) {

        }
        }



    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(this)
                    .title(title)
                    .content(msg)

                    .negativeText("CONTINUE")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            dialog.dismiss();
                            finish();


                            // After logout redirect user to Loing Activity
                            Intent i = new Intent(c, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // Staring Login Activity
                            startActivity(i);

                        }
                    })
                    .show();
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


    private void GetBVNMicro(final String bvn) {
        pro.show();

        String usid = Utility.gettUtilUserId(getApplicationContext());

        ApiInterface apiService =
                RetrofitInstance.getClient().create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();

            paramObject.put("channel", "1");
            paramObject.put("userId", usid);
            paramObject.put("bvnNumber", bvn);


            String data = SecurityLayer.encryptdata(paramObject.toString(),getApplicationContext());
            String hash = SecurityLayer.gethasheddata(paramObject);
            String appid = Utility.getNewAppID(getApplicationContext());

            JSONObject finalparam = new JSONObject();
            finalparam.put("data", data);
            finalparam.put("hash", hash);
            finalparam.put("appId", appid);

            SecurityLayer.Log("data parm",paramObject.toString());






            Call<String> call = apiService.validatebvn(finalparam.toString());




            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object

                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getApplicationContext());

                        SecurityLayer.Log("decrypted_response", obj.toString());

                        String respcode = obj.optString("responseCode");

                        String responsemessage = obj.optString("message");


                        JSONObject servdata = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                //LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    String fname = servdata.optString("firstName");
                                    String lname = servdata.optString("lastName");

                                    String midname = servdata.optString("midName");
                                    String maritalStatus = servdata.optString("maritalStatus");

                                    String yob = servdata.optString("dob");
                                    String gender = servdata.optString("gender");

                                    String email = servdata.optString("email");
                                    String state = servdata.optString("state");

                                    String address = servdata.optString("address");
                                    String mobileNumber = servdata.optString("mobileNumber");

                                    String salutation = servdata.optString("salutation");
                                    Intent intent  = new Intent(OpenAccBVN.this,OpenAccBVNConfirm.class);



                                    intent.putExtra("fname", fname);
                                    intent.putExtra("lname", lname);
                                    intent.putExtra("midname", midname);
                                    intent.putExtra("yob", yob);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("city", "NA");
                                    intent.putExtra("state", state);
                                    intent.putExtra("straddr", address);
                                    intent.putExtra("email", email);
                                    intent.putExtra("hmadd", bvn);
                                    intent.putExtra("mobn", mobileNumber);
                                    intent.putExtra("salut", salutation);
                                    intent.putExtra("marstatus", maritalStatus);




                                    startActivity(intent);

                                } else {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "There was an error processing your request",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        if(!(getApplicationContext() == null)) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            // SecurityLayer.Log(e.toString());
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if(!(getApplicationContext() == null)) {
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    try {
                        if ((pro != null) && pro.isShowing() && !(getApplicationContext() == null)) {
                            pro.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        //   prgDialog = null;
                    }

                    //   prgDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    SecurityLayer.Log("Throwable error",t.toString());


                    if ((pro != null) && pro.isShowing() && !(getApplicationContext() == null)) {
                        pro.dismiss();
                    }
                    if(!(getApplicationContext() == null)) {
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error processing your request",
                                Toast.LENGTH_LONG).show();
                        // SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
