package com.fospe.remember.ui.post

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.text.SimpleDateFormat
import java.util.*


class CreatePostActivity : AppCompatActivity() {
    lateinit var cal:Calendar
    lateinit var dateSetListener :DatePickerDialog.OnDateSetListener
     var year :Int = 0
    var month:Int =0
    var day :Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        changeToolbarText("Create", supportActionBar, toolbar)
        initialiseCalenderPicker()
        et_date.setOnClickListener{
            val  dpd :DatePickerDialog = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DAY_OF_MONTH,1);
            val dp = dpd.datePicker
            dp.minDate= cal.timeInMillis
            dpd.show()

        }
    }

    fun initialiseCalenderPicker() {
        cal = Calendar.getInstance()
          dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                et_date.setText(sdf.format(cal.time).toString())
            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //
        if(item.itemId == android.R.id.home)
        {
            onBackPressed()
            return true
        }
        else{
            return super.onOptionsItemSelected(item)
        }
    }
}