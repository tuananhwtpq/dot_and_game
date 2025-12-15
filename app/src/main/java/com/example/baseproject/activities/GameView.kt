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

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val playerOneBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val playerTwoBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs,R.styleable.GameView, 0,0)

        try {

            val dotPaintColor = typedArray.getColor(R.styleable.GameView_dotsColor, Color.BLACK)
            dotPaint.color = dotPaintColor

            val linePainColor = typedArray.getColor(R.styleable.GameView_lineColor, Color.GREEN)
            linePaint.color = linePainColor

            val playerOneColor = typedArray.getColor(R.styleable.GameView_player_1_color, Color.RED)
            playerOneBoxPaint.color = playerOneColor

            val playerTwoColor = typedArray.getColor(R.styleable.GameView_player_2_color, Color.CYAN)
            playerTwoBoxPaint.color = playerTwoColor

        } catch (e: Exception){
            Toast.makeText(context, "Loi gameview: ${e.message} - ${e.printStackTrace()}", Toast.LENGTH_LONG).show()
        } finally {
            typedArray.recycle()
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()


    }
}