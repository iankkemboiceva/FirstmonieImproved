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
import android.os.Parcelable
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

class FinalConfirmCableTVActivity : AppCompatActivity(),View.OnClickListener {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var recdatetimee: TextView? = null
    internal var btnsub: Button? = null
    internal var prgDialog: ProgressDialog? = null
    internal var prgDialog2:ProgressDialog  ? = null

    internal var telcoop: String? = null
    internal var reccustid: TextView? = null
    internal var recamo:TextView? = null
    internal var recnarr:TextView? = null
    internal var recsendnum:TextView? = null
    internal var recsendnam:TextView? = null
    internal var recfee:TextView? = null
    internal var recagcmn:TextView? = null
    internal var rectrref:TextView? = null
    internal var txtlabel:TextView? = null
    internal var txtrfcd:TextView? = null
    internal var txtbillname:TextView? = null
    internal var recname:TextView? = null
    internal var rlsave: RelativeLayout? = null
    internal var rlshare:RelativeLayout? = null

    internal var shareImage: Button? = null
    internal var repissue:Button? = null
    // android built in classes for bluetooth operations
    internal var mBluetoothAdapter: BluetoothAdapter? = null
    internal var mmSocket: BluetoothSocket? = null
    internal var mmDevice: BluetoothDevice? = null

    // needed for communication to bluetooth device / network
    internal var mmOutputStream: OutputStream? = null
    internal var mmInputStream: InputStream? = null
    internal var workerThread: Thread? = null
    internal var txtrfc: String? = null
    internal var readBuffer: ByteArray? = null
    internal var readBufferPosition: Int = 0
    @Volatile
    internal var stopWorker: Boolean = false


    private val mUsbManager: UsbManager? = null
    private val mDevice: UsbDevice? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mInterface: UsbInterface? = null
    private var mEndPoint: UsbEndpoint? = null
    private val mPermissionIntent: PendingIntent? = null
    private val mContext: Context? = null
    internal var rlagfee: RelativeLayout? = null
    internal var rlaccom:RelativeLayout? = null
    // private BroadcastReceiver mUsbReceiver;
    private val ACTION_USB_PERMISSION = "firstmob.firstbank.com.firstagent.USB_PERMISSION"
    private val forceCLaim = true
    internal var mDeviceList: HashMap<String, UsbDevice>? = null
    internal var mDeviceIterator: Iterator<UsbDevice>? = null

    internal var layout_to_image: Layout_to_Image? = null
    internal var relativeLayout: LinearLayout? = null
    internal var bitmap: Bitmap? = null
    internal var txtcustid: String? = null
    internal var amou:String? = null
    internal var narra:String? = null
    internal var ednamee:String? = null
    internal var ednumbb:String? = null
    internal var serviceid:String? = null
    internal var billid:String? = null
    internal var txtfee:String? = null
    internal var stragcms:String? = null
    internal var strfee:String? = null
    internal var strtref:String? = null
    internal var strlabel:String? = null
    internal var strbillnm:String? = null
    internal var fullname:String? = null
    internal var amon: EditText? = null
    internal var edacc:EditText? = null
    internal var pno:EditText? = null
    internal var txtamount:EditText? = null
    internal var txtnarr:EditText? = null
    internal var edname:EditText? = null
    internal var ednumber:EditText? = null
    internal var edagname: TextView? = null
    internal var edattid:TextView? = null
    internal var edagid:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_confirm_cable_tv)
        txtlabel = findViewById(R.id.textViewnb) as TextView

        txtbillname = findViewById(R.id.txtbillname) as TextView
        recnarr = findViewById(R.id.textViewrr) as TextView
        recname = findViewById(R.id.textViewcvv) as TextView
        recsendnam = findViewById(R.id.sendnammm) as TextView
        recsendnum = findViewById(R.id.sendno) as TextView


        reccustid = findViewById(R.id.textViewnb2) as TextView
        txtrfcd = findViewById(R.id.txtrfcd) as TextView

        recamo = findViewById(R.id.textViewrrs) as TextView


        recagcmn = findViewById(R.id.txtaccom) as TextView
        recfee = findViewById(R.id.txtfee) as TextView
        rectrref = findViewById(R.id.txref) as TextView
        prgDialog2 = ProgressDialog(this)
        prgDialog2!!.setMessage("Loading....")
        prgDialog2!!.setCancelable(false)

        rlagfee = findViewById(R.id.rlagfee) as RelativeLayout
        rlaccom = findViewById(R.id.rlaccom) as RelativeLayout
        recdatetimee = findViewById(R.id.textfinaldatet) as TextView

        btnsub = findViewById(R.id.button2) as Button
        btnsub!!.setOnClickListener(this)

        val intent = intent
        if (intent != null) {


            txtcustid = intent.getStringExtra("custid")
            amou = intent.getStringExtra("amou")

            narra = intent.getStringExtra("narra")
            strbillnm = intent.getStringExtra("billname")

            ednamee = intent.getStringExtra("ednamee")
            ednumbb = intent.getStringExtra("ednumbb")

            strlabel = intent.getStringExtra("label")
            var txtamou = Utility.returnNumberFormat(amou)
            if (txtamou == "0.00") {
                txtamou = amou
            }

            billid = intent.getStringExtra("billid")
            serviceid = intent.getStringExtra("serviceid")
            strfee = intent.getStringExtra("fee")
            strtref = intent.getStringExtra("tref")
            stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"))
            val txtrfc = intent.getStringExtra("refcode")
            fullname = intent.getStringExtra("fullname")
            recname = findViewById(R.id.textViewcvv) as TextView
            recname!!.setText(fullname)
            txtrfcd!!.setText(strtref)
            reccustid!!.setText(txtcustid)

            if (Utility.checkStateCollect(serviceid)) {
                recfee!!.setText(Constants.KEY_NAIRA + "63.00")
                recagcmn!!.setText(Constants.KEY_NAIRA + "30.00")

            } else {
                recfee!!.setText(Constants.KEY_NAIRA + strfee)
                recagcmn!!.setText(Constants.KEY_NAIRA + stragcms)

            }
            rectrref!!.setText(strtref)

            recamo!!.setText(Constants.KEY_NAIRA + txtamou)


            val redatetim = intent.getStringExtra("datetime")
            recdatetimee!!.setText(Utility.changeDate(redatetim))



            stragcms = Utility.returnNumberFormat(intent.getStringExtra("agcmsn"))


            reccustid!!.setText(txtcustid)
            txtbillname!!.setText(strbillnm)

            //   recamo.setText(amou);
            recnarr!!.setText(narra)
            //     recfee.setText(ApplicationConstants.KEY_NAIRA+strfee);
            rectrref!!.setText(strtref)
            recsendnam!!.setText(ednamee)
            recsendnum!!.setText(ednumbb)
            txtlabel!!.setText(strlabel)
        }

        shareImage = findViewById(R.id.share_image) as Button
        shareImage!!.setOnClickListener(this)

        repissue = findViewById(R.id.reportiss) as Button
        repissue!!.setOnClickListener(this)



        rlsave = findViewById(R.id.rlsave) as RelativeLayout
        rlshare = findViewById(R.id.rlshare) as RelativeLayout

        rlsave!!.setOnClickListener(View.OnClickListener {
            rlaccom!!.setVisibility(View.GONE)
            rlagfee!!.setVisibility(View.GONE)

            layout_to_image = Layout_to_Image(applicationContext, relativeLayout)

            //now call the main working function ;) and hold the returned image in bitmap

            bitmap = layout_to_image!!.convert_layout()

            val filename = "ShareRec" + System.currentTimeMillis() + ".jpg"
            if (Utility.checkPermission(this@FinalConfirmCableTVActivity)) {
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
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, FMobActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onClick(view: View) {

        if (view.id == R.id.button2) {
            finish()
            val i = Intent(this@FinalConfirmCableTVActivity, FMobActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // Staring Login Activity
            startActivity(i)
        }

        if (view.id == R.id.share_image) {
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
                        if (Utility.checkPermission(this@FinalConfirmCableTVActivity)) {
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
                        if (Utility.checkPermission(this@FinalConfirmCableTVActivity)) {
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
        if (view.id == R.id.reportiss) {
            try {
                val userid = Utility.gettUtilUserId(applicationContext)
                sendData("   \n \n    AIRTIME  \nUSERID: $userid \nTelco: $telcoop \nRef Number:$txtrfc \nMobile Number:$txtcustid \nAmount:$amou Naira\n Sender Name:$ednamee \n Fee:$strfee Naira \n \n \n \n")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        // put your code here...

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

    internal fun beginListenForData() {
        try {
            val handler = Handler()

            // this is the ASCII code for a newline character
            val delimiter: Byte = 10

            stopWorker = false
            readBufferPosition = 0
            readBuffer = ByteArray(1024)

            workerThread = Thread(Runnable {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {

                    try {

                        val bytesAvailable = mmInputStream!!.available()

                        if (bytesAvailable > 0) {

                            val packetBytes = ByteArray(bytesAvailable)
                            mmInputStream!!.read(packetBytes)

                            for (i in 0 until bytesAvailable) {

                                val b = packetBytes[i]
                                if (b == delimiter) {

                                    val encodedBytes = ByteArray(readBufferPosition)
                                    System.arraycopy(
                                            readBuffer, 0,
                                            encodedBytes, 0,
                                            encodedBytes.size
                                    )

                                    // specify US-ASCII encoding
                                 //   val data = String(encodedBytes, "US-ASCII")
                                    readBufferPosition = 0

                                    // tell the user data were sent to bluetooth printer device
                                    handler.post {
                                        //   myLabel.setText(data);
                                    }

                                } else {
                                    readBuffer!![readBufferPosition++] = b
                                }
                            }
                        }

                    } catch (ex: IOException) {
                        stopWorker = true
                    }

                }
            })

            workerThread!!.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    internal val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            mInterface = device.getInterface(0)
                            mEndPoint = mInterface!!.getEndpoint(1)
                            mConnection = mUsbManager!!.openDevice(device)

                            //setup();
                        }
                    } else {
                        //Log.d("SUB", "permission denied for device " + device);
                        Toast.makeText(context, "PERMISSION DENIED FOR THIS DEVICE", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


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


    // tries to open a connection to the bluetooth printer device
    @Throws(IOException::class)
    internal fun openBT() {
        try {

            // Standard SerialPortService ID
            val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            mmSocket = mmDevice!!.createRfcommSocketToServiceRecord(uuid)
            mmSocket!!.connect()
            mmOutputStream = mmSocket!!.getOutputStream()
            mmInputStream = mmSocket!!.getInputStream()

            beginListenForData()


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

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

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this@FinalConfirmCableTVActivity.sendBroadcast(mediaScanIntent)
    }
}
