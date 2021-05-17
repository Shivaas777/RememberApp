package com.fospe.remember.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.fospe.remember.ui.store.ProductDetailsActivity
import com.fospe.remember.utility.loadImage
import kotlinx.android.synthetic.main.product_item.view.*

class ProductListAdapter(private var productList:ArrayList<Int>,private var context: Context):
    RecyclerView.Adapter<ProductListAdapter.holder>() {

        class holder(itemView: View):RecyclerView.ViewHolder(itemView)
        {
            fun bindItems(value: Int,context: Context){
               itemView.tvProductPriceBefore.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if(value==1) {
                    itemView.img_product.loadImage(
                        "https://images-na.ssl-images-amazon.com/images/I/91LQjiZ8r7L._AC_SY450_.jpg",
                        context
                    )
                }
                else{
                    itemView.img_product.loadImage(
                        "https://nbdant00-a.akamaihd.net//product_images/737328/lg/11532.itsawfullydarkin_lrg_0.jpg",
                        context
                    )
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
       val v =LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return holder(v)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {

        holder.bindItems(productList[position],context)
        holder.itemView.setOnClickListener {
            var intent: Intent = Intent(context,ProductDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return productList.size
    }
}