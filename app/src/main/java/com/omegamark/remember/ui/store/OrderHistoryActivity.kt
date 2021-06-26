package com.omegamark.remember.ui.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.omegamark.remember.R
import com.omegamark.remember.adapters.OrderHistoryListAdapter
import com.omegamark.remember.utility.changeToolbarText
import com.omegamark.remember.viewmodels.store.OrderHistoryViewModel
import com.omegamark.remember.viewmodels.store.OrderHistoryViewModelFactory
import com.remember.api.models.store.OrderHistoryItem
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var adapter: OrderHistoryListAdapter
    private lateinit var orderList: ArrayList<OrderHistoryItem>
    private lateinit var apiRepository: APIRepository
    private lateinit var orderHistoryViewModelFactory: OrderHistoryViewModelFactory
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var user_id:String
    private lateinit var refreshListener : SwipeRefreshLayout.OnRefreshListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        setSupportActionBar(toolbar)
        changeToolbarText("Order history", supportActionBar, toolbar)
        user_id= intent.getStringExtra("user_id")
        orderList= ArrayList()
        adapter= OrderHistoryListAdapter(orderList,this,user_id)
        apiRepository= APIRepository()
        orderHistoryViewModelFactory= OrderHistoryViewModelFactory(apiRepository)
        orderHistoryViewModel= ViewModelProvider(this,orderHistoryViewModelFactory).get(OrderHistoryViewModel::class.java)
        rv_orderList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = true }
        rv_orderList.adapter=adapter
        refreshListener= SwipeRefreshLayout.OnRefreshListener { getOrderList() }
        swipeRefreshLayout.setOnRefreshListener(refreshListener)
        refreshListener.onRefresh()
        observeOrderListResponse(this)

    }
    fun observeOrderListResponse(owner: LifecycleOwner) {
        orderHistoryViewModel.getOrderHistoryResponse.observe(owner, Observer { response ->

           swipeRefreshLayout.isRefreshing = false
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if(response.body()!!.response.size>0)
                            {
                            tv_no_order.visibility= View.GONE
                                adapter.setList(response.body()!!.response,this,user_id)
                                adapter.notifyDataSetChanged()
                            }
                            else{
                                tv_no_order.visibility= View.VISIBLE
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
    private fun getOrderList(){
        var param = HashMap<String,String>()
        param["user_id"]=user_id

        orderHistoryViewModel.orderHistoryResponse(param)
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