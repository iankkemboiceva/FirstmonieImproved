package firstmob.firstbank.com.firstagent.Activity

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.usb.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.vipul.hp_hp.library.Layout_to_Image
import firstmob.firstbank.com.firstagent.constants.Constants
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.UtilsPhoto
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.*
import java.util.*
import javax.inject.Inject

class FinalConfWithdrawActivity : AppCompatActivity(),View.OnClickListener {
    @Inject
    internal lateinit var ul: Utility

    init {
        ApplicationClass.getMyComponent().inject(this)
    }
    internal var recacno: TextView? =null
    internal var recname:TextView? =null
    internal var recamo:TextView? =null
    internal var recnarr:TextView? =null
    internal var recsendnum:TextView? =null
    internal var recsendnam:TextView? =null
    internal var recfee:TextView? =null
    internal var rectrref:TextView? =null
    internal var recagcmn:TextView? =null
    internal var txtrfcd:TextView? =null
    internal var recdatetimee:TextView? =null
    internal var btnsub: Button? =null
    internal var recanno: String? =null
    internal var amou:String? =null
    internal var narra:String? = null
    internal var ednamee:String? = null
    internal var ednumbb:String? = null
    internal var txtname:String? =null
    internal var strfee:String? =null
    internal var txref:String? =null
    internal var stragcms:String? =null
    internal var prgDialog: ProgressDialog? = null
    internal var prgDialog2:ProgressDialog? =null

    internal var rlsave: RelativeLayout? =null
    internal var rlshare:RelativeLayout? =null


    internal var shareImage: Button? =null
    internal var repissue:Button? = null
    // android built in classes for bluetooth operations
    internal var mBluetoothAdapter: BluetoothAdapter? = null
    internal var mmSocket: BluetoothSocket? =null
    internal var mmDevice: BluetoothDevice? =null

    // needed for communication to bluetooth device / network
    internal var mmOutputStream: OutputStream? =null
    internal var mmInputStream: InputStream? =null
    internal var workerThread: Thread? =null
    internal var txtrfc: String? = null
    internal var readBuffer: ByteArray? =null
    internal var readBufferPosition: Int = 0
    @Volatile
    internal var stopWorker: Boolean = false

    internal var rlagfee: RelativeLayout? =null
    internal var rlaccom:RelativeLayout? =null


    private val mUsbManager: UsbManager? = null
    private val mDevice: UsbDevice? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mInterface: UsbInterface? = null
    private var mEndPoint: UsbEndpoint? = null
    private val mPermissionIntent: PendingIntent? = null
    private val mContext: Context? = null
    // private BroadcastReceiver mUsbReceiver;
    private val ACTION_USB_PERMISSION = "firstmob.firstbank.com.firstagent.USB_PERMISSION"
    private val forceCLaim = true

    internal var mDeviceList: HashMap<String, UsbDevice>? = null
    internal var mDeviceIterator: Iterator<UsbDevice>? = null
    internal var layout_to_image: Layout_to_Image? =null
    internal var relativeLayout: LinearLayout? =null
    internal var bitmap: Bitmap? =null

    internal var edagname: TextView? =null
    internal var edattid:TextView? =null
    internal var edagid:TextView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_conf_withdraw)
        recacno = findViewById(R.id.textViewnb2) as TextView
        recname = findViewById(R.id.textViewcvv) as TextView
        txtrfcd = findViewById(R.id.txtrfcd) as TextView
        recamo = findViewById(R.id.textViewrrs) as TextView
        recnarr = findViewById(R.id.textViewrr) as TextView
        rectrref = findViewById(R.id.tranref) as TextView
        recfee = findViewById(R.id.txtfee) as TextView
        recsendnam = findViewById(R.id.sendnammm) as TextView
        recsendnum = findViewById(R.id.sendno) as TextView
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading....")
        prgDialog2!!.setCancelable(false)

        btnsub = findViewById(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)
        recagcmn = findViewById(R.id.txtaccom) as TextView


        rlagfee = findViewById(R.id.rlagfee) as RelativeLayout
        rlaccom = findViewById(R.id.rlaccom) as RelativeLayout

        recdatetimee = findViewById(R.id.textfinaldatet) as TextView

        val intent = intent
        if (intent != null) {

            recanno = intent.getStringExtra("recanno")
            amou = intent.getStringExtra("amou")
            strfee = intent.getStringExtra("fee")
            txtname = intent.getStringExtra("txtname")
            txref = intent.getStringExtra("txref")
            val txtrfc = intent.getStringExtra("refcode")
            txtrfcd!!.setText(txtrfc)
            stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"))
            //   otp = bundle.getString("otp");
            recacno!!.setText(recanno)
            recname!!.setText(txtname)
            recfee!!.setText(Constants.KEY_NAIRA + strfee)
            rectrref!!.setText(txref)
            recamo!!.setText(Constants.KEY_NAIRA + amou)
            recagcmn!!.setText(stragcms)


            val redatetim = intent.getStringExtra("datetime")
            recdatetimee!!.setText(Utility.changeDate(redatetim))


        }
        shareImage = findViewById(R.id.share_image) as Button
        shareImage!!.setOnClickListener(this)



        rlsave = findViewById(R.id.rlsave) as RelativeLayout
        rlshare = findViewById(R.id.rlshare) as RelativeLayout

           rlsave!!.setOnClickListener(View.OnClickListener {
            rlaccom!!.setVisibility(View.GONE)
            rlagfee!!.setVisibility(View.GONE)

            layout_to_image = Layout_to_Image(applicationContext, relativeLayout)

            //now call the main working function ;) and hold the returned image in bitmap

            bitmap = layout_to_image!!.convert_layout()

            val filename = "ShareRec" + System.currentTimeMillis() + ".jpg"
            if (Utility.checkPermission(this@FinalConfWithdrawActivity)) {
                saveImage(bitmap!!, filename)
                Toast.makeText(
                        applicationContext,
                        "Receipt downloaded successfully to gallery",
                        Toast.LENGTH_LONG).show()
            }

            rlaccom!!.setVisibility(View.VISIBLE)
            rlagfee!!.setVisibility(View.VISIBLE)
        })

            rlshare!!.setOnClickListener(View.OnClickListener {
            rlaccom!!.setVisibility(View.GONE)
            rlagfee!!.setVisibility(View.GONE)
            layout_to_image = Layout_to_Image(applicationContext, relativeLayout)

            //now call the main working function ;) and hold the returned image in bitmap

            bitmap = layout_to_image!!.convert_layout()
            if (Utility.checkWriteStoragePermission()) {
                shareImage(getImageUri(applicationContext, bitmap!!))
            }

            rlaccom!!.setVisibility(View.VISIBLE)
            rlagfee!!.setVisibility(View.VISIBLE)
        })



        edagid = findViewById(R.id.txtagid) as TextView
        edagname = findViewById(R.id.txtgname) as TextView
        edattid = findViewById(R.id.txtattid) as TextView

        val stragid = Utility.gettUtilAgentId(applicationContext)
        val stragname = Utility.gettUtilCustname(applicationContext)
        val strattid = Utility.gettUtilUserId(applicationContext)

        edagid!!.setText(stragid)
        edagname!!.setText(stragname)
        edattid!!.setText(strattid)


        relativeLayout = findViewById(R.id.receipt) as LinearLayout
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {


            finish()


            val i = Intent(applicationContext, FMobActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // Staring Login Activity
            startActivity(i)
        }

        if (view!!.getId() == R.id.share_image) {
            /* String userid = Utility.gettUtilUserId(getApplicationContext());
            String test =  "   \n \n    TRANSFER TO FIRSTBANK  \nUSERID: "+userid+" \nReceipient Name: "+txtname+" \nRef Number:"+txtrfc+" \nAccount Number:"+recanno+" \nAmount:"+amou+" Naira\n Sender Name:"+ednamee+" \n Fee:"+strfee+" Naira \n \n \n \n";


            print(mConnection, mInterface,test);*/

            /*   if(Utility.checkPermission(this)) {
                shareImage(getImageUri(getApplicationContext(), bitmap));
            }*/


            val builder = AlertDialog.Builder(this)
            builder.setTitle("SELECT AN OPTION")

            // add a list
            val animals = arrayOf("Save to Gallery", "Share Receipt")
            builder.setItems(animals) { dialog, which ->
                when (which) {
                    0 // horse
                    -> {

                        rlaccom!!.setVisibility(View.GONE)
                        rlagfee!!.setVisibility(View.GONE)

                        layout_to_image = Layout_to_Image(applicationContext, relativeLayout)

                        //now call the main working function ;) and hold the returned image in bitmap

                        bitmap = layout_to_image!!.convert_layout()

                        val filename = "ShareRec" + System.currentTimeMillis() + ".jpg"
                        if (Utility.checkPermission(this@FinalConfWithdrawActivity)) {
                            saveImage(bitmap!!, filename)
                            Toast.makeText(
                                    applicationContext,
                                    "Receipt downloaded successfully to gallery",
                                    Toast.LENGTH_LONG).show()

                        }
                    }
                    1 // cow
                    -> {
                        rlaccom!!.setVisibility(View.GONE)
                        rlagfee!!.setVisibility(View.GONE)
                        layout_to_image = Layout_to_Image(applicationContext, relativeLayout)

                        //now call the main working function ;) and hold the returned image in bitmap

                        bitmap = layout_to_image!!.convert_layout()
                        if (Utility.checkPermission(this@FinalConfWithdrawActivity)) {
                            shareImage(getImageUri(applicationContext, bitmap!!))
                        }

                        rlaccom!!.setVisibility(View.VISIBLE)
                        rlagfee!!.setVisibility(View.VISIBLE)
                    }
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
        if (view.getId() == R.id.reportiss) {
            try {
                val userid = Utility.gettUtilUserId(applicationContext)
                sendData("   \n \n    TRANSFER TO FIRSTBANK  \nUSERID: $userid \nReceipient Name: $txtname \nRef Number:$txtrfc \nAccount Number:$recanno \nAmount:$amou Naira\n Sender Name:$ednamee \n Fee:$strfee Naira \n \n \n \n")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }
    }


    private fun shareImage(imagePath: Uri) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath)
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
    }


    private fun saveImage(finalBitmap: Bitmap, image_name: String) {

        addJpgSignatureToGallery(finalBitmap)
        rlaccom!!.setVisibility(View.VISIBLE)
        rlagfee!!.setVisibility(View.VISIBLE)
    }

   override fun onResume() {
        super.onResume()
        // put your code here...

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }



    // close the connection to bluetooth printer.
    @Throws(IOException::class)
    internal fun closeBT() {
        try {
            stopWorker = true
            mmOutputStream!!.close()
            mmInputStream!!.close()
            mmSocket!!.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // this will find a bluetooth printer device
    internal fun findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            if (mBluetoothAdapter == null) {
                Toast.makeText(
                        applicationContext,
                        "No bluetooth available",
                        Toast.LENGTH_LONG).show()
            }

            if (!mBluetoothAdapter!!.isEnabled()) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetooth, 0)
            }

            val pairedDevices = mBluetoothAdapter!!.getBondedDevices()

            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.name == "BlueTooth Printer") {
                        mmDevice = device
                        break
                    }
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    // this will send text data to be printed by the bluetooth printer
    @Throws(IOException::class)
    internal fun sendData(msg: String) {
        try {
            val img = R.drawable.monochrome
            try {
                var bmp: Bitmap? = BitmapFactory.decodeResource(resources,
                        img)
                bmp = getResizedBitmap(bmp!!, 120, 360)
                if (bmp != null) {
                    val command = UtilsPhoto.decodeBitmap(bmp)
                    //  mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    mmOutputStream!!.write(command)

                    mmOutputStream!!.write(msg.toByteArray())
                } else {
                    Log.e("Print Photo error", "the file isn't exists")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PrintTools", "the file isn't exists")
            }

            //  mmOutputStream.write(msg.getBytes());
            //  sendData(bytes);
            //   mmOutputStream.write(bytes);


            //  ImageIO.write(mmOutputStream, "PNG", myNewPNGFile);


        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            closeBT()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val neww = width.toFloat() * 0.6.toFloat()
        val newh = height.toFloat() * 0.6.toFloat()
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP

        return Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    fun getAlbumStorageDir(albumName: String): File {
        // Get the directory for the user's public pictures directory.
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName)
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()


    }

    fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {


            val flname = String.format("ShareRec_%d", System.currentTimeMillis())
            val photo = File(getAlbumStorageDir("FirstAgent"), String.format("ShareR%d.jpg", System.currentTimeMillis()))
            val filename = photo
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, FMobActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this@FinalConfWithdrawActivity.sendBroadcast(mediaScanIntent)
    }

}
