package kim.uno.mock.ui.main

import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.databinding.MainActivityBinding
import kim.uno.mock.extension.*
import kim.uno.mock.ui.BaseViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    val viewModel by viewModels<BaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        invalidateSystemWindows()
        binding.root.applyKeyboardInsetsAnimator()

        try {
            val method =
                ValueAnimator::class.java.getDeclaredMethod("setDurationScale", Float::class.java)
            method.isAccessible = true
            method.invoke(null, 1f)
        } catch (e: Exception) {

        }
    }

    private fun invalidateSystemWindows() {
        binding.root.topPadding = statusBarHeight
        binding.root.bottomPadding = navigationBarHeight.toInt()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        invalidateSystemWindows()
    }

}
