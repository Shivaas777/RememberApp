package com.fospe.remember.utility

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_password.*

fun hideToolbar() {

}

fun changeBG(text : String, editText: EditText, next : Button) {
    Log.d("shiva", "This is a debug message")

    if (text.length > 1 && editText.text.length > 1) {
        next.setBackgroundForRegisterButton("set")
    } else if (text.length == 0) {
        next.setBackgroundForRegisterButton("?")
    }


}

fun showDialogConfirmation ( context: Context,buttonAction:(String) -> Unit) {

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Security Alert")
        //set message for alert dialog
        builder.setMessage("Do you want to add fingerprint security")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            buttonAction.invoke("positive")
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

}

fun checkForBiometrics(context :Context) : Boolean{
    Log.d("SHIVA", "checkForBiometrics started")
    var canAuthenticate = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Build.VERSION.SDK_INT < 29) {
            val keyguardManager : KeyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            val packageManager : PackageManager = context.packageManager
            if(!packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                Log.w("SHIVA", "checkForBiometrics, Fingerprint Sensor not supported")
                canAuthenticate = false
            }
            if (!keyguardManager.isKeyguardSecure) {
                Log.w("SHIVA", "checkForBiometrics, Lock screen security not enabled in Settings")
                canAuthenticate = false
            }
        } else {
            val biometricManager : BiometricManager = context.getSystemService(BiometricManager::class.java)
            if(biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS){
                Log.w("SHIVA", "checkForBiometrics, biometrics not supported")
                canAuthenticate = false
            }
        }
    }else{
        canAuthenticate = false
    }
    Log.d("SHIVA", "checkForBiometrics ended, canAuthenticate=$canAuthenticate ")
    return canAuthenticate
}


