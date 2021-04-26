package com.fospe.remember.ui.HomeScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fospe.remember.R
import com.fospe.remember.adapters.PostListAdapter
import com.fospe.remember.ui.post.CreatePostActivity
import com.fospe.remember.ui.register.RegisterActivity
import com.fospe.remember.utility.SharedPref
import com.fospe.remember.utility.snack
import com.fospe.remember.viewmodels.Post.GetPostViewModel
import com.fospe.remember.viewmodels.Post.GetPostViewModelFactory
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
        observePostListResponse(this)
        var user: User? =sharedPref.get<User>("user")
        var params = HashMap<String,String>()
        if (user != null) {
            params["user_id"]= user.id

        }
        viewForlayout.rv_postlist.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        viewForlayout.swipeRefreshLayout.post { viewForlayout.swipeRefreshLayout.isRefreshing = true }
         postList = ArrayList<PostItem>()
         adapter = PostListAdapter(requireContext(),postList)
        viewForlayout.rv_postlist.adapter=adapter
        refreshListener= SwipeRefreshLayout.OnRefreshListener {
            getPostViewModel.getPostResponse(params)
        }

        viewForlayout.swipeRefreshLayout.setOnRefreshListener(refreshListener)
        refreshListener.onRefresh()

        viewForlayout.btn_createMemories.setOnClickListener {
            val intent = Intent(activity, CreatePostActivity::class.java)
            startActivity(intent)
        }


        return viewForlayout
    }

    fun observePostListResponse(context : LifecycleOwner)
    {
        getPostViewModel.postResponse.observe(context, Observer {

            when (it.isSuccess)
            {
                true->{
                    adapter.setPostList(it.response,requireContext())
                    viewForlayout.swipeRefreshLayout.isRefreshing = false
                }
                false->{
                    swipeRefreshLayout.snack(it.message)
                }
            }
        })
    }
}

