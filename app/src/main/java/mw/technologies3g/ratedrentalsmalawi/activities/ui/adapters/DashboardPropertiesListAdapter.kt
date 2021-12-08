package mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_dashboard_property_layout.view.*
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.activities.ui.activities.PropertyDetailsActivity
import mw.technologies3g.ratedrentalsmalawi.models.Properties
import mw.technologies3g.ratedrentalsmalawi.utils.Constants
import mw.technologies3g.ratedrentalsmalawi.utils.GlideLoader

class DashboardPropertiesListAdapter(
    private val context: Context,
    private var list: ArrayList<Properties>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_property_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadPropertyPicture(model.display_image, holder.itemView.iv_dashboard_property_item_image)
            holder.itemView.tv_dashboard_property_item_title.text = model.property_title
            holder.itemView.tv_dashboard_property_item_price.text  = "K${model.property_price}"
            holder.itemView.dashboard_number_of_bedrooms.text = model.property_number_of_bedrooms
            holder.itemView.dashboard_number_of_bathrooms.text = model.property_number_of_bathrooms
            holder.itemView.dashboard_number_of_garage.text = model.property_number_of_garages
            holder.itemView.dashboard_property_area.text = model.property_area_location
            holder.itemView.dashboard_property_district.text = model.property_district_location

            if (model.property_number_of_garages == "0"){
                holder.itemView.dashboard_second_divider.visibility = View.GONE
                holder.itemView.dashboard_number_of_garage.visibility = View.GONE
                holder.itemView.dashboard_number_of_garages.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PROPERTY_ID, model.property_id) //Property id
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}