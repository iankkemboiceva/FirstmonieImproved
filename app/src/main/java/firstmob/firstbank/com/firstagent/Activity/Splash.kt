package firstmob.firstbank.com.firstagent.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.ISLOGIN
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.SESS_REG
import firstmob.firstbank.com.firstagent.utils.Run

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newsplash)


        Run.after(3000) {

            val sessreg = Prefs.getString(SESS_REG, "N")
            if (sessreg == "Y") {

                val chklog = Prefs.getBoolean(ISLOGIN, false)

                if (chklog) {
                    finish()
                    val i = Intent(this, FMobActivity::class.java)
                    startActivity(i)

                } else {
                    finish()
                    val i = Intent(this, SignInActivity::class.java)
                    startActivity(i)
                }


            } else {
                finish()
                val i = Intent(this, ActivateAgentBefore::class.java)
                startActivity(i)
            }
        }
    }
}
