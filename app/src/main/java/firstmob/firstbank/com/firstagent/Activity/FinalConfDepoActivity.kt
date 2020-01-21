package firstmob.firstbank.com.firstagent.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import firstmob.firstbank.com.firstagent.constants.Constants.KEY_NAIRA
import firstmob.firstbank.com.firstagent.utils.Utility.returnNumberFormat
import android.content.Intent

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import firstmob.firstbank.com.firstagent.utils.Utility
import firstmob.firstbank.com.firstagent.utils.Utility.changeDate
import kotlinx.android.synthetic.main.activity_confirm_cash_depo.*
import kotlinx.android.synthetic.main.activity_final_conf_depo.*
import android.R.attr.bitmap
import android.content.Context
import android.widget.Toast

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.vipul.hp_hp.library.Layout_to_Image

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.MediaStore

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.*


class FinalConfDepoActivity : AppCompatActivity() {

    var recanno: String? = null; var txtrfc: String? = null; var amou:String? = null;var narra:String? = null;var ednamee:String? = null;var ednumbb:String? = null;var txtname:String? = null;var strfee:String? = null;var stragcms:String? = null
    var layout_to_image: Layout_to_Image? = null
    var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_conf_depo)


        val intent = intent
        if (intent != null) {

            recanno = intent.getStringExtra("recanno")
            amou = intent.getStringExtra("amou")
            narra = intent.getStringExtra("narra")
            ednamee = intent.getStringExtra("ednamee")
            ednumbb = intent.getStringExtra("ednumbb")
            txtname = intent.getStringExtra("txtname")
            txtrfc = intent.getStringExtra("refcode")
            val redatetim = intent.getStringExtra("datetime")
            textfinaldatet.text = changeDate(redatetim)
            txtrfcd.text = txtrfc
            stragcms = returnNumberFormat(intent.getStringExtra("agcmsn"))
            val trantype = intent.getStringExtra("trantype")
            strfee = intent.getStringExtra("fee")
            if (trantype == "D") {
                rlsendnam.visibility = View.GONE
                rlsendnum.visibility = View.GONE
                txtype.text = "CASH DEPOSIT SUCCESSFUL"
            }
            recacno.text = recanno
            recname.text = txtname
            recamo.text = KEY_NAIRA + amou
            recnarr.text = narra
            txtshowfee.text = KEY_NAIRA + strfee
            sendnammm.text = ednamee
            sendno.text = ednumbb
            txtaccom.text = KEY_NAIRA + stragcms



        }


        rlsave.setOnClickListener {
            rlaccom.visibility = View.GONE
            rlagfee.visibility = View.GONE

            layout_to_image = Layout_to_Image(applicationContext, receipt)

            //now call the main working function ;) and hold the returned image in bitmap

            bitmap = layout_to_image!!.convert_layout()

            val filename = "ShareRec" + System.currentTimeMillis() + ".jpg"
            if (Utility.checkPermission()) {
                saveImage(bitmap!!, filename)
                Toast.makeText(
                        applicationContext,
                        "Receipt downloaded successfully to gallery",
                        Toast.LENGTH_LONG).show()
            }

            rlaccom.visibility = View.VISIBLE
            rlagfee.visibility = View.VISIBLE
        }



        rlshare.setOnClickListener {
            rlaccom.visibility = View.GONE
            rlagfee.visibility = View.GONE
            layout_to_image = Layout_to_Image(applicationContext, receipt)

            //now call the main working function ;) and hold the returned image in bitmap

            bitmap = layout_to_image!!.convert_layout()
            val newbitmap = bitmap
            if (Utility.checkWriteStoragePermission()) {
                if (applicationContext != null) {
                    shareImage(getImageUri(applicationContext, newbitmap!!))
                }
            }

            rlaccom.visibility = View.VISIBLE
            rlagfee.visibility = View.VISIBLE
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }


    private fun shareImage(imagePath: Uri) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath)
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
    }
    fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {


            val flname = String.format("ShareRec_%d", System.currentTimeMillis())
            val photo = File(getExternalFilesDir("FirstAgent"), String.format("ShareR%d.jpg", System.currentTimeMillis()))
            val filename = photo
            //saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

  /* private fun  saveBitmapToJPG(bitmap: Bitmap, photo: File) {
        val newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        val canvas =  Canvas(newBitmap)
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        val stream = FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();


    }*/

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    }

    private fun saveImage(finalBitmap: Bitmap, image_name: String) {


        addJpgSignatureToGallery(finalBitmap);

        rlaccom.setVisibility(View.VISIBLE);
        rlagfee.setVisibility(View.VISIBLE);
    }



}
