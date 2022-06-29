package kim.uno.mock.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.navigation.fragment.FragmentNavigator

class SharedElements(
    private val include: (targetView: View, include: Boolean) -> Unit,
    private val exclude: (targetView: View, exclude: Boolean) -> Unit
) {

    private val elements = ArrayList<View>()
    private val includes = ArrayList<View>()
    private val excludes = ArrayList<View>()

    fun addTarget(vararg view: View): SharedElements {
        elements.addAll(view.toList())
        includeTarget(excludeChildren = false, view = *view)
        return this
    }

    fun addTargetWithExcludeChildren(vararg view: View): SharedElements {
        elements.addAll(view.toList())
        includeTarget(excludeChildren = true, view = *view)
        return this
    }

    private fun includeTarget(excludeChildren: Boolean, vararg view: View): SharedElements {
        view
            .filter { !includes.contains(it) && !excludes.contains(it) }
            .forEach {
                includes.add(it)
                include(it, true)
                if (excludeChildren && it is ViewGroup) {
                    excludeChildren(it)
                }
            }
        return this
    }

    fun excludeTarget(vararg view: View): SharedElements {
        view
            .filter { !includes.contains(it) && !excludes.contains(it) }
            .forEach {
                excludes.add(it)
                exclude(it, true)
            }
        return this
    }

    fun excludeChildren(view: ViewGroup): SharedElements {
        view.children.forEach {
            excludeTarget(it)
        }
        return this
    }

    fun getTransitionName() = elements.firstOrNull()?.transitionName

    fun getNavigatorExtra() =
        FragmentNavigator.Extras.Builder().apply {
            addSharedElements(elements.associateWith { it.transitionName })
        }.build()

    fun clear() {
        elements.clear()
        includes.forEach { include(it, false) }
        includes.clear()
        excludes.forEach { exclude(it, false) }
        excludes.clear()
    }

}