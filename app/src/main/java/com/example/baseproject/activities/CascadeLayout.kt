package com.example.baseproject.activities

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class CascadeLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    // 1. Bước ĐO ĐẠC (Manager hỏi nhân viên: "Các chú cần chỗ ngồi rộng bao nhiêu?")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Hàm tiện ích này sẽ tự động gọi onMeasure của TẤT CẢ các view con bên trong
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // Để đơn giản cho bài học đầu tiên, ta cứ lấy kích thước to nhất mà hệ thống cho phép
        // (Thực tế bạn sẽ cần tính toán tổng kích thước các con cộng lại)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    // 2. Bước SẮP XẾP (Manager chỉ tay: "A ngồi đây, B ngồi kia")
    // Đây là hàm quan trọng nhất của ViewGroup
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val indent = 50 // Độ lệch (khoảng cách giữa các view)

        var currentLeft = 0
        var currentTop = 0

        // Duyệt qua từng đứa con (child)
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            // Lấy kích thước mà đứa con đã "đăng ký" ở bước onMeasure
            val width = child.measuredWidth
            val height = child.measuredHeight

            // Ra lệnh bố trí: child.layout(Trái, Trên, Phải, Dưới)
            child.layout(
                currentLeft,
                currentTop,
                currentLeft + width,
                currentTop + height
            )

            // Tăng toạ độ lên để đứa sau nằm lệch đi một chút
            currentLeft += indent
            currentTop += indent
        }
    }
}