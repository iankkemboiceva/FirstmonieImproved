package firstmob.firstbank.com.firstagent.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast


import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller

import org.json.JSONException
import org.json.JSONObject

import java.util.Date

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import firstmob.firstbank.com.firstagent.fragments.FragmentDrawer

import firstmob.firstbank.com.firstagent.fragments.NewHomeGrid
import firstmob.firstbank.com.firstagent.fragments.NewHomeGridview2
import firstmob.firstbank.com.firstagent.fragments.NewHomeGridviewLatest
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import kotlinx.android.synthetic.main.activity_fmob.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FMobActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener, View.OnClickListener {
    private var mDropdown: PopupWindow? = null
    internal var mInflater: LayoutInflater? =null
    internal var count = 1
    private var drawerFragment: FragmentDrawer? = null
    internal var greet: TextView? = null

    internal var prgDialog: ProgressDialog? = null
    internal var pro: ProgressDialog? = null
    internal lateinit var drawerLayout: DrawerLayout
    internal var itemb: TextView? =null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fmob)
        // session = new SessionManagement(this);


        setSupportActionBar(toolbar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout


        drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_navigation_drawer) as FragmentDrawer?
        //   drawerFragment.setArguments(bundle);

        drawerFragment!!.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar)
        drawerFragment!!.setDrawerListener(this)



        updateAndroidSecurityProvider(this)

        displayView(40)


    }


    override fun onDrawerItemSelected(view: View, position: Int) {
        displayView(position)
    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            SecurityLayer.Log("SecurityException", "Google Play Services not available.")
        }

    }
    fun launchmenu(v: View){
        initiatePopupWindow(v)
    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//
//            R.id.inbox -> {
//              initiatePopupWindow(item.actionView.rootView)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
    private fun initiatePopupWindow(v: View): PopupWindow {

        try {

            mInflater = applicationContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = mInflater!!.inflate(R.layout.row, null)

            //If you want to add any listeners to your textviews, these are two //textviews.
            val itema = layout.findViewById(R.id.ItemA) as TextView

            itema.setOnClickListener {

                //finish()
                startActivity(Intent(this@FMobActivity, ChangePinActivity::class.java))


            }

            itemb = layout.findViewById(R.id.ItemB) as TextView

            itemb!!.setOnClickListener(View.OnClickListener {
                finish()
                val intent = Intent(this, SignInActivity::class.java)


                // Staring Login Activity
                startActivity(intent);
                //  this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(this, "You have successfully signed out", Toast.LENGTH_LONG).show();
                //  startActivity(Intent(this@MainActivtyfbn, Changepinfbn::class.java))
                //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
            })

            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED)
            mDropdown = PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, true)

            val background = resources.getDrawable(R.drawable.union1)
            //mDropdown!!.setBackgroundDrawable(ContextCompat.getColor(this,R.drawable.union1))
            mDropdown!!.showAsDropDown(v, 5, 5)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mDropdown!!

    }
    private fun displayView(position: Int) {
        var fragment: Fragment? = null
        var title = getString(R.string.app_name)


        when (position) {
            40, 0 -> {

                fragment = NewHomeGridview2()

                title = "Welcome"
            }

            1 -> {
                val intent = Intent(this, ChangePinActivity::class.java)
                startActivity(intent);
            }


            2 -> {
              //  finish()
                val intent = Intent(this, MyPerfActivity::class.java)


                // Staring Login Activity
                startActivity(intent);
            }
            3 -> {
                val intent = Intent(this, MinistatActivity::class.java)
                startActivity(intent);
            }
            4 -> {
                //finish()
                val intent = Intent(this, CommisionActivity::class.java)


                // Staring Login Activity
                startActivity(intent);
            }
            5 -> {
                finish()
                val intent = Intent(this, SignInActivity::class.java)


                // Staring Login Activity
                startActivity(intent);
                //  this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(this, "You have successfully signed out", Toast.LENGTH_LONG).show();
            }

            6 -> {

                val intent = Intent(this, ComplaintsActivity::class.java)


                // Staring Login Activity
                startActivity(intent);

            }


            7 -> {

               drawerLayout.closeDrawers()

            }

            else -> {
            }
        }

        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment, title)

            fragmentTransaction.addToBackStack(title)

            fragmentTransaction.commit()

            // set the toolbar title
            //   getSupportActionBar().setTitle(title);
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val backStateName = fragment.javaClass.name

        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft = manager.beginTransaction()
            ft.replace(R.id.container_body, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    override fun onBackPressed() {
       finish()
        val fm = supportFragmentManager
        val fm2 = fragmentManager
        val bentry = fm.backStackEntryCount
        Log.i("TAG", "Frag Entry: $bentry")

        val bentry2 = fm2.backStackEntryCount
        Log.i("TAG", "Comm Report Frag Entry: $bentry2")
        val f = supportFragmentManager.findFragmentById(R.id.container_body)



        if (bentry2 > 0) {

        }

        super.onBackPressed()
    }



    override fun onClick(v: View) {

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    companion object {

        //  public ResideMenu resideMenuClass;

        private val SPLASH_TIME_OUT = 2000

        val DISCONNECT_TIMEOUT: Long = 180000 // 5 min = 5 * 60 * 1000 ms
    }


}
