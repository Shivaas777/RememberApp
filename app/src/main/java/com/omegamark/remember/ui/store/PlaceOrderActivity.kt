package com.omegamark.remember.ui.store

import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.omegamark.remember.R
import com.omegamark.remember.adapters.GetMemberSpinnerAdapter
import com.omegamark.remember.adapters.StateListAdapter
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModel
import com.omegamark.remember.viewmodels.members.GetAddedMembersViewModelfactory
import com.omegamark.remember.viewmodels.store.BuyProductViewModel
import com.omegamark.remember.viewmodels.store.BuyProductViewModelFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.remember.api.models.members.Members
import com.remember.api.models.registration.User
import com.remember.api.models.store.Address
import com.remember.api.models.store.PlaceOrderRequest
import com.remember.api.models.store.StoreItem
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.order_history_item.img_product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceOrderActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var member: Members
    private lateinit var product:StoreItem
    private lateinit var sharedPref: SharedPref
    lateinit var stateAdapter: StateListAdapter
    lateinit var cityAdapter: StateListAdapter
    lateinit var stateAdapterBilling: StateListAdapter
    lateinit var cityAdapterBilling: StateListAdapter
    lateinit var city :ArrayList<String>
    lateinit var states :ArrayList<String>
    lateinit var countryJson: String
    private lateinit var apiRepository: APIRepository
    lateinit var getAddedMembersViewModelfactory: GetAddedMembersViewModelfactory
    lateinit var getAddedMembersViewModel: GetAddedMembersViewModel
    private lateinit var buyProductViewModelFactory: BuyProductViewModelFactory
    private lateinit var buyProductViewModel: BuyProductViewModel
    lateinit var spinnerAdapter: GetMemberSpinnerAdapter
    lateinit var memberList: ArrayList<Members>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        setSupportActionBar(toolbar)
        changeToolbarText("Order Summary", supportActionBar, toolbar)
        apiRepository= APIRepository()
        getAddedMembersViewModelfactory = GetAddedMembersViewModelfactory((apiRepository))
        getAddedMembersViewModel = ViewModelProvider(this, getAddedMembersViewModelfactory).get(GetAddedMembersViewModel::class.java)
        buyProductViewModelFactory= BuyProductViewModelFactory(apiRepository)
        buyProductViewModel= ViewModelProvider(this,buyProductViewModelFactory).get(BuyProductViewModel::class.java)
        product= GsonBuilder().create().fromJson(intent.getStringExtra("product"), StoreItem::class.java)
        sharedPref= SharedPref(this)
        user = sharedPref.get<User>("user")!!
        memberList = ArrayList()
        spinnerAdapter = GetMemberSpinnerAdapter(this, memberList)
        spinner_member.adapter = spinnerAdapter
        getAddedMembers()
        observeGetAddedmemberResponse()
        observerProductResponse()
        setProductInfo(product)
        setUserInfo(user)
        setShippingAddress(user,this)
        removeBillingAddress(this)
        //uncheckShipping()
        cb_same_shipping.setOnCheckedChangeListener { compoundButton, b ->
            if(b){setBillingAddress(this)}else{removeBillingAddress(this)}
        }
        btn_place_order.setOnClickListener { buyProduct() }
    }

    private fun setBillingAddress(context: Context) {

        cityAdapterBilling= StateListAdapter(context,city)
        spinner_cityBilling.adapter=cityAdapter
        spinner_stateBilling?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                city= getStateCityList(context,countryJson,false,selectedItem)
                cityAdapter.setList(context,city)
                cityAdapter.notifyDataSetChanged()

            }

        }
        etStreetAddressBilling.setText(user.street)
        etZipcodeBilling.setText(user.zipcode)
        Handler().postDelayed({
            if(spinner_state_sum.selectedItem.toString().length>0)
            {
                var index :Int= states.indexOf(spinner_state_sum.selectedItem.toString())
                spinner_stateBilling.setSelection(index)
            }
            else {
                spinner_stateBilling.setSelection(0)
            }
        }, 1000)


        Handler().postDelayed({
            if(spinner_city_sum.selectedItem.toString().length>0)
            {

                var index :Int= city.indexOf(spinner_city_sum.selectedItem.toString())
                Log.d("shiva","updated" +index)
                spinner_cityBilling.setSelection(index)
            }
            else {
                spinner_cityBilling.setSelection(0)
            }
        }, 2000)

    }

    private fun removeBillingAddress(context: Context) {
        cityAdapterBilling= StateListAdapter(context,city)
        spinner_cityBilling.adapter=cityAdapter
        spinner_stateBilling?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                city= getStateCityList(context,countryJson,false,selectedItem)
                cityAdapterBilling.setList(context,city)
                cityAdapterBilling.notifyDataSetChanged()
            }

        }
        etStreetAddressBilling.setText(" ")
        etZipcodeBilling.setText(" ")
        spinner_stateBilling.setSelection(0)
    }

    private fun setShippingAddress(user: User,context: Context) {

        states= ArrayList()
        lifecycleScope.launch(Dispatchers.IO) {
            countryJson= loadJSONFromAsset(context)
            withContext(Dispatchers.Main) {
                states = getStateCityList(context,countryJson,true,"")
                stateAdapter = StateListAdapter(context, states)
                spinner_state_sum.adapter = stateAdapter
                stateAdapterBilling = StateListAdapter(context, states)
                spinner_stateBilling.adapter = stateAdapterBilling
                spinner_state_sum.setSelection(0)
                spinner_stateBilling.setSelection(0)
                Log.d("shiva","statelist" +states.toString())
            }
        }
        city=ArrayList()
        cityAdapter= StateListAdapter(context,city)
        cityAdapterBilling= StateListAdapter(context,city)
        spinner_city_sum.adapter=cityAdapter
        spinner_state_sum?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                city= getStateCityList(context,countryJson,false,selectedItem)
                cityAdapter.setList(context,city)
                cityAdapter.notifyDataSetChanged()


            }

        }
        etStreetAddress.setText(user.street)
        etZipcode.setText(user.zipcode)
        Handler().postDelayed({
        if(user.state.length>0)
        {
            var index :Int= states.indexOf(user.state)
            spinner_state_sum.setSelection(index)
        }
        else {
            spinner_state_sum.setSelection(0)
        }
        }, 1000)


        Handler().postDelayed({
            if(user.city.length>0)
            {

                var index :Int= city.indexOf(user.city)
                Log.d("shiva","updated" +index)
                spinner_city_sum.setSelection(index)
            }
            else {
                spinner_city_sum.setSelection(0)
            }
        }, 2000)

    }

    private fun setUserInfo(user: User) {

        etMobile.setText(user.mobile)
        etEmail.setText(user.email)
        etName.setText((user.name))
    }

    private fun uncheckShipping()
    {
        etStreetAddress.afterTextChangedChangeButtonImage { cb_same_shipping.isChecked=false }
        etStreetAddressBilling.afterTextChangedChangeButtonImage { cb_same_shipping.isChecked=false }
        etZipcode.afterTextChangedChangeButtonImage { cb_same_shipping.isChecked=false }
        etZipcodeBilling.afterTextChangedChangeButtonImage { cb_same_shipping.isChecked=false }


    }

    private fun setProductInfo(value:StoreItem) {
        value.imgUrl?.let { img_product.loadImage(it,this) }
        tvProductName_sum.text=value.productName
        tvProductPrice_sum.text= getString(R.string.product_price,value.productDisPrice)
        tvProductPriceBefore_sum.text=getString(R.string.product_price,value.productPrice)
        tvProductPriceSave_sum.text=getString(R.string.product_price_desc,value.discountDiffrence)
        tvProductPriceSavePercent_sum.text=getString(R.string.product_price_per,value.discountPercent)
        tvProductPriceBefore_sum.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun buyProduct() {
        if(validate()) {
            showDialogConfirmation(this,
                "Buy " + product.productName + "?",
                "Place an order for this item",
                true, { s: String, dialogInterface: DialogInterface ->
                    dialogInterface.dismiss()
                    showProgressDialog(this, "placing your order")

                    buyProductViewModel.getResponse(getParams())
                })
        }


    }

    private fun validate(): Boolean {
if(etName.text.length==0)
{
    Toast.makeText(applicationContext,"Name cannot be empty",Toast.LENGTH_SHORT).show()

}
       else if(etEmail.text.length==0)
        {
            Toast.makeText(applicationContext,"Email cannot be empty",Toast.LENGTH_SHORT).show()
            return false
        }
       else if(etMobile.text.length==0)
        {
            Toast.makeText(applicationContext,"Mobile cannot be empty",Toast.LENGTH_SHORT).show()
            return false

        }
       else if(etStreetAddress.text.length==0||etStreetAddressBilling.text.length==0)
        {
            Toast.makeText(applicationContext," street address cannot be empty",Toast.LENGTH_SHORT).show()
            return false

        }
       else if(etZipcode.text.length==0||etZipcodeBilling.text.length==0)
        {
            Toast.makeText(applicationContext,"zipcode cannot be empty",Toast.LENGTH_SHORT).show()
            return false

        }
        else {
            return true
        }
        return false
    }

    private fun observerProductResponse(){

        buyProductViewModel.getBuyProductResponse.observe(this, Observer {response->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            Toast.makeText(applicationContext,"Successfully placed order", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        false -> {


                            Toast.makeText(applicationContext,"Unable to buy this product", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(applicationContext,"Unable to buy this product", Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    fun getAddedMembers() {
        showProgressDialog(this, "Fetcing list of members")
        var params = HashMap<String, String>()
        params["user_id"] = user.id
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
                                ) { s: String, dialogInterface: DialogInterface ->  finish() }
                            }
                        }
                        false -> {
                            showDialogConfirmation(this, "user alert", response.message(), false
                            ) { s: String, dialogInterface: DialogInterface ->  finish() }
                        }
                    }
                }
                false -> {


                    showDialogConfirmation(this, "Network error", "Unable to fetch data", false
                    ) { s: String, dialogInterface: DialogInterface ->  finish() }
                }
            }
        })

    }

    fun getParams():HashMap<String,Object>
    {
        var shippingAddress = Address(
            etStreetAddress.text.toString(),
            spinner_city_sum.selectedItem as String,
            etCountry.text.toString(),
            spinner_state_sum.selectedItem as String,
            etStreetAddress.text.toString(),
            etZipcode.text.toString(),
            etName.text.toString(),
            etEmail.text.toString(),
            etMobile.text.toString()
        )
        var billingAddress = Address(
            etStreetAddressBilling.text.toString(),
            spinner_cityBilling.selectedItem as String,
            etCountryBilling.text.toString(),
            spinner_stateBilling.selectedItem as String,
            etStreetAddressBilling.text.toString(),
            etZipcodeBilling.text.toString(),
            etName.text.toString(),
            etEmail.text.toString(),
            etMobile.text.toString()
        )
    var request = PlaceOrderRequest(
        shippingAddress,
        billingAddress,
        (spinner_member.selectedItem as Members).id,
        product.id.toString(),
        user.id
    )
        val json = Gson().toJson(request)
        Log.d("shiva","request"+json)
        //var req=HashMap<String, Object>()
       // req["billing_add"]= billingAddress
       // req["delivery_add"]= shippingAddress


        val req: HashMap<String, Object> = Gson().fromJson(json, object : TypeToken<HashMap<String?, Object?>?>() {}.type)
        return req
    }
}