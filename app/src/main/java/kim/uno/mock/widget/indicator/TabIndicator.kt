package kim.uno.mock.widget.indicator

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.ColorUtils
import kim.uno.mock.databinding.TabIndicatorBinding
import kim.uno.mock.databinding.TabSelectedIndicatorBinding
import kim.uno.mock.extension.leftPadding
import kim.uno.mock.extension.rightPadding
import kim.uno.mock.extension.toPixel

open class TabIndicator : UnderlineIndicator<String> {

    override val indicatorColor by lazy {
        TypedValue().let {
            context.theme.resolveAttribute(android.R.attr.colorPrimary, it, true)
            it.data
        }
    }
    override val indicatorHeight by lazy { 2f.toPixel() }
    override val underlineColor by lazy {
        ColorUtils.blendARGB(Color.TRANSPARENT, indicatorColor, 0.3f)
    }
    override val underlineHeight by lazy { 2f.toPixel() }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun createView(item: String): View {
        val binding = TabIndicatorBinding.inflate(LayoutInflater.from(context), null, false)
        binding.item = item
        return binding.root
    }

    override fun createSelectedView(item: String): View {
        val binding = TabSelectedIndicatorBinding.inflate(LayoutInflater.from(context), null, false)
        binding.item = item
        return binding.root
    }

}
