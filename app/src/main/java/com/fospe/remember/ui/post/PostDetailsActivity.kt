package com.fospe.remember.ui.post

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class PostDetailsActivity : AppCompatActivity() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        setSupportActionBar(toolbar)
        changeToolbarText("Details", supportActionBar, toolbar)
        var scrollingMovementMethod:ScrollingMovementMethod= ScrollingMovementMethod()
        tvPostDetails.movementMethod=scrollingMovementMethod
        btn_addComment.setOnClickListener {
            showAddCommentDialog()
        }

    }

    fun showAddCommentDialog()
    {
        bottomSheetDialog= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_comment, null)
        bottomSheetDialog.run {
            setContentView(view)
            show()
            bottomSheetDialog.etComment.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bottomSheetDialog.tvCount.text =p0?.length.toString()+"/250"
                }


                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
        bottomSheetDialog.btn_publishComment.setOnClickListener {

            bottomSheetDialog.dismiss()
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