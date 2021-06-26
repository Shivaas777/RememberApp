package com.omegamark.remember.ui.members

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omegamark.remember.R
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModel
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.remember.api.models.members.Members
import com.remember.api.network.Url
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_member_details.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.qr_code_dialog.*

class MemberDetailsActivity : AppCompatActivity() {
    private lateinit var user_id :String
    private lateinit var member_id:String
    private lateinit var apiRepository: APIRepository
    private lateinit var membersViewModel: GetAddedMembersViewModel
    private lateinit var membersViewModelfactory: GetAddedMembersViewModelfactory
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var qrImage : Bitmap? = null
    lateinit var restingLatitude:String
    lateinit var restingLongitude:String
    lateinit var qrCodeUrl:String
    lateinit var cover_picUrl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_details)
        setSupportActionBar(toolbar)
        changeToolbarText("Details", supportActionBar, toolbar)
        apiRepository= APIRepository()
        membersViewModelfactory= GetAddedMembersViewModelfactory(apiRepository)
        membersViewModel = ViewModelProvider(this, membersViewModelfactory).get(
            GetAddedMembersViewModel::class.java
        )
        var intent : Intent = getIntent()
        user_id= intent.getStringExtra("user_id")
        member_id=intent.getStringExtra("member_id")
        observeMemberListResponse(this)
        navigate.setOnClickListener {
            val gmmIntentUri: Uri = Uri.parse("google.navigation:q="+restingLatitude+","+restingLongitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        tvQRCode.setOnClickListener { showQRCodeDialog(qrCodeUrl) }
        getMemberDetails()


    }
    fun getMemberDetails()
    {
        showProgressDialog(this, "Fetching member details")
        var params = HashMap<String, String>()
        params["user_id"]= user_id
        params["member_id"]= member_id
        membersViewModel.getSingleMembers(params)
    }

    fun observeMemberListResponse(owner: LifecycleOwner) {
        membersViewModel.getSingleMemberResponse.observe(owner, Observer { response ->
            hideProgreeDialog()

            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {

                            updateView(response.body()!!.response)

                        }
                        false -> {

                        }
                    }
                }
                false -> {


                }
            }
        })

    }

    fun updateView(member: Members){
        restingLatitude= member.resting_in_lat
        restingLongitude=member.resting_in_lon
        qrCodeUrl =member.qr_code_url
        cover_picUrl= member.cover_pic
        if(qrCodeUrl.length>0)
        {tvQRCode.visibility=View.VISIBLE}
        imageView_upper.loadImage(Url.url + member.img_url, this)
        tvUsername.text=member.name
        tvBirthPlace.text=member.born_place
        tvBorndate.text="born: "+member.born_date
        tvDeathDate.text="died: "+member.death_date
        tvDeathPlace.text=member.death_place
        tvRestingPlace.text=member.resting_in
        tvBio.text=member.bio
        tvRelation.text=member.relation
        tvPostCreated.text="Created by:"+member.created_by
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
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK)
        finish()
    }

    fun showQRCodeDialog(url:String)
    {
        bottomSheetDialog= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.qr_code_dialog, null)
        bottomSheetDialog.run {
            setContentView(view)
            show() }
           bottomSheetDialog.imageView_qrCode.loadImage(Url.url + url, this)






    }
}