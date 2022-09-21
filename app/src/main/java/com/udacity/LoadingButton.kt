package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.provider.CalendarContract
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
     var rightval =0
        set(value) {
            field = value
            invalidate()
        }
    var angle =0f
        set(value) {
            field = value
            invalidate()
        }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    val rect=Rect()
    val paint = Paint()
    val txtpaint = Paint()
    val arcpaint = Paint()

    init {
        buttonState=ButtonState.Completed
        isClickable=true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

       when(event!!.action){
           MotionEvent.ACTION_UP->{
               buttonState=ButtonState.Loading
               val animator=ObjectAnimator.ofInt(this,"rightval",widthSize).apply {
                   duration=2000
               }
               val arcanimator=ObjectAnimator.ofFloat(this,"angle",360f).apply {
                   duration=2000
               }
               animator.doOnEnd {
                   buttonState=ButtonState.Completed
                   rightval=0
                   angle=0f
                   invalidate()
               }
               arcanimator.start()
               animator.start()

           }
       }
        return super.dispatchTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.left=0
        rect.top=0
        rect.right=widthSize
        rect.bottom=heightSize

        paint.setColor(Color.GREEN)

        canvas!!.drawRect(rect,paint)
       if (buttonState==ButtonState.Completed || buttonState==ButtonState.Clicked){
           txtpaint.setColor(Color.BLACK);
           txtpaint.setTextSize(80f);
           canvas.drawText("Download", (widthSize/3).toFloat(), (heightSize/1.5).toFloat(), txtpaint);
       }else if(buttonState==ButtonState.Loading){
           rect.left=0
           rect.top=0
           rect.right= rightval
           rect.bottom=heightSize
           txtpaint.setColor(Color.BLACK);
           txtpaint.setTextSize(80f);
           arcpaint.setColor(Color.BLUE)
           paint.setColor(Color.YELLOW)

           canvas.drawArc(
                10f,10f,010f,10f,
               0f,angle,false,arcpaint
           )
           canvas.drawRect(rect,paint)

           canvas.drawText("Loading", (widthSize/3).toFloat(), (heightSize/1.5).toFloat(), txtpaint)

       }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}