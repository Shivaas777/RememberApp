package com.omegamark.remember.ui.members

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omegamark.remember.R
import com.omegamark.remember.adapters.GetRelationSpinnerAdapter
import com.omegamark.remember.adapters.SearchListAdapter
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.members.AddMemberFromSearchViewModelFactory
import com.omegamark.remember.viewmodels.members.AddMembersFromSearchViewModel
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModel
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.omegamark.remember.viewmodels.relation.GetRelationViewModel
import com.omegamark.remember.viewmodels.relation.GetRelationViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.remember.api.models.members.Members
import com.remember.api.models.registration.User
import com.remember.api.models.relation.Relation
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.add_comment.btn_publishComment
import kotlinx.android.synthetic.main.add_comment.tvUser_nm
import kotlinx.android.synthetic.main.fragment_add_members.*
import kotlinx.android.synthetic.main.fragment_add_members.view.*
import kotlinx.android.synthetic.main.fragment_create_member.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_members.view.*
import kotlinx.android.synthetic.main.relation_dialog.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMembersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMembersFragment : Fragment() {

private lateinit var viewForLayout:View
    private lateinit var apiRepository: APIRepository
    private lateinit var membersViewModel: GetAddedMembersViewModel
    private lateinit var membersViewModelfactory: GetAddedMembersViewModelfactory
    private lateinit var relationViewModel: GetRelationViewModel
    private lateinit var relationViewModelFactory: GetRelationViewModelFactory
    private lateinit var addMembersFromSearchViewModel: AddMembersFromSearchViewModel
    private lateinit var addMembersFromSearchViewModelFactory: AddMemberFromSearchViewModelFactory
    private lateinit var memberList : ArrayList<Members>
    private lateinit var sharedPref: SharedPref
    private lateinit var user : User
    private lateinit var  selectedMember:Members
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_add_members, container, false)
        apiRepository= APIRepository()
        membersViewModelfactory= GetAddedMembersViewModelfactory(apiRepository)
        membersViewModel = ViewModelProvider(this,membersViewModelfactory).get(GetAddedMembersViewModel::class.java)
        relationViewModelFactory= GetRelationViewModelFactory(apiRepository)
        relationViewModel=ViewModelProvider(this,relationViewModelFactory).get(GetRelationViewModel::class.java)
        addMembersFromSearchViewModelFactory= AddMemberFromSearchViewModelFactory(apiRepository)
        addMembersFromSearchViewModel=ViewModelProvider(this,addMembersFromSearchViewModelFactory).get(AddMembersFromSearchViewModel::class.java)
        viewForLayout.rv_memberList_search.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        sharedPref= SharedPref(requireContext())
        observeMemberListResponse(this)
        observeRelationResponse(this)
        observeAddMember(this)
        user = sharedPref.get<User>("user")!!
        memberList= ArrayList()
        searchListAdapter= SearchListAdapter(requireContext(),memberList,{members -> handleClick(members) })
        viewForLayout.rv_memberList_search.adapter=searchListAdapter
        viewForLayout.simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                    Toast.makeText(requireContext(), "Searching "+query, Toast.LENGTH_LONG).show()
                var params=HashMap<String,String>()
                params["user_id"]=user.id
                params["search"]=query
                membersViewModel.getMembers(params,2)


                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })

        return viewForLayout
    }

    fun observeMemberListResponse(owner: LifecycleOwner) {
        membersViewModel.getAddedmemberResponse.observe(owner, Observer { response ->


            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if(response.body()!!.response.size>0)
                            {
                                viewForLayout.tvMsgSearch.visibility= View.GONE
                                searchListAdapter.setMemberList(response.body()!!.response,requireContext())
                                searchListAdapter.notifyDataSetChanged()
                            }
                            else{
                                viewForLayout.tvMsgSearch.visibility= View.VISIBLE
                                searchListAdapter.setMemberList(response.body()!!.response,requireContext())
                                searchListAdapter.notifyDataSetChanged()
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

    fun handleClick(members: Members)
    {
        selectedMember=members
        getRelation(requireContext(),user.id,relationViewModel)
    }

    fun observeRelationResponse(owner: LifecycleOwner) {
        relationViewModel.relationResponse.observe(owner, Observer { response ->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if (response.body()!!.response.size > 0) {
                               showRelationDialog(response.body()!!.response)
                            } else {
                                showDialogConfirmation(
                                    requireContext(),
                                    "user alert",
                                    "unable to fetch relation",
                                    false
                                ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                            }
                        }
                        false -> {
                            showDialogConfirmation(
                                requireContext(), "user alert", response.message(), false
                            ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                        }
                    }
                }
                false -> {


                    showDialogConfirmation(
                        requireContext(), "Network error", "Unable to fetch data", false
                    ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                }
            }
        })

    }

    fun showRelationDialog(relationList:ArrayList<Relation>)
    {
        bottomSheetDialog= BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.relation_dialog, null)
        bottomSheetDialog.run { setContentView(view)
            show()
        }
        var spinnerAdapter  = GetRelationSpinnerAdapter(requireContext(), relationList)
        bottomSheetDialog.spinner.adapter = spinnerAdapter
        bottomSheetDialog.tvUser_nm.setText(selectedMember.name)
        bottomSheetDialog.btn_publishComment.setOnClickListener {
            var relation: Relation = bottomSheetDialog.spinner.selectedItem as Relation
            addRelation(bottomSheetDialog.etRelationSub.text.toString(),relation.id.toString())

        }
    }

    fun addRelation(sub_relation :String,relationId:String)
    {
        if(sub_relation.length>0)
        {

            var params =HashMap<String,String>()
            params["user_id"] =user.id
            params["member_id"] =selectedMember.id.toString()
            params["relation"] =relationId
            addMembersFromSearchViewModel.getAddMemeberFromSearchResponse(params)
        }else{Toast.makeText(requireContext(),"Relation with cannot be empty",Toast.LENGTH_LONG).show()}
    }

    fun observeAddMember(owner:LifecycleOwner){

        addMembersFromSearchViewModel.getResponse.observe(owner, Observer {response->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            activity?.finish()
                        }
                        false -> {


                            Toast.makeText(requireContext(),response.body()?.message,Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(requireContext(),"Unable to add profile",Toast.LENGTH_LONG).show()

                }
            }

        })
    }

    companion object {
        fun create(): AddMembersFragment {
            return AddMembersFragment()
        }
    }
}