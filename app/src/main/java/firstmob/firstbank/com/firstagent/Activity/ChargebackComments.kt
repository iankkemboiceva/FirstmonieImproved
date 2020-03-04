package firstmob.firstbank.com.firstagent.Activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import firstmob.firstbank.com.firstagent.contract.ChargebackCommentsContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ChargebackCommentsPresenter
import firstmob.firstbank.com.firstagent.presenter.ChargebackPresenter
import firstmob.firstbank.com.firstagent.utils.FileCompressor
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.chargebckcomm.*
import kotlinx.android.synthetic.main.toolbarnewui.*
import org.apache.commons.codec.digest.DigestUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ChargebackComments : AppCompatActivity(), ChargebackCommentsContract.ILoginView {
    var photoFile: File? = null
    var viewDialog: ViewDialog? = null
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    var mCurrentPhotoPath: String? = null
    var txrefnum: String? = null
    var iscashgiv: String? = null
    var mCompressor: FileCompressor? = null
    var chgbckid: Int = 0
    internal lateinit var presenter: ChargebackCommentsContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chargeback_comments)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.nbkyellow)))
        titlepg.text="Chargeback"

        val intent = intent
        if (intent != null) {
          chgbckid = intent.getIntExtra("id", 0)
            txrefnum = intent.getStringExtra("ref")
            iscashgiv = intent.getStringExtra("iscashgiv")
            if(iscashgiv.equals("0")){
picture.visibility = View.GONE
            }

        }

        viewDialog = ViewDialog(this)
        presenter = ChargebackCommentsPresenter(this, FetchServerResponse())


        //button click
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

        mCompressor = FileCompressor(this)

        buttonnxt.setOnClickListener{
            var strpin = pin.text.toString()
            val strcomm = edacc.text.toString()
            var receipt = "NA"
            if(iscashgiv.equals("1")) {
                receipt = convertToBase64(photoFile)
            }
            Log.v("receipt",receipt)

            strpin = Utility.b64_sha256(strpin)


            presenter.saveChargeback(strpin,strcomm,iscashgiv,txrefnum,chgbckid,receipt)


        }
    }


    fun convertToBase64(attachment: File?): String {
        return android.util.Base64.encodeToString(attachment?.readBytes(), android.util.Base64.NO_WRAP).trim { it <= ' ' }

    }

    override fun goNextPage(){
        val intent = Intent(this, FinalConfChargeback::class.java)

        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        // Create the File where the photo should go
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(this,
                    applicationContext.packageName + ".provider",
                    photoFile!!)

            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? { // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view


            //onCaptureImageResult(data);
            Log.v("compressor", photoFile!!.absolutePath)
            try {
                photoFile = mCompressor!!.compressToFile(photoFile, "P")
                val thumbnail = mCompressor!!.compressToBitmap(photoFile)
                imgview.setImageBitmap(thumbnail)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
    }

    override fun showToast(text: String) {

        Toast.makeText(
                applicationContext,
                text,
                Toast.LENGTH_LONG).show()


    }

    override fun showProgress() {
        viewDialog?.showDialog()
    }


}