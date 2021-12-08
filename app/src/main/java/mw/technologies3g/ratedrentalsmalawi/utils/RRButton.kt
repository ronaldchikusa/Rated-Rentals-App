package mw.technologies3g.ratedrentalsmalawi.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class RRButton(context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {

    init {

        applyfont()
    }

    private fun applyfont(){

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Metropolis-Bold.otf")
        setTypeface(typeface)
    }
}