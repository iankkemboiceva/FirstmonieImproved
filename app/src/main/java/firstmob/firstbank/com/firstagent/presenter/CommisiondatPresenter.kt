package firstmob.firstbank.com.firstagent.presenter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs

import firstmob.firstbank.com.firstagent.Activity.R
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.*
import firstmob.firstbank.com.firstagent.contract.CommisionContract
import firstmob.firstbank.com.firstagent.contract.MainContract
import firstmob.firstbank.com.firstagent.security.SecurityLayer
class CommisiondatPresenter : CommisionContract.Presenter, MainContract.GetDataIntractor.OnFinishedListener {
    internal var iView: CommisionContract.IViewCommission? = null
    private var getDataIntractor: MainContract.GetDataIntractor? = null
    internal var context: Context? = null

    constructor(context: Context, iView: CommisionContract.IViewCommission, getDataIntractor: MainContract.GetDataIntractor) {
        this.iView = iView
        this.getDataIntractor = getDataIntractor
        this.context = context
    }

    override fun requestCall(flag : String?,extraparam: String?) {
        iView!!.showProgress()
            val endpoint = "report/genrpt.action"
            val usid = Prefs.getString(KEY_USERID, "NA")
            val agentid = Prefs.getString(AGENTID, "NA")
            val mobileno = Prefs.getString(AGMOB, "NA")
            val params = "1/$usid/$agentid/$mobileno/CMSNRPT/$extraparam"
            var urlparams=""
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint)
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams)
                SecurityLayer.Log("refurl", urlparams)
                SecurityLayer.Log("params", params)
            } catch (e: Exception) {
                SecurityLayer.Log("encryptionerror", e.toString())
            }
            getDataIntractor!!.getResults(this, urlparams)

    }

    override fun ondestroy() {
        iView = null
    }

    override fun onFinished(response: String?) {
        iView!!.hideProgress()
        iView!!.onResult("comission",response)

    }

    override fun onFailure(t: Throwable?) {
        iView!!.hideProgress()
        SecurityLayer.Log("encryptionJSONException", t.toString())
        Toast.makeText(context, context!!.getText(R.string.conn_error), Toast.LENGTH_LONG).show()
    }
}





