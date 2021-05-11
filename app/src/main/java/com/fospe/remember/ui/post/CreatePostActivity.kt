package com.fospe.remember.ui.post

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.fospe.remember.R
import com.fospe.remember.adapters.GetMemberSpinnerAdapter
import com.fospe.remember.utility.*
import com.fospe.remember.viewmodels.Post.CreatePostViewModel
import com.fospe.remember.viewmodels.Post.CreatePostViewModelfactory
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModel
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.remember.api.models.members.Members
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CreatePostActivity : AppCompatActivity() {
    lateinit var cal: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    lateinit var userid: String
    lateinit var apiRepository: APIRepository
    lateinit var getAddedMembersViewModelfactory: GetAddedMembersViewModelfactory
    lateinit var getAddedMembersViewModel: GetAddedMembersViewModel
    lateinit var createPostViewModelfactory: CreatePostViewModelfactory
    lateinit var createPostViewModel: CreatePostViewModel

    lateinit var spinnerAdapter: GetMemberSpinnerAdapter
    lateinit var memberList: ArrayList<Members>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        changeToolbarText("Create", supportActionBar, toolbar)
        var intent = getIntent()
        userid = intent.getStringExtra("user_id")
        apiRepository = APIRepository()
        getAddedMembersViewModelfactory = GetAddedMembersViewModelfactory((apiRepository))
        getAddedMembersViewModel = ViewModelProvider(this, getAddedMembersViewModelfactory).get(GetAddedMembersViewModel::class.java)
        createPostViewModelfactory = CreatePostViewModelfactory(apiRepository)
        createPostViewModel = ViewModelProvider(this, createPostViewModelfactory).get(CreatePostViewModel::class.java)
        memberList = ArrayList()
        spinnerAdapter = GetMemberSpinnerAdapter(this, memberList)
        spinner.adapter = spinnerAdapter
        getAddedMembers()
        observeGetAddedmemberResponse()
        observeCreatePostResponse()
        initialiseCalenderPicker()
        et_date.setOnClickListener {
            val dpd: DatePickerDialog = DatePickerDialog(
                    this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DAY_OF_MONTH, 1);
            val dp = dpd.datePicker
            dp.minDate = cal.timeInMillis
            dpd.show()

        }

        btn_create_memories.setOnClickListener { createPostApiCall() }
        et_title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tvTitleCount.text =p0?.length.toString()+"/50"
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })
        et_desc.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tvDescCount.text =p0?.length.toString()+"/250"
            }


            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun getAddedMembers() {
        showProgressDialog(this, "Feting list of members")
        var params = HashMap<String, String>()
        params["user_id"] = userid
        getAddedMembersViewModel.getMembers(params,1)
    }

    fun observeGetAddedmemberResponse() {
        getAddedMembersViewModel.getAddedmemberResponse.observe(this, Observer { response ->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if(response.body()!!.response.size>0)
                            {
                            spinnerAdapter.setList(this, response.body()!!.response)
                            spinnerAdapter.notifyDataSetChanged()
                            }
                            else{
                                showDialogConfirmation(this, "user alert", "Please create or add a member in your profile", false
                                ) { if (it.equals("positive", true)) finish() }
                            }
                        }
                        false -> {
                            showDialogConfirmation(this, "user alert", response.message(), false
                            ) { if (it.equals("positive", true)) finish() }
                        }
                    }
                }
                false -> {


                    showDialogConfirmation(this, "Network error", "Unable to fetch data", false
                    ) { if (it.equals("positive", true)) finish() }
                }
            }
        })

    }

    fun validate(): Int {
        if (et_date.text.length == 0) {
            return 1
        } else if (et_title.text.length == 0) {
            return 2
        } else if (et_desc.text.length == 0) {
            return 3
        }
        return 0

    }

    fun createPostApiCall() {
        when (validate()) {
            0 -> {
                createPost()
            }
            1 -> {
                btn_create_memories.snack("Please select date")
            }
            2 -> {
                btn_create_memories.snack("Please enter title")
            }
            3 -> {
                btn_create_memories.snack("Please enter description")
            }
        }
    }

    fun createPost() {
        showProgressDialog(this, "Creating memories")
        var members: Members = spinner.selectedItem as Members
        var params = HashMap<String, String>()
        params["user_id"] = userid
        params["member_id"] = members.id.toString()
        params["title"] = et_title.text.toString()
        params["content"] = et_desc.text.toString()
        params["event_date"] = et_date.text.toString()
        createPostViewModel.createPost(params)

    }

    fun observeCreatePostResponse() {
        createPostViewModel.getCreatePostResponse.observe(this, Observer { response ->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            setResult(RESULT_OK)
                            finish()

                        }
                        false -> {
                            showDialogConfirmation(this, "user alert", response.message(), false
                            ) { if (it.equals("positive", true)) finish() }
                        }
                    }
                }
                false -> {


                    showDialogConfirmation(this, "Network error", "Unable to fetch data", false
                    ) { if (it.equals("positive", true)) finish() }
                }
            }
        })
    }

    fun initialiseCalenderPicker() {
        cal = Calendar.getInstance()
        dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "MM/dd/yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    et_date.setText(sdf.format(cal.time).toString())
                }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

}




