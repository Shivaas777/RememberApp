package com.fospe.remember.ui.post

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fospe.remember.R
import com.fospe.remember.adapters.CommentListAdapter
import com.fospe.remember.adapters.PostListAdapter
import com.fospe.remember.utility.changeToolbarText
import com.fospe.remember.utility.hideProgreeDialog
import com.fospe.remember.utility.showProgressDialog
import com.fospe.remember.viewmodels.Post.GetPostDetailsViewModel
import com.fospe.remember.viewmodels.Post.GetPostDetailsViewModelFactory
import com.fospe.remember.viewmodels.Post.GetPostViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.remember.api.models.post.Comment
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.add_comment.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class PostDetailsActivity : AppCompatActivity() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var apiRepository: APIRepository
    private lateinit var getPostDetailsViewModelFactory: GetPostDetailsViewModelFactory
    private lateinit var getPostDetailsViewModel: GetPostDetailsViewModel
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
        getPostDetailsViewModel= ViewModelProvider(this,getPostDetailsViewModelFactory).get(GetPostDetailsViewModel::class.java)
        var scrollingMovementMethod:ScrollingMovementMethod= ScrollingMovementMethod()
        tvPostDetails.movementMethod=scrollingMovementMethod
        val intent =getIntent()
        userid = intent.getStringExtra("user_id")
        postid =intent.getStringExtra("post_id")
        commentList= ArrayList()
        commentListAdapter = CommentListAdapter(this,commentList)
        rv_commentList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_commentList.adapter=commentListAdapter


        ObservePostResponse()
        getPostDetails()

        btn_addComment.setOnClickListener {
            showAddCommentDialog()
        }

    }

    fun getPostDetails(){

        showProgressDialog(this,"fetching post details...")
        var params =HashMap<String,String>()
        params["user_id"] = userid
        params["post_id"]=postid
        getPostDetailsViewModel.getPostDetailsResponse(params)
    }
    fun ObservePostResponse()
    {
        getPostDetailsViewModel.postDetailsResponse.observe(this, Observer {

            hideProgreeDialog()
            cardView.visibility=View.VISIBLE
            tvUserName.text=it.response.post_head.profile_name
            tvUser_location.text=it.response.post_head.profile_location
            tvPostTime.text=it.response.post_time_diff
            tvPostTitle.text=it.response.post_title
            tvPostDetails.text=it.response.post_content
            tvCommentCount.text=it.response.share.toString()
            tvViewCount.text=it.response.views.toString()
            commentListAdapter.setCommentList(it.response.comments,this)

        })
    }
    fun showAddCommentDialog()
    {
        bottomSheetDialog= BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.add_comment, null)
        bottomSheetDialog.run {
            setContentView(view)
            show()
            bottomSheetDialog.etComment.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    bottomSheetDialog.tvCount.text =p0?.length.toString()+"/250"
                }


                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
        bottomSheetDialog.btn_publishComment.setOnClickListener {

            bottomSheetDialog.dismiss()
        }
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
}