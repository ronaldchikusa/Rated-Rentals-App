package mw.technologies3g.ratedrentalsmalawi.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import mw.technologies3g.ratedrentalsmalawi.R
import mw.technologies3g.ratedrentalsmalawi.activities.ui.adapters.OnBoardingItemsAdapter
import mw.technologies3g.ratedrentalsmalawi.models.OnBoardingItem

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var onBoardingItemsAdapter: OnBoardingItemsAdapter
    private lateinit var indicatorsContainer: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

            setOnBoardingItems()
            setupIndicators()
            setCurrentIndicator(0)

    }

    private fun setOnBoardingItems(){
        onBoardingItemsAdapter = OnBoardingItemsAdapter(
            listOf(
                OnBoardingItem(
                    onBoardingImage = R.drawable.welcome,
                    title = "Welcome To Rated Rentals",
                    description = "Looking forward to assisting you in your search for a new rental property or new tenants."
                ),
                OnBoardingItem(
                    onBoardingImage = R.drawable.search_home,
                    title = "Find Rental Properties ",
                    description = "Take a look at the posted properties. If you are interested you can easily contact the owner."
                ),
                OnBoardingItem(
                    onBoardingImage = R.drawable.add_property,
                    title = "Showcase Homes",
                    description = "Looking for a tenant? Showcase your property with us and a potential tenant will surely contact you."
                ),
                OnBoardingItem(
                    onBoardingImage = R.drawable.contact,
                    title = "Easy Contact",
                    description = "If you are interested in any properties, you can directly contact the owner, via phone call, Email or WhatsApp."
                )
            )
        )

        val onBoardingViewPager = findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onBoardingViewPager.adapter = onBoardingItemsAdapter
        onBoardingViewPager.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        findViewById<ImageView>(R.id.imageNext).setOnClickListener{

            if (onBoardingViewPager.currentItem + 1 < onBoardingItemsAdapter.itemCount){
                onBoardingViewPager.currentItem += 1

            }else{
                navigateToLoginActivity()
            }
        }

        findViewById<TextView>(R.id.textSkip).setOnClickListener {
           navigateToLoginActivity()
        }

    }

    private fun setupIndicators(){
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onBoardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8,0,8,0)

        for (i in indicators.indices){
            indicators[i]= ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int){
        val childCount= indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorsContainer.getChildAt(i) as ImageView

            if (i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }
    }



    private fun navigateToLoginActivity(){
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }


}