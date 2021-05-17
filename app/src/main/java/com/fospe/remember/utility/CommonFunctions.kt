package com.fospe.remember.utility

import android.app.Activity
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.fospe.remember.R
import com.fospe.remember.viewmodels.members.UploadImageViewModel
import com.fospe.remember.viewmodels.relation.GetRelationViewModel
import com.shashank.platform.fancyflashbarlib.Flashbar
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.progress_dialog.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.Permission


private lateinit var progressDailog :Dialog
private lateinit var alertDialog: AlertDialog
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
    alertDialog = builder.create()
    alertDialog.setOnShowListener(OnShowListener {
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,R.color.black))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.black))
    })
        alertDialog.setCancelable(false)
        alertDialog.show()

}

fun hideAlertdialog()
{
    if (alertDialog!=null)
    {
        alertDialog.dismiss()
    }
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

 suspend fun get(bitmap: Bitmap, fileNameToSave: String, context: Context) :File?{

        var file: File? = null
        var path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            .toString() + File.separator + fileNameToSave
        Log.d("Shiva", "path" + path)
        return try {
            file = File(path)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }


  suspend fun  bitmapToFile(bitmap: Bitmap, fileNameToSave: String, context: Context): File?=get(bitmap,fileNameToSave,context)


fun pickGalleryImage(launcher: ActivityResultLauncher<Intent>) {


    var intent = Intent()
    intent = Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    intent.setType("image/*");
    launcher.launch(intent)


}

fun requestMultiplePermission(resultLauncher: ActivityResultLauncher<Array<out String>>,permission :Array<out String>){
    resultLauncher.launch(permission) }

fun uploadImage(imageFile :File?,uploadImageViewModel:UploadImageViewModel)
{

    var finalRequestBody: RequestBody? = null

    val builder = MultipartBody.Builder()
    builder.setType(MultipartBody.FORM)
    var requestBody: RequestBody? = null
    //builder.addFormDataPart("userId", user.id)
    requestBody = imageFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
    builder.addFormDataPart("file", imageFile?.getName(), requestBody!!)
    finalRequestBody = builder.build()
    uploadImageViewModel.getResponse(finalRequestBody)
}

fun getRelation(context: Context,userId:String,relationViewModel: GetRelationViewModel) {
    showProgressDialog(context, "Feting relations")
    var params= HashMap<String, String>()
    params["user_id"]= userId
    relationViewModel.getRelation(params)
}



