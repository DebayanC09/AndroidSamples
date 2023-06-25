package com.dc.mlkitsampe.utils

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraUtil {

    // Initialize background executor
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraSelected: CameraSelector,
        previewView: PreviewView,
        cameraListener: CameraListener
    ) {
        val cameraProvider: ProcessCameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }



        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            //.setTargetRotation(previewView.display.rotation)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    cameraListener.imageAnalysis(imageProxy)
                }
            }

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelected, preview, imageAnalyzer)

        } catch (exc: Exception) {
            Log.e("CameraX", "startCamera: ${exc.message}")
        }
    }

    interface CameraListener {
        fun imageAnalysis(imageProxy: ImageProxy)
    }
}