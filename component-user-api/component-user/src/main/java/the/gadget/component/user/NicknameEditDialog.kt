package the.gadget.component.user

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.fragment.BindingDialogFragment
import the.gadget.component.user.databinding.NicknameEditDialogBinding

class NicknameEditDialog : BindingDialogFragment<NicknameEditDialogBinding>() {

    override fun getLayoutRes(): Int = R.layout.nickname_edit_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.etNickname.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.tvCancel.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvConfirm.setOnClickListener {
            val nickname = binding.etNickname.text?.toString()?.trim()
            if (!nickname.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    componentUserApi.updateNickname(nickname)
                }
                dismissAllowingStateLoss()
            }
        }
        componentUserApi.getCurrentUser().observe(viewLifecycleOwner) { binding.user = it }
        contentPopup(binding.content)
    }
}