package com.fospe.remember.utility

import android.app.Activity
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.DialogInterface.OnShowListener
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.fospe.remember.R
import com.shashank.platform.fancyflashbarlib.Flashbar
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.progress_dialog.*


private lateinit var progressDailog :Dialog
fun hideToolbar() {

}

fun changeBG(text: String, editText: EditText, next: Button) {
    Log.d("shiva", "This is a debug message")

    if (text.length > 1 && editText.text.length > 1) {
        next.setBackgroundForRegisterButton("set")
    } else if (text.length == 0) {
        next.setBackgroundForRegisterButton("?")
    }


}

fun showDialogConfirmation(
    context: Context,
    title: String,
    message: String,
    showNegativeButton: Boolean,
    buttonAction: (String) -> Unit
) {

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(message)
        //builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("OK") { dialogInterface, which ->

            buttonAction.invoke("positive")
        }

        //performing negative action
    if(showNegativeButton)
    {
        builder.setNegativeButton("Cancel") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
    alertDialog.setOnShowListener(OnShowListener {
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,R.color.black))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.black))
    })
        alertDialog.setCancelable(false)
        alertDialog.show()

}

fun checkForBiometrics(context: Context) : Boolean{
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

fun changeToolbarText(text: String, supportActionBar: ActionBar?, toolbar: Toolbar) {
    supportActionBar?.apply {

        title=text
        // show back button on toolbar
        // on back button press, it will navigate to parent activity
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(true)
        //setDisplayShowTitleEnabled(false)

    }

    // change the back button color
    toolbar.navigationIcon?.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            colorFilter = BlendModeColorFilter(Color.BLACK, BlendMode.SRC_IN)
        } else {
            setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        }
    }


}

fun showProgressDialog(context: Context, text: String)
{
    progressDailog = Dialog(context)
    progressDailog.setContentView(R.layout.progress_dialog)
    val window: Window? = progressDailog.getWindow()
    progressDailog.setCancelable(false)
    window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    );
    window?.setBackgroundDrawableResource(android.R.color.transparent);
    val wlp: WindowManager.LayoutParams? = window?.getAttributes()

    wlp?.gravity = Gravity.TOP
    wlp?.flags = wlp?.flags
    WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
    window?.setAttributes(wlp)
    progressDailog.tvProgress_text.text=text
    progressDailog.show()
}

fun hideProgreeDialog()
{
    if(progressDailog.isShowing)
    {
        progressDailog.dismiss()
    }


}

fun showSnack(activity: Activity, text: String)
{

        Flashbar.Builder(activity)
            .gravity(Flashbar.Gravity.TOP)
            .message(text)
            .build()

}


