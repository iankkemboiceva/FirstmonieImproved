package firstmob.firstbank.com.firstagent.Activity

import android.app.PendingIntent
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.usb.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject

class FinalConfirmCableTV : AppCompatActivity(),View.OnClickListener {

    internal var reccustid: TextView? =null
    internal var recamo:TextView? =null
    internal var recnarr:TextView? =null
    internal var recsendnum:TextView? =null
    internal var recsendnam:TextView? =null
    internal var recfee:TextView? =null
    internal var recagcmn:TextView? =null
    internal var rectrref:TextView? =null
    internal var txtlabel:TextView? =null
    internal var txtrfcd:TextView? =null
    internal var txtbillname:TextView? =null
    internal var recname:TextView? =null
    internal var btnsub: Button? =null
    internal var txtcustid: String? = null
    internal var amou:String? = null
    internal var narra:String? = null
    internal var ednamee:String? = null
    internal var ednumbb:String? = null
    internal var serviceid:String? = null
    internal var billid:String? = null
    internal var txtfee:String? = null
    internal var stragcms:String? =null
    internal var strfee:String? = null
    internal var strtref:String? = null
    internal var strlabel:String? = null
    internal var strbillnm:String? = null
    internal var fullname:String? = null
//    internal var prgDialog: ProgressDialog? = null
//    internal var prgDialog2:ProgressDialog? =null
    internal var amon: EditText? = null
    internal var edacc:EditText? = null
    internal var pno:EditText? = null
    internal var txtamount:EditText? = null
    internal var txtnarr:EditText? = null
    internal var edname:EditText? = null
    internal var ednumber:EditText? = null


    internal var shareImage: Button? =null
    internal var repissue:Button? =null
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
    // private BroadcastReceiver mUsbReceiver;
    private val ACTION_USB_PERMISSION = "firstmob.firstbank.com.firstagent.USB_PERMISSION"
    private val forceCLaim = true

    internal var mDeviceList: HashMap<String, UsbDevice>? = null
    internal var mDeviceIterator: Iterator<UsbDevice>? = null

    internal var layout_to_image: Layout_to_Image? =null
    internal var relativeLayout: LinearLayout? =null
    internal var bitmap: Bitmap? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_confirm_cable_tv)
        reccustid = findViewById(R.id.textViewnb2)
        txtlabel = findViewById(R.id.textViewnb)
        txtrfcd = findViewById(R.id.txtrfcd)
        recamo = findViewById(R.id.textViewrrs)
        txtbillname = findViewById(R.id.txtbillname)
        recnarr = findViewById(R.id.textViewrr)
        recname = findViewById(R.id.textViewcvv)
        recsendnam = findViewById(R.id.sendnammm)
        recsendnum = findViewById(R.id.sendno)

//        prgDialog2 = ProgressDialog(this)
//        prgDialog2!!.setMessage("Loading....")
//        prgDialog2!!.setCancelable(false)


        recagcmn = findViewById(R.id.txtaccom)
        recfee = findViewById(R.id.txtfee)
        rectrref = findViewById(R.id.txref)

        btnsub = findViewById(R.id.button2)
        btnsub!!.setOnClickListener(this)

        val bundle = intent.extras
        if (bundle != null) {

            txtcustid = bundle!!.getString("custid")
            amou = bundle!!.getString("amou")
            narra = bundle!!.getString("narra")
            ednamee = bundle!!.getString("ednamee")
            ednumbb = bundle!!.getString("ednumbb")
            strlabel = bundle!!.getString("label")
            billid = bundle!!.getString("billid")
            strbillnm = bundle!!.getString("billname")
            serviceid = bundle!!.getString("serviceid")
            strfee = bundle!!.getString("fee")
            strtref = bundle!!.getString("tref")
            fullname = bundle!!.getString("fullname")
            recname!!.setText(fullname)
            stragcms = Utility.returnNumberFormat(bundle!!.getString("agcmsn"))
            val txtrfc = bundle!!.getString("refcode")
            txtrfcd?.setText(txtrfc)
            reccustid?.setText(txtcustid)
            txtbillname?.setText(strbillnm)

            recamo?.setText(amou)
            recnarr?.setText(narra)
            recfee?.setText(Constants.KEY_NAIRA + strfee)
            rectrref?.setText(strtref)
            recsendnam?.setText(ednamee)
            recsendnum?.setText(ednumbb)
            txtlabel?.setText(strlabel)
            recagcmn?.setText(Constants.KEY_NAIRA + stragcms)
        }

        shareImage = findViewById(R.id.share_image)
        shareImage!!.setOnClickListener(this)

        repissue = findViewById(R.id.reportiss)
        repissue!!.setOnClickListener(this)

        relativeLayout = findViewById(R.id.receipt)
        layout_to_image = Layout_to_Image(this, relativeLayout)

        //now call the main working function ;) and hold the returned image in bitmap

        bitmap = layout_to_image!!.convert_layout()
    }
    override fun onClick(view: View?) {
        if (view!!.getId() == R.id.button2) {
            finish()
            startActivity(Intent(this, FMobActivity::class.java))
        }
        if (view.getId() == R.id.share_image) {
            if (Utility.checkPermission(this)) {
                shareImage(getImageUri(this, bitmap!!))
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun shareImage(imagePath: Uri) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath)
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
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
                                    //val data = String(encodedBytes, "US-ASCII")
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
    internal fun findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            if (mBluetoothAdapter == null) {
                Toast.makeText(
                        this,
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
}
