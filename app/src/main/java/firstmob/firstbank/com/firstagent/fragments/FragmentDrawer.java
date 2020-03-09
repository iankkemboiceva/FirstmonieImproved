package firstmob.firstbank.com.firstagent.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.pixplicity.easyprefs.library.Prefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants;
import firstmob.firstbank.com.firstagent.utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class FragmentDrawer extends Fragment implements View.OnClickListener {


    TextView tv,home,tvmobno,tvlastl,tvusid;
    ImageView imgclose;

    Button lyhomeid;
    RelativeLayout lysignout,rlreports,rlhome,rlminist;
    RelativeLayout header;
    //  private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    TextView txlstlog;

    ProgressDialog prgDialog,prgDialog2;
    public static TypedArray navMenuIcons,navIcpad;
    ImageView ivclose;

    private View containerView;

    RelativeLayout rltransf,rlperf,rlccare,rlvsshop;
    private  static  String[] titles = null;
    private  static int [] dricons = null;
    private static ArrayList<String> vd = new ArrayList<String>();
    protected static ArrayList<Integer> vimges = new ArrayList<Integer>();
    protected static ArrayList<Integer> vicpad = new ArrayList<Integer>();
    private static Integer[] icons = null;
    private static Integer[] iconspad = null;
    String uploadFileName = null,userChoosenTask;
    int SELECT_FILE = 345;
    int REQUEST_CAMERA =3293;
    private FragmentDrawerListener drawerListener;



    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.nav_drawer, container, false);
        lysignout = (RelativeLayout) layout.findViewById(R.id.rllogout);
        imgclose = (ImageView) layout.findViewById(R.id.closeicon);

        rlreports = (RelativeLayout) layout.findViewById(R.id.rlreports);

        rlhome = (RelativeLayout) layout.findViewById(R.id.rlagentcredit);
        rlminist = (RelativeLayout) layout.findViewById(R.id.rlmyaccount);


        lysignout.setOnClickListener(this);
        rlhome.setOnClickListener(this);
        rlreports.setOnClickListener(this);
        rlminist.setOnClickListener(this);
        imgclose.setOnClickListener(this);




        txlstlog = (TextView) layout.findViewById(R.id.lastlog);
        String lastlog = Prefs.getString(SharedPrefConstants.LASTL,"");
        lastlog = Utility.convertDate(lastlog);
        txlstlog.setText("Last Login: \n"+lastlog);



        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout,final  Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //  invalidateOptionsMenu();

                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // toolbar.setAlpha(1 - slideOffset / 2);
            }
        };


//        mDrawerToggle.setDrawerIndicatorEnabled(false);
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ico, getActivity().getTheme());
//        mDrawerToggle.setHomeAsUpIndicator(drawable);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.rllogout) {

            drawerListener.onDrawerItemSelected(v, 5);
            mDrawerLayout.closeDrawer(containerView);
        }



        if (v.getId() == R.id.rlagentcredit) {

            drawerListener.onDrawerItemSelected(v, 9);
            mDrawerLayout.closeDrawer(containerView);
        }

        if (v.getId() == R.id.rlreports) {

            drawerListener.onDrawerItemSelected(v, 8);
            mDrawerLayout.closeDrawer(containerView);
        }


        if (v.getId() == R.id.rlmyaccount) {

            drawerListener.onDrawerItemSelected(v, 3);
            mDrawerLayout.closeDrawer(containerView);
        }
        if (v.getId() == R.id.closeicon) {

            mDrawerLayout.closeDrawer(containerView);
        }

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }





}
