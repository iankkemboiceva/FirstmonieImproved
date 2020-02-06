package firstmob.firstbank.com.firstagent.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import firstmob.firstbank.com.firstagent.Activity.*

import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import firstmob.firstbank.com.firstagent.Activity.*
import firstmob.firstbank.com.firstagent.constants.SharedPrefConstants
import firstmob.firstbank.com.firstagent.dialogs.ViewDialog
import firstmob.firstbank.com.firstagent.security.SecurityLayer
import firstmob.firstbank.com.firstagent.utils.Utility.generateHashString
import kotlinx.android.synthetic.main.fragment_new_home_grid.*


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
class NewHomeGrid : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        val views: View = inflater.inflate(R.layout.fragment_new_home_grid, container, false)


        val open_airtime = views.findViewById<RelativeLayout>(R.id.rl1)
        val open_withdrwa = views.findViewById<RelativeLayout>(R.id.rl3)
        val open_paybills = views.findViewById<RelativeLayout>(R.id.rl6)
        val open_p = views.findViewById<RelativeLayout>(R.id.rlinbox)
      //  val rltransferbox = views.findViewById<RelativeLayout>(R.id.rltransfer)
        val rlopenacc = views.findViewById<RelativeLayout>(R.id.rlopenaccinside)
        val txtusid = views.findViewById<TextView>(R.id.usid)

        // Set a click listener for text view object
        open_airtime.setOnClickListener{
            val intent = Intent (getActivity(), AirtimeTransfActivity::class.java)
            this.startActivity(intent)
        }
        open_withdrwa.setOnClickListener{
            val intent = Intent (getActivity(), WithdrawActivity::class.java)
            this.startActivity(intent)
        }
        open_p.setOnClickListener{
            val intent = Intent (getActivity(), MyPerfActivity::class.java)
          //  intent.putExtra("pinna","pinna")
            this.startActivity(intent)
        }
        open_paybills.setOnClickListener{
            val intent = Intent (getActivity(), BillMenuActivity::class.java)
            this.startActivity(intent)
        }


   /*     rltransferbox?.setOnClickListener(){

            val i = Intent(activity, FTMenuActivity::class.java)

            startActivity(i)
        }*/
        rlopenacc?.setOnClickListener(){

            val i = Intent(activity, OpenAccActivity::class.java)

            startActivity(i)
        }







        val rldepo: RelativeLayout? = views?.findViewById(R.id.rl5)
        rldepo?.setOnClickListener(){

            val i = Intent(activity, CashDepoActivity::class.java)

            startActivity(i)
        }


        val rlinboxchos: RelativeLayout? = views?.findViewById(R.id.rlinbox)
        rlinboxchos?.setOnClickListener(){

            val i = Intent(activity, Reportspg::class.java)

            startActivity(i)
        }




        val pin = "12346";
        val hashedpin = generateHashString(pin);
        SecurityLayer.Log(hashedpin)
        val strusid = Prefs.getString(SharedPrefConstants.KEY_USERID,"")
        txtusid.text = "User ID:$strusid"

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
