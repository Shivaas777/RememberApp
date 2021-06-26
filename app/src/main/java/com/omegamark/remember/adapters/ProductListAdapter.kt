package com.omegamark.remember.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omegamark.remember.R
import com.omegamark.remember.ui.store.ProductDetailsActivity
import com.omegamark.remember.utility.loadImage
import com.google.gson.GsonBuilder
import com.remember.api.models.store.StoreItem
import kotlinx.android.synthetic.main.product_item.view.*

class ProductListAdapter(private var productList:ArrayList<StoreItem>,private var context: Context,private var user_id:String):
    RecyclerView.Adapter<ProductListAdapter.holder>() {

        class holder(itemView: View):RecyclerView.ViewHolder(itemView)
        {
            fun bindItems(value: StoreItem,context: Context){
                itemView.tvProductName.text=value.productName
                itemView.tvProductPrice.text= context.getString(R.string.product_price,value.productDisPrice)
                itemView.tvProductPriceBefore.text=context.getString(R.string.product_price,value.productPrice)
                itemView.tvProductPriceSave.text=context.getString(R.string.product_price_desc,value.discountDiffrence)
                itemView.tvProductPriceSavePercent.text=context.getString(R.string.product_price_per,value.discountPercent)
                //itemView.tvProductGetItBy.text=context.getString(R.string.delivery_on,value.deliveryOn)
                itemView.tvProductPriceBefore.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                value.imgUrl?.let { itemView.img_product.loadImage(it, context) }



            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
       val v =LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return holder(v)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {

        holder.bindItems(productList[position],context)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("product", GsonBuilder().create().toJson(productList[position]))
            intent.putExtra("user_id",user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    fun setList(productList:ArrayList<StoreItem>,context: Context, user_id: String)
    {
        this.productList= productList
        this.context=context
        this.user_id=user_id
        notifyDataSetChanged()
    }
}