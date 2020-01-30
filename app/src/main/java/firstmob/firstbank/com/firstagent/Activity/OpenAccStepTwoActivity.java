package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OpenAccStepTwoActivity extends BaseActivity implements View.OnClickListener {
    File finalFile;
    int REQUEST_CAMERA =3293;
    Button sigin;
    TextView gendisp;

    EditText idno,mobno,fnam,lnam,yob;
    List<String> planetsList = new ArrayList<String>();
    List<String> prodid = new ArrayList<String>();
    ArrayAdapter<String> mArrayAdapter;
    Spinner sp1,sp2,sp5,sp3,sp4;
    Button btn4;
    TextView step2;
    static Hashtable<String, String> data1;
    String paramdata = "";
    ProgressDialog prgDialog,prgDialog2,prgDialog7;
    int fcsize = 0;
    List<String> mobopname  = new ArrayList<String>();
    List<String> mobopid  = new ArrayList<String>();

    TextView tvdate;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    String strfname,strlname,strmidnm,stryob,strcity,strstate,strgender,stremail,strhmadd,strsalut,strmarstat,strmno;
    EditText edemail,edmobno,edhm,edstraddr;
    public static final String DATEPICKER_TAG = "datepicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_step_two);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.nbkyellow)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);


        sigin = (Button) findViewById(R.id.button1);
        sigin.setOnClickListener(this);



        sp1 = (Spinner)findViewById(R.id.spinsal);
        sp2 = (Spinner)findViewById(R.id.spin2);
        sp3 = (Spinner)findViewById(R.id.spin3);

        edemail = (EditText) findViewById(R.id.email);
        edhm = (EditText) findViewById(R.id.houseno);
        edmobno = (EditText) findViewById(R.id.mobno);
        edstraddr = (EditText) findViewById(R.id.streetadrr);



        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                OpenAccStepTwoActivity.this, R.array.mar_status, R.layout.my_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);




        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);


        Intent intent = getIntent();
        if (intent != null) {


            strfname = intent.getStringExtra("fname");

            strlname = intent.getStringExtra("lname");
            strmidnm = intent.getStringExtra("midname");
            stryob = intent.getStringExtra("yob");
            strcity = intent.getStringExtra("city");
            strstate = intent.getStringExtra("state");
            strgender = intent.getStringExtra("gender");
            stremail = intent.getStringExtra("email");
            strhmadd = intent.getStringExtra("hmadd");
            strsalut = intent.getStringExtra("salut");
            strmarstat = intent.getStringExtra("marstatus");
            strmno = intent.getStringExtra("mobn");
            edemail.setText(stremail);
            edhm.setText(strhmadd);
            edmobno.setText(strmno);
            if(Utility.isNotNull(strmarstat)) {
                if (strmarstat.equals("MARR")) {
                    sp2.setSelection(1);

                } else if (strmarstat.equals("UNMAR")) {
                    sp2.setSelection(2);
                }
            }
            ArrayAdapter<CharSequence> adapter = null;
            if(Utility.isNotNull(strgender)) {

                if (strgender.equals("M")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccStepTwoActivity.this, R.array.malesalut, R.layout.my_spinner);
                } else if (strgender.equals("F")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccStepTwoActivity.this, R.array.femsalut, R.layout.my_spinner);
                }
            }else{
                adapter = ArrayAdapter.createFromResource(
                        OpenAccStepTwoActivity.this, R.array.salut, R.layout.my_spinner);
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(adapter);

            if(Utility.isNotNull(strgender)) {
                if (strgender.equals("M")) {
                    String[] myResArray = getResources().getStringArray(R.array.malesalut);
                    List<String> myResArrayList = Arrays.asList(myResArray);
                    List<String> myResMutableList = new ArrayList<String>(myResArrayList);
                    int indexx = 0;
                    for (int sd = 0; sd < myResMutableList.size(); sd++) {


                        String strstt = myResMutableList.get(sd);
                        if (strstt.equals(strsalut)) {
                            indexx = sd;
                        }


                    }
                    sp1.setSelection(indexx);
                } else if (strgender.equals("F")) {
                    String[] myResArray = getResources().getStringArray(R.array.femsalut);
                    List<String> myResArrayList = Arrays.asList(myResArray);
                    List<String> myResMutableList = new ArrayList<String>(myResArrayList);
                    int indexx = 0;
                    for (int sd = 0; sd < myResMutableList.size(); sd++) {


                        String strstt = myResMutableList.get(sd);
                        if (strstt.equals(strsalut)) {
                            indexx = sd;
                        }


                    }
                    sp1.setSelection(indexx);
                }
            }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            onCaptureImageResult(data);
        }
    }



    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float neww = ((float)width)*((float)0.6);
        float newh = ((float)height)*((float)0.6);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = getResizedBitmap((Bitmap) data.getExtras().get("data"),300,300);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String filename = System.currentTimeMillis()+".jpg";
        finalFile = new File(Environment.getExternalStorageDirectory(),filename);
        FileOutputStream fo;
        try {
            finalFile.createNewFile();
            fo = new FileOutputStream(finalFile);
            fo.write(bytes.toByteArray());
            fo.close();
            SecurityLayer.Log("Filename stored",filename);
            String filePath = finalFile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
           /* new Thread(new Runnable() {
                public void run() {


                    uploadFile(finalFile);

                }
            }).start();*/
            //    new FragmentDrawer.AsyncUplImg().execute();
           /* getApplicationContext().finish();
            Toast.makeText(
                    getApplicationContext(),
                    "Image Set Successfully",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),FMobActivity.class));*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   iv.setImageBitmap(thumbnail);



    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Bundle bundle = new Bundle();
            String strmobn = edmobno.getText().toString();
            String strhmadd = edhm.getText().toString();
            String stremail = edemail.getText().toString();
            String straddr = edstraddr.getText().toString();
            String salut = sp1.getSelectedItem().toString();
            String marstatus = sp2.getSelectedItem().toString();

            if(sp2.getSelectedItemPosition() == 1){
                marstatus = "MARR";
            }
            else if(sp2.getSelectedItemPosition() == 2){
                marstatus = "UNMAR";
            }
            boolean boolemail = false;
            if(Utility.isNotNull(stremail)){
                if(Utility.validate(stremail)){
                    boolemail = true;
                }else{
                    boolemail = false;
                }
            }else{
                boolemail = true;
            }

                if (Utility.isNotNull(strmobn)) {
                    if (Utility.isNotNull(straddr)) {


                        if(strmobn.length() > 9){
                            if(!(sp2.getSelectedItemPosition() == 0)){
                                if(!(sp1.getSelectedItemPosition() == 0)){
                                    if(boolemail){
                                        if(!(Utility.isNotNull(strhmadd))|| straddr.equals("")){
                                            strhmadd = "NA";
                                        }
                                        if (!(Utility.isNotNull(stremail)) || stremail.equals("")) {
                                            stremail = "NA";
                                        }

strmobn = "234"+strmobn.substring(strmobn.length() - 10);

                                        Intent intent  = new Intent(OpenAccStepTwoActivity.this,OpenAccUpPicActivity.class);



                                        intent.putExtra("fname", strfname);
                                        intent.putExtra("lname", strlname);
                                        intent.putExtra("midname", strmidnm);
                                        intent.putExtra("yob", stryob);
                                        intent.putExtra("gender", strgender);
                                        intent.putExtra("city", strcity);
                                        intent.putExtra("state", strstate);
                                        intent.putExtra("straddr", straddr);
                                        intent.putExtra("email", stremail);
                                        intent.putExtra("hmadd", strhmadd);
                                        intent.putExtra("mobn", strmobn);
                                        intent.putExtra("salut", salut);
                                        intent.putExtra("marstatus", marstatus);




                                        startActivity(intent);

                                      /*
                                        Fragment fragment = new OpenAccUpPic();
                                        fragment.setArguments(bundle);

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        //  String tag = Integer.toString(title);
                                        fragmentTransaction.replace(R.id.container_body, fragment, "Step Three");
                                        fragmentTransaction.addToBackStack("Step Three");
                                        ((FMobActivity) getApplicationContext())
                                                .setActionBarTitle("Step Three");
                                        fragmentTransaction.commit(); */


                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Please enter a valid email value",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Please select a valid Title",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Please select a valid Marital Status",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please enter a valid value for mobile number of proper length",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter a value for Street Name",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a value for mobile Number",
                            Toast.LENGTH_LONG).show();
                }



        }
        if (view.getId() == R.id.tv2) {
           /* Bundle bundle = new Bundle();
            bundle.putString("fname", strfname);
            bundle.putString("lname", strlname);
            bundle.putString("midname", strmidnm);
            bundle.putString("yob", stryob);
            bundle.putString("gender", strgender);
            bundle.putString("city", strcity);
            bundle.putString("state", strstate);
            Fragment  fragment = new OpenAcc();

            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Biller Menu");
            fragmentTransaction.addToBackStack("Biller Menu");
            ((FMobActivity)getApplicationContext())
                    .setActionBarTitle("Biller Menu");
            fragmentTransaction.commit();*/

            finish();
            Intent intent  = new Intent(OpenAccStepTwoActivity.this,OpenAccActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }


        if(view.getId()==  R.id.button4){

        }
        if(view.getId() == R.id.tdispedit){

          /*  Fragment fragment =  new NatWebProd();;
String title = "Bank Info";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            Activity activity123 = getApplicationContext();
            if(activity123 instanceof MainActivity) {
                ((MainActivity)getApplicationContext())
                        .setActionBarTitle(title);
            }
            if(activity123 instanceof SignInActivity) {
                ((SignInActivity) getApplicationContext())
                        .setActionBarTitle(title);
            }*/
        }

        if(view.getId() == R.id.textView3){



        }
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            //	new SendTask().execute();
            registerUser();
            //	RegTest();
            return true;
        } else {

            Toast.makeText(
                    getApplicationContext(),
                    "No Internet Connection. Please check your internet setttings",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public void registerUser(){
        final   String acty = "01261";
        //  final String prodn = sp1.getSelectedItem().toString();

        String mobnumm = mobno.getText().toString().trim();
        final  String idn = idno.getText().toString().trim();
        final  String fname = fnam.getText().toString().trim();
        final  String lname = lnam.getText().toString().trim();
        final  String yb  = yob.getText().toString().trim();

        final String nt = sp2.getSelectedItem().toString();
        final String    mobnum = setMobFormat(mobnumm);
        SecurityLayer.Log("Mobile Number Formatted",mobnum);
        if( Utility.isNotNull(mobnum)){
            if( Utility.isNotNull(idn)){
                if( Utility.isNotNull(fname)){

                    if( Utility.isNotNull(lname)){
                        if( Utility.isNotNull(yb)){
                            if(Utility.checknum(mobnum) == true) {
                                if(Utility.checknum(yb) == true) {
                                    if(yb.length() == 4) {
                                        if(!(mobnum.equals("N"))) {

                                            if( Utility.isValidWord(fname)){
                                                if( Utility.isValidWord(lname)){
                                                    if( Utility.isValidWord(idn)){
                                                        new MaterialDialog.Builder(getApplicationContext())
                                                                .title("Open Account")
                                                                .content("Are you sure you want to Open an Account with these particulars? \n First Name: "+fname+"  \n  Last Name "+ lname+" \n Mobile Number  "+mobnum+" \n Mobile Operator "+nt+" \n ID Number: "+idn+" \n Year Of Birth  "+yb+"  ")
                                                                .positiveText("YES")
                                                                .negativeText("NO")

                                                                .callback(new MaterialDialog.ButtonCallback() {
                                                                    @Override
                                                                    public void onPositive(MaterialDialog dialog) {
                                                                     //   invokeWS(acty, mobnum, idn, fname, lname, yb);
                                                                    }

                                                                    @Override
                                                                    public void onNegative(MaterialDialog dialog) {

                                                                    }
                                                                })
                                                                .show();

                                                    }else{
                                                        Toast.makeText(getApplicationContext(), "Please enter a valid Id Number/Passport", Toast.LENGTH_LONG).show();

                                                    }
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Please enter a valid Last Name", Toast.LENGTH_LONG).show();

                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Please enter a valid First Name", Toast.LENGTH_LONG).show();

                                            }


                                        }else{
                                            Toast.makeText(getApplicationContext(), "Please enter a valid mobile number", Toast.LENGTH_LONG).show();

                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "The Year Of Birth field should only contain numeric characters. Please fill in appropiately", Toast.LENGTH_LONG).show();
                                    }}else{
                                    Toast.makeText(getApplicationContext(), "The Mobile Number field should only contain numeric characters. Please fill in appropiately", Toast.LENGTH_LONG).show();
                                }}else{
                                Toast.makeText(getApplicationContext(), "The Year Of Birth field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();

                            }}else{
                            Toast.makeText(getApplicationContext(), "The Last Name  field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();

                        }}else{
                        Toast.makeText(getApplicationContext(), "The First Name field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();

                    }}else{
                    Toast.makeText(getApplicationContext(), "The ID Number field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();
                }}else{
                Toast.makeText(getApplicationContext(), "The ID No/Passport  field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();


            }}else{
            Toast.makeText(getApplicationContext(), "The Mobile Number field is empty. Please fill in appropiately", Toast.LENGTH_LONG).show();

        }
    }



    public void SetDialog(String msg,String title){
        new MaterialDialog.Builder(getApplicationContext())
                .title(title)
                .content(msg)

                .negativeText("Close")
                .show();
    }

    public String setMobFormat(String mobno){
        String vb = mobno.substring(Math.max(0, mobno.length() - 9));
        SecurityLayer.Log("Logged Number is", vb);
        if(vb.length() == 9 && (vb.substring(0, Math.min(mobno.length(), 1)).equals("7"))){
            return "254"+vb;
        }else{
            return  "N";
        }
    }

}
