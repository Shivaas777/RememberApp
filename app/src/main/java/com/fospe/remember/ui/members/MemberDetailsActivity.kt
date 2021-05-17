package com.fospe.remember.ui.members

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import com.fospe.remember.utility.hideProgreeDialog
import com.fospe.remember.utility.loadImage
import com.fospe.remember.utility.showProgressDialog
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModel
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.remember.api.models.members.Members
import com.remember.api.network.Url
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_member_details.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_members.view.*

class MemberDetailsActivity : AppCompatActivity() {
    private lateinit var user_id :String
    private lateinit var member_id:String
    private lateinit var apiRepository: APIRepository
    private lateinit var membersViewModel: GetAddedMembersViewModel
    private lateinit var membersViewModelfactory: GetAddedMembersViewModelfactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_details)
        setSupportActionBar(toolbar)
        changeToolbarText("Details", supportActionBar, toolbar)
        apiRepository= APIRepository()
        membersViewModelfactory= GetAddedMembersViewModelfactory(apiRepository)
        membersViewModel = ViewModelProvider(this,membersViewModelfactory).get(GetAddedMembersViewModel::class.java)
        var intent : Intent = getIntent()
        user_id= intent.getStringExtra("user_id")
        member_id=intent.getStringExtra("member_id")
        observeMemberListResponse(this)
        getMemberDetails()


    }
    fun getMemberDetails()
    {
        showProgressDialog(this,"Fetching member details")
        var params = HashMap<String,String>()
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

    fun updateView(member:Members){

        imageView_upper.loadImage(Url.url+member.img_url,this)
        tvUsername.text=member.name
        tvBirthPlace.text=member.born_place
        tvBorndate.text=member.born_date
        tvDeathDate.text=member.death_date
        tvDeathPlace.text=member.death_place
        tvRestingPlace.text=member.resting_in
        tvBio.text=member.bio
        tvRelation.text=member.relation
        tvPostCreated.text=member.created_by
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
}