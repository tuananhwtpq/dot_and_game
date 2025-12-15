package com.example.baseproject.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.baseproject.R

class SimpleCircleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

//    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = Color.RED
//        style = Paint.Style.FILL
//    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var defaultColor = Color.RED

    private var isActivate = false

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SimpleCircleView, 0, 0)

        try {
//            val colorFromXml = typedArray.getColor(R.styleable.SimpleCircleView_circleColor, Color.RED)
//            paint.color = colorFromXml
//            paint.style = Paint.Style.FILL_AND_STROKE

            val defaultColor =
                typedArray.getColor(R.styleable.SimpleCircleView_circleColor, Color.RED)
        } finally {
            typedArray.recycle()
        }

        paint.color = defaultColor
        paint.style = Paint.Style.FILL_AND_STROKE

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isActivate) {
            paint.color = Color.GREEN
        } else {
            paint.color = defaultColor
        }

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        // Tính toạ độ tâm của view (x, y)
        val cx = viewWidth / 2
        val cy = viewHeight / 2

        // Tính bán kính (lấy cạnh nhỏ hơn chia 2 để hình tròn lọt lòng view)
        val radius = minOf(viewWidth, viewHeight) / 2

        // Ra lệnh cho Canvas vẽ hình tròn tại tâm (cx, cy) với bán kính và cọ vẽ đã chọn
        canvas.drawCircle(cx, cy, radius, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 1. Định nghĩa kích thước mong muốn mặc định (ví dụ: 200 pixel)
        // Đây là kích thước View sẽ chọn nếu người dùng set là "wrap_content"
        val desiredSize = 200

        // 2. Tính toán chiều rộng thực tế
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                // Trường hợp 1: Người dùng set cứng (ví dụ: 100dp) hoặc match_parent
                // Ta PHẢI theo kích thước hệ thống giao cho.
                widthSize
            }

            MeasureSpec.AT_MOST -> {
                // Trường hợp 2: Người dùng set wrap_content
                // Hệ thống bảo: "Cậu muốn to bao nhiêu cũng được, nhưng TỐI ĐA chỉ được bằng widthSize thôi".
                // Nên ta lấy số nhỏ hơn giữa (mong muốn) và (giới hạn).
                minOf(desiredSize, widthSize)
            }

            else -> {
                // Trường hợp 3: Không giới hạn (ít gặp, thường trong ScrollView)
                desiredSize
            }
        }

        // 3. Tính toán chiều cao thực tế (Logic tương tự chiều rộng)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> minOf(desiredSize, heightSize)
            else -> desiredSize
        }

        // 4. QUAN TRỌNG NHẤT: Phải gọi hàm này để chốt kích thước với hệ thống
        // Nếu quên dòng này, App sẽ bị Crash ngay lập tức.
        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            isActivate = !isActivate
            invalidate()
            return true
        }

        return super.onTouchEvent(event)
    }

}