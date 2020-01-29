package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.adapter.DepoMenuAdapt
import firstmob.firstbank.com.firstagent.adapter.OTBList
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class FTMenuActivity : AppCompatActivity() {
    var gridView: GridView? = null
    var otblist: MutableList<OTBList> = ArrayList()
    var ptype: String? = null
    var lv: ListView? = null
    var aAdpt: DepoMenuAdapt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ftmenu)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.normalcolor)));
        //checkInternetConnection2();
        lv = findViewById<View>(R.id.lv) as ListView
        SetPop()
        lv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val title: String? = null
            if (position == 0) {
                val intent = Intent(this, CashDepoTransActivity::class.java)

                startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(this, SendOTBActivity::class.java)

                startActivity(intent)
            } else if (position == 2) { // startActivity(new Intent(FTMenuActivity.this, SendOtherWalletActivity.class));
            }
        }
        Prefs.putString("bankname", null)
        Prefs.putString("bankcode", null)
        Prefs.putString("recanno", null)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun SetPop() {
        otblist.clear()
        otblist.add(OTBList("FirstBank", "057"))
        otblist.add(OTBList("Other Banks", "058"))
        //    otblist.add(new OTBList("FirstMonie","059"));
        otblist.add(OTBList("Mobile Money Wallet", "059"))
        aAdpt = DepoMenuAdapt(otblist, this)
        lv!!.adapter = aAdpt
    }
}