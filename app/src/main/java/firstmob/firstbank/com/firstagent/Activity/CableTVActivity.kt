package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import firstmob.firstbank.com.firstagent.adapter.BillMenuParcelable
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.SessionManagement
import firstmob.firstbank.com.firstagent.utils.Utility
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


    class CableTVActivity : AppCompatActivity(), View.OnClickListener {
        internal var imageView1: ImageView? = null

        internal var btnsub: Button? =null
        internal var session: SessionManagement? =null
        //internal var prgDialog2: ProgressDialog? = null
        var viewDialog: ViewDialog? =null
        internal var amon: EditText? =null
        internal var edacc: EditText? =null
        internal var pno: EditText? = null
        internal var txtamount: EditText? =null
        internal var txtnarr: EditText? =null
        internal var edname: EditText? =null
        internal var ednumber: EditText? =null
        internal var billid: String? =null
        internal var serviceid: String? =null
        internal var servlabel: String? =null
        internal var servicename: String? =null
        internal var blname: String? =null
        internal var packid: String? =null
        internal var paycode: String? =null
        internal var charge: String? =null
        internal var billname: TextView? =null
        internal var smcno: TextView? =null
        internal var step2: Button? =null
        internal var step1: Button? =null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cable_tv)

            val toolbar = findViewById(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
            // Get the ActionBar here to configure the way it behaves.
            val ab = supportActionBar
            //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
            ab!!.setDisplayShowHomeEnabled(true) // show or hide the default home button
            ab.setDisplayHomeAsUpEnabled(true)
            ab.setDisplayShowCustomEnabled(true) // enable overriding the default toolbar layout
            ab.setDisplayShowTitleEnabled(false)
            ab!!.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.theme_paybills)));

            session = SessionManagement(this)
            amon = findViewById(R.id.amount) as EditText
            edacc = findViewById(R.id.acc) as EditText
            billname = findViewById(R.id.textView1) as TextView
            smcno = findViewById(R.id.smcno) as TextView
            viewDialog= ViewDialog(this)
//            prgDialog2 = ProgressDialog(this)
//            prgDialog2!!.setMessage("Loading Request....")
//            prgDialog2!!.setCancelable(false)

            btnsub = findViewById(R.id.button2) as Button
            btnsub!!.setOnClickListener(this)
            step2 = findViewById(R.id.tv2) as Button
            step2!!.setOnClickListener(this)
            step1 = findViewById(R.id.tv) as Button
            step1!!.setOnClickListener(this)

            txtamount = findViewById(R.id.amount) as EditText
            txtnarr = findViewById(R.id.ednarr) as EditText
            edname = findViewById(R.id.sendname) as EditText
            ednumber = findViewById(R.id.sendnumber) as EditText


            val ofcListener = MyFocusChangeListener()
            txtamount!!.onFocusChangeListener = ofcListener
            txtnarr!!.onFocusChangeListener = ofcListener
            edname!!.onFocusChangeListener = ofcListener
            ednumber!!.onFocusChangeListener = ofcListener


            val intent = intent
            if (intent != null) {

                val bcp = intent.getParcelableExtra<BillMenuParcelable>("bcp")

                billid = bcp.getbillid()
                serviceid = bcp.getserviceid()
                servicename = bcp.getservicename()
                servlabel = bcp.getservlabel()
                blname = bcp.getbillname()

                packid = bcp.getpackid()
                paycode = bcp.getpaymentCode()
                charge = bcp.getcharge()

                val dispname = bcp.getdispname()
                if (Utility.isNotNull(charge)) {
                    if (charge != "N") {
                        txtamount!!.setText(charge)
                    }
                }
                billname!!.setText(dispname)
                smcno!!.text = servlabel
                edacc!!.hint = servlabel

            }
        }
        override fun attachBaseContext(newBase: Context) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == android.R.id.home) {
                onBackPressed()    //Call the back button's method
                return true
            }

            return super.onOptionsItemSelected(item)
        }


        override fun onClick(view: View) {
            if (view.id == R.id.button2) {

                if (Utility.checkInternetConnection()) {
                    val recanno = edacc!!.text.toString()
                    val amou = txtamount!!.text.toString()
                    val narra = txtnarr!!.text.toString()
                    val ednamee = edname!!.text.toString()
                    val ednumbb = ednumber!!.text.toString()
                    if (Utility.isNotNull(recanno)) {
                        if (Utility.isNotNull(amou)) {
                            val nwamo = amou.replace(",", "")
                            SecurityLayer.Log("New Amount", nwamo)
                            val txamou = java.lang.Double.parseDouble(nwamo)
                            if (txamou >= 100) {
                                if (Utility.isNotNull(narra)) {
                                    if (Utility.isNotNull(ednamee)) {
                                        if (Utility.isNotNull(ednumbb)) {
                                            val intent = Intent(this@CableTVActivity, ConfirmCableActivity::class.java)
                                            //    PayBillResp(params);
                                            intent.putExtra("custid", recanno)
                                            intent.putExtra("amou", amou)
                                            intent.putExtra("narra", narra)
                                            intent.putExtra("ednamee", ednamee)
                                            intent.putExtra("ednumbb", ednumbb)
                                            intent.putExtra("billid", billid)
                                            intent.putExtra("billname", blname)
                                            intent.putExtra("serviceid", serviceid)
                                            intent.putExtra("servicename", servicename)
                                            intent.putExtra("label", servlabel)
                                            intent.putExtra("packId", packid)
                                            intent.putExtra("paymentCode", paycode)
                                            startActivity(intent)

                                        } else {
                                            Utility.showToast("Please enter a valid value for Depositor Number")
                                        }
                                    } else {
                                        Utility.showToast("Please enter a valid value for Depositor Name")
                                    }
                                } else {
                                    Utility.showToast("Please enter a valid value for Narration")
                                }
                            } else {
                                Utility.showToast("Please enter an amount value more than 100 Naira")
                            }
                        } else {
                            Utility.showToast("Please enter a valid value for Amount")
                        }
                    } else {
                        Utility.showToast("Please enter a value for $servlabel")
                    }
                }


            }
            if (view.id == R.id.tv2) {

                finish()
                val intent = Intent(this@CableTVActivity, BillMenuActivity::class.java)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                startActivity(intent)
            }
            if (view.id == R.id.tv) {

                onBackPressed()

            }

        }


        private inner class MyFocusChangeListener : View.OnFocusChangeListener {

            override fun onFocusChange(v: View, hasFocus: Boolean) {

                if (v.id == R.id.amount && !hasFocus) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    val txt = txtamount!!.text.toString()
                    val fbal = Utility.returnNumberFormat(txt)
                    txtamount!!.setText(fbal)

                }

                if (v.id == R.id.ednarr && !hasFocus) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)


                }
                if (v.id == R.id.sendname && !hasFocus) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)


                }
                if (v.id == R.id.sendnumber && !hasFocus) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)


                }

            }
        }
        private fun dismissProgressDialog() {
            if (viewDialog != null ) {
                viewDialog!!.hideDialog()
            }
        }

        override fun onDestroy() {
            dismissProgressDialog()
            super.onDestroy()
        }


    }


