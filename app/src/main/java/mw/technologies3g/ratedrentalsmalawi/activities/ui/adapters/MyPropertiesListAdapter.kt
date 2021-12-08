package mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_properties_layout.view.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.activities.ui.activities.MyPropertyDetailsActivity
import mw.technologies3g.ratedrentalsmalawi.activities.ui.fragments.AddPropertiesFragment
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader

open class MyPropertiesListAdapter(
    private val context: Context,
    private var list: ArrayList<Properties>,
    private val fragment: AddPropertiesFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.user_properties_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadPropertyPicture(model.display_image, holder.itemView.iv_property_item_image)
            holder.itemView.tv_my_property_item_title.text = model.property_title
            holder.itemView.tv_my_property_item_price.text = "K${model.property_price}"

            holder.itemView.ib_delete_property.setOnClickListener{
                fragment.deleteMyProperty(model.property_id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, MyPropertyDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PROPERTY_ID, model.property_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {

        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}