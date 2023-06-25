package com.dc.mlkitsampe.facedetection

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import com.dc.mlkitsampe.databinding.ActivityFaceDetectionBinding
import com.dc.mlkitsampe.utils.CameraUtil
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectionActivity : AppCompatActivity() {
    private val binding: ActivityFaceDetectionBinding by lazy {
        ActivityFaceDetectionBinding.inflate(layoutInflater)
    }

    private val cameraUtil: CameraUtil by lazy {
        CameraUtil()
    }
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val detector: FaceDetector by lazy {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startCamera()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.cameraSwitch.setOnClickListener {
            binding.faceBoxOverlay.clear()
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }
    }

    private fun startCamera() {
        cameraUtil.startCamera(
            context = this,
            lifecycleOwner = this,
            cameraSelected = cameraSelector,
            previewView = binding.previewView,
            object : CameraUtil.CameraListener {
                override fun imageAnalysis(imageProxy: ImageProxy) {
                    processImage(imageProxy)
                }
            }
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImage(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            detector.process(inputImage).addOnSuccessListener { faces ->
                binding.faceBoxOverlay.clear()
                faces.forEach { face ->
                    val faceBox = FaceBox(
                        overlay = binding.faceBoxOverlay,
                        face = face,
                        imageRect = image.cropRect,
                        cameraSelected = cameraSelector
                    )
                    binding.faceBoxOverlay.add(faceBox)
                }
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }


    }
}