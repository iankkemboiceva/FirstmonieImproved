package firstmob.firstbank.com.firstagent.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
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
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import firstmob.firstbank.com.firstagent.security.SecurityLayer;
import firstmob.firstbank.com.firstagent.utils.FileCompressor;
import firstmob.firstbank.com.firstagent.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class OpenAccUpPicActivity extends BaseActivity implements View.OnClickListener {
    File finalFile;
    int REQUEST_CAMERA = 3293;
    File photoFile = null;
    Button sigin, next;
    int fcsizes = 0;
    TextView gendisp;

    EditText idno, mobno, fnam, lnam, yob;
    List<String> planetsList = new ArrayList<String>();
    List<String> prodid = new ArrayList<String>();
    ArrayAdapter<String> mArrayAdapter;
    Spinner sp1, sp2, sp5, sp3, sp4;
    Button btn4;
    static Hashtable<String, String> data1;
    String paramdata = "";
    ProgressDialog prgDialog, prgDialog2, prgDialog7;
    TextView tnc;
    List<String> mobopname = new ArrayList<String>();
    List<String> mobopid = new ArrayList<String>();

    TextView accopeningttle;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    boolean uploadpic = false;
    public static final String DATEPICKER_TAG = "datepicker";
    ImageView img;
    String strfname, strlname, strmidnm, stryob, stremail, strhmdd, strmobn, strsalut, strmarst, strcity, strstate, strgender, straddr;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    TextView step2, step1;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    FileCompressor mCompressor;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_up_pic);

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
        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);

        sigin = (Button) findViewById(R.id.button1);
        sigin.setOnClickListener(this);
        next = (Button) findViewById(R.id.buttonnxt);
        next.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.imgview);



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



        mCompressor = new FileCompressor(this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {

                //onCaptureImageResult(data);
               Log.v("compressor",photoFile.getAbsolutePath());
                try {
                    photoFile = mCompressor.compressToFile(photoFile,"P");
                    Bitmap thumbnail = mCompressor.compressToBitmap(photoFile);
                    runFaceContourDetection(thumbnail, thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void dispatchTakePictureIntent() {
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
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }



    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float neww = ((float) width) * ((float) 0.6);
        float newh = ((float) height) * ((float) 0.6);
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



    private void runFaceContourDetection(final Bitmap myBitmap, final Bitmap origbit) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(origbit);
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .build();

        //   mFaceButton.setEnabled(false);
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<FirebaseVisionFace>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionFace> faces) {

                                int facesizes = faces.size();
                                fcsizes = facesizes;
                                Log.v("Faces found", "There are" + Integer.toString(facesizes) + " faces here");

                                if (fcsizes > 0) {
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    myBitmap.compress(Bitmap.CompressFormat.JPEG,90,bytes);

                                    String filename = "facepic.jpg";

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
                                        Log.v("Filename stored", filename);
                                        String filePath = finalFile.getPath();
                                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                                        if (img != null) {
                                            img.setImageBitmap(myBitmap);
                                        }
                                        Prefs.putString("CUSTIMGFILEPATH", filePath);
                                        uploadpic = true;


                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //   iv.setImageBitmap(thumbnail);

                                } else {
                                    img.setImageBitmap(origbit);
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Please ensure you have taken a clear picture of the customer's face"
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception

                                e.printStackTrace();

                                Toast.makeText(
                                        getApplicationContext(),
                                        "There was an error running facial detection on the image.Please retake the photo again"
                                        , Toast.LENGTH_LONG).show();
                            }
                        });

    }

    private void onCaptureImageResult(Intent data) {
        if (!(data == null)) {

            Bitmap origbit = (Bitmap) data.getExtras().get("data");
            int srcWidth = origbit.getWidth();
            int srcHeight = origbit.getHeight();
            int dstWidth = (int) (srcWidth * 0.95f);
            int dstHeight = (int) (srcHeight * 0.95f);
            try {
                Bitmap thumbnail = mCompressor.compressToBitmap(photoFile);
                runFaceContourDetection(thumbnail, origbit);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (img != null) {
                img.setImageBitmap(origbit);
            }
            //  finalizeup(thumbnail);

        }

    }

    private void finalizeup(Bitmap myBitmap) {

        if (img != null) {
            img.setImageBitmap(myBitmap);
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        String filename = System.currentTimeMillis() + ".jpg";

        final File path = new File(Environment.getExternalStorageDirectory(), "FirstAgent");

        // Make sure the path directory exists.
        if(!path.exists())

        {
            // Make it, if it doesn't exit
            path.mkdirs();
            Log.v("was it crated", "created");
        }

        finalFile = new File(Environment.getExternalStorageDirectory(), "FirstAgent/"+filename);
        FileOutputStream fo;
        try

        {
            finalFile.createNewFile();
            fo = new FileOutputStream(finalFile);
            fo.write(bytes.toByteArray());
            fo.close();
            Log.v("Filename stored", filename);
            String filePath = finalFile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            Prefs.putString("CUSTIMGFILEPATH", filePath);
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
        } catch(
                FileNotFoundException e)

        {
            e.printStackTrace();
        } catch(
                IOException e)

        {
            e.printStackTrace();
        }

        //   iv.setImageBitmap(thumbnail);
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
        if (view.getId() == R.id.button1) {


            fcsizes = 0;
            uploadpic = false;

            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now
                                cameraIntent();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // permission is denied permenantly, navigate user to app settings
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
          /*  boolean camresult= checkCameraPermission(this);
            if(camresult) {
                boolean result=checkPermission(OpenAccUpPicActivity.this);
                if(result) {
                    boolean readresult= Utility.checkWritePermission(OpenAccUpPicActivity.this);
                    if(readresult) {
                        //  dispatchTakePictureIntent();
                        cameraIntent();
                    }
                }
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "Please enable camera permission",
                        Toast.LENGTH_LONG).show();
            }*/
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
            Fragment fragment = new OpenAcc();

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
            Intent intent  = new Intent(OpenAccUpPicActivity.this,OpenAccActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        if (view.getId() == R.id.tv) {


            finish();
            Intent intent  = new Intent(OpenAccUpPicActivity.this,OpenAccStepTwoActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }



        if(view.getId()==  R.id.button4){

        }
        if(view.getId() == R.id.buttonnxt){
            if(uploadpic) {

                Intent intent  = new Intent(OpenAccUpPicActivity.this,OpenAccCustPicActivity.class);





                intent.putExtra("fname", strfname);
                intent.putExtra("lname", strlname);
                intent.putExtra("midname", strmidnm);
                intent.putExtra("yob", stryob);
                intent.putExtra("email", stremail);
                intent.putExtra("hmadd", strhmdd);
                intent.putExtra("mobn", strmobn);
                intent.putExtra("salut", strsalut);
                intent.putExtra("marstatus", strmarst);

                intent.putExtra("gender", strgender);
                intent.putExtra("city", strcity);
                intent.putExtra("state", strstate);

                intent.putExtra("straddr", straddr);

                startActivity(intent);

            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "Please upload a valid customer passport with their face in focus to proceed",
                        Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId() == R.id.textView3){

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  boolean checkCameraPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Permission to use camera is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            boolean camresult= checkCameraPermission(context);
                            if(camresult) {
                                boolean result=checkPermission(context);
                                if(result) {
                                    //  dispatchTakePictureIntent();
                                    cameraIntent();
                                }
                            }
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            boolean camresult= checkCameraPermission(context);
                            if(camresult) {
                                boolean result=checkPermission(context);
                                if(result) {
                                    //  dispatchTakePictureIntent();
                                    cameraIntent();
                                }
                            }
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    private void cameraIntent()
    {


        dispatchTakePictureIntent();
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                SecurityLayer.Log(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    public File createImageFile() throws IOException {
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



    public void ClearOpenAcc(){
        //   sp1.setSelection(0);

        mobno.setText(" ");
        idno.setText(" ");
        fnam.setText(" ");
        lnam.setText(" ");
        //   yob.setText(" ");
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

}
