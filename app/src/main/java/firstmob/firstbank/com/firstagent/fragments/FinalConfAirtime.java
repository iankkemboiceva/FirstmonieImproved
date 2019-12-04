package firstmob.firstbank.com.firstagent.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import firstmob.firstbank.com.firstagent.Activity.FMobActivity;
import firstmob.firstbank.com.firstagent.Activity.R;

public class FinalConfAirtime  extends Fragment implements View.OnClickListener {
    Button btnsub;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.final_conf_airtime, null);
        btnsub = (Button) root.findViewById(R.id.button2);
        return root;
    }



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), FMobActivity.class));
        }
    }
}
