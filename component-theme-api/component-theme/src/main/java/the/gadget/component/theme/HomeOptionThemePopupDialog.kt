package the.gadget.component.theme

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.api.*
import the.gadget.component.theme.databinding.HomeOptionThemePopupDialogBinding
import the.gadget.fragment.BindingDialogFragment
import the.gadget.theme.ThemeApi
import the.gadget.weight.beVisible
import the.gadget.weight.listener.ViewAnimatorListener
import the.gadget.weight.postAutoRemove

class HomeOptionThemePopupDialog : BindingDialogFragment<HomeOptionThemePopupDialogBinding>() {

    private val albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var isOk = false
            val uri = result.data?.data
            if (uri != null) {
                isOk = true
                CoroutineScope(Dispatchers.IO).launch {
                    ThemeApi.instance.switchTheme(ImageApi.instance.loadWallpaperBitmap(uri))
                }
                dismissAllowingStateLoss()
            }
            if (!isOk) {
                "切换主题失败，请稍后重试或选择其他壁纸！".singleToastS()
            }
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var isOk = false
            ThemeApi.WALLPAPER_TEMP_FILE.also { file ->
                if (file.exists()) {
                    isOk = true
                    CoroutineScope(Dispatchers.IO).launch {
                        ThemeApi.instance.switchTheme(ImageApi.instance.loadWallpaperBitmap(file.path))
                    }
                    dismissAllowingStateLoss()
                }
            }
            if (!isOk) {
                "相机拍摄失败，请稍后重试！".singleToastS()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.home_option_theme_popup_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { dismissAllowingStateLoss() }
        binding.rgbColorPicker.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                ThemeApi.instance.switchTheme(binding.rgbColorPicker.currentColor)
            }
            dismissAllowingStateLoss()
        }
        binding.tvFromCamera.setOnClickListener {
            if (DeviceApi.instance.hasCamera()) {
                val file = ThemeApi.WALLPAPER_TEMP_FILE.also { it.deleteIfExists() }
                val uri = FileProvider.getUriForFile(requireContext(), FileApi.FILE_PROVIDER_NAME, file)
                cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri))
            } else {
                "当前设备摄像头不可用".singleToastS()
            }
        }
        binding.tvFromAlbum.setOnClickListener {
            albumLauncher.launch(Intent().setAction(Intent.ACTION_PICK).setType("image/*"))
        }
        binding.tvReset.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                ThemeApi.instance.switchTheme(ResourceApi.instance.getColorInt(R.color.themeOrigin))
            }
            dismissAllowingStateLoss()
        }

        binding.content.postAutoRemove {
            ObjectAnimator.ofFloat(binding.content, "translationY", binding.content.height.toFloat(), 0F)
                .also {
                    it.duration = 200
                    it.interpolator = DecelerateInterpolator()
                    it.addListener(object : ViewAnimatorListener() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            binding.content.beVisible()
                        }
                    })
                }.start()
        }
    }
}