package com.omegamark.remember.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.omegamark.remember.R
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.verification.VerificationViewModel
import com.omegamark.remember.viewmodels.verification.VerificationViewModelFactory
import com.google.gson.Gson
import com.mukesh.OnOtpCompletionListener
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.fragment_name.view.btn_next
import kotlinx.android.synthetic.main.fragment_verify_mobile.view.*


private const val ARG_PARAM1 = "id"
private const val ARG_PARAM2 = "code"
private const val ARG_PARAM3 = "password"
class verifyMobileFragment : Fragment() {

    lateinit var  viewForlayout :View
    lateinit var navController : NavController
    private var id: String? = null
    private var code: String? = null
    private var password:String?=null
    lateinit var apiRepository: APIRepository
    lateinit var verifyViewModelFactory: VerificationViewModelFactory
    lateinit var verifyViewModel  : VerificationViewModel
    private lateinit var sharedPreferences: SharedPref
    private lateinit var gson: Gson;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ARG_PARAM1)
            code = it.getString(ARG_PARAM2)
            password= it.getString(ARG_PARAM3)
            Toast.makeText(activity, code, Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_verify_mobile, container, false)
        viewForlayout.tvCode.setText(code)
        apiRepository= APIRepository()
        verifyViewModelFactory= VerificationViewModelFactory(apiRepository)
        verifyViewModel = ViewModelProvider(this, verifyViewModelFactory).get(VerificationViewModel::class.java)
        sharedPreferences= SharedPref(requireContext())
        observeVerifyResponse(this)
        viewForlayout.btn_next.setOnClickListener {
            Log.d("shiva", "button clicked")
            verify()
        }
        viewForlayout.btn_back.setOnClickListener { activity?.onBackPressed() }
        viewForlayout.otp_view!!.setOtpCompletionListener(object : OnOtpCompletionListener {
            override fun onOtpCompleted(otp: String?) {
                Log.d("shiva", "otp" + otp)
                viewForlayout.btn_next.setBackgroundForRegisterButton("set")

            }
        })

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

    fun verify()
    {
        showProgressDialog(requireContext(),"Verifying user")
        val params = HashMap<String, String>()

        params["verify_id"] = id.toString()
        params["verify_OTP"] = viewForlayout.otp_view.text.toString()
        verifyViewModel.getVerificationResponse(params)


    }

    fun observeVerifyResponse(context: LifecycleOwner)
    {
        verifyViewModel.verifyResponse.observe(context, Observer {

            when (it.isSuccess) {
                true -> {

                    hideProgreeDialog()
                    sharedPreferences.put(it.response,"user")
                    sharedPreferences.putString(password,"password")
                    navController.navigate(R.id.action_verifyMobileFragment_to_getStartedFragment)
                }
                false -> {

                    hideProgreeDialog()
                    viewForlayout.btn_next.snack(it.message)

                }
            }
        })
    }
}