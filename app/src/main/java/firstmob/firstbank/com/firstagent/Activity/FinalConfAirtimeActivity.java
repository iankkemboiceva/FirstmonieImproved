package firstmob.firstbank.com.firstagent.Activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import firstmob.firstbank.com.firstagent.Activity.FMobActivity;
import firstmob.firstbank.com.firstagent.Activity.R;
import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.utils.Utility;
import firstmob.firstbank.com.firstagent.utils.UtilsPhoto;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinalConfAirtimeActivity extends AppCompatActivity implements View.OnClickListener {

    @Inject
    Utility utility;

    public FinalConfAirtimeActivity() {
        ApplicationClass.getMyComponent().inject(this);
    }

    Button btnsub;
    TextView reccustid, recamo, rectelco, recfee, rectrref, recagcmn, txtrfcd, recdatetimee;
    String txtcustid, amou, narra, ednamee, ednumbb, serviceid, billid, strfee, strtref, stragcms;
    ProgressDialog prgDialog2;
    String telcoop;
    EditText amon, edacc;


    Button shareImage, repissue;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    String txtrfc;
    volatile boolean stopWorker;

    // private BroadcastReceiver mUsbReceiver;
    private static final String ACTION_USB_PERMISSION = "firstmob.firstbank.com.firstagent.USB_PERMISSION";
    private static Boolean forceCLaim = true;
    RelativeLayout rlagfee, rlaccom;
    Layout_to_Image layout_to_image;
    LinearLayout relativeLayout;
    Bitmap bitmap;
    RelativeLayout rlsave, rlshare;
    TextView edagname, edattid, edagid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_conf_airtime);
        reccustid = (TextView) findViewById(R.id.textViewnb2);
        txtrfcd = (TextView) findViewById(R.id.txtrfcd);
        recdatetimee = (TextView) findViewById(R.id.textfinaldatet);
        recamo = (TextView) findViewById(R.id.textViewrrs);
        rectelco = (TextView) findViewById(R.id.textViewrr);

        recagcmn = (TextView) findViewById(R.id.txtaccom);
        recfee = (TextView) findViewById(R.id.txtfee);
        rectrref = (TextView) findViewById(R.id.txref);
        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
        rlagfee = (RelativeLayout) findViewById(R.id.rlagfee);
        rlaccom = (RelativeLayout) findViewById(R.id.rlaccom);


        btnsub = (Button) findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            txtcustid = intent.getStringExtra("mobno");
            amou = intent.getStringExtra("amou");
            telcoop = intent.getStringExtra("telcoop");

            String txtamou = Utility.returnNumberFormat(amou);
            if (txtamou.equals("0.00")) {
                txtamou = amou;
            }
            billid = intent.getStringExtra("billid");
            serviceid = intent.getStringExtra("serviceid");
            strfee = intent.getStringExtra("fee");
            strtref = intent.getStringExtra("tref");
            stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"));
            String txtrfc = intent.getStringExtra("refcode");
            txtrfcd.setText(strtref);
            String redatetim = intent.getStringExtra("datetime");
            recdatetimee.setText(Utility.changeDate(redatetim));
            reccustid.setText(txtcustid);
            recfee.setText(Constants.KEY_NAIRA + strfee);
            rectrref.setText(strtref);
            recamo.setText(Constants.KEY_NAIRA + txtamou);
            rectelco.setText(telcoop);
            recagcmn.setText(Constants.KEY_NAIRA + stragcms);
        }

        shareImage = (Button) findViewById(R.id.share_image);
        shareImage.setOnClickListener(this);

        repissue = (Button) findViewById(R.id.reportiss);
        repissue.setOnClickListener(this);
        rlsave = (RelativeLayout) findViewById(R.id.rlsave);
        rlshare = (RelativeLayout) findViewById(R.id.rlshare);

        rlsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlaccom.setVisibility(View.GONE);
                rlagfee.setVisibility(View.GONE);

                layout_to_image = new Layout_to_Image(getApplicationContext(), relativeLayout);

                //now call the main working function ;) and hold the returned image in bitmap

                bitmap = layout_to_image.convert_layout();

                String filename = "ShareRec" + System.currentTimeMillis() + ".jpg";
                if (Utility.checkPermission(FinalConfAirtimeActivity.this)) {
                    saveImage(bitmap, filename);
                    Toast.makeText(
                            getApplicationContext(),
                            "Receipt downloaded successfully to gallery",
                            Toast.LENGTH_LONG).show();
                }

                rlaccom.setVisibility(View.VISIBLE);
                rlagfee.setVisibility(View.VISIBLE);
            }
        });

        rlshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlaccom.setVisibility(View.GONE);
                rlagfee.setVisibility(View.GONE);
                layout_to_image = new Layout_to_Image(getApplicationContext(), relativeLayout);

                //now call the main working function ;) and hold the returned image in bitmap

                bitmap = layout_to_image.convert_layout();
                if (Utility.checkWriteStoragePermission()) {
                    shareImage(getImageUri(getApplicationContext(), bitmap));
                }

                rlaccom.setVisibility(View.VISIBLE);
                rlagfee.setVisibility(View.VISIBLE);
            }
        });


        edagid = (TextView) findViewById(R.id.txtagid);
        edagname = (TextView) findViewById(R.id.txtgname);
        edattid = (TextView) findViewById(R.id.txtattid);

        String stragid = Utility.gettUtilAgentId(getApplicationContext());
        String stragname = Utility.gettUtilCustname(getApplicationContext());
        String strattid = Utility.gettUtilUserId(getApplicationContext());

        edagid.setText(stragid);
        edagname.setText(stragname);
        edattid.setText(strattid);
        relativeLayout = (LinearLayout) findViewById(R.id.receipt);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            finish();


            Intent i = new Intent(getApplicationContext(), FMobActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            startActivity(i);
        }

        if(view.getId() == R.id.share_image){




            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SELECT AN OPTION");

            // add a list
            String[] animals = {"Save to Gallery", "Share Receipt"};
            builder.setItems(animals, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // horse

                            rlaccom.setVisibility(View.GONE);
                            rlagfee.setVisibility(View.GONE);

                            layout_to_image=new Layout_to_Image(getApplicationContext(),relativeLayout);

                            //now call the main working function ;) and hold the returned image in bitmap

                            bitmap=layout_to_image.convert_layout();

                            String filename = "ShareRec"+System.currentTimeMillis()+".jpg";
                            if(Utility.checkPermission(FinalConfAirtimeActivity.this)) {
                                saveImage(bitmap, filename);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Receipt downloaded successfully to gallery",
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 1: // cow
                            rlaccom.setVisibility(View.GONE);
                            rlagfee.setVisibility(View.GONE);
                            layout_to_image=new Layout_to_Image(getApplicationContext(),relativeLayout);

                            //now call the main working function ;) and hold the returned image in bitmap

                            bitmap=layout_to_image.convert_layout();
                            if(Utility.checkPermission(FinalConfAirtimeActivity.this)) {
                                shareImage(getImageUri(getApplicationContext(), bitmap));
                            }

                            rlaccom.setVisibility(View.VISIBLE);
                            rlagfee.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(view.getId() == R.id.reportiss) {
            try {
                String userid = Utility.gettUtilUserId(getApplicationContext());
                sendData("   \n \n    AIRTIME  \nUSERID: " + userid + " \nTelco: " + telcoop + " \nRef Number:" + txtrfc + " \nMobile Number:" + txtcustid + " \nAmount:" + amou + " Naira\n Sender Name:" + ednamee + " \n Fee:" + strfee + " Naira \n \n \n \n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {


        addJpgSignatureToGallery(finalBitmap);


        rlaccom.setVisibility(View.VISIBLE);
        rlagfee.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }
    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }


    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // tries to open a connection to the bluetooth printer device


    // this will send text data to be printed by the bluetooth printer
    void sendData(String msg) throws IOException {
        try {
            int img = R.drawable.monochrome;
            try {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                        img);
                bmp =  getResizedBitmap(bmp, 120, 360);
                if(bmp!=null){
                    byte[] command = UtilsPhoto.decodeBitmap(bmp);
                    //  mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream.write(command);

                    mmOutputStream.write(msg.getBytes());
                }else{
                    Log.e("Print Photo error", "the file isn't exists");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }
            //  mmOutputStream.write(msg.getBytes());
            //  sendData(bytes);
            //   mmOutputStream.write(bytes);


            //  ImageIO.write(mmOutputStream, "PNG", myNewPNGFile);


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            closeBT();
        } catch (IOException ex) {
            ex.printStackTrace();
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



    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }
    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();


    }
    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {


            String flname = String.format("ShareRec_%d", System.currentTimeMillis());
            File photo = new File(getAlbumStorageDir("FirstAgent"), String.format("ShareR%d.jpg", System.currentTimeMillis()));
            File filename = photo;
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        FinalConfAirtimeActivity.this.sendBroadcast(mediaScanIntent);
    }
}
