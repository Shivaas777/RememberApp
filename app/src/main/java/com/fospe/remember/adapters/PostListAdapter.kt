package com.fospe.remember.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.fospe.remember.ui.post.PostDetailsActivity
import com.remember.api.models.post.PostItem
import kotlinx.android.synthetic.main.event_item.view.*

class PostListAdapter(private var context: Context, private var list :ArrayList<PostItem>) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position],context)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun setPostList(list:ArrayList<PostItem>,context: Context)
    {
        this.list=list
        this.context=context
    }

    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(postItem: PostItem,context: Context) {

            //itemView.user_image.setImageResource(R.drawable.user)
            itemView.tvUserName.text=postItem.post_head.profile_name
            itemView.tvUser_location.text=postItem.post_head.profile_location

            itemView.tvPostCreated.text="Post created by "+postItem.post_created_by+" on"+postItem.post_time_value
            itemView.tvPostTime.text =postItem.post_time_diff+ " ago"
            itemView.tvPostTitle.text=postItem.post_title
            itemView.tvPostDetails.text=postItem.post_content
            itemView.tvCommentCount.text=postItem.comments.toString()
            itemView.tvViewCount.text=postItem.share.toString()

            itemView.layout_item.setOnClickListener{

                val intent = Intent(context, PostDetailsActivity::class.java)
                context.startActivity(intent)

            }

        }
    }
}