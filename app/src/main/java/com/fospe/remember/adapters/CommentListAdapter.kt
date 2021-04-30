package com.fospe.remember.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.remember.api.models.post.Comment
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentListAdapter(private var context: Context,private var commentList: ArrayList<Comment>) :RecyclerView.Adapter<CommentListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListAdapter.ViewHolder {


        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bindItems(commentList[position],context)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun setCommentList(list:ArrayList<Comment>, context: Context)
    {
        this.commentList=list
        this.context=context

    }


    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){

        fun bindItems(commentItem : Comment,context: Context) {

            itemView.tvUserName.text=commentItem.title.name
            itemView.tvCommentTime.text=commentItem.title.time
            itemView.tvUserComment.text=commentItem.comment
        }
    }
}