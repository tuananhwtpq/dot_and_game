package com.example.baseproject.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.baseproject.R
import java.lang.Exception
import kotlin.coroutines.Continuation

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dotPaintDefault = Color.BLACK

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaintDefault = Color.GREEN

    private val playerOneBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerOneBoxPaintDefault = Color.CYAN

    private val playerTwoBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerTwoBoxPaintDefault = Color.YELLOW

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs,R.styleable.GameView, 0,0)

        try {

            val dotPaintColor = typedArray.getColor(R.styleable.GameView_dotsColor, Color.BLACK)

        } catch (e: Exception){
            Toast.makeText(context, "Loi gameview: ${e.message} - ${e.printStackTrace()}", Toast.LENGTH_LONG).show()
        } finally {
            typedArray.recycle()
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}