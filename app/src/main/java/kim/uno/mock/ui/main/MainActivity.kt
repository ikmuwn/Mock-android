package kim.uno.mock.ui.main

import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.databinding.MainActivityBinding
import kim.uno.mock.extension.applyKeyboardInsetsAnimator
import kim.uno.mock.extension.bottomPadding
import kim.uno.mock.extension.navigationBarHeight
import kim.uno.mock.ui.BaseViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    val viewModel by viewModels<BaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        invalidateNavigationBarHeight()
        binding.root.applyKeyboardInsetsAnimator()

        try {
            val method =
                ValueAnimator::class.java.getDeclaredMethod("setDurationScale", Float::class.java)
            method.isAccessible = true
            method.invoke(null, 1f)
        } catch (e: Exception) {

        }
    }

    private fun invalidateNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.root.bottomPadding = navigationBarHeight.toInt()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        invalidateNavigationBarHeight()
    }

}