package com.example.esemkagym

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.esemkagym.data.model.BarChart
import kotlin.math.max

class HorizontalBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {
    private val barPaint = Paint().apply {
        isAntiAlias = true
    }

    private val textPain = Paint().apply {
        color = Color.WHITE

        textSize = 25f
        isAntiAlias = true
    }

    private var list: MutableList<BarChart> = mutableListOf()

    fun setData(data: MutableList<BarChart>) {
        list.clear()
        list.addAll(data)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (list.count() == 0) return

        val maxVal = list.maxOf { it.value }
        val barHeight = 60f
        val spacing = 40f
        val startX = 150f
        var startY = 80f

        list.forEach { item ->
            val barWidth = (item.value.toFloat() / maxVal) * (width - startX - 50)
            barPaint.color = item.color
            canvas.drawText(item.label, 10f, startY + barHeight / 2, textPain)

            canvas.drawRect(
                startX,
                startY,
                startX + barWidth,
                startY + barHeight,
                barPaint
            )

            startY += barHeight + spacing
        }
    }
}