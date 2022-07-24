package kim.uno.mock.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

abstract class UnderlineIndicator<T> : PageIndicator<T> {

    private val indicatorPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = indicatorColor
        }
    }

    abstract val indicatorColor: Int
    abstract val indicatorHeight: Float

    private val underlinePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = underlineColor
        }
    }

    open val underlineColor = 0
    open val underlineHeight = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val currentTab = container.getChildAt(currentPosition) as? ViewGroup ?: return

        val currentContent = currentTab.getChildAt(0)
        var lineLeft = paddingLeft + currentTab.left.toFloat()
        var lineRight = paddingLeft + currentTab.right.toFloat()
        val contentHeight = currentContent.bottom - currentContent.top
        val containerBottom = (height - contentHeight) / 2f + contentHeight

        if (underlineColor != 0 && underlineHeight > 0f) {
            canvas?.drawRect(
                0f,
                containerBottom - underlineHeight,
                max(width, paddingLeft + container.width + paddingRight).toFloat(),
                containerBottom,
                underlinePaint
            )
        }

        if (currentPositionOffset > 0f) {
            var nextTabLeft = lineRight
            var nextTabRight = lineRight

            if (currentPosition < container.childCount - 1) {
                val nextTab: View = container.getChildAt(currentPosition + 1)
                nextTabLeft = paddingLeft + nextTab.left.toFloat()
                nextTabRight = paddingLeft + nextTab.right.toFloat()
            } else {
                val nextTab: View = container.getChildAt(0)
                val nextTabLeft = paddingLeft + nextTab.left.toFloat()
                val nextTabRight = paddingLeft + nextTab.right.toFloat()

                val lineLeft =
                    currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * nextTabLeft
                val lineRight =
                    currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * nextTabLeft
                val rectLeft = lineLeft + currentContent.paddingLeft
                var rectRight = lineRight - currentContent.paddingRight
                if (rectLeft > rectRight) rectRight = rectLeft

                canvas?.drawRect(
                    rectLeft,
                    containerBottom - indicatorHeight,
                    rectRight,
                    containerBottom,
                    indicatorPaint
                )
            }

            lineLeft = currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft
            lineRight =
                currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight
        }

        val rectLeft = lineLeft + currentContent.paddingLeft
        var rectRight = lineRight - currentContent.paddingRight
        if (rectLeft > rectRight) rectRight = rectLeft

        canvas?.drawRect(
            rectLeft,
            containerBottom - indicatorHeight,
            rectRight,
            containerBottom,
            indicatorPaint
        )
    }

}
