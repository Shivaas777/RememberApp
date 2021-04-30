package com.fospe.remember.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.fospe.remember.ui.post.PostDetailsActivity
import com.remember.api.models.post.PostItem
import kotlinx.android.synthetic.main.event_item.view.*

class PostListAdapter(private var context: Context, private var list :ArrayList<PostItem>,private var userId:String?) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position],context,userId)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setPostList(list:ArrayList<PostItem>,context: Context,userId:String?)
    {
        this.list=list
        this.context=context
        this.userId=userId
    }

    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(postItem: PostItem,context: Context,userId: String?) {

            //itemView.user_image.setImageResource(R.drawable.user)
            itemView.tvUserName.text=postItem.post_head.profile_name

            itemView.tvUser_born.text =postItem.born_date
            itemView.tvUser_died.text=postItem.death_date
            itemView.tvDaysRemaining.text=postItem.days_to_go
            itemView.tvRelation.text=postItem.relation
            itemView.tvPostCreated.text="by : "+postItem.post_created_by

            itemView.tvPostTitle.text=postItem.post_title

            itemView.tvCommentCount.text=postItem.comments.toString()
            itemView.tvViewCount.text=postItem.share.toString()

            itemView.layout_item.setOnClickListener{

                val intent = Intent(context, PostDetailsActivity::class.java)
                Log.d("Shiva","userid :"+userId)
                intent.putExtra("user_id",userId)
                intent.putExtra("post_id",postItem.post_id.toString())
                context.startActivity(intent)

            }

        }
    }
}