package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import firstmob.firstbank.com.firstagent.fragments.Airtime_transfirst
import firstmob.firstbank.com.firstagent.fragments.Withdraw_Firsts
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility

class Withdraw : AppCompatActivity() {
var session :SessionManagement? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.lightgree_withdrcolor)));
        supportActionBar!!.title = null
        session=SessionManagement(this)
        loadFragment(Withdraw_Firsts())
    }
    private fun loadFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    fun SetForceOutDialog(msg: String, title: String, c: Context?) {
        if (c != null) {
            MaterialDialog.Builder(c)
                    .title(title)
                    .content(msg)

                    .negativeText("CONTINUE")
                    .callback(object : MaterialDialog.ButtonCallback() {
                        override fun onPositive(dialog: MaterialDialog?) {
                            dialog!!.dismiss()
                        }

                        override fun onNegative(dialog: MaterialDialog?) {

                            dialog!!.dismiss()
                            finish()
                            session!!.logoutUser()

                            // After logout redirect user to Loing Activity
                            val i = Intent(c, SignInActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            // Staring Login Activity
                            startActivity(i)

                        }
                    })
                    .show()
        }
    }
    fun LogOut() {
        session!!.logoutUser()

        // After logout redirect user to Loing Activity
        finish()
        val i = Intent(applicationContext, SignInActivity::class.java)

        // Staring Login Activity
        startActivity(i)
        Utility.showToast("You have been locked out of the app.Please call customer care for further details")

    }
}
