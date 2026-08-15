package gadget.component.scan

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import gadget.basic.fragment.GadgetFragment
import gadget.basic.link.GLink
import gadget.basic.link.GLinkHandler
import gadget.basic.permission.registerCameraPermission
import gadget.component.scan.ui.QRCodeCandidate
import gadget.component.scan.ui.QRCodeScanView
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class ComponentScanFragment : GadgetFragment() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val processing = AtomicBoolean(false)
    private val cameraPermission = registerCameraPermission { granted ->
        if (granted) {
            startCamera()
        } else {
            toastAndClose("没有相机权限")
        }
    }

    private lateinit var scanView: QRCodeScanView
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraBindRequestId = 0
    private var handlingChoice = false
    private var closing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = QRCodeScanView(requireContext()).also { view ->
        scanView = view
        view.onCandidateClick = ::onQRCodeSelected
        view.onOutsideClick = ::restartScanning
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraPermission.request()
    }

    override fun onDestroyView() {
        cameraBindRequestId++
        cameraProvider?.unbindAll()
        cameraProvider = null
        scanView.release()
        processing.set(false)
        handlingChoice = false
        super.onDestroyView()
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

    private fun startCamera() {
        if (!isAlive()) {
            return
        }
        val previewView = scanView.previewView
        val viewPort = previewView.viewPort
        if (viewPort == null) {
            previewView.post {
                if (isAlive() && scanView.previewView === previewView) {
                    startCamera()
                }
            }
            return
        }

        val context = requireContext()
        val providerFuture = ProcessCameraProvider.getInstance(context)
        val requestId = ++cameraBindRequestId
        providerFuture.addListener(
            {
                if (requestId != cameraBindRequestId || !isAlive()) {
                    return@addListener
                }
                runCatching {
                    bindCamera(providerFuture.get(), previewView, viewPort)
                }.onFailure {
                    toastAndClose("相机启动失败")
                }
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    private fun bindCamera(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
        viewPort: ViewPort,
    ) {
        this.cameraProvider = cameraProvider
        val rotation = previewView.display?.rotation ?: Surface.ROTATION_0
        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }
        val analysis = ImageAnalysis.Builder()
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(
                    cameraExecutor,
                    QRCodeAnalyzer(
                        shouldAnalyze = { !processing.get() },
                        onAnalyzeResult = ::onQRCodeAnalyzeResult,
                    ),
                )
            }
        val useCases = UseCaseGroup.Builder()
            .setViewPort(viewPort)
            .addUseCase(preview)
            .addUseCase(analysis)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            useCases,
        )
    }

    private fun onQRCodeAnalyzeResult(result: QRCodeAnalyzeResult) {
        if (!processing.compareAndSet(false, true)) {
            result.release()
            return
        }
        if (!mainHandler.post {
            if (!isAlive()) {
                result.release()
                processing.set(false)
                return@post
            }
            when (result) {
                is QRCodeAnalyzeResult.Single -> handleQRCode(result.text)
                is QRCodeAnalyzeResult.Multiple -> showChoices(result)
            }
        }) {
            result.release()
            processing.set(false)
        }
    }

    private fun showChoices(result: QRCodeAnalyzeResult.Multiple) {
        cameraBindRequestId++
        cameraProvider?.unbindAll()
        handlingChoice = false
        scanView.hideChoices()
        scanView.showChoices(result.bitmap, result.candidates)
    }

    private fun onQRCodeSelected(candidate: QRCodeCandidate) {
        if (handlingChoice) {
            return
        }
        handlingChoice = true
        scanView.enableChoices(false)
        handleQRCode(candidate.text)
    }

    private fun restartScanning() {
        if (handlingChoice) {
            return
        }
        scanView.hideChoices()
        processing.set(false)
        startCamera()
    }

    private fun handleQRCode(text: String) {
        val link = GLink.parse(text)
        if (link == null) {
            toastAndClose("无法识别该二维码")
            return
        }

        lifecycleScope.launch {
            runCatching {
                GLinkHandler.post(link)
            }.onSuccess {
                toastAndClose("识别成功")
            }.onFailure {
                toastAndClose("二维码处理失败")
            }
        }
    }

    private fun toastAndClose(message: String) {
        context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
        close()
    }

    private fun close() {
        if (closing || !isAdded) {
            return
        }
        closing = true

        parentFragmentManager.setFragmentResult(
            ComponentScanContract.REQUEST_SCAN_CLOSED,
            Bundle(),
        )
        parentFragmentManager
            .beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }
}
