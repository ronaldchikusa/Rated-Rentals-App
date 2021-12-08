package mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_add_properties.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.activities.ui.activities.AddRentalPropertyActivity
import mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters.MyPropertiesListAdapter
import mw.technologies3g.ratedrentalsmalawi.databinding.FragmentAddPropertiesBinding
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import java.util.*

class AddPropertiesFragment : BaseFragment() {


    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentAddPropertiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_property_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_add_property->{
                startActivity(Intent(activity, AddRentalPropertyActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    fun successPropertyListFromFirestore(propertyList: ArrayList<Properties>){


        if (propertyList.size > 0){

            recycler_view_my_personal_properties.visibility = View.VISIBLE
            tv_no_properties_found.visibility = View.GONE
            illustration_add_property.visibility = View.GONE

            recycler_view_my_personal_properties.layoutManager = LinearLayoutManager(activity)

            recycler_view_my_personal_properties.setHasFixedSize(true)


            val adapterMyProperties = MyPropertiesListAdapter(requireActivity(), propertyList,this@AddPropertiesFragment)
            recycler_view_my_personal_properties.adapter = adapterMyProperties

            hideProgressDialog()
        }else{
            tv_no_properties_found.visibility = View.VISIBLE
            illustration_add_property.visibility = View.VISIBLE
            recycler_view_my_personal_properties.visibility = View.GONE

            hideProgressDialog()
        }
    }

    fun deleteMyProperty(propertyID: String){

        showAlertDialogToDeleteProduct(propertyID)
    }

    fun propertyDeleteSuccess(){
        hideProgressDialog()
        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.property_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        getPropertyListFromFirestore()
    }

    private fun getPropertyListFromFirestore(){
        showProgressDialog()
        FirestoreClass().getPropertyList(this@AddPropertiesFragment)
    }

    override fun onResume() {
        super.onResume()
            getPropertyListFromFirestore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String){

        val builder = AlertDialog.Builder(requireActivity())
        //set the title for the alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for the dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(R.drawable.ic_verctor_warning)

        //perform positive action
        builder.setPositiveButton(resources.getString(R.string.yes)){
                dialogInterface,_ ->
            showProgressDialog()
            FirestoreClass().deleteProperty(this,productID)
            dialogInterface.dismiss()
        }
        //perform negative action
        builder.setNegativeButton(resources.getString(R.string.no)){
                dialogInterface,_ ->

            dialogInterface.dismiss()
        }

        //create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        //set other AlertDialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentAddPropertiesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}