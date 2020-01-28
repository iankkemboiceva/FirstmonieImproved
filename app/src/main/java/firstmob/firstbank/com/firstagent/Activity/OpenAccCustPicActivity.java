package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.pixplicity.easyprefs.library.Prefs;


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
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;


import firstmob.firstbank.com.firstagent.contract.MainContract;
import firstmob.firstbank.com.firstagent.contract.OpenAccCustContract;
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog;
import firstmob.firstbank.com.firstagent.network.FetchServerResponse;
import firstmob.firstbank.com.firstagent.presenter.LoginPresenterCompl;
import firstmob.firstbank.com.firstagent.presenter.OpenAccCustPicPresenter;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.FileCompressor;
import firstmob.firstbank.com.firstagent.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class OpenAccCustPicActivity extends BaseActivity implements View.OnClickListener,OpenAccCustContract.ILoginView {
    File finalFile;
    int REQUEST_CAMERA = 3293;
    ProgressDialog pDialog;
    Button sigin,next2;
    TextView gendisp;
    File photoFile = null;

    EditText idno, mobno, fnam, lnam, yob;
    List<String> planetsList = new ArrayList<String>();
    List<String> prodid = new ArrayList<String>();
    ArrayAdapter<String> mArrayAdapter;
    Spinner sp1, sp2, sp5, sp3, sp4;
    Button btn4, next;
    static Hashtable<String, String> data1;
    String paramdata = "";
    ViewDialog prgDialog;
    TextView tnc;
    List<String> mobopname = new ArrayList<String>();
    List<String> mobopid = new ArrayList<String>();

    TextView tvdate;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    boolean uploadpic = false;
    public static final String DATEPICKER_TAG = "datepicker";
    ImageView img;
    String strfname, strlname, strmidnm, stryob, stremail, strhmdd, strmobn, strsalut, strmarst, strcity, strstate, strgender, straddr;
    TextView step2, step1, step3, stt;
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    FileCompressor mCompressor;
    boolean hasSigned = false;
    OpenAccCustContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_cust_pic);



        img = (ImageView) findViewById(R.id.imgview);


        next2 = (Button) findViewById(R.id.buttonnxt2);
        next2.setOnClickListener(this);
        prgDialog = new ViewDialog(this);




        Intent intent = getIntent();
        if (intent != null) {
            strfname = intent.getStringExtra("fname");
            strlname = intent.getStringExtra("lname");
            strmidnm = intent.getStringExtra("midname");
            stryob = intent.getStringExtra("yob");
            stremail = intent.getStringExtra("email");
            strhmdd = intent.getStringExtra("hmadd");
            strmobn = intent.getStringExtra("mobn");
            strsalut = intent.getStringExtra("salut");
            strmarst = intent.getStringExtra("marstatus");
            strcity = intent.getStringExtra("city");
            strstate = intent.getStringExtra("state");
            strgender = intent.getStringExtra("gender");
            straddr = intent.getStringExtra("straddr");



        }






        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
             //   Toast.makeText(OpenAccCustPicActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
                hasSigned = true;
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
                hasSigned = false;
            }
        });

        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                convertSignedImage(signatureBitmap);
            }
        });

        mCompressor = new FileCompressor(this);
        presenter = new OpenAccCustPicPresenter(this,new FetchServerResponse());
    }


/*

    private void invokeAgent(final String params) {

        prgDialog.show();


        String sessid = UUID.randomUUID().toString();






        String endpoint= "otp/generatecustomerotp.action";

        String url = "";
        try {
            url = SecurityLayer.genURLCBC(params,endpoint,getApplicationContext());
            SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("params", params);
            SecurityLayer.Log("refurl", url);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }

        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(url);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 */
/*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getApplicationContext()); *//*

                    obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");


                    if(!(response.body() == null)) {
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                if (respcode.equals("00")) {

                                    SecurityLayer.Log("Response Message", responsemessage);
                            */
/*Bundle bundle = new Bundle();
                            bundle.putString("fname", strfname);
                            bundle.putString("lname", strlname);
                            bundle.putString("midname", strmidnm);
                            bundle.putString("yob", stryob);
                            bundle.putString("email", stremail);
                            bundle.putString("hmadd", strhmdd);
                            bundle.putString("mobn", strmobn);
                            bundle.putString("salut", strsalut);
                            bundle.putString("marstatus", strmarst);
                            bundle.putString("gender", strgender);
                            bundle.putString("city", strcity);
                            bundle.putString("state", strstate);

                            Fragment fragment = new OpenAccOTP();
                            fragment.setArguments(bundle);


                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            //  String tag = Integer.toString(title);
                            fragmentTransaction.replace(R.id.container_body, fragment,"Step Four");
                            fragmentTransaction.addToBackStack("Step Four");
                            ((FMobActivity)getApplicationContext())
                                    .setActionBarTitle("Step Four");
                            fragmentTransaction.commit();*//*


                                    Intent intent = new Intent(OpenAccCustPicActivity.this, OpenAccOTPActivity.class);


                                    intent.putExtra("fname", strfname);
                                    intent.putExtra("lname", strlname);
                                    intent.putExtra("midname", strmidnm);
                                    intent.putExtra("yob", stryob);
                                    intent.putExtra("email", stremail);
                                    intent.putExtra("hmadd", strhmdd);
                                    intent.putExtra("mobn", strmobn);
                                    intent.putExtra("salut", strsalut);
                                    intent.putExtra("marstatus", strmarst);
                                    intent.putExtra("straddr", straddr);
                                    intent.putExtra("gender", strgender);
                                    intent.putExtra("city", strcity);
                                    intent.putExtra("state", strstate);


                                    startActivity(intent);


                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                LogOut();
                            }

                        }else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error processing your request ",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error processing your request ",
                                Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        // SecurityLayer.Log(e.toString());
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                prgDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());

                if(!(getApplicationContext() == null)) {
                    Toast.makeText(
                            getApplicationContext(),
                            "There was an error processing your request",
                            Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());
                   SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                }
                prgDialog.dismiss();
            }
        });

    }

*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                //onCaptureImageResult(data);
//                SecurityLayer.Log("compressor",photoFile.getAbsolutePath());
                try {
                    photoFile = mCompressor.compressToFile(photoFile,"S");
                    Bitmap thumbnail = mCompressor.compressToBitmap(photoFile);
                    onCaptureImageResult(thumbnail,thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
   /* private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "firstmob.firstbank.com.firstagent.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }*/

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
private void convertSignedImage(Bitmap origbit){



    Bitmap thumbnail = origbit;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
    String filename = "signed.jpg";

    final File path = new File(Environment.getExternalStorageDirectory(), "FirstAgent");


    // Make sure the path directory exists.
    if (!path.exists()) {
        // Make it, if it doesn't exit
        path.mkdirs();
        Log.v("was it crated", "created");
    }

        finalFile = new File(Environment.getExternalStorageDirectory(),"FirstAgent/"+filename);

    FileOutputStream fo;
    try {
        finalFile.createNewFile();
        fo = new FileOutputStream(finalFile);
        fo.write(bytes.toByteArray());
        fo.close();
        SecurityLayer.Log("Filename stored", filename);




File newfile  = mCompressor.compressToFile(finalFile,"S");
        SecurityLayer.Log("compressor",finalFile.getAbsolutePath());
        String filePath = newfile.getPath();
        Prefs.putString("CUSTSIGNPATH", filePath);
        uploadpic = true;

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

if(uploadpic) {
presenter.GenCustOTP(strmobn);
}else{
    Toast.makeText(
            getApplicationContext(),
            "There was an error capturing the signature",
            Toast.LENGTH_LONG).show();
}
    //   iv.setImageBitmap(thumbnail);
}
    private void onCaptureImageResult(Bitmap thumbnail,Bitmap origbit) {
        if(!(origbit == null)) {

            if(img != null) {
                img.setImageBitmap(origbit);
            }

            int srcWidth = origbit.getWidth();
            int srcHeight = origbit.getHeight();
            int dstWidth = (int) (srcWidth * 0.8f);
            int dstHeight = (int) (srcHeight * 0.8f);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            String filename = "camerapic.jpg";

            final File path = new File(Environment.getExternalStorageDirectory(), "FirstAgent");


            // Make sure the path directory exists.
            if (!path.exists()) {
                // Make it, if it doesn't exit
                path.mkdirs();
                Log.v("was it crated", "created");
            }
            finalFile = new File(Environment.getExternalStorageDirectory(),"FirstAgent/"+filename);
            FileOutputStream fo;
            try {
                finalFile.createNewFile();
                fo = new FileOutputStream(finalFile);
                fo.write(bytes.toByteArray());
                fo.close();
                SecurityLayer.Log("Filename stored", filename);
                String filePath = finalFile.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                Prefs.putString("CUSTSIGNPATH", filePath);
                uploadpic = true;
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

    }
    @Override
    public void onClick(View view) {

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
            fragmentTransaction.replace(R.id.container_body, fragment, "Biller Menu");
            fragmentTransaction.addToBackStack("Biller Menu");
            ((FMobActivity) getApplicationContext())
                    .setActionBarTitle("Biller Menu");
            fragmentTransaction.commit();*/


            finish();
            Intent intent  = new Intent(OpenAccCustPicActivity.this,OpenAccActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv3) {


            finish();
            Intent intent  = new Intent(OpenAccCustPicActivity.this,OpenAccUpPicActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv) {


            finish();
            Intent intent  = new Intent(OpenAccCustPicActivity.this,OpenAccStepTwoActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if(view.getId() == R.id.buttonnxt){

               /* Fragment  fragment = new OpenAccFullImgPreview();


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //  String tag = Integer.toString(title);
                fragmentTransaction.replace(R.id.container_body, fragment,"Step Four");
                fragmentTransaction.addToBackStack("Step Four");
                ((FMobActivity)getApplicationContext())
                        .setActionBarTitle("Step Four");
                fragmentTransaction.commit();*/

        }
        if(view.getId() == R.id.buttonnxt2){

               /* Fragment  fragment = new OpenAccFullImgPreview();


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //  String tag = Integer.toString(title);
                fragmentTransaction.replace(R.id.container_body, fragment,"Step Four");
                fragmentTransaction.addToBackStack("Step Four");
                ((FMobActivity)getApplicationContext())
                        .setActionBarTitle("Step Four");
                fragmentTransaction.commit();*/
if(hasSigned) {
    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
    Bitmap thumbnail = null;


    convertSignedImage(signatureBitmap);
}else{
    Toast.makeText(
            getApplicationContext(),
            "The customer has to provide a signature to proceed",
            Toast.LENGTH_LONG).show();
}

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
   /* private void cameraIntent()
    {
        String defaultCameraPackage = null;

        List<ApplicationInfo> list = getApplicationContext().getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (int n=0;n<list.size();n++) {
            if((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM)==1)
            {
                SecurityLayer.Log("TAG", "Installed Applications  : " + list.get(n).loadLabel( getApplicationContext().getPackageManager()).toString());
                SecurityLayer.Log("TAG", "package name  : " + list.get(n).packageName);
                if(list.get(n).loadLabel( getApplicationContext().getPackageManager()).toString().equalsIgnoreCase("Camera")) {
                    defaultCameraPackage = list.get(n).packageName;
                    break;
                }
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setPackage(defaultCameraPackage);
        startActivityForResult(intent, REQUEST_CAMERA);
    }*/



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

    public void SetDialog(String msg,String title){
        new MaterialDialog.Builder(this)
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
    protected void onDestroy() {
        // TODO Auto-generated method stub


        super.onDestroy();
    }

    @Override
    public void onfetchResult() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public void showProgress() {
        prgDialog.showDialog();

    }

    @Override
    public void hideProgress() {
        prgDialog.hideDialog();

    }
}
