package gr.jkapsouras.butterfliesofgreece.views.cameraView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.View
import androidx.core.graphics.toRect

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    lateinit  var canvas : Canvas
    var tracker: MultiBoxTracker? = null

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        this.canvas = canvas
        if(tracker!=null)
        {
            tracker!!.draw(canvas)
        }
    }
}