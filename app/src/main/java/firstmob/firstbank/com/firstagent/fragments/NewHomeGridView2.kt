package firstmob.firstbank.com.firstagent.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import firstmob.firstbank.com.firstagent.Activity.*

import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.*
import firstmob.firstbank.com.firstagent.adapter.ImageAdapter
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility.generateHashString
import kotlinx.android.synthetic.main.fragment_new_home_grid.*
import kotlinx.android.synthetic.main.fragnewhomegrid.*
import android.util.DisplayMetrics
import androidx.cardview.widget.CardView
import android.opengl.ETC1.getHeight
import com.github.gcacace.signaturepad.view.ViewTreeObserverCompat.removeOnGlobalLayoutListener
import android.view.ViewTreeObserver
import android.R.layout
import com.github.gcacace.signaturepad.view.ViewTreeObserverCompat.removeOnGlobalLayoutListener
import android.os.Build
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import kotlinx.android.synthetic.main.fragmnewhome2.*
import kotlinx.android.synthetic.main.fragnewhomegrid.rlcomm
import kotlinx.android.synthetic.main.fragnewhomegrid.rlsupport


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewHomeGrid.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewHomeGrid.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewHomeGridview2 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views: View = inflater.inflate(R.layout.fragmnewhome2, container, false)
        val gridvieww = views.findViewById<GridView>(R.id.gridview)
        val rlcomm = views.findViewById<RelativeLayout>(R.id.rlcomm)
        val rlrepo = views.findViewById<RelativeLayout>(R.id.rlrepo)
         val supprt=views.findViewById<RelativeLayout>(R.id.rlsupport)

        val rlcommissions = views.findViewById<LinearLayout>(R.id.commisions)
        val txtagid = views.findViewById<TextView>(R.id.agentid)
        val txtusid = views.findViewById<TextView>(R.id.usid)

        val cardrltrnsfer = views.findViewById<CardView>(R.id.cardrltrnsfer)
        val cardrlwithdraw = views.findViewById<CardView>(R.id.cardrlwithdraw)
        val cardrldepo = views.findViewById<CardView>(R.id.cardrldepo)
        val cardrlairtimer = views.findViewById<CardView>(R.id.cardrlairtimer)
        val cardrlpsybill = views.findViewById<CardView>(R.id.cardrlpsybill)
        val openaccount = views.findViewById<CardView>(R.id.openaccount)
        cardrltrnsfer?.setOnClickListener(){

            val i = Intent(activity, FTMenuActivity::class.java)

            startActivity(i)
        }
        rlcommissions?.setOnClickListener(){

            val i = Intent(activity, CommisionActivity::class.java)

            startActivity(i)
        }
        cardrlwithdraw?.setOnClickListener(){

            val i = Intent(activity, WithdrawActivity::class.java)

            startActivity(i)
        }

        cardrldepo?.setOnClickListener(){

            val i = Intent(activity, CashDepoActivity::class.java)

            startActivity(i)
        }

        cardrlairtimer?.setOnClickListener(){

            val i = Intent(activity, AirtimeTransfActivity::class.java)

            startActivity(i)
        }

        cardrlpsybill?.setOnClickListener(){

            val i = Intent(activity, BillMenuActivity::class.java)

            startActivity(i)
        }

        openaccount?.setOnClickListener(){

            val i = Intent(activity, OpenAccActivity::class.java)

            startActivity(i)
        }

        supprt?.setOnClickListener(){

            val i = Intent(activity, ComplaintsActivity::class.java)

            startActivity(i)
        }
        rlcomm?.setOnClickListener(){

            val i = Intent(activity, CommisionActivity::class.java)

            startActivity(i)
        }

        rlrepo?.setOnClickListener(){

            val i = Intent(activity, MyPerfActivity::class.java)

            startActivity(i)
        }
        rlsupport?.setOnClickListener(){


        }
        val pin = "12346";
        val hashedpin = generateHashString(pin);
        SecurityLayer.Log(hashedpin)
        val strusid = Prefs.getString(SharedPrefConstants.KEY_USERID,"")
        txtusid.text = "User ID:$strusid"

        val stragid = Prefs.getString(SharedPrefConstants.AGENTID,"")
        txtagid.text = "Agent ID:$stragid"

        return views

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewHomeGrid.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                NewHomeGrid().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }


    }
}
