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

    companion object {
        private const val DEFAULT_BgDown_COLOR = Color.GREEN
        private const val DEFAULT_BgLoad_COLOR = Color.YELLOW
        private const val DEFAULT_TXTLoad_COLOR = Color.BLACK
        private const val DEFAULT_TXTDown_COLOR = Color.BLACK
        private const val DEFAULT_TXTSize = 0f

    }
    val rect=Rect()
    val paint = Paint()
    val txtpaint = Paint()
    val arcpaint = Paint()
    var BgColorDown=DEFAULT_BgDown_COLOR
    var BgColorLoad=DEFAULT_BgLoad_COLOR
    var TxtColorDown=DEFAULT_TXTDown_COLOR
    var TxtColorLoad=DEFAULT_TXTLoad_COLOR
    var TxtSize= DEFAULT_TXTSize

    init {
        buttonState=ButtonState.Completed
        isClickable=true
        setupAttributes(attrs)
    }
    private fun setupAttributes(attrs: AttributeSet?) {

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton,
            0, 0)

        BgColorDown = typedArray.getColor(R.styleable.LoadingButton_BgColorDown,
            DEFAULT_BgDown_COLOR)
        BgColorLoad = typedArray.getColor(R.styleable.LoadingButton_BgColorLoad, DEFAULT_BgLoad_COLOR)
        TxtColorDown = typedArray.getColor(R.styleable.LoadingButton_TxtColorDown, DEFAULT_TXTDown_COLOR)
        TxtColorLoad = typedArray.getColor(R.styleable.LoadingButton_TxtColorLoad,
            DEFAULT_TXTLoad_COLOR)
        TxtSize = typedArray.getFloat(R.styleable.LoadingButton_TxtSize,
            DEFAULT_TXTSize)




        typedArray.recycle()
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

        paint.setColor(BgColorDown)

        canvas!!.drawRect(rect,paint)
       if (buttonState==ButtonState.Completed || buttonState==ButtonState.Clicked){
           txtpaint.setColor(TxtColorDown);
           txtpaint.setTextSize(TxtSize);
           canvas.drawText("Download", (widthSize/3).toFloat(), (heightSize/1.5).toFloat(), txtpaint);
       }else if(buttonState==ButtonState.Loading){
           rect.left=0
           rect.top=0
           rect.right= rightval
           rect.bottom=heightSize
           txtpaint.setColor(TxtColorDown);
           txtpaint.setTextSize(TxtSize);
           arcpaint.setColor(Color.BLUE)
           paint.setColor(BgColorLoad)

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