package mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_dashboard_property_layout.*
import mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters.DashboardPropertiesListAdapter
import mw.technologies3g.ratedrentalsmalawi.databinding.FragmentDashboardBinding
import mw.technologies3g.ratedrentalsmalawi.firestore.FirestoreClass
import mw.technologies3g.ratedrentalsmalawi.models.Properties

class DashboardFragment : BaseFragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun successDashboardPropertiesList(dashboardItemList: ArrayList<Properties>){

        if (dashboardItemList.size > 0){

            recycler_view_dashboard_properties.visibility = View.VISIBLE
            tv_no_properties_found.visibility = View.GONE

            recycler_view_dashboard_properties.layoutManager = LinearLayoutManager(activity)
            recycler_view_dashboard_properties.setHasFixedSize(true)

            val dashAdapter = DashboardPropertiesListAdapter(requireActivity(),dashboardItemList)
            recycler_view_dashboard_properties.adapter = dashAdapter

            hideProgressDialog()

        }else{

            tv_no_properties_found.visibility = View.VISIBLE
            recycler_view_dashboard_properties.visibility = View.GONE
            hideProgressDialog()
        }
    }


    private fun getDashboardPropertiesList(){
        showProgressDialog()
        FirestoreClass().getDashboardPropertyList(this@DashboardFragment)
    }


    override fun onResume() {
        super.onResume()
        getDashboardPropertiesList()
    }
}