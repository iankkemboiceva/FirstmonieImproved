package firstmob.firstbank.com.firstagent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import firstmob.firstbank.com.firstagent.Activity.FMobActivity;
import firstmob.firstbank.com.firstagent.Activity.R;

public class FinalConfAirtimeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnsub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_conf_airtime);
        btnsub = (Button) findViewById(R.id.button2);
    }




    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            finish();
            startActivity(new Intent(this, FMobActivity.class));
        }
    }
}
