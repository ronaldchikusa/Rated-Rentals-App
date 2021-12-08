package mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.activities.ui.activities.*
import mw.technologies3g.ratedrentalsmalawi.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {


    //private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_profile->{
                startActivity(Intent(activity,ViewUserProfileActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


                button_real_estate_agencies.setOnClickListener{
                    startActivity(Intent(view.context,RealEstateAgencyActivity::class.java))
                }

                button_about_us.setOnClickListener{

                    startActivity(Intent(view.context,AboutUsActivity::class.java))
                }

                button_log_out.setOnClickListener {

                    val builder = AlertDialog.Builder(requireActivity())
                    //set the title for the alert dialog
                    builder.setTitle(resources.getString(R.string.logout_dialog_title))
                    //set message for the dialog
                    builder.setMessage(resources.getString(R.string.you_sure_you_wanna_log_out))
                    builder.setIcon(R.drawable.ic_verctor_warning)

                    //perform positive action
                    builder.setPositiveButton(resources.getString(R.string.yes)){
                            dialogInterface,_ ->
                        showProgressDialog()
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(view.context,LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity?.finish()
                          hideProgressDialog()
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

                button_app_instructions.setOnClickListener{
                    startActivity(Intent(view.context,InstructionsActivity::class.java))
                }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

