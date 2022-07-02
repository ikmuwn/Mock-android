package kim.uno.mock.ui.keyboard

import android.view.LayoutInflater
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.databinding.KeyboardAnimatorFragmentBinding
import kim.uno.mock.extension.autoCleared
import kim.uno.mock.ui.BaseFragment

@AndroidEntryPoint
class KeyboardAnimatorFragment : BaseFragment() {

    private var binding by autoCleared<KeyboardAnimatorFragmentBinding>()

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = KeyboardAnimatorFragmentBinding.inflate(inflater)
        return binding.root
    }

}
