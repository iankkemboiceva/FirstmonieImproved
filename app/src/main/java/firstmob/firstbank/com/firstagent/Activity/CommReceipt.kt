package firstmob.firstbank.com.firstagent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import firstmob.firstbank.com.firstagent.utils.SessionManagement


class CommReceipt : DialogFragment() {
    var txtname: TextView? =null
    var txtmobno: TextView? =null
    var txtamo: TextView? =null
    var refnumber: TextView? =null
    var statuss: TextView? =null
    var txtto: TextView? =null
    var txservtype: TextView? =null
    internal var lystatus: LinearLayout? =null
    internal var session: SessionManagement? =null
    internal var serv: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.commreceipt, container, false)
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            /*  getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);*/
            //   getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        val displayRectangle = Rect()
        val window = getDialog().getWindow()
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle)

        /*   view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        view.setMinimumHeight((int)(displayRectangle.height() * 0.9f));*/
        session = SessionManagement(getActivity())

        lystatus = view.findViewById(R.id.lystatus)
        txtname = view.findViewById(R.id.txt)
        txtto = view.findViewById(R.id.txtto)
        txtmobno = view.findViewById(R.id.txt2)
        txtamo = view.findViewById(R.id.tamo)
        refnumber = view.findViewById(R.id.txtt14)
        statuss = view.findViewById(R.id.status)
        txservtype = view.findViewById(R.id.txt9)
        val bundle = getArguments()
        val recamo = bundle!!.getString("amo", "")
        val recnarr = bundle.getString("narr", "")
        val recnarrtor = bundle.getString("narrtor", "")
        val recdatetime = bundle.getString("datetime", "")
        val recservtime = bundle.getString("servtype", "")
        val recrefno = bundle.getString("refno", "")
        val recstatus = bundle.getString("status", "")
        txtamo!!.setText(recamo)
        refnumber!!.setText(recrefno)
        txtname!!.setText(recnarr)
        txtmobno!!.setText(recdatetime)
        txservtype!!.setText(recservtime)
        txtto!!.setText(recnarrtor)


        if (recstatus == "FAILURE") {
            lystatus!!.visibility = View.VISIBLE
            statuss!!.setTextColor(activity!!.getResources().getColor(R.color.fab_material_red_900))
            statuss!!.setText(recstatus)
        }
        if (recstatus == "SUCCESS") {
            lystatus!!.visibility = View.VISIBLE
            statuss!!.setTextColor(activity!!.getResources().getColor(R.color.fab_material_light_green_900))
            statuss!!.setText(recstatus)
        }


        return view
    }


}// Required empty public constructor
