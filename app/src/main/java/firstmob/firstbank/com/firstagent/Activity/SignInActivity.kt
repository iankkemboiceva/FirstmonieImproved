package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import com.andrognito.pinlockview.PinLockListener


import kotlinx.android.synthetic.main.activity_sign_in.*
import com.andrognito.pinlockview.PinLockView

import android.R.attr.name
import android.content.Intent
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.SignInContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter
import firstmob.firstbank.com.firstagent.presenter.SignInPresenter

import firstmob.firstbank.com.firstagent.utils.Utility.*
import javax.inject.Inject


class SignInActivity : AppCompatActivity(), SignInContract.ILoginView {


    var finpin = ""
    internal lateinit var presenter: SignInContract.Presenter
    var viewDialog: ViewDialog? = null


    private val mPinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {

            //SecurityLayer.Log(TAG, "Pin complete: " + pin);
            finpin = pin
        }

        override fun onEmpty() {
            SecurityLayer.Log("Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            //	SecurityLayer.Log(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        pin_lock_view.setPinLockListener(mPinLockListener)
        pin_lock_view.attachIndicatorDots(indicator_dots)
        pin_lock_view.pinLength = 5
        pin_lock_view.textColor = resources.getColor(R.color.colorPrimary)

        viewDialog = ViewDialog(this)

        signinn.setOnClickListener() {
            presenter.Login(finpin)

        }
        presenter = SignInPresenter(this, FetchServerResponse())

        presenter.setAppVersion()

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun showProgress() {
        viewDialog?.showDialog()
    }

    override fun onLoginError(error: String) {
        showToast(error)
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

    override fun setTextApp(text: String) {
        versname.text = text
    }


    override fun onLoginResult() {

        val i = Intent(this,FMobActivity::class.java)
        startActivity(i)
    }


}


