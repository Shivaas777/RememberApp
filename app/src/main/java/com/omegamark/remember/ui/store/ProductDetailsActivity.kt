package com.omegamark.remember.ui.store

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.omegamark.remember.R
import com.omegamark.remember.utility.*
import com.google.gson.GsonBuilder
import com.remember.api.models.store.StoreItem
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var product:StoreItem

    private  var user_id:String? = null
    private lateinit var list :MutableList<CarouselItem>
    private  var prod:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setSupportActionBar(toolbar)
        changeToolbarText("Details", supportActionBar, toolbar)

        prod= intent.getStringExtra("product")
        product= GsonBuilder().create().fromJson(prod, StoreItem::class.java)
        intent.getStringExtra("user_id").let { user_id = it }
        Log.d("Shiva","product" +product.toString())
        setValueToUI(product)

        btn_buy_product.setOnClickListener {
            val intent = Intent(this,PlaceOrderActivity::class.java)
            intent.putExtra("product", prod)
            startActivity(intent)

        }
    }



    private fun getCoruselImage(image :ArrayList<String>)
    {
        list = mutableListOf<CarouselItem>()
        for (i in 0..image.size-1){
        list.add(CarouselItem(imageUrl = image[i]))
    }

        img_product.addData(list);
    }



    private fun setValueToUI(value : StoreItem) {
        tvProductName.text=value.productName
        tvProductPrice.text= getString(R.string.product_price,value.productDisPrice)
        tvProductPriceBefore.text=getString(R.string.product_price,value.productPrice)
        tvProductPriceSave.text=getString(R.string.product_price_desc,value.discountDiffrence)
        tvProductPriceSavePercent.text=getString(R.string.product_price_per,value.discountPercent)
        tvProductDesc.text=value.productDesc
        if(value.inStock>0)
        tvProductLeft.text=getString(R.string.product_left,value.inStock)
        else{
            tvProductLeft.text=getString(R.string.product_out_of_stock)
            btn_buy_product.visibility= View.GONE
        }
        tvProductPriceBefore.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        var img = ArrayList<String>()
        value.imgUrl?.let { img.add(it) }
       getCoruselImage(img)
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