package mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.models.OnBoardingItem

class OnBoardingItemsAdapter(private val onBoardingItem: List<OnBoardingItem>):
    RecyclerView.Adapter<OnBoardingItemsAdapter.OnBoardingItemViewHolder>() {

    inner class OnBoardingItemViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val imageOnBoarding = view.findViewById<ImageView>(R.id.imageOnBoarding)
        private val textTitle = view.findViewById<TextView>(R.id.textTitle)
        private val  textDescription = view.findViewById<TextView>(R.id.textDescription)

        fun bind (onBoardingItem: OnBoardingItem){
            imageOnBoarding.setImageResource(onBoardingItem.onBoardingImage)
            textTitle.text = onBoardingItem.title
            textDescription.text = onBoardingItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        return OnBoardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        holder.bind(onBoardingItem[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItem.size
    }

}