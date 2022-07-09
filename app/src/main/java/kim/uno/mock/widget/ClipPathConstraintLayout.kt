package kim.uno.mock.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import kim.uno.mock.R
import kim.uno.mock.extension.getClipPath
import kim.uno.mock.extension.makeClipPath
import kim.uno.mock.extension.toPixel
import kotlin.math.max

open class ClipPathConstraintLayout : ConstraintLayout {

    var clipRadius = true
    var radiusTopLeft = 0
    var radiusTopRight = 0
    var radiusBottomLeft = 0
    var radiusBottomRight = 0

    var clipCutout = true
    var cutoutTopLeft = 0
    var cutoutTopRight = 0
    var cutoutBottomLeft = 0
    var cutoutBottomRight = 0
    var cutoutLeftInset = 0
    var cutoutTopInset = 0
    var cutoutRightInset = 0
    var cutoutBottomInset = 0

    var shadowColor = 0
    var shadowDx = 0f
    var shadowDy = 3f
    var shadowRadius = 0f

    var leftShadowCutout = false
    var topShadowCutout = false
    var rightShadowCutout = false
    var bottomShadowCutout = false

    var strokeWidth = 0f
    var strokeColor = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var onResumeCallback: (() -> Unit)? = null
    var onPauseCallback: (() -> Unit)? = null

    private val clipPath: Path
        get() {
            val radiusTopLeft = max(0f, radiusTopLeft - strokeWidth).toInt()
            val radiusTopRight = max(0f, radiusTopRight - strokeWidth).toInt()
            val radiusBottomLeft = max(0f, radiusBottomLeft - strokeWidth).toInt()
            val radiusBottomRight = max(0f, radiusBottomRight - strokeWidth).toInt()
            return getClipPath(
                radiusTopLeft = radiusTopLeft,
                radiusTopRight = radiusTopRight,
                radiusBottomLeft = radiusBottomLeft,
                radiusBottomRight = radiusBottomRight,
                cutoutTopLeft = cutoutTopLeft,
                cutoutTopRight = cutoutTopRight,
                cutoutBottomLeft = cutoutBottomLeft,
                cutoutBottomRight = cutoutBottomRight,
                cutoutLeftInset = cutoutLeftInset,
                cutoutTopInset = cutoutTopInset,
                cutoutRightInset = cutoutRightInset,
                cutoutBottomInset = cutoutBottomInset,
                inset = strokeWidth
            )
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

    init {
        setWillNotDraw(false)
    }

    private fun applyAttributeSet(attrs: AttributeSet?) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ClipPathConstraintLayout)
        val radius =
            attributes.getDimensionPixelSize(R.styleable.ClipPathConstraintLayout_android_radius, 0)
        setRadius(radius)
        radiusTopLeft = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_android_topLeftRadius,
            radiusTopLeft
        )
        radiusTopRight = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_android_topRightRadius,
            radiusTopRight
        )
        radiusBottomLeft = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_android_bottomLeftRadius,
            radiusBottomLeft
        )
        radiusBottomRight = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_android_bottomRightRadius,
            radiusBottomRight
        )
        cutoutTopLeft = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_topLeftCutout,
            cutoutTopLeft
        )
        cutoutTopRight = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_topRightCutout,
            cutoutTopRight
        )
        cutoutBottomLeft = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_bottomLeftCutout,
            cutoutBottomLeft
        )
        cutoutBottomRight = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_bottomRightCutout,
            cutoutBottomRight
        )
        cutoutLeftInset = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_cutoutLeftInset,
            cutoutLeftInset
        )
        cutoutTopInset = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_cutoutTopInset,
            cutoutTopInset
        )
        cutoutRightInset = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_cutoutRightInset,
            cutoutRightInset
        )
        cutoutBottomInset = attributes.getDimensionPixelSize(
            R.styleable.ClipPathConstraintLayout_cutoutBottomInset,
            cutoutBottomInset
        )
        shadowColor = attributes.getColor(
            R.styleable.ClipPathConstraintLayout_android_shadowColor,
            ResourcesCompat.getColor(resources, R.color.black_alpha_20, null)
        )
        shadowDx = attributes.getFloat(
            R.styleable.ClipPathConstraintLayout_android_shadowDx,
            shadowDx
        )
        shadowDy = attributes.getFloat(
            R.styleable.ClipPathConstraintLayout_android_shadowDy,
            shadowDy
        )
        shadowRadius = attributes.getFloat(
            R.styleable.ClipPathConstraintLayout_android_shadowRadius,
            0f
        ).toPixel()

        leftShadowCutout = attributes.getBoolean(
            R.styleable.ClipPathConstraintLayout_leftShadowCutout,
            leftShadowCutout
        )
        topShadowCutout = attributes.getBoolean(
            R.styleable.ClipPathConstraintLayout_topShadowCutout,
            topShadowCutout
        )
        rightShadowCutout = attributes.getBoolean(
            R.styleable.ClipPathConstraintLayout_rightShadowCutout,
            rightShadowCutout
        )
        bottomShadowCutout = attributes.getBoolean(
            R.styleable.ClipPathConstraintLayout_bottomShadowCutout,
            bottomShadowCutout
        )
        val strokeWidth = attributes.getFloat(
            R.styleable.ClipPathConstraintLayout_android_strokeWidth,
            0f
        )
        if (strokeWidth > 0f) {
            this.strokeWidth = strokeWidth.toPixel()
        }
        strokeColor = attributes.getColor(
            R.styleable.ClipPathConstraintLayout_android_strokeColor,
            strokeColor
        )
        attributes.recycle()

        clipRadius = radiusTopLeft + radiusTopRight + radiusBottomLeft + radiusBottomRight > 0
        clipCutout = cutoutTopLeft + cutoutTopRight + cutoutBottomLeft + cutoutBottomRight > 0
    }

    fun setRadius(radius: Int) {
        radiusTopLeft = radius
        radiusTopRight = radius
        radiusBottomLeft = radius
        radiusBottomRight = radius
    }

    override fun draw(canvas: Canvas?) {
        if (shadowRadius > 0) {
            drawShadow(canvas)
        }

        drawStroke(canvas)

        if (clipRadius || clipCutout) {
            canvas?.clipPath(clipPath)
        }
        super.draw(canvas)
    }

    internal open fun drawShadow(canvas: Canvas?) {
        canvas ?: return

        val offset = shadowRadius * 2
        val shadow = Bitmap.createBitmap(
            (offset + measuredWidth + offset).toInt(),
            (offset + measuredHeight + offset).toInt(),
            Bitmap.Config.ARGB_8888
        )

        Canvas(shadow).apply {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.FILL
                color = shadowColor
                maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL)
            }

            drawPath(
                makeClipPath(
                    width = width,
                    height = height,
                    radiusTopLeft = radiusTopLeft,
                    radiusTopRight = radiusTopRight,
                    radiusBottomLeft = radiusBottomLeft,
                    radiusBottomRight = radiusBottomRight,
                    cutoutTopLeft = cutoutTopLeft,
                    cutoutTopRight = cutoutTopRight,
                    cutoutBottomLeft = cutoutBottomLeft,
                    cutoutBottomRight = cutoutBottomRight,
                    cutoutLeftInset = cutoutLeftInset,
                    cutoutTopInset = cutoutTopInset,
                    cutoutRightInset = cutoutRightInset,
                    cutoutBottomInset = cutoutBottomInset,
                    inset = offset
                ), paint
            )

            val cutoutPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            }

            if (leftShadowCutout) {
                drawRect(
                    RectF(0f, 0f, offset - shadowDx.toPixel(), height.toFloat()),
                    cutoutPaint
                )
            }

            if (topShadowCutout) {
                drawRect(
                    RectF(0f, 0f, width.toFloat(), offset - shadowDy.toPixel()),
                    cutoutPaint
                )
            }

            if (rightShadowCutout) {
                drawRect(
                    RectF(
                        width - offset - shadowDx.toPixel(),
                        0f,
                        width.toFloat(),
                        height.toFloat()
                    ),
                    cutoutPaint
                )
            }

            if (bottomShadowCutout) {
                drawRect(
                    RectF(
                        0f,
                        height - offset - shadowDy.toPixel(),
                        width.toFloat(),
                        height.toFloat()
                    ),
                    cutoutPaint
                )
            }

            val clipPathBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            Canvas(clipPathBitmap).apply {
                drawPath(clipPath, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.FILL
                })
            }
            drawBitmap(clipPathBitmap, offset, offset, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            })
        }

        shadow?.let {
            canvas.drawBitmap(it, -offset + shadowDx.toPixel(), -offset + shadowDy.toPixel(), paint)
        }
    }

    private fun drawStroke(canvas: Canvas?) {
        if (strokeWidth > 0) {
            val stroke = Bitmap.createBitmap(
                measuredWidth,
                measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            Canvas(stroke).apply {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.FILL
                    strokeWidth = this@ClipPathConstraintLayout.strokeWidth
                    color = strokeColor
                }
                drawPath(
                    makeClipPath(
                        width = measuredWidth,
                        height = measuredHeight,
                        radiusTopLeft = radiusTopLeft,
                        radiusTopRight = radiusTopRight,
                        radiusBottomLeft = radiusBottomLeft,
                        radiusBottomRight = radiusBottomRight,
                        cutoutTopLeft = cutoutTopLeft,
                        cutoutTopRight = cutoutTopRight,
                        cutoutBottomLeft = cutoutBottomLeft,
                        cutoutBottomRight = cutoutBottomRight,
                        cutoutLeftInset = cutoutLeftInset,
                        cutoutTopInset = cutoutTopInset,
                        cutoutRightInset = cutoutRightInset,
                        cutoutBottomInset = cutoutBottomInset,
                    ), paint
                )

                val radiusTopLeft = max(0f, radiusTopLeft - strokeWidth).toInt()
                val radiusTopRight = max(0f, radiusTopRight - strokeWidth).toInt()
                val radiusBottomLeft = max(0f, radiusBottomLeft - strokeWidth).toInt()
                val radiusBottomRight = max(0f, radiusBottomRight - strokeWidth).toInt()

                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                drawPath(
                    makeClipPath(
                        width = measuredWidth,
                        height = measuredHeight,
                        radiusTopLeft = radiusTopLeft,
                        radiusTopRight = radiusTopRight,
                        radiusBottomLeft = radiusBottomLeft,
                        radiusBottomRight = radiusBottomRight,
                        cutoutTopLeft = cutoutTopLeft,
                        cutoutTopRight = cutoutTopRight,
                        cutoutBottomLeft = cutoutBottomLeft,
                        cutoutBottomRight = cutoutBottomRight,
                        cutoutLeftInset = cutoutLeftInset,
                        cutoutTopInset = cutoutTopInset,
                        cutoutRightInset = cutoutRightInset,
                        cutoutBottomInset = cutoutBottomInset,
                        inset = strokeWidth
                    ), paint
                )
            }

            stroke?.let {
                canvas?.drawBitmap(it, 0f, 0f, Paint(Paint.ANTI_ALIAS_FLAG))
            }
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            onResumeCallback?.invoke()
        } else {
            onPauseCallback?.invoke()
        }
    }

}
