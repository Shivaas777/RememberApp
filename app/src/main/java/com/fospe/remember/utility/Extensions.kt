package com.fospe.remember.utility

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.fospe.remember.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_name.*

fun EditText.afterTextChangedChangeButtonImage(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
            Log.d("text",editable.toString())
        }
    })
}

fun Button.setBackgroundForRegisterButton(text :String)
{

        if(text.length>1)
        {
            this.setBackgroundResource(R.drawable.button_bg_selected)
            this.isEnabled=true
        }
        else
        {
            this.setBackgroundResource(R.drawable.next)
            this.isEnabled=false
        }

}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}