package com.example.downloadprogress

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class downloadView:View {
    //遮挡移动的距离
    private var distance = 0f
    //遮挡移动动画
    private var ggShow:ValueAnimator? = null
    //圆的圆心
    private var cx = 0f
    private var cy = 0f
    //改变的角度
    private var changeAlpha = 0f
    //矩形的宽
    private var rectX = 0f
    //矩形的高
    private var rectY = 0f
    //角度改变后移动的距离
    private var moveX = 0f
    //矩形移动的距离
    private var translateX = 0f
    //手动代码创建
    constructor(contex: Context):super(contex){}
    //xml创建
    constructor(context: Context, attrs: AttributeSet?):super(context,attrs){  }
//    private val mPath by lazy {
//        Path().apply {
//            moveTo(cx-rectY/4f,cy)
//            lineTo(cx,cy+rectY/4f)
//            moveTo(cx,cy+rectY/4f)
//            lineTo(cx+rectY/4f,cy-rectY/4f)
//        }
//    }
    private val mPaint3 by lazy {
        Paint().apply {
            color = Color.RED
            strokeWidth = 3f
        }
    }
    private val mPaint by lazy {
        Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        }
    }
    private  val mPaint1 by lazy {
        Paint().apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectX = w.toFloat()
        rectY = h.toFloat()
        cx = rectX/2f
        cy = rectY/2f
    }

    //视图
    override fun onDraw(canvas: Canvas?) {
        //创建底层方块，用于后期绘制圆形
        canvas?.drawRoundRect(0f+moveX,0f,rectX-moveX,rectY,changeAlpha,changeAlpha,mPaint1)
        //画勾勾
        canvas?.drawLine(cx-rectY/4f,cy,cx,cy+rectY/4f,mPaint3)
        canvas?.drawLine(cx,cy+rectY/4f,cx+rectY/4f,cy-rectY/4f,mPaint3)
        //创建同色遮挡方块挡住勾勾
        canvas?.drawRect(cx-rectY/4f+distance,cy-rectY/4f,cx+rectY/4f,cy+rectY/4f,mPaint1)
        //创建加载方块
        canvas?.drawRoundRect(0f+translateX,0f,rectX,rectY,changeAlpha,changeAlpha,mPaint)
    }
    //创建平移动画
    private var translateAnim:ValueAnimator? = null
    //创建一个矩形的动画
    private var rectAnim: ValueAnimator? = null
    //创建一个圆角的动画
    private var alphaAnim:ValueAnimator? = null
    //创建动画集
    private val animator = AnimatorSet()
    fun creation(){
        //矩形加载动画
        if(rectAnim == null){
            rectAnim = ValueAnimator.ofFloat(0f,rectX).apply {
                duration = 1000
                repeatCount = 0
                addUpdateListener {
                    translateX = it.animatedValue as Float
                    invalidate()
                }
            }
        }
        //圆角动画
        if(alphaAnim == null){
            alphaAnim = ValueAnimator.ofFloat(0f,rectY/2f).apply {
                duration = 1000
                repeatCount = 0
                addUpdateListener {
                    changeAlpha = it.animatedValue as Float
                    invalidate()
                }
            }
        }
        //移至圆心动画
        if(translateAnim == null){
            translateAnim = ValueAnimator.ofFloat(0f,(rectX-rectY)/2f).apply {
                duration = 1000
                repeatCount = 0
                addUpdateListener {
                    moveX = it.animatedValue as Float
                    invalidate()
                }
            }
        }
        //勾勾显示动画
        if(ggShow == null){
            ggShow = ValueAnimator.ofFloat(0f,rectY/2f).apply {
                duration = 1000
                repeatCount = 0
                addUpdateListener {
                    distance = it.animatedValue as Float
                    invalidate()
                }
            }
        }
        //动画集合进行播放
        animator.apply {
            playSequentially(rectAnim,alphaAnim,translateAnim,ggShow)
            start()
        }
    }
    //启动动画
    fun startAnim(){
        creation()
    }
    //判断是否下载，如果下载就启动动画
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            startAnim()
        }
        return true
    }
}