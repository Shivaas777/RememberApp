package com.fospe.remember.ui.members

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fospe.remember.R
import com.fospe.remember.adapters.MembersListAdapter
import com.fospe.remember.adapters.PostListAdapter
import com.fospe.remember.utility.*
import com.fospe.remember.viewmodels.members.DeleteMemberViewModel
import com.fospe.remember.viewmodels.members.DeleteMemberViewModelFactory
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModel
import com.fospe.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.remember.api.models.members.Members
import com.remember.api.models.post.PostItem
import com.remember.api.models.registration.User
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_members.view.*


class MembersFragment : Fragment() {

    private lateinit var viewForlayout:View
    private lateinit var apiRepository: APIRepository
    private lateinit var membersViewModel: GetAddedMembersViewModel
    private lateinit var membersViewModelfactory: GetAddedMembersViewModelfactory
    private lateinit var deleteMemberViewModel: DeleteMemberViewModel
    private lateinit var deleteMemberViewModelFactory: DeleteMemberViewModelFactory
    private lateinit var memberList : ArrayList<Members>
    private lateinit var adapter : MembersListAdapter
    private lateinit var sharedPref: SharedPref
    private lateinit var refreshListener :SwipeRefreshLayout.OnRefreshListener
    private lateinit var user :User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_members, container, false)
        apiRepository= APIRepository()
        membersViewModelfactory= GetAddedMembersViewModelfactory(apiRepository)
        membersViewModel = ViewModelProvider(this,membersViewModelfactory).get(GetAddedMembersViewModel::class.java)
        deleteMemberViewModelFactory= DeleteMemberViewModelFactory((apiRepository))
        deleteMemberViewModel=ViewModelProvider(this,deleteMemberViewModelFactory).get(DeleteMemberViewModel::class.java)
        viewForlayout.rv_memberList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewForlayout.swipeRefreshLayout.post { viewForlayout.swipeRefreshLayout.isRefreshing = true }
        sharedPref= SharedPref(requireContext())
        observeMemberListResponse(this)
        observeDeletMemberResponse(this)
        user = sharedPref.get<User>("user")!!
        var params = HashMap<String,String>()
        if (user != null) {
            params["user_id"]= user.id

        }
        memberList = ArrayList<Members>()
        adapter = MembersListAdapter(requireContext(),memberList,user.id) { member ->

            showDialogConfirmation(
                requireContext(),
                "Delete Member ?",
                "Are you sure you want to delete this member?",
                true,
                {
                    removeMember(member)
                })
        }
        viewForlayout.rv_memberList.adapter=adapter
        refreshListener= SwipeRefreshLayout.OnRefreshListener {
            membersViewModel.getMembers(params,1)
        }

        viewForlayout.swipeRefreshLayout.setOnRefreshListener(refreshListener)
        refreshListener.onRefresh()

        viewForlayout.btn_addmembers.setOnClickListener{

            var intent : Intent = Intent(activity,CreateMemberActivity::class.java)
            startActivity(intent)
        }
        return viewForlayout
    }
    fun observeMemberListResponse(owner: LifecycleOwner) {
        membersViewModel.getAddedmemberResponse.observe(owner, Observer { response ->

            viewForlayout.swipeRefreshLayout.isRefreshing = false
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if(response.body()!!.response.size>0)
                            {
                                viewForlayout.tvMsg.visibility= View.GONE
                               adapter.setMemberList(response.body()!!.response,requireContext(),user.id)
                                adapter.notifyDataSetChanged()
                            }
                            else{
                                viewForlayout.tvMsg.visibility= View.VISIBLE
                            }
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

    fun removeMember(members: Members){

        hideAlertdialog()
        showProgressDialog(requireContext(),"deleting member...")
        var params=HashMap<String,String>()
        params["user_id"] = user.id
        params["member_id"] = members.id.toString()
        deleteMemberViewModel.getResponse(params)

    }

    fun observeDeletMemberResponse(owner:LifecycleOwner){
        deleteMemberViewModel.getDeleteResponse.observe(owner, Observer {response ->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            refreshListener.onRefresh()
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

}