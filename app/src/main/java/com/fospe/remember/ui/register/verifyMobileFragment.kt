package com.fospe.remember.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fospe.remember.R
import com.fospe.remember.utility.afterTextChangedChangeButtonImage
import com.fospe.remember.utility.setBackgroundForRegisterButton
import com.mukesh.OnOtpCompletionListener
import kotlinx.android.synthetic.main.fragment_name.view.btn_next
import kotlinx.android.synthetic.main.fragment_verify_mobile.view.*


class verifyMobileFragment : Fragment() {

    lateinit var  viewForlayout :View
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_verify_mobile, container, false)
        viewForlayout.btn_next.setOnClickListener { navController.navigate(R.id.action_verifyMobileFragment_to_getStartedFragment) }
        viewForlayout.btn_back.setOnClickListener { activity?.onBackPressed() }
        viewForlayout.otp_view!!.setOtpCompletionListener(object : OnOtpCompletionListener
        {
            override fun onOtpCompleted(otp: String?) {
                Log.d("shiva", "otp" +otp)
                viewForlayout.btn_next.setBackgroundForRegisterButton("set")

            }})

     viewForlayout.otp_view.afterTextChangedChangeButtonImage {
         if(it.length==4){
             viewForlayout.btn_next.setBackgroundForRegisterButton("set")
         }
     else {viewForlayout.btn_next.setBackgroundForRegisterButton("?")}}
        return viewForlayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
    }
}