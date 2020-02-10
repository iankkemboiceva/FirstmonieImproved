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
class NewHomeGridviewLatest : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views: View = inflater.inflate(R.layout.fragnewhomegrid, container, false)
        val gridvieww = views.findViewById<GridView>(R.id.gridview)
        val rlcomm = views.findViewById<RelativeLayout>(R.id.rlcomm)
        val rlrepo = views.findViewById<RelativeLayout>(R.id.rlrepo)
        val txtusid = views.findViewById<TextView>(R.id.usid)
        val txtagid = views.findViewById<TextView>(R.id.agentid)
        gridvieww.adapter = ImageAdapter(activity)

        // Set an item click listener for grid view items
        gridvieww.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Get the GridView selected/clicked item text


           if(position == 0){
               val i = Intent(activity, FTMenuActivity::class.java)

               startActivity(i)
           }
            if(position == 1){
                val i = Intent(activity, WithdrawActivity::class.java)

                startActivity(i)
            }
            if(position == 2){
                val i = Intent(activity, CashDepoActivity::class.java)

                startActivity(i)
            }
            if(position == 3){
                val i = Intent(activity, AirtimeTransfActivity::class.java)

                startActivity(i)
            }
            if(position == 4){
                val i = Intent(activity, BillMenuActivity::class.java)

                startActivity(i)
            }
            if(position == 5){
                val i = Intent(activity, OpenAccActivity::class.java)

                startActivity(i)
            }
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
