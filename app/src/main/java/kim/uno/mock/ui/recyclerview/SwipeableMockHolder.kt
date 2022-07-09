package kim.uno.mock.ui.recyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import kim.uno.mock.R
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.MockHolderBinding
import kim.uno.mock.extension.showToast
import kim.uno.mock.util.recyclerview.DraggableRecyclerAdapter
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter
import kotlin.math.abs

class SwipeableMockHolder(adapter: RecyclerViewAdapter) :
    RecyclerViewAdapter.ViewHolder<MockEntity>(adapter, R.layout.mock_holder),
    DraggableRecyclerAdapter.Swipeable {

    private val binding = MockHolderBinding.bind(itemView)

    override fun onBindView(item: MockEntity, position: Int) {
        super.onBindView(item, position)
        binding.item = item
    }

    override fun onChildDraw(c: Canvas, dX: Float, dY: Float, actionState: Int): Boolean {
        if (dX != 0f) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.RED
            }

            val rectF = RectF(
                binding.root.left.toFloat(),
                binding.root.top.toFloat(),
                binding.root.right.toFloat(),
                binding.root.bottom.toFloat(),
            )
            paint.alpha = when {
                abs(dX) > binding.root.right -> 255
                else -> (abs(dX) / binding.root.right * 255).toInt()
            }
            c.drawRect(rectF, paint)
        }

        return super.onChildDraw(c, dX, dY, actionState)
    }

    override fun onSwiped(): Boolean {
        context.showToast("Swiped ${binding.item!!.message}")
        return super.onSwiped()
    }

}
