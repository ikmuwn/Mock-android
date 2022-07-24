package kim.uno.mock.widget.indicator

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter
import kotlin.math.abs

abstract class PageIndicator<T> : HorizontalScrollView {

    internal val container: LinearLayout by lazy {
        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
        }.also {
            addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
    }

    private val containerParam: LinearLayout.LayoutParams by lazy {
        LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        ).apply {
            if (isFillViewport) {
                weight = 1f
            }
        }
    }

    private val childParam: LayoutParams by lazy {
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
    }

    private enum class State {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    private var scrolling = true
    private var state = State.IDLE
    private val tabs = ArrayList<ArrayMap<String, View>>()
    private var scrolled = 0
    var currentPosition = 0
    var currentPositionOffset = 0f

    private val transitionIds = ArrayList<Int>()

    internal var selectedPosition = 0
    var index: Int = 0
        get() = viewPager?.currentItem
            ?: (recyclerView?.adapter as? RecyclerViewAdapter)?.currentItem
            ?: field
        set(value) {
            val indexCalibrated = positionCalibrate(value)
            items.forEachIndexed { index, item ->
                val normal = if (index == indexCalibrated) 0f else 1f
                val selected = if (index == indexCalibrated) 1f else 0f
                viewScrolled(index, normal, selected)
            }

            viewPager?.takeIf { it.currentItem != value }?.currentItem = value
            (recyclerView?.adapter as? RecyclerViewAdapter)?.currentItem = value
            field = value
        }

    internal val items: ArrayList<T> by lazy { ArrayList() }
    var currentItem: T?
        get() = items.getOrNull(index)
        set(value) {
            setIndex(items.indexOf(value))
        }

    protected fun positionCalibrate(position: Int): Int {
        val adapter = viewPager?.adapter
            ?: recyclerView?.adapter
        return when (adapter) {
            is RecyclerViewAdapter -> adapter.positionCalibrate(position)
            else -> position
        }
    }

    var recyclerView: RecyclerView? = null
        set(recyclerView) {
            field = recyclerView

            recyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDrawOver(
                    c: Canvas,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.onDrawOver(c, parent, state)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    if (firstVisiblePosition == RecyclerView.NO_POSITION) return

                    val position = positionCalibrate(firstVisiblePosition)
                    val rightPosition = positionCalibrate(firstVisiblePosition + 1)

                    val positionOffset =
                        layoutManager.findViewByPosition(firstVisiblePosition)!!.let {
                            -it.left / it.measuredWidth.toFloat()
                        }

                    pageScrolled(position, rightPosition, positionOffset)
                }
            })

            recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        selectedPosition = index
                        pageSelectedUnit?.invoke(
                            items[selectedPosition],
                            selectedPosition,
                            scrolling,
                            true
                        )
                        scrolling = true
                        for (i in 0 until container.childCount) {
                            viewScrolled(
                                i,
                                if (i == index) 0f else 1f,
                                if (i == index) 1f else 0f
                            )
                        }
                    }
                }
            })
        }

    open var viewPager: ViewPager2? = null
        set(pager) {
            field = pager

            pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val position = positionCalibrate(position)
                    val rightPosition = positionCalibrate(position + 1)
                    pageScrolled(position, rightPosition, positionOffset)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val position = positionCalibrate(position)
                    selectedPosition = position
                    if (!scrolling) {
                        scrollToChild(position, 0)
                    } else {
                        pageSelectedUnit?.invoke(items[position], position, true, true)
                    }

                    for (i in 0 until container.childCount) {
                        viewScrolled(
                            i,
                            if (i == position) 0f else 1f,
                            if (i == position) 1f else 0f
                        )
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        scrolling = true
                    }
                }
            })
        }

    var pageSelectedUnit: ((item: T, index: Int, isScroll: Boolean, changed: Boolean) -> Unit)? =
        null

    val hasViewPager: Boolean
        get() = recyclerView != null || viewPager != null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        clipToPadding = false
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }

    private fun pageScrolled(currentPosition: Int, rightPosition: Int, positionOffset: Float) {
        this.currentPosition = currentPosition
        this.currentPositionOffset = positionOffset

        val currentView = container.getChildAt(currentPosition)
        val rightView = container.getChildAt(rightPosition)

        if (scrolling && currentView != null) {
            val rightView = container.getChildAt(rightPosition) ?: currentView
            val widthOffset = (rightView.width - currentView.width) / 2f * positionOffset
            val width = currentView.width + widthOffset
            scrollToChild(currentPosition, (positionOffset * width).toInt())
        }

        val idlePosition = positionCalibrate(selectedPosition)
        if (state == State.IDLE && positionOffset > 0f)
            state = if (currentPosition == idlePosition) State.GOING_RIGHT else State.GOING_LEFT

        val goingRight = currentPosition == idlePosition
        when {
            state == State.GOING_RIGHT && !goingRight -> state = State.GOING_LEFT
            state == State.GOING_LEFT && goingRight -> state = State.GOING_RIGHT
        }

        val effectOffset = if (abs(positionOffset) < 0.0001f) 0f else positionOffset
        if (effectOffset == 0f) {
            state = State.IDLE
        }

        if (scrolling && state != State.IDLE) {
            if (currentView != null) {
                viewScrolled(currentPosition, effectOffset, 1 - effectOffset)
            }

            if (rightView != null) {
                viewScrolled(rightPosition, 1 - effectOffset, effectOffset)
            }
        }

        invalidate()
    }

    private fun scrollToChild(position: Int, offset: Int) {
        val position = positionCalibrate(position)

        var scroll: Int = container.getChildAt(position).left + offset

        if (position > 0 || offset > 0) {
            scroll -= paddingLeft
        }

        if (scroll != scrolled) {
            scrolled = scroll

            // 좌측 padding 2배의 위치에 앵커
//            var left = container.getChildAt(position).left + offset
//            smoothScrollTo(left - (container.paddingLeft * 2), 0)

            // 중앙 정렬
            val measuredWidth: Int = container.getChildAt(position).measuredWidth
            val left: Int = paddingLeft + container.getChildAt(position).left + offset
            val scrollLeft = left + measuredWidth / 2 - this.measuredWidth / 2

            smoothScrollTo(scrollLeft, 0)
        }
    }

    private fun viewScrolled(position: Int, normal: Float, selected: Float) {
        tabs.getOrNull(position)?.get(NORMAL)?.let {
            viewScrolled(view = it, position = position, value = normal)
        }
        tabs.getOrNull(position)?.get(SELECTED)?.let {
            viewScrolled(view = it, position = position, value = selected)
        }
    }

    private fun viewScrolled(view: View, position: Int, value: Float) {
        if (onViewScrolled(
                item = items[position],
                position = position,
                view = view,
                offset = value
            )
        ) {
            return
        }

        if (transitionIds.isNotEmpty()) {
            transitionIds.forEach { id ->
                view.findViewById<View>(id)?.let {
                    it.alpha = value
                }
            }
        } else {
            view.alpha = value
        }
    }

    fun setItems(items: List<T>) {
        this.items.clear()
        tabs.clear()
        container.removeAllViews()
        items.forEach { add(it) }
        setIndex(index = index, scrolling = true)
    }

    private fun add(item: T) {
        items.add(item)

        val index = items.size - 1
        val normalView = createView(item)
        val selectedView = createSelectedView(item)

        val container = FrameLayout(context)
        container.addView(normalView, childParam)
        selectedView?.let { container.addView(it, childParam) }
        container.setOnClickListener {
            val currentItem = positionCalibrate(this.index)
            val changed = currentItem != index
            pageSelectedUnit?.invoke(item, index, false, changed)
            if (changed) {
                setIndex(currentItem + (index - currentItem))
                scrollToChild(index, 0)
                ValueAnimator.ofFloat(0f, 1f).apply {
                    addUpdateListener {
                        val value = it.animatedValue as Float
                        viewScrolled(currentItem, value, 1f - value)
                        viewScrolled(index, 1f - value, value)

                        if (!hasViewPager) {
                            val offset = index - currentItem
                            val position = currentItem + offset * value
                            currentPosition = position.toInt()
                            currentPositionOffset = position - currentPosition
                            invalidate()
                        }
                    }

                    duration = 300L
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }

        this.container.addView(container, containerParam)

        tabs.add(ArrayMap<String, View>().apply {
            put(NORMAL, normalView)
            selectedView?.let { put(SELECTED, it) }
        })
    }

    private fun setIndex(index: Int, scrolling: Boolean = false) {
        this.scrolling = scrolling
        this.index = index
    }

    fun addTransitionIds(vararg ids: Int) {
        transitionIds.addAll(ids.toList())
    }

    abstract fun createView(item: T): View
    open fun createSelectedView(item: T): View? = null
    open fun onViewScrolled(item: T, position: Int, view: View, offset: Float): Boolean {
        return false
    }

    companion object {
        private const val NORMAL = "normal"
        private const val SELECTED = "selected"
    }

}
