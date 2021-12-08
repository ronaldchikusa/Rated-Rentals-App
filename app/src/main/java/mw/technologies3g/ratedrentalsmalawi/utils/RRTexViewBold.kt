package mw.technologies3g.ratedrentalsmalawi.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RRTexViewBold (context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {

    init {

        applyfont()
    }

    private fun applyfont(){
        //Get proper font
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Metropolis-Bold.otf")
        setTypeface(typeface)
    }
}