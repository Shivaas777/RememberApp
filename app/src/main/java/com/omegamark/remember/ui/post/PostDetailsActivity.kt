package com.omegamark.remember.ui.post

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.omegamark.remember.R
import com.omegamark.remember.adapters.CommentListAdapter
import com.omegamark.remember.ui.members.MemberDetailsActivity
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.Post.AddCommentViewModel
import com.omegamark.remember.viewmodels.Post.AddCommentViewModelFactory
import com.omegamark.remember.viewmodels.Post.GetPostDetailsViewModel
import com.omegamark.remember.viewmodels.Post.GetPostDetailsViewModelFactory
import com.remember.api.models.post.Comment
import com.remember.api.network.Url
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PostDetailsActivity : AppCompatActivity() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var apiRepository: APIRepository
    private lateinit var getPostDetailsViewModelFactory: GetPostDetailsViewModelFactory
    private lateinit var getPostDetailsViewModel: GetPostDetailsViewModel
    private lateinit var addCommentViewModelFactory: AddCommentViewModelFactory
    private lateinit var addCommentViewModel: AddCommentViewModel
    private lateinit var commentListAdapter: CommentListAdapter
    private lateinit var commentList: ArrayList<Comment>
    private lateinit var userid:String
    private lateinit var postid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        setSupportActionBar(toolbar)
        changeToolbarText("Details", supportActionBar, toolbar)
        apiRepository= APIRepository()
        getPostDetailsViewModelFactory= GetPostDetailsViewModelFactory(apiRepository)
        getPostDetailsViewModel= ViewModelProvider(this, getPostDetailsViewModelFactory).get(
            GetPostDetailsViewModel::class.java
        )
        addCommentViewModelFactory= AddCommentViewModelFactory((apiRepository))
        addCommentViewModel=ViewModelProvider(this, addCommentViewModelFactory).get(
            AddCommentViewModel::class.java
        )
        var scrollingMovementMethod:ScrollingMovementMethod= ScrollingMovementMethod()
        tvPostDetails.movementMethod=scrollingMovementMethod
        val intent =getIntent()
        userid = intent.getStringExtra("user_id")
        postid =intent.getStringExtra("post_id")
        commentList= ArrayList()
        commentListAdapter = CommentListAdapter(this, commentList)
        rv_commentList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_commentList.adapter=commentListAdapter


        ObservePostResponse()
        getPostDetails(true)
        observeAddCommentResponse()
        btn_addComment.setOnClickListener {
            showAddCommentDialog()
        }
        user_image.setOnClickListener {
            var intent = Intent(this, MemberDetailsActivity::class.java)
            intent.putExtra("user_id", userid)
            intent.putExtra("member_id", "25")
            startActivity(intent)
        }

    }

    fun getPostDetails(showProgress: Boolean){

        if(showProgress) {
            showProgressDialog(this, "fetching post details...")
        }
        var params =HashMap<String, String>()
        params["user_id"] = userid
        params["post_id"]=postid
        getPostDetailsViewModel.getPostDetailsResponse(params)
    }
    fun ObservePostResponse()
    {
        getPostDetailsViewModel.postDetailsResponse.observe(this, Observer {

            hideProgreeDialog()
            cardView.visibility = View.VISIBLE
            it.response.let { item ->
                tvUserName.text = item.post_head.profile_name
                tvUser_born.text = "Born : " + item.born_date
                tvUser_died.text = "Died : " + item.death_date
                tvPostTime.text = item.created_at + " ago"
                tvPostCreated.text = "by : " + item.post_created_by
                tvPostTitle.text = item.post_title.trim()
                tvPostDetails.text = item.post_content.trim()
                tvCommentCount.text = item.comment_count.toString()
                tvEventDate.text = getAnniversaryYear(item.death_date)
                tvViewCount.text = item.views.toString()
                user_image.loadImage(Url.url + item.post_head.profile_image, this)
                if (item.comments.size > 0) {
                    tvMsg.visibility = View.GONE
                    commentListAdapter.setCommentList(item.comments, this)
                    commentListAdapter.notifyDataSetChanged()
                } else {
                    tvMsg.visibility = View.VISIBLE
                }

            }

        })
    }
    fun showAddCommentDialog()
    {
        bottomSheetDialog= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_comment, null)
        bottomSheetDialog.run {

            setContentView(view)
            show()
            bottomSheetDialog.etComment.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bottomSheetDialog.tvCount.text = p0?.length.toString() + "/250"

                }


                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
        bottomSheetDialog.tvUser_nm.setText(tvUserName.text.toString())
        bottomSheetDialog.btn_publishComment.setOnClickListener {

            if(bottomSheetDialog.etComment.text.length>0) {
                showProgressDialog(this, "posting...")
                var params = HashMap<String, String>()
                params["user_id"] = userid
                params["post_id"] = postid
                params["comment"] = bottomSheetDialog.etComment.text.toString()
                addCommentViewModel.addComment(params)
            }
            else {
                showSnack(this, "Comment cannot be empty")
            }
        }
    }

    fun observeAddCommentResponse()
    {
        addCommentViewModel.getCommentResponse.observe(this, Observer { response ->

            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            getPostDetails(false)
                            bottomSheetDialog.dismiss()
                        }
                        false -> {
                            var msg = response.body()?.message
                            showSnack(this, msg.toString())
                        }
                    }
                }
                false -> {


                    showSnack(this, "Unable to add your comment")
                }
            }
        })
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

    private fun getAnniversaryYear(date:String):String?{
        var anniversary: String? =null;
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val currentYear = calendar[Calendar.YEAR]
        val strs =date.split("/").toTypedArray()
        var year:Int= currentYear - Integer.parseInt(strs[2])
        var suffix = getDayNumberSuffix(year)
        anniversary= year.toString() + suffix +" death anniversary"
        return anniversary

    }

    private fun getDayNumberSuffix(day: Int): String? {
        return if (day >= 11 && day <= 13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}