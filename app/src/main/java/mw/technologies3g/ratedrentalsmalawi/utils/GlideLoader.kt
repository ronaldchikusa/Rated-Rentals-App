package mw.technologies3g.ratedrentalsmalawi.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mw.technologies3g.ratedrentalsmalawi.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            //Load the user image in the imageview
            Glide
                .with(context)
                .load(image)//URI of the image
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)//A default placeholder if image is failed to load
                .into(imageView)//the view in which the image is loaded

        }catch (e: IOException){
            e.printStackTrace()
        }
    }


    fun loadPropertyPicture(image: Any, imageView: ImageView){
        try {
            //Load the user image in the imageview
            Glide
                .with(context)
                .load(image)//URI of the image
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                //.apply(RequestOptions().override(600,200))
                //.override(1280,720)
                //No placeholder here
                .into(imageView)//the view in which the image is loaded

        }catch (e: IOException){
            e.printStackTrace()
        }
    }

}