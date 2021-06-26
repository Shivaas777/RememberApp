package com.omegamark.remember.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.omegamark.remember.R
import com.omegamark.remember.utility.changeToolbarText
import kotlinx.android.synthetic.main.activity_q_r_code_scanner.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class QRCodeScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val requestPermission=this.registerForActivityResult(ActivityResultContracts.RequestPermission()){granted->
       if(granted)
       { codeScanner.startPreview()}
       else{  Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_code_scanner)
        setSupportActionBar(toolbar)
        changeToolbarText("Scan QR Code", supportActionBar, toolbar)
        requestPermission.launch(Manifest.permission.CAMERA)
        codeScanner = CodeScanner(this, scanner_view)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }
}