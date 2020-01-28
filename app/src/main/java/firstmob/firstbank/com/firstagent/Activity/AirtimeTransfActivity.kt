package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import firstmob.firstbank.com.firstagent.contract.AirtimeContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.model.GetAirtimeBillersData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.AirtimeTransFirstPresenter
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import org.json.JSONArray
import org.json.JSONException
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import javax.inject.Inject

class AirtimeTransfActivity : AppCompatActivity(), View.OnClickListener, AirtimeContract.IviewAirtimeFirst {
    @Inject
    internal lateinit var ul: Utility
    init {

        ApplicationClass.getMyComponent().inject(this)
        // initUser();
    }
    internal var planetsList2: List<GetAirtimeBillersData>? = ArrayList<GetAirtimeBillersData>()
    internal var amon: EditText? = null
    internal var edacc:EditText? = null
    lateinit var viewDialog: ViewDialog
    internal var pno:EditText? = null
    internal var txtamount:EditText? = null
    internal var txtnarr:EditText? = null
    internal var phonenumb:EditText? = null
    internal var sp1: Spinner? = null
    internal var sp5:Spinner? = null
    internal var sp7:Spinner? = null
    internal var presenter: AirtimeContract.PresenterAirtimeFirst? =null
    //  RecyclerView lvbann;
    internal var session: SessionManagement? =null
    internal var mobadapt: ArrayAdapter<GetAirtimeBillersData>? = null

    internal var tx: TextView? = null
    internal var layoutManager: LinearLayoutManager? = null
    internal var layoutManager2:LinearLayoutManager? = null


    internal var btn2: Button? = null
    internal var btn_back: Button? = null
    internal var telcochosen: String? = null
  //  internal var prgDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.airtime_firstpage)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
      val ab = supportActionBar
      ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.pink_btncolor)));
      //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
      ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
      ab.setDisplayHomeAsUpEnabled(true)
      ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
      ab.setDisplayShowTitleEnabled(false)
        layoutManager2 = LinearLayoutManager(this)
        layoutManager2!!.setOrientation(LinearLayoutManager.HORIZONTAL)
        session = SessionManagement(this)
        sp1 = findViewById(R.id.spin1) as Spinner
        btn2 = findViewById(R.id.button2) as Button
        btn_back = findViewById(R.id.button3) as Button
        btn2!!.setOnClickListener(this)
        btn_back!!.setOnClickListener(this)
        viewDialog= ViewDialog(this)
        phonenumb = findViewById(R.id.phonenumb) as EditText
       presenter = AirtimeTransFirstPresenter(this, this, FetchServerResponse())
        txtamount = findViewById(R.id.amount) as EditText
        val ofcListener = MyFocusChangeListener()
        phonenumb!!.setOnFocusChangeListener(ofcListener)
        txtamount!!.setOnFocusChangeListener(ofcListener)
        val strservdata = session!!.getString(SessionManagement.KEY_AIRTIME)
        if (strservdata != null) {
            var servdata: JSONArray? = null
            try {
                servdata = JSONArray(strservdata)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            if (servdata!!.length() > 0) {
                presenter!!.loadCachedAirtimeBillers()
               // SetAirtimStored()
            } else {
                if (Utility.checkInternetConnection()) {
                    presenter!!.loadAirtimeBillers()
                    //PopulateAirtime()
                }
            }
        } else {
            if (Utility.checkInternetConnection()) {
                presenter!!.loadAirtimeBillers()
               // PopulateAirtime()
            }
        }
        sp1!!.setOnTouchListener(spinnerOnTouch)
        sp1!!.setOnKeyListener(spinnerOnKey)
        sp1!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                SecurityLayer.Log("Good Adapter", "Am I in")
                if (sp1!!.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter!!.loadAirtimeBillers()
                       // PopulateAirtime()
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter")
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {
                SecurityLayer.Log("Good Adapter", "Am I in")
                if (sp1!!.getAdapter() == null) {
                    if (Utility.checkInternetConnection()) {
                        presenter!!.loadAirtimeBillers()
                       // PopulateAirtime()
                    }
                } else {
                    SecurityLayer.Log("Good Adapter", "Good Adapter")
                }

            }
        })
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onClick(v: View?) {
        if (v!!.getId() == R.id.button2) {

            val phoneno = phonenumb!!.getText().toString()
            var amou = txtamount!!.getText().toString()

            amou = Utility.returnNumberFormat(amou)
            val mnop = sp1!!.getSelectedItem().toString()

            val spinpos = sp1!!.getSelectedItemPosition()
            val billid = planetsList2!!.get(spinpos).billerId
            val serviceid = planetsList2!!.get(spinpos).sId

            if (Utility.checkInternetConnection()) {
                if (Utility.isNotNull(phoneno)) {
                    if (Utility.isNotNull(amou)) {
                        if (phoneno.length >= 10) {
                            val nwamo = amou.replace(",", "")
                            SecurityLayer.Log("New Amount", nwamo)
                            val txamou = java.lang.Double.parseDouble(nwamo)
                            if (txamou >= 100) {
                                if (serviceid != "0000") {
                                    val intent = Intent(this@AirtimeTransfActivity, ConfirmAirtimeActivity::class.java)
                                    intent.putExtra("mobno", phoneno)
                                    intent.putExtra("amou", amou)
                                    intent.putExtra("telcoop", mnop)

                                    intent.putExtra("billid", billid)
                                    intent.putExtra("serviceid", serviceid)

                                    startActivity(intent)

                                } else {
                                        Utility.showToast("Please select a valid mobile network operator")
                                }
                            } else {
                                Utility.showToast("Please enter an airtime value more than 100 Naira")
                            }
                        } else {
                            Utility.showToast("Please ensure valid mobile number has been set")
                        }
                    } else {
                        Utility.showToast("Please enter a valid value for Amount")
                    }
                } else {
                    Utility.showToast("Please enter a value for Phone Number")
                }
            }


        }
        if(v.id==R.id.button3){
            finish()
        }
    }

    private val spinnerOnTouch = View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            //Your code
            Log.v("Spinner on click", "Spinner on click")
            if (sp1!!.getAdapter() == null) {
                if (Utility.checkInternetConnection()) {
                    presenter!!.loadAirtimeBillers()
                   // PopulateAirtime()
                }
            } else {
                SecurityLayer.Log("Good Adapter", "Good Adapter")
            }
        }
        false
    }
    private val spinnerOnKey = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //your code
            Log.v("Spinner on click", "Spinner on click")
            if (sp1!!.getAdapter() == null) {
                if (Utility.checkInternetConnection()) {
                    presenter!!.loadAirtimeBillers()
                    //PopulateAirtime()
                }
            } else {
                SecurityLayer.Log("Good Adapter", "Good Adapter")
            }
            true
        } else {
            false
        }
    }

    private inner class MyFocusChangeListener : View.OnFocusChangeListener {

        override fun onFocusChange(v: View, hasFocus: Boolean) {

            if (v.id == R.id.amount && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val txt = txtamount!!.getText().toString()
                val fbal = Utility.returnNumberFormat(txt)
                //  txtamount.setText(fbal);

            }

            if (v.id == R.id.input_payacc && !hasFocus) {
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)


            }
        }
    }

    override fun onResult(planetsList: List<GetAirtimeBillersData>) {
        SecurityLayer.Log("Get Biller Data Name", planetsList[0].billerName)
        planetsList2 = planetsList
        Collections.sort(planetsList) { d1, d2 -> d1.billerName.compareTo(d2.billerName) }
        //  Collections.swap(planetsList,0,planetsList.size()-1);

        mobadapt = ArrayAdapter(this, android.R.layout.simple_spinner_item, planetsList)
        mobadapt!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp1!!.setAdapter(mobadapt)
        sp1!!.setSelection(planetsList.size - 1)
    }

    override fun onProcessingError(error: String) {
        Utility.showToast(error)
    }


    override fun showProgress() {
        viewDialog?.showDialog()
       // prgDialog.show()
    }

    override fun hideProgress() {
        viewDialog?.hideDialog()
        //prgDialog.dismiss()
    }



    override fun logout() {
        session!!.logoutUser()
        // After logout redirect user to Loing Activity
        finish()
        val i = Intent(this, SignInActivity::class.java)
        // Staring Login Activity
        startActivity(i)
        Utility.showToast("You have been locked out of the app.Please call customer care for further details")
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()    //Call the back button's method
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        presenter?.ondestroy()
        super.onDestroy()
    }

}
