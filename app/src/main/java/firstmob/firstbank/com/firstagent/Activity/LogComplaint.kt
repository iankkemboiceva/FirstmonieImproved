package firstmob.firstbank.com.firstagent.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.logcomplaint.*


class LogComplaint : AppCompatActivity() {

    var stracno: String? = null;var stramo:String? = null; var strefno:kotlin.String? = null;var strdatee:kotlin.String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logcomplaint)


        val intent = intent
        if (intent != null) {
            stracno = intent.getStringExtra("txaco")
            stramo = intent.getStringExtra("txamo")
            strefno = intent.getStringExtra("txref")
            strdatee = intent.getStringExtra("txdate")
            edamo.setText(stramo)
            recacno.setText(stracno)
            edrefno.setText(strefno)
            timestamp.setText(strdatee)

            timestamp.isEnabled = false
            recacno.isEnabled = false
            edrefno.isEnabled = false
            edamo.isEnabled = false
        }
    }
}
