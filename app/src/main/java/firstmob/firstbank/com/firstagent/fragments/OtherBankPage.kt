package firstmob.firstbank.com.firstagent.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.InboxActivity
import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.Activity.SendOTBActivity
import firstmob.firstbank.com.firstagent.adapter.GetBanksData
import firstmob.firstbank.com.firstagent.adapter.InboxListAdapter
import firstmob.firstbank.com.firstagent.adapter.OTBRetroAdapt
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.contract.InboxContract
import firstmob.firstbank.com.firstagent.contract.OtherBankContract
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.model.GetCommPerfData
import firstmob.firstbank.com.firstagent.network.FetchServerResponse
import firstmob.firstbank.com.firstagent.presenter.InboxPresenter
import firstmob.firstbank.com.firstagent.presenter.OtherBankPresenter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*



class OtherBankPage : Fragment(), OtherBankContract.ILoginView {
    var lv: ListView? = null

    var prgDialog: ProgressDialog? = null
    var aAdpt: OTBRetroAdapt? = null
    var bankname: String? = null
    var bankcode: String? = null


    var otblist: MutableList<GetBanksData> = ArrayList()

    internal lateinit var presenter: OtherBankContract.Presenter
    /*  private static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }
*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.otherbankslist, null) as ViewGroup
        lv = root.findViewById<View>(R.id.lv) as ListView

        prgDialog = ProgressDialog(activity);
        prgDialog!!.setMessage("Loading....");
        prgDialog!!.setCancelable(false);

        presenter = OtherBankPresenter(this, FetchServerResponse())



        val strservdata = Prefs.getString(SharedPrefConstants.KEY_BANKS, "N")
        if (strservdata != null) {
            var servdata: JSONArray? = null
            try {
                servdata = JSONArray(strservdata)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (servdata!!.length() > 0) {
                SetbanksStored()
            } else { //  GetServv();

                presenter.BanksList()
            }
        } else {

            presenter.BanksList()
        }
        lv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val bankname = otblist[position].bankName;
                val bankcode = otblist[position].bankCode;
             //INTENT OBJ


            val i = Intent(activity?.baseContext, SendOTBActivity::class.java)

        //PACK DATA
    i.putExtra("SENDER_KEY", "MyFragment");
        i.putExtra("bankname", bankname);
        i.putExtra("bankcode", bankcode);



        //START ACTIVITY
        activity?.startActivity(i);

        }
        return root
    }

    fun SetbanksStored() {
        otblist.clear()
        try {
            val strservdata = Prefs.getString(SharedPrefConstants.KEY_BANKS, "N")
            val servdata = JSONArray(strservdata)
            if (servdata.length() > 0) {
                var json_data: JSONObject? = null
                for (i in 0 until servdata.length()) {
                    json_data = servdata.getJSONObject(i)
                    //String accid = json_data.getString("benacid");
                    val instName = json_data.optString("instName")
                    val bankCode = json_data.optString("bankCode")
                    otblist.add(GetBanksData(instName, bankCode))
                }
                if (activity != null) {
                    otblist.sortWith(Comparator { d1, d2 -> d1.bankName.compareTo(d2.bankName) })
                    aAdpt = OTBRetroAdapt(otblist, activity)
                    lv!!.adapter = aAdpt
                }
            } else {
                Toast.makeText(
                        activity,
                        "No services available  ",
                        Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) { // TODO Auto-generated catch block
            Toast.makeText(activity, activity!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
            // SecurityLayer.Log(e.toString());
        }
    }


    override fun hideProgress() {
        prgDialog?.dismiss()
    }

    override fun setList(bankslistpar: ArrayList<GetBanksData>) {
        otblist = bankslistpar
        aAdpt = OTBRetroAdapt(otblist, activity)
        lv?.adapter  = aAdpt
    }

    override fun showToast(text: String) {

        Toast.makeText(
                activity,
                text,
                Toast.LENGTH_LONG).show()


    }
    override fun showProgress() {
        prgDialog?.show()
    }


}