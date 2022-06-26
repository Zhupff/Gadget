package the.gadget.module.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.*
import the.gadget.api.ImageApi
import the.gadget.api.singleToastS
import the.gadget.fragment.BindingDialogFragment
import the.gadget.fragment.FragmentApi
import the.gadget.module.user.databinding.UserInfoPopupDialogBinding
import the.gadget.user.UserApi
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class UserInfoPopupDialog : BindingDialogFragment<UserInfoPopupDialogBinding>() {
    companion object {
        private val AVATAR_LOCK: AtomicBoolean = AtomicBoolean(false)

        private fun isAvatarLock(): Boolean = AVATAR_LOCK.get()

        private fun launch(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
        ): Job {
            AVATAR_LOCK.set(true)
            return CoroutineScope(Dispatchers.IO).launch(context, start, block).also {
                it.invokeOnCompletion { AVATAR_LOCK.set(false) }
            }
        }
    }

    private val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var isOk = false
            val uri = result.data?.data
            if (uri != null) {
                isOk = true
                launch {
                    UserApi.instance.updateAvatar(ImageApi.instance.loadAvatarBitmap(uri))
                }
            }
            if (!isOk) {
                "切换头像失败，请稍后重试或选择其他图片".singleToastS()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.user_info_popup_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.avatarLayout.setOnClickListener {
            if (isAvatarLock()) {
                "正在生成头像".singleToastS()
            } else {
                albumLauncher.launch(Intent().setAction(Intent.ACTION_PICK).setType("image/*"))
            }
        }
        binding.nicknameLayout.setOnClickListener {
            FragmentApi.instance.showDialogFragment(childFragmentManager, NicknameEditDialog())
        }
        UserApi.instance.getCurrentUser().observe(viewLifecycleOwner) { binding.user = it }
        contentPopup(binding.content)
    }
}