package com.example.baseproject.utils


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.example.baseproject.R


class TextViewOutlineSecond : AppCompatTextView {
    private var mOutlineSize = 0
    private var mOutlineColor = 0
    private var mTextColor = 0

    private var mShadowRadius = 0f
    private var mShadowDx = 0f
    private var mShadowDy = 0f
    private var mShadowColor = 0

    constructor(@NonNull context: Context) : super(context)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        setAttributes(attrs)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        val mText: CharSequence? = text?.let { " $it " }
        super.setText(mText, type)
    }

    private fun setAttributes(attr: AttributeSet?) {
        mOutlineSize = DEFAULT_OUTLINE_SIZE
        mOutlineColor = DEFAULT_OUTLINE_COLOR

        mTextColor = getCurrentTextColor()
        if (attr != null) {
            val a = getContext().obtainStyledAttributes(attr, R.styleable.TextViewOutlineSecond)
            if (a.hasValue(R.styleable.TextViewOutlineSecond_outlineSize)) {
                mOutlineSize = a.getDimension(
                    R.styleable.TextViewOutlineSecond_outlineSize,
                    DEFAULT_OUTLINE_SIZE.toFloat()
                ).toInt()
            }

            if (a.hasValue(R.styleable.TextViewOutlineSecond_outlineColor)) {
                mOutlineColor =
                    a.getColor(
                        R.styleable.TextViewOutlineSecond_outlineColor,
                        DEFAULT_OUTLINE_COLOR
                    )
            }

            if (a.hasValue(R.styleable.TextViewOutlineSecond_android_shadowRadius)
                || a.hasValue(R.styleable.TextViewOutlineSecond_android_shadowDx)
                || a.hasValue(R.styleable.TextViewOutlineSecond_android_shadowDy)
                || a.hasValue(R.styleable.TextViewOutlineSecond_android_shadowColor)
            ) {
                mShadowRadius =
                    a.getFloat(R.styleable.TextViewOutlineSecond_android_shadowRadius, 0f)
                mShadowDx = a.getFloat(R.styleable.TextViewOutlineSecond_android_shadowDx, 0f)
                mShadowDy = a.getFloat(R.styleable.TextViewOutlineSecond_android_shadowDy, 0f)
                mShadowColor =
                    a.getColor(
                        R.styleable.TextViewOutlineSecond_android_shadowColor,
                        Color.TRANSPARENT
                    )
            }

            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setPaintToOutline()
    }

    private fun setPaintToOutline() {
        val paint: Paint = getPaint()
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(mOutlineSize.toFloat())
//        paint.strokeJoin = Paint.Join.ROUND
        super.setTextColor(mOutlineColor)
        super.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    }

    private fun setPainToRegular() {
        val paint: Paint = getPaint()
        paint.setStyle(Paint.Style.FILL)
        paint.setStrokeWidth(0f)
        super.setTextColor(mTextColor)
        super.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        mTextColor = color
    }

    override fun onDraw(canvas: Canvas) {
        setPaintToOutline()
        super.onDraw(canvas)

        setPainToRegular()
        super.onDraw(canvas)
    }

    companion object {
        private const val DEFAULT_OUTLINE_SIZE = 0
        private val DEFAULT_OUTLINE_COLOR = Color.TRANSPARENT
    }
}