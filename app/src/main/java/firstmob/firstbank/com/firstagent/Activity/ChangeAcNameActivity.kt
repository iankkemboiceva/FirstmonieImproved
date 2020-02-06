package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


import com.afollestad.materialdialogs.MaterialDialog

import java.io.File

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pixplicity.easyprefs.library.Prefs
import de.hdodenhof.circleimageview.CircleImageView
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.toolbarnewui.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ChangeAcNameActivity : AppCompatActivity(), View.OnClickListener {
    private val mToolbar: Toolbar? = null
    internal var agentname: TextView? =null
    internal var agemail: TextView? =null
    internal var agphonenumb: TextView? =null

    internal var chkb: CheckBox? =null
    internal var chkus: CheckBox? = null
    internal var chkast: CheckBox? = null
    internal var chktpin: CheckBox? = null
    internal var chkbal: CheckBox? = null

    internal var session: SessionManagement? =null
    internal var numb: String? = null
    internal var initdisp = false
    internal var upLoadServerUri: String? = null

    internal var cvlast: CardView? =null
    internal var uploadFilePath: String? = null
    internal var uploadFileName: String? = null
    private val image: String? = null
    internal var lv: RecyclerView? =null
    internal var lv2: RecyclerView? =null
    internal var lv3: RecyclerView? =null
    internal var myact: Button? =null
    internal var chglgpin: Button? =null
    internal var grid: RadioButton? = null
    internal var list: RadioButton? = null
    internal var ivgrid: ImageView? =null
    internal var ivlist: ImageView? =null
    internal var iv: CircleImageView? =null
    internal var rlbutton: RelativeLayout? =null


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_ac_name)

        session = SessionManagement(this)


        val toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)

        // Get the ActionBar here to configure the way it behaves.
        val ab = getSupportActionBar()
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab!!.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab!!.setDisplayShowTitleEnabled(false) // disable the default title element here (for centered title)

       // lv = findViewById(R.id.listView1) as RecyclerView
        cvlast = findViewById(R.id.card_view023) as CardView
        cvlast!!.setOnClickListener(this)

       // lv2 = findViewById(R.id.listView2) as RecyclerView
       titlepg.text="Profile"
        rlbutton = findViewById(R.id.rlbutton) as RelativeLayout
        rlbutton!!.setOnClickListener(this)
        lv3 = findViewById(R.id.listView3) as RecyclerView

        ivgrid = findViewById(R.id.ivgrid) as ImageView
        ivlist = findViewById(R.id.ivlist) as ImageView
        ivgrid!!.setOnClickListener(this)
        ivlist!!.setOnClickListener(this)

        chkb = findViewById(R.id.biochk) as CheckBox
        chkb!!.setOnClickListener(this)



        iv = findViewById(R.id.profile_image) as CircleImageView
        //  iv.setImageBitmap(null);
        iv!!.setOnClickListener(this)
        //loadImage();

        agentname = findViewById(R.id.textViewnb2) as TextView
        agemail = findViewById(R.id.textViewrrs) as TextView
        agphonenumb = findViewById(R.id.textViewcvv) as TextView

       val txtagname = Prefs.getString(SharedPrefConstants.KEY_CUSTNAME, "NA")
       val txtphonenumb = Prefs.getString(SharedPrefConstants.AGMOB, "NA")
       val txtemail = Prefs.getString(SharedPrefConstants.KEY_EMAIL, "NA")


        agphonenumb!!.setText(txtphonenumb)
        agemail!!.setText(txtemail)
        agentname!!.setText(txtagname)

       // myact = findViewById(R.id.tdispedit) as Button
        chglgpin = findViewById(R.id.button10) as Button


        session = SessionManagement(getApplicationContext())


        uploadFilePath = Environment.getExternalStorageDirectory().toString() + File.separator + "cache" + File.separator
        // uploadFileName = numb;
        uploadFileName = numb

        uploadFilePath = Environment.getExternalStorageDirectory().toString() + File.separator + "req_images" + File.separator




        upLoadServerUri = ""


    }



     override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
           // onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {


        if (v.id == R.id.rlbutton) {


          //  startActivity(Intent(getApplicationContext(), ChangePinActivity::class.java))

        }


        if (v.id == R.id.distpin) {
            if (chktpin!!.isChecked) {
                session!!.setTpinPref()
            } else {
                session!!.UnSetTpinPref()
            }
            Utility.showToast("Settings Applied Successfully")
        }

        if (v.id == R.id.chkus) {
            if (chkus!!.isChecked) {
                session!!.setUser()
            } else {
                session!!.UnSetUser()
            }
            Utility.showToast("Settings Applied Successfully")
        }

        if (v.id == R.id.ivgrid) {
            session!!.setString("VTYPE", "grid")
            finish()


            // After logout redirect user to Loing Activity
            val i = Intent(getApplicationContext(), FMobActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // Staring Login Activity
            startActivity(i)
        }
        if (v.id == R.id.ivlist) {
            session!!.setString("VTYPE", "list")
            finish()


            val i = Intent(getApplicationContext(), FMobActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            // Staring Login Activity
            startActivity(i)
        }



    }


}
