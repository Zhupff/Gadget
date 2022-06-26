package the.gadget.module.user

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.fragment.BindingDialogFragment
import the.gadget.module.user.databinding.NicknameEditDialogBinding
import the.gadget.user.UserApi

class NicknameEditDialog : BindingDialogFragment<NicknameEditDialogBinding>() {

    override fun getLayoutRes(): Int = R.layout.nickname_edit_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvCancel.setOnClickListener { dismissAllowingStateLoss() }
        binding.tvConfirm.setOnClickListener {
            val nickname = binding.etNickname.text?.toString()?.trim()
            if (!nickname.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    UserApi.instance.updateNickname(nickname)
                }
                dismissAllowingStateLoss()
            }
        }
        UserApi.instance.getCurrentUser().observe(viewLifecycleOwner) { binding.user = it }
        contentPopup(binding.content)
    }
}