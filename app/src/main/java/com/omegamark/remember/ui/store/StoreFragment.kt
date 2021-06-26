package com.omegamark.remember.ui.store

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.omegamark.remember.R
import com.omegamark.remember.adapters.ProductListAdapter
import com.omegamark.remember.utility.SharedPref
import com.omegamark.remember.viewmodels.store.StoreListViewModel
import com.omegamark.remember.viewmodels.store.StoreListViewModelFactory
import com.remember.api.models.registration.User
import com.remember.api.models.store.StoreItem
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.fragment_store.view.*
import kotlin.collections.set


class StoreFragment : Fragment() {

    private lateinit var viewForLayout : View

    private lateinit var adapter : ProductListAdapter
    private lateinit var productList: ArrayList<StoreItem>
    private lateinit var apiRepository: APIRepository
    private lateinit var storeListViewModelFactory: StoreListViewModelFactory
    private lateinit var storeListViewModel: StoreListViewModel
    private lateinit var sharedPref: SharedPref
    private lateinit var user : User
    private lateinit var refreshListener : SwipeRefreshLayout.OnRefreshListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_store, container, false)
        setHasOptionsMenu(true);

        sharedPref= SharedPref(requireContext())
        user = sharedPref.get<User>("user")!!
        viewForLayout.swipeRefreshLayoutStore.post { viewForLayout.swipeRefreshLayoutStore.isRefreshing = true }

        initilaiseAdapter()
        apiRepository= APIRepository()
        storeListViewModelFactory= StoreListViewModelFactory(apiRepository)
        storeListViewModel= ViewModelProvider(this, storeListViewModelFactory).get(
            StoreListViewModel::class.java
        )
        refreshListener= SwipeRefreshLayout.OnRefreshListener {
           getStoreList()
        }

        viewForLayout.swipeRefreshLayoutStore.setOnRefreshListener(refreshListener)
        refreshListener.onRefresh()

        observeStoreList(this)
        return viewForLayout
    }

    private fun getStoreList()
    {
        val params= HashMap<String, String>()
        params["user_id"]= user.id
        storeListViewModel.getStoreListResponse(params)
    }
    private fun observeStoreList(owner: LifecycleOwner)
    {
       storeListViewModel.storeListResponse.observe(owner, Observer { response ->
           viewForLayout.swipeRefreshLayoutStore.isRefreshing = false
           when (response.isSuccessful) {
               true -> {
                   when (response.body()?.isSuccess) {
                       true -> {
                           if (response.body()!!.response.size > 0) {
                               //viewForlayout.tvMsg.visibility= View.GONE
                               adapter.setList(
                                   response.body()!!.response,
                                   requireContext(),
                                   user.id
                               )
                               adapter.notifyDataSetChanged()
                           } else {
                               //viewForlayout.tvMsg.visibility= View.VISIBLE
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
   private  fun initilaiseAdapter()
    {
        viewForLayout.rv_productList.layoutManager = GridLayoutManager(requireContext(), 2)

        productList = ArrayList<StoreItem>()
        adapter = ProductListAdapter(productList, requireContext(), user.id)
        viewForLayout.rv_productList.adapter=adapter
    }






    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_cart)
        item.setVisible(true)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.action_cart){
            val intent = Intent(activity,OrderHistoryActivity::class.java)
            intent.putExtra("user_id",user.id.toString())
            activity?.startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}