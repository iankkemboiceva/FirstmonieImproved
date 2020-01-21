package firstmob.firstbank.com.firstagent.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import firstmob.firstbank.com.firstagent.utils.SessionManagement

/**
 * Created by ian on 7/23/2018.
 */

 open class BaseActivity : AppCompatActivity() {
    internal var session: SessionManagement? =null

    val sharedPreference: SharedPreferences
        get() = getSharedPreferences(PREF_FILE, MODE_PRIVATE)

    val isValidLogin: Boolean
        get() {
            val last_edit_time = sharedPreference.getLong(KEY_SP_LAST_INTERACTION_TIME, 0)
            return last_edit_time == 0L || System.currentTimeMillis() - last_edit_time < TIMEOUT_IN_MILLI
        }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (isValidLogin)
            sharedPreference.edit().putLong(KEY_SP_LAST_INTERACTION_TIME, System.currentTimeMillis()).apply()
        else
            gohome()
    }

     override fun onResume() {
        super.onResume()

        if (isValidLogin)
            sharedPreference.edit().putLong(KEY_SP_LAST_INTERACTION_TIME, System.currentTimeMillis()).apply()
        else
            gohome()
    }

    fun gohome() {
        val intent = Intent(this, FMobActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        //   Toast.makeText(this, "User logout due to inactivity", Toast.LENGTH_SHORT).show();
        sharedPreference.edit().remove(KEY_SP_LAST_INTERACTION_TIME).apply() // make shared preference null.
    }

    open fun LogOut() {
        session = SessionManagement(this)
        session!!.logoutUser()

        // After logout redirect user to Loing Activity
        finish()
        val i = Intent(getApplicationContext(), SignInActivity::class.java)

        // Staring Login Activity
        startActivity(i)
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show()
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }

    companion object {
        val TIMEOUT_IN_MILLI = (1000 * 60 * 3).toLong()
        val PREF_FILE = "App_Pref"
        val KEY_SP_LAST_INTERACTION_TIME = "KEY_SP_LAST_INTERACTION_TIME"
    }
}