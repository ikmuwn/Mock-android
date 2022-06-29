package kim.uno.mock.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import kim.uno.mock.extension.hideKeyboard
import kim.uno.mock.extension.instantFocusInTouchMode
import kim.uno.mock.extension.observe
import kim.uno.mock.ui.main.MainActivity
import kim.uno.mock.util.SharedElements

abstract class BaseFragment : Fragment() {

    private var itemView: View? = null
    protected open val activityViewModel by lazy { (activity as MainActivity).viewModel }
    private val backPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    private val fade = Fade().setDuration(200L)

    val sharedElementTransition by lazy {
        MaterialContainerTransform().apply {

            // Optionally add a curved path to the transform
            setPathMotion(MaterialArcMotion())

            // Since View to View transforms often are not transforming into full screens,
            // remove the transition's scrim.
            scrimColor = Color.TRANSPARENT
            duration = 300L
        }
    }

    val sharedElements by lazy {
        SharedElements(
            include = { targetView, include ->
                fade.excludeTarget(targetView, include)
                fade.excludeChildren(targetView, false)
            },
            exclude = { targetView, exclude ->
                sharedElementTransition.excludeTarget(targetView, exclude)
            }
        )
    }

    private var isUserVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = fade

        sharedElementEnterTransition = sharedElementTransition

        fade.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                super.onTransitionEnd(transition)
                sharedElements.clear()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (itemView == null) {
            activityViewModel.dismissProgress()
            itemView = onCreateViewOnce(inflater) ?: View(context)
            itemView?.setOnClickListener {
                it.instantFocusInTouchMode()
                it.hideKeyboard()
            }
            itemView?.post { onCreateView(reenter = false) }
        } else {
            onCreateView(reenter = true)
        }
        return itemView
    }

    open fun onCreateViewOnce(inflater: LayoutInflater): View? = null

    open fun onCreateView(reenter: Boolean) { }

    open fun observeActivityViewModel(viewModel: BaseViewModel) {
        observe(viewModel.progress, activityViewModel::progress)
        observe(viewModel.error, activityViewModel::error)
        observe(viewModel.directions, this::directions)
    }

    private fun directions(directions: NavDirections) {
        findNavController().navigate(directions, sharedElements.getNavigatorExtra())
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressed)
    }

    override fun onStop() {
        super.onStop()
        backPressed.remove()
        itemView?.hideKeyboard()
    }

    open fun onBackPressed() {
        backPressed.isEnabled = false
        activity?.onBackPressed()
        backPressed.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        isUserVisible = true
    }

    override fun onPause() {
        super.onPause()
        isUserVisible = false
    }

}