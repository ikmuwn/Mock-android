package kim.uno.mock.widget.indicator

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.viewpager2.widget.ViewPager2
import kim.uno.mock.R
import kim.uno.mock.databinding.RoundedBarIndicatorBinding
import kim.uno.mock.extension.leftPadding
import kim.uno.mock.extension.rightPadding
import kim.uno.mock.util.recyclerview.InfiniteRecyclerViewAdapter
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class RoundedBarIndicator : PageIndicator<Int> {

    private var indicatorCircleColor = 0
    private var indicatorBarColor = 0
    private var indicatorSize = 0
    private var indicatorSpreadSize = 0
    private var indicatorGap = 0

    override var viewPager: ViewPager2?
        get() = super.viewPager
        set(value) {
            super.viewPager = value
            val adapter = value?.adapter
            if (adapter is InfiniteRecyclerViewAdapter) {
                pages = adapter.getUniqueItemCount()
            } else if (adapter is RecyclerViewAdapter) {
                pages = adapter.itemCount
            }
        }

    private var pages = 0
        set(value) {
            field = value
            setItems(IntArray(value) { it }.toList())
        }

    constructor(context: Context) : super(context) {
        applyAttributeSet(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyAttributeSet(attrs)
    }

    private fun applyAttributeSet(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RoundedBarIndicator)
        indicatorBarColor =
            attributes.getColor(R.styleable.RoundedBarIndicator_indicatorBarColor,
                TypedValue().let {
                    context.theme.resolveAttribute(android.R.attr.colorPrimary, it, true)
                    it.data
                }
            )
        indicatorCircleColor = attributes.getColor(
            R.styleable.RoundedBarIndicator_indicatorCircleColor,
            ColorUtils.blendARGB(Color.TRANSPARENT, indicatorBarColor, 0.3f)
        )
        indicatorSize = attributes.getDimensionPixelSize(
            R.styleable.RoundedBarIndicator_indicatorSize,
            0
        )
        indicatorSpreadSize = attributes.getDimensionPixelSize(
            R.styleable.RoundedBarIndicator_indicatorSpreadSize,
            0
        )
        indicatorGap = attributes.getDimensionPixelSize(
            R.styleable.RoundedBarIndicator_indicatorGap,
            0
        )
        attributes.recycle()

        leftPadding = -indicatorGap / 2
        rightPadding = -indicatorGap / 2
    }

    override fun createView(item: Int): View {
        val binding = RoundedBarIndicatorBinding.inflate(LayoutInflater.from(context), null, false)
        (binding.roundedBar.background as GradientDrawable).setColor(indicatorCircleColor)
        binding.root.leftPadding = indicatorGap / 2
        binding.root.rightPadding = indicatorGap / 2
        binding.roundedBar.layoutParams.apply {
            width = indicatorSize
            height = indicatorSize
        }
        binding.roundedBar.requestLayout()
        return binding.root
    }

    override fun onViewScrolled(item: Int, position: Int, view: View, offset: Float): Boolean {
        super.onViewScrolled(item, position, view, offset)
        val roundedBar = view.findViewById<View>(R.id.rounded_bar)
        (roundedBar.background as GradientDrawable).setColor(
            ColorUtils.blendARGB(
                indicatorCircleColor,
                indicatorBarColor,
                1f - offset
            )
        )

        val spreadOffset = indicatorSpreadSize - indicatorSize
        roundedBar.layoutParams.width = (indicatorSize + (spreadOffset * (1f - offset))).toInt()
        roundedBar.requestLayout()
        return true
    }

}
