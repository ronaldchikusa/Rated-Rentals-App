package mw.technologies3g.ratedrentalsmalawi.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RRRadioButton(context: Context, attrs: AttributeSet) : AppCompatRadioButton(context, attrs){

    init {
        applyFont()
    }

    fun applyFont(){
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Metropolis-Bold.otf")
        setTypeface(typeface)
    }
}