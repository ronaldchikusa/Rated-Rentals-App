package mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mw.technologies3g.ratedrentalsmalawi.R


open class BaseFragment : Fragment() {
    //Our progress dialog
    private lateinit var mProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showProgressDialog(){

        mProgressDialog = Dialog(requireActivity())
        /*Set the screen content from a layout source
        * The resource will be inflated, adding all the top level views to the screen*/

        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start dialog and display on screen
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

}