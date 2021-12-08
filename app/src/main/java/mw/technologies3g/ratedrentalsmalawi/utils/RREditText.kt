package mw.technologies3g.ratedrentalsmalawi.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class RREditText(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {

    init {
        applyfont()
    }

    private fun applyfont(){
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Metropolis-Regular.otf")
        setTypeface(typeface)

    }
}