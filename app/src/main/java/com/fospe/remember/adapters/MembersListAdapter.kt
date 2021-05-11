package com.fospe.remember.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fospe.remember.R
import com.fospe.remember.ui.members.MemberDetailsActivity
import com.fospe.remember.utility.loadImage
import com.remember.api.models.members.Members
import com.remember.api.models.post.Comment
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.comment_item.view.tvUserName
import kotlinx.android.synthetic.main.event_item.view.*
import kotlinx.android.synthetic.main.event_item.view.tvUser_born
import kotlinx.android.synthetic.main.event_item.view.tvUser_died
import kotlinx.android.synthetic.main.member_item.view.*

class MembersListAdapter(private var context: Context, private var memberList :ArrayList<Members>):
    RecyclerView.Adapter<MembersListAdapter.ViewHolder>() {


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bindItems(memberItem : Members,context: Context) {

            itemView.tvUserName.text= memberItem.name
            itemView.tvUser_born.text= "born : "+memberItem.born_date
            itemView.tvUser_died.text="died : "+memberItem.death_date
            itemView.tvBio.text=memberItem.bio
            itemView.user_image_member.loadImage(memberItem.img_url,context)
            itemView.tvRelationMember.text = memberItem.relation
            itemView.img_delete.setOnClickListener{


            }
            //itemView.tvCreated.text= "profile created by: "+memberItem.cr

        }
    }

    fun setMemberList(list:ArrayList<Members>, context: Context)
    {
        this.memberList=list
        this.context=context

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val v = LayoutInflater.from(parent.context).inflate(R.layout.member_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(memberList[position],context)
        holder.itemView.setOnClickListener {
            var intent = Intent(context,MemberDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  memberList.size
    }
}