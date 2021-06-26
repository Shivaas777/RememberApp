package com.omegamark.remember.ui.HomeScreen

import android.app.Activity
import android.content.DialogInterface
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.omegamark.remember.R
import com.omegamark.remember.adapters.PostListAdapter
import com.omegamark.remember.ui.post.CreatePostActivity
import com.omegamark.remember.utility.SharedPref
import com.omegamark.remember.utility.showDialogConfirmation
import com.omegamark.remember.utility.snack
import com.omegamark.remember.viewmodels.Post.GetPostViewModel
import com.omegamark.remember.viewmodels.Post.GetPostViewModelFactory
import com.remember.api.models.post.PostItem
import com.remember.api.models.registration.User
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var apiRepository: APIRepository
    private lateinit var getPostViewModelFactory: GetPostViewModelFactory
    private lateinit var getPostViewModel: GetPostViewModel
    private lateinit var viewForlayout: View
    private lateinit var sharedPref: SharedPref
    private lateinit var refreshListener :SwipeRefreshLayout.OnRefreshListener
    private lateinit var postList : ArrayList<PostItem>
    private lateinit var adapter :PostListAdapter
    private lateinit var user :User

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //  you will get result here in result.data
            Toast.makeText(activity,"refresh List",Toast.LENGTH_SHORT).show()
            viewForlayout.swipeRefreshLayout.post { viewForlayout.swipeRefreshLayout.isRefreshing = true }
            refreshListener.onRefresh() }
        }

    override fun onResume() {
        viewForlayout.swipeRefreshLayout.post { viewForlayout.swipeRefreshLayout.isRefreshing = true }
        refreshListener.onRefresh()
        observePostListResponse(this)
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForlayout=  inflater.inflate(R.layout.fragment_home, container, false)
        apiRepository= APIRepository()
        getPostViewModelFactory= GetPostViewModelFactory(apiRepository)
        getPostViewModel =ViewModelProvider(this,getPostViewModelFactory).get(GetPostViewModel::class.java)
        sharedPref= SharedPref(requireContext())

         user = sharedPref.get<User>("user")!!
        val params = HashMap<String,String>()
        params["user_id"]= user.id
        viewForlayout.rv_postlist.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

         postList = ArrayList<PostItem>()
         adapter = PostListAdapter(requireContext(),postList,user.id)
        viewForlayout.rv_postlist.adapter=adapter
        refreshListener= SwipeRefreshLayout.OnRefreshListener {
            getPostViewModel.getPostResponse(params)
        }

        viewForlayout.swipeRefreshLayout.setOnRefreshListener(refreshListener)


        viewForlayout.btn_createMemories.setOnClickListener {
            val intent = Intent(activity, CreatePostActivity::class.java)
            intent.putExtra("user_id",user.id)
            startForResult.launch(intent)
           // startActivity(intent)
        }

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                showDialogConfirmation(
                    requireContext(),
                    "Remove ?",
                    "Do you want to remove the post ?",
                    false
                ) { s: String, dialogInterface: DialogInterface ->
                    dialogInterface.dismiss() }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


            }
        }).attachToRecyclerView(viewForlayout.rv_postlist)


        return viewForlayout
    }

    fun observePostListResponse(context : LifecycleOwner)
    {
        getPostViewModel.postResponse.observe(context, Observer {

            viewForlayout.swipeRefreshLayout.isRefreshing = false

            when (it.isSuccess)
            {
                true->{



                    if(it.response.size>0)
                    {
                        tv_noEvents.visibility= View.GONE
                        adapter.setPostList(it.response,requireContext(),user.id)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        tv_noEvents.visibility= View.VISIBLE
                        adapter.setPostList(postList,requireContext(),user.id)
                        adapter.notifyDataSetChanged()
                    }
                }

                false->{
                    swipeRefreshLayout.snack(it.message)
                }
            }
        })
    }
}

