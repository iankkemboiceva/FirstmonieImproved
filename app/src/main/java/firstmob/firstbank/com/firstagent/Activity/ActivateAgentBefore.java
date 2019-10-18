package firstmob.firstbank.com.firstagent.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;
import firstmob.firstbank.com.firstagent.network.FetchServerResponse;
import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivateAgentBefore extends AppCompatActivity implements MainContract.ILoginView {

    @BindView(R.id.agentid) EditText agentid;
    @BindView(R.id.button2) Button btnLogin;

    ProgressDialog pDialog;
   // private ProgressBar progressBar;
    MainContract.Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_agentbefore);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //find view
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False
        pDialog.setCancelable(false);
        ButterKnife.bind(this);


        //init
        loginPresenter = new LoginPresenterCompl(this,new FetchServerResponse());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void showToast(String text) {

        Toast.makeText(
                getApplicationContext(),
                text,
                Toast.LENGTH_LONG).show();


    }


    @OnClick(R.id.button2)
    public void submit(View view) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { loginPresenter.doLogin(agentid.getText().toString());}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();

    }



    @Override
    public void onLoginResult(String response) {

        try {
            JSONObject jsonObject1 = new JSONObject(response);
            String res_description=jsonObject1.getString("respdesc");
            if(res_description.equals("SUCCESS")){
                Toast.makeText(this, "successfully loggedin", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        loginPresenter.ondestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }


    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void onLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}