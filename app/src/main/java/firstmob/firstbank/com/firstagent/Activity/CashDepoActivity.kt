package firstmob.firstbank.com.firstagent.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import firstmob.firstbank.com.firstagent.utils.Utility.hideKeyboardFrom
import firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection
import android.text.TextWatcher

import android.widget.Toast
import firstmob.firstbank.com.firstagent.contract.ActivateAgentContract
import firstmob.firstbank.com.firstagent.contract.CashDepoContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.ActivateAgentPresenter
import firstmob.firstbank.com.firstagent.presenter.CashDepoPresenter
import firstmob.firstbank.com.firstagent.utils.Utility
import kotlinx.android.synthetic.main.activity_cash_depo.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import firstmob.firstbank.com.firstagent.security.SecurityLayer

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar


class CashDepoActivity : AppCompatActivity(), CashDepoContract.ILoginView {
    override fun onLoginResult() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var viewDialog: ViewDialog? = null
    internal lateinit var presenter: CashDepoContract.Presenter
    lateinit var acno: String;
    lateinit var acname: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_depo)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Get the ActionBar here to configure the way it behaves.
        val ab = supportActionBar
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false)
        ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.nbkyellow)));
        viewDialog = ViewDialog(this)



        edacc.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edacc.text.toString().length === 10) {


                    hideKeyboardFrom(applicationContext, edacc)


                    acno = edacc.text.toString()
                    presenter.NameEnquiry(acno)


                }
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        })

        button2.setOnClickListener() {

            val recanno = edacc.text.toString()
            val amou = amount.text.toString()
            val narra = ednarr.text.toString()


            if (Utility.isNotNull(recanno)) {
                if (Utility.isNotNull(amou)) {
                    val nwamo = amou.replace(",", "")
                    SecurityLayer.Log("New Amount", nwamo)
                    val txamou = java.lang.Double.parseDouble(nwamo)

                    if (Utility.isNotNull(narra)) {

                        if (Utility.isNotNull(acno)) {


                            val intent = Intent(this@CashDepoActivity, ConfirmCashDepoActivity::class.java)


                            intent.putExtra("recanno", recanno)
                            intent.putExtra("amou", amou)
                            intent.putExtra("narra", narra)
                            intent.putExtra("ednamee", acname)
                            intent.putExtra("ednumbb", "NA")
                            intent.putExtra("trantype", "D")
                            intent.putExtra("txtname", acname)
                            startActivity(intent)


                        } else {
                            Toast.makeText(
                                    applicationContext,
                                    "Please enter a valid account number",
                                    Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(
                                applicationContext,
                                "Please enter a valid value for Narration",
                                Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Please enter a valid value for Amount",
                            Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                        applicationContext,
                        "Please enter a value for Account Number",
                        Toast.LENGTH_LONG).show()
            }

        }

        presenter = CashDepoPresenter(this, FetchServerResponse())
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

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun showProgress() {
        viewDialog?.showDialog()
    }

    override fun setaccname(name: String) {
        acname = name
        cname.setText(name)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
