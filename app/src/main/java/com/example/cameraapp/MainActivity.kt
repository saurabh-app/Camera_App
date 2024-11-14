package com.example.cameraapp

import android.app.Activity
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() , Animation.AnimationListener {
    private lateinit var textView: TextView
    private lateinit var buttonZoom: Button
    private lateinit var buttonBlink: Button
    private lateinit var cameraButton: Button
    private lateinit var zoom: Animation
    private lateinit var blink: Animation

    private val CAMERA_REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    private val captureImageResult = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"
        textView = findViewById(R.id.textView)
        buttonZoom = findViewById(R.id.buttonZoom)
        buttonBlink = findViewById(R.id.buttonBlink)
        imageView = findViewById(R.id.imageView)
        cameraButton = findViewById(R.id.cameraButton)
        blink = AnimationUtils.loadAnimation(applicationContext, R.anim.blink)
        blink.setAnimationListener(this)
        zoom = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom)
        zoom.setAnimationListener(this)
        buttonZoom.setOnClickListener {
            textView.visibility = View.VISIBLE
            textView.startAnimation(zoom)
        }
        buttonBlink.setOnClickListener {
            textView.visibility = View.VISIBLE
            textView.startAnimation(blink)
        }

        // Button to open the camera
        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }
    private fun openCamera() {
        captureImageResult.launch(null)
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun openCamera() {
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val photo = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(photo)
//        }
//    }


    override fun onAnimationStart(animation:Animation) {}
    override fun onAnimationEnd(animation1:Animation) {}
    override fun onAnimationRepeat(animation:Animation) {}
}