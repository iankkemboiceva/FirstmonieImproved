package firstmob.firstbank.com.firstagent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pixplicity.easyprefs.library.Prefs;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.BitmapCompat;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants;
import firstmob.firstbank.com.firstagent.network.ApiInterface;
import firstmob.firstbank.com.firstagent.network.RetrofitInstance;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.security.TLSSocketFactory;
import firstmob.firstbank.com.firstagent.utils.FileCompressor;
import firstmob.firstbank.com.firstagent.utils.Utility;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class OpenAccOTPActivity extends BaseActivity implements View.OnClickListener {
    File finalFile;
    int REQUEST_CAMERA =3293;

    Button sigin;
    String refnumber;
    TextView accopeningttle;

    EditText otp,pin;
    List<String> planetsList = new ArrayList<String>();
    List<String> prodid = new ArrayList<String>();
    ArrayAdapter<String> mArrayAdapter;
    Spinner sp1,sp2,sp5,sp3,sp4;
    Button btn4,next;
    static Hashtable<String, String> data1;
    String paramdata = "";
    ProgressDialog prgDialog,prgDialog7;
    TextView tnc;
    List<String> mobopname  = new ArrayList<String>();
    List<String> mobopid  = new ArrayList<String>();

    TextView tvdate;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    public static final String DATEPICKER_TAG = "datepicker";
    ImageView img;
    String finparams = null;
    String upurl = Constants.IMG_UPURL;
    String strfname,strlname,strmidnm,stryob,stremail,strhmdd,strmobn,strsalut,strmarst,strgender,strcity,strstate,straddr;
    TextView step2, step1, step3, step4;
    FileCompressor mCompressor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_otp);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.nbkyellow)));
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        accopeningttle=findViewById(R.id.titlepg);
        accopeningttle.setText("Account Opening");

        sigin = (Button) findViewById(R.id.button1);
        sigin.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.imgview);
        next = (Button) findViewById(R.id.buttonnxt);
        otp = (EditText) findViewById(R.id.agentphon);
        pin = (EditText) findViewById(R.id.agentpin);
        //    next.setOnClickListener(this);
        sigin = (Button) findViewById(R.id.button1);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading ....");
        // Set Cancelable as False

        prgDialog.setCancelable(false);

        String usid = Utility.gettUtilUserId(getApplicationContext());
        upurl = upurl+usid;


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
        String getsign = Prefs.getString("CUSTSIGNPATH","NA");
        Bitmap bitmapsign = BitmapFactory.decodeFile(getsign);

        String getcust = Prefs.getString("CUSTIMGFILEPATH","NA");
        Bitmap bitmapcust = BitmapFactory.decodeFile(getcust);

        Bitmap [] bmap = new Bitmap[2];
        bmap[0] = bitmapcust;
        bmap[1] = bitmapsign;



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




    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = new File(Environment.getExternalStorageDirectory(),"Firstagent");
        // Create imageDir
        File mypath=new File(Environment.getExternalStorageDirectory(), "/FirstAgent/profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }



    public void uploadImage(File file){

     try{
        OkHttpClient okHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

         // Create a trust manager that does not validate certificate chains
         final TrustManager[] trustAllCerts = new TrustManager[]{
                 new X509TrustManager() {
                     @Override
                     public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                     }

                     @Override
                     public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                     }

                     @Override
                     public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                         return new java.security.cert.X509Certificate[]{};
                     }
                 }
         };

         // Install the all-trusting trust manager
         final SSLContext sslContext = SSLContext.getInstance("SSL");
         sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
         // Create an ssl socket factory with our all-trusting manager

         SSLSocketFactory sslSocketFactory = null;
         try {
             sslSocketFactory = new TLSSocketFactory();

         } catch (KeyManagementException ignored) {
             ignored.printStackTrace();
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         }
        SSLContext sslcontext = null;

        try {
            sslcontext = SSLContext.getInstance("TLSv1.2");
            sslcontext.init(null, null, null);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
         builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
         builder.hostnameVerifier(new HostnameVerifier() {
             @Override
             public boolean verify(String hostname, SSLSession session) {
                 return true;
             }
         });

         String edpin = pin.getText().toString();


         String  encrypted = Utility.b64_sha256(edpin);
         SecurityLayer.Log("secret",encrypted);

        okHttpClient = builder.connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS).build();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "tmp_photo_" + System.currentTimeMillis(),
                        RequestBody.create(MediaType.parse("image/jpg"), file))

                .build();
            Request request = new Request.Builder()
         .url(upurl).post(formBody)

                 .header("secret", encrypted)


                 .build();
            //  Response<String> response = null;
            okhttp3.Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();


                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                else{
                    refnumber = response.body().string();
                    SecurityLayer.Log("response body..:",response.body().toString());

                    SecurityLayer.Log("response..:",refnumber);

                    SecurityLayer.Log("Success upload","Success Upload");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
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
            img.setImageBitmap(bitmap);
            Prefs.putString("CUSTSIGNPATH",filePath);
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


        if(view.getId() == R.id.button1){


            String edotp = otp.getText().toString();
            String edpin = pin.getText().toString();



            if (Utility.isNotNull(edotp)) {
                if (Utility.isNotNull(edpin)) {

            String getsign = Prefs.getString("CUSTSIGNPATH","NA");
            Bitmap bitmapsign = BitmapFactory.decodeFile(getsign);

            String getcust = Prefs.getString("CUSTIMGFILEPATH","NA");
            Bitmap bitmapcust = BitmapFactory.decodeFile(getcust);

            Bitmap [] bmap = new Bitmap[2];
            bmap[0] = bitmapcust;
            bmap[1] = bitmapsign;
            Bitmap res = mergeBitmap(bitmapcust,bitmapsign);
         //   res = mCompressor.compressToBitmap(res);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
        res.compress(Bitmap.CompressFormat.JPEG, 5, out);
            Bitmap decoded = res;
            int kb = BitmapCompat.getAllocationByteCount(res)/1024;



            Log.e("Original   dimensions", res.getWidth()+" "+res.getHeight());
            Log.e("Compressed dimensions", decoded.getWidth()+" "+decoded.getHeight());
            saveToInternalStorage(decoded);
            String filename = "accopen.jpg";
            String encimage = encodeTobase64(decoded);
            writeToFile(encimage);
            final File path =
                    Environment.getExternalStoragePublicDirectory
                            (
                                    //Enviro.cnment.DIRECTORY_PICTURES
                                    "FirstAgent/"
                            );

            // Make sure the path directory exists.
            if(!path.exists())
            {
                // Make it, if it doesn't exit
                path.mkdirs();
            }

            finalFile = new File(Environment.getExternalStorageDirectory(), "/FirstAgent/base64fl.txt");


/*String edotp = otp.getText().toString();
                String edpin = pin.getText().toString();
String params = "1/"+usid+"/"+agentid+"/"+mobnoo+"/"+strsalut+"/"+strfname+"/"+strlname+"/"+strmidnm+"/"+strmarst+"/"+stryob+"/"+stremail+"/"+strgender+"/"+strstate+"/"+strcity+"/"+strhmdd+"/"+strmobn+"/N/"+edotp+"/"+edpin;
   */            // {channel}/{userId}/{merchantId}/{mobileNumber}/{salutation}/{firstName}/{lastName}/{midName}/{maritalStatus}/{dob}/{email}/{gender}/{state}/{city}/{address}/{phone}/{mandateCard}/{otp}/{pin}
            //   invokeAccOTP(params);
            //  uploadImage(file);

           new AsyncUplImg().execute("");

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid value for PIN", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please enter a valid value for OTP", Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId()==  R.id.button4){

        }
        if (view.getId() == R.id.tv2) {

         /*   Bundle bundle = new Bundle();
            bundle.putString("fname", strfname);
            bundle.putString("lname", strlname);
            bundle.putString("midname", strmidnm);
            bundle.putString("yob", stryob);
            bundle.putString("gender", strgender);
            bundle.putString("city", strcity);
            bundle.putString("state", strstate);
            Fragment fragment = new OpenAcc();

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
            Intent intent  = new Intent(OpenAccOTPActivity.this,OpenAccActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv3) {
           /* Bundle bundle = new Bundle();
            bundle.putString("fname", strfname);
            bundle.putString("lname", strlname);
            bundle.putString("midname", strmidnm);
            bundle.putString("yob", stry
            bundle.putString("gender", strgender);
            bundle.putString("city", strcity);
            bundle.putString("state", strstate);
            bundle.putString("email", stremail);
            bundle.putString("hmadd", strhmdd);
            bundle.putString("mobn", strmobn);
            bundle.putString("salut", strsalut);
            bundle.putString("marstatus", strmarst);
            Fragment fragment = new OpenAccUpPic();
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
            Intent intent  = new Intent(OpenAccOTPActivity.this,OpenAccUpPicActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv) {
            /*Bundle bundle = new Bundle();
            bundle.putString("fname", strfname);
            bundle.putString("lname", strlname);
            bundle.putString("midname", strmidnm);
            bundle.putString("gender", strgender);
            bundle.putString("city", strcity);
            bundle.putString("state", strstate);
            bundle.putString("yob", stryob);
            bundle.putString("email", stremail);
            bundle.putString("hmadd", strhmdd);
            bundle.putString("mobn", strmobn);
            bundle.putString("salut", strsalut);
            bundle.putString("marstatus", strmarst);
            Fragment  fragment = new OpenAccStepTwo();
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
            Intent intent  = new Intent(OpenAccOTPActivity.this,OpenAccStepTwoActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv4) {
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
            Fragment fragment = new OpenAccCustPic();
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
            Intent intent  = new Intent(OpenAccOTPActivity.this,OpenAccCustPicActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    class AsyncUplImg extends AsyncTask<String, String, String> {
        Bitmap bmp = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {


            uploadImage(finalFile);
            return "34";
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (!(getApplicationContext() == null)) {
                prgDialog.dismiss();
            }


            String usid = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA");
            String agentid = Prefs.getString(SharedPrefConstants.AGENTID,"NA");
            String mobnoo = Prefs.getString(SharedPrefConstants.AGMOB,"NA");
            String edotp = otp.getText().toString();
            String edpin = pin.getText().toString();


            String  encrypted = Utility.b64_sha256(edpin);

            finparams = "1/" + usid + "/" + agentid + "/" + mobnoo + "/" + strsalut + "/" + strfname + "/" + strlname + "/" + strmidnm + "/" + strmarst + "/" + stryob + "/" + stremail + "/" + strgender + "/" + strstate + "/" + strcity + "/" + strhmdd + "/" + strmobn + "/" + refnumber + "/" + edotp + "/" + encrypted+"/"+straddr;

          //  finparams = "1/" + usid + "/" + agentid + "/" + mobnoo + "/" + strsalut + "/" + strfname + "/" + strlname + "/" + strmidnm + "/" + strmarst + "/" + stryob + "/" + stremail + "/" + strgender + "/" + strstate + "/" + strcity + "/" + strhmdd + "/" + strmobn + "/" + refnumber + "/" + edotp + "/" + encrypted;


           String bvnparams = "1/" + usid + "/" + strsalut + "/" + strfname + "/" + strlname + "/" + strmidnm + "/" + strmarst + "/" + stryob + "/" + stremail + "/" + strgender + "/" + strstate + "/" + strcity + "/" + straddr + "/" + strmobn + "/" + refnumber + "/" + strhmdd + "/" + edotp + "/" + encrypted;
         //   {channel}/{userId}/{salutation}/{firstName}/{lastName}/{midName}/{maritalStatus}/{dob}/{email}/{gender}/{state}/{city}/{address}/{phone}/{mandateCard}/{bvn}/{otp}/{pin}

            //    1/112128164/Chief/Ranae/Benjamin/L/UNMARR/19931003/Michaellhenderson@armyspy.com/M/05/NA/2922 Rocky Road/08013952719/424655476196982/08013952719/12344/6a697a7579387463656a426e6b714d515335736b6e78626a625844614874417365366c4961516f4947624d


            Toast.makeText(getApplicationContext(),refnumber,Toast.LENGTH_LONG).show();

            if (!((refnumber == null))) {
                if (!(refnumber.equals(""))) {
if(Prefs.getString("ISBVN","NA").equals("Y")) {

 //   BVNOpenAcc(bvnparams);
    BVNAccOpenMicro(edotp,encrypted);
}else{
    AccOpenMicro(edotp,encrypted);
}
                }/**/
                else{
                    Toast.makeText(getApplicationContext(),"There was an error uploading the image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"There was an error uploading the image",Toast.LENGTH_LONG).show();
            }

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

    private Bitmap mergeMultiple(Bitmap[] parts){

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {

            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }
        return result;
    }


    public Bitmap mergeBitmap(Bitmap fr, Bitmap sc)
    {

        Bitmap comboBitmap;

        int width, height;

        width = fr.getWidth() + sc.getWidth();
        height = fr.getHeight();

        comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(comboBitmap);


        comboImage.drawBitmap(fr, 0f, 0f, null);
        comboImage.drawBitmap(sc, fr.getWidth(), 0f , null);
        return comboBitmap;

    }



    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public   String getImageBase64() {
        String getsign = Prefs.getString("CUSTSIGNPATH","NA");
        Bitmap bitmapsign = BitmapFactory.decodeFile(getsign);

        String getcust = Prefs.getString("CUSTIMGFILEPATH","NA");
        Bitmap bitmapcust = BitmapFactory.decodeFile(getcust);

        Bitmap[] bmap = new Bitmap[2];
        bmap[0] = bitmapcust;
        bmap[1] = bitmapsign;
        Bitmap res = mergeBitmap(bitmapcust,bitmapsign);
        String encimage = encodeTobase64(res);
        return encimage;
    }

    public void writeToFile(String data)
    {
        // Get the directory for the user's public pictures directory.
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                //Environment.DIRECTORY_PICTURES
                                "FirstAgent/"
                        );

        // Make sure the path directory exists.
        if(!path.exists())
        {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(Environment.getExternalStorageDirectory(), "/FirstAgent/base64fl.txt");


        // Save your stream, don't forget to flush() it before closing it.

        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            SecurityLayer.Log("Exception", "File write failed: " + e.toString());
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

        if(prgDialog!=null && prgDialog.isShowing()){

            prgDialog.dismiss();
        }
        super.onDestroy();
    }

    public  void LogOut(){
      //  session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }



    private void BVNAccOpenMicro(String otpp,String pin) {
        prgDialog.show();



        String usid = Prefs.getString(SharedPrefConstants.KEY_USERID,"NA");


        ApiInterface apiService =
                RetrofitInstance.getClient().create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();

            paramObject.put("channel", "1");
            paramObject.put("salutation", strsalut);
            paramObject.put("firstName", strfname);
            paramObject.put("lastName", strlname);
            paramObject.put("midName", strmidnm);
            paramObject.put("maritalStatus", strmarst);
            paramObject.put("dob", stryob);
            paramObject.put("gender", strgender);
            paramObject.put("state", strstate);
            paramObject.put("city", strcity);
            paramObject.put("address", straddr);
            paramObject.put("phone", strmobn);
            paramObject.put("mandateCard", refnumber);
            paramObject.put("userId", usid);
            paramObject.put("pin", pin);
            paramObject.put("otp", otpp);
            paramObject.put("bvn", strhmdd);
            paramObject.put("street", straddr);


            SecurityLayer.Log("plain params",paramObject.toString());
            String data = SecurityLayer.encryptdata(paramObject.toString(),getApplicationContext());
            String hash = SecurityLayer.gethasheddata(paramObject);
            String appid = Utility.getNewAppID(getApplicationContext());

            JSONObject finalparam = new JSONObject();
            finalparam.put("data", data);
            finalparam.put("hash", hash);
            finalparam.put("appId", appid);







            Call<String> call = apiService.bvnaccopen(finalparam.toString());




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


                        JSONObject datas = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                //LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    String acno = "";
                                    if(!(datas == null)){
                                        acno = datas.optString("accountNumber")  ;
                                    }

                                    SecurityLayer.Log("Response Message", responsemessage);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Account Opening request has been successfully received ",
                                            Toast.LENGTH_LONG).show();

                                  /*  finish();
                                    Intent i = new Intent(OpenAccOTPActivity.this, FinalConfAccountOpening.class);
                                    i.putExtra("accountno", acno);
                                    startActivity(i);*/


                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        responsemessage,
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
                        if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                            prgDialog.dismiss();
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


                    if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                        prgDialog.dismiss();
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



    private void AccOpenMicro(String otpp,String pin) {
        prgDialog.show();

        String usid = Utility.gettUtilUserId(getApplicationContext());

        ApiInterface apiService =
                RetrofitInstance.getClient().create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();
            //  String bvnparams = "1/" + usid + "/" + strsalut + "/" + strfname + "/" + strlname + "/" + strmidnm + "/" + strmarst + "/" + stryob + "/" + stremail + "/" + strgender + "/" + strstate + "/" + strcity + "/" + straddr + "/" + strmobn + "/" + refnumber + "/" + strhmdd + "/" + edotp + "/" + encrypted;


            paramObject.put("channel", "1");
            paramObject.put("salutation", strsalut);
            paramObject.put("firstName", strfname);
            paramObject.put("lastName", strlname);
            paramObject.put("midName", strmidnm);
            paramObject.put("maritalStatus", strmarst);
            paramObject.put("dob", stryob);
            paramObject.put("gender", strgender);
            paramObject.put("state", strstate);
            paramObject.put("city", strcity);
            paramObject.put("address", strhmdd);
            paramObject.put("phone", strmobn);
            paramObject.put("mandateCard", refnumber);
            paramObject.put("userId", usid);
            paramObject.put("pin", pin);
            paramObject.put("otp", otpp);
            paramObject.put("bvn", "");
            paramObject.put("street", straddr);

            SecurityLayer.Log("plain params",paramObject.toString());

            String data = SecurityLayer.encryptdata(paramObject.toString(),getApplicationContext());
            String hash = SecurityLayer.gethasheddata(paramObject);
            String appid = Utility.getNewAppID(getApplicationContext());

            JSONObject finalparam = new JSONObject();
            finalparam.put("data", data);
            finalparam.put("hash", hash);
            finalparam.put("appId", appid);









            Call<String> call = apiService.nonbvnaccopen(finalparam.toString());




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


                        JSONObject datas = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                //LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    String acno = "";
                                    if(!(datas == null)){
                                        acno = datas.optString("accountNumber")  ;
                                    }

                                    SecurityLayer.Log("Response Message", responsemessage);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Account Opening request has been successfully received ",
                                            Toast.LENGTH_LONG).show();

                                    /*finish();
                                    Intent i = new Intent(OpenAccOTPActivity.this, FinalConfAccountOpening.class);
                                    i.putExtra("accountno", acno);
                                    startActivity(i);*/


                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {

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
                        if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                            prgDialog.dismiss();
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


                    if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                        prgDialog.dismiss();
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
