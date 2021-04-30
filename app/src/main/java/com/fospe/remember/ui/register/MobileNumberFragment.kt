package com.fospe.remember.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fospe.remember.R
import com.fospe.remember.utility.*
import com.fospe.remember.viewmodels.APIViewModelFactory
import com.fospe.remember.viewmodels.RegisterViewModel

import com.remember.api.repository.APIRepository

import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_login.view.etPassword
import kotlinx.android.synthetic.main.fragment_mobile_number.view.*
import kotlinx.android.synthetic.main.fragment_mobile_number.view.etMobile
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.btn_next
import kotlinx.android.synthetic.main.fragment_password.view.*


private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "password"
class MobileNumberFragment : Fragment() {



    lateinit var  viewForlayout :View
    lateinit var navController : NavController
    private var name: String? = null
    private var password: String? = null
    lateinit var apiRepository: APIRepository
    lateinit var apiViewModelFactory: APIViewModelFactory
    lateinit var registerViewModel  : RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            password= it.getString(ARG_PARAM2)
            Toast.makeText(activity, name, Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_mobile_number, container, false)
        apiRepository= APIRepository()
        apiViewModelFactory= APIViewModelFactory(apiRepository)
        registerViewModel = ViewModelProvider(this,apiViewModelFactory).get(RegisterViewModel::class.java)
        observeRegistration()
        viewForlayout.btn_next.setOnClickListener {
            callRegisterApi()
             }
        viewForlayout.btn_back_.setOnClickListener { activity?.onBackPressed() }
        viewForlayout.etMobile.afterTextChangedChangeButtonImage {btn_next.setBackgroundForRegisterButton(
            it
        ) }
        return viewForlayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

    }

    fun callRegisterApi()
    {
        showProgressDialog(requireContext(),"Registering user...")
        val params = HashMap<String, String>()

        params["name"] = name.toString()
        params["email"] = ""
        params["mobile"] =  "+1"+viewForlayout.etMobile.text.toString()
        params["password"] = password.toString()
        params["device_type"] = "1"
        params["device_token"] = "qqwee"

        registerViewModel.registerUser(params)


    }

    fun observeRegistration()
    {
        registerViewModel.myResponse.observe(viewLifecycleOwner, Observer {

           when (it.isSuccess)
           {
               true ->{
                   hideProgreeDialog()
                   val bundle = Bundle()
                   bundle.putString("id", it.response.verifyId)
                   bundle.putString("code", it.response.verifyOTP)
                   bundle.putString("password",password)
                   navController.navigate(R.id.action_mobileNumberFragment_to_verifyMobileFragment,bundle)
               }
               false->{
                   hideProgreeDialog()
                   viewForlayout.btn_next.snack(it.message)
               }
           }


            Log.d("Shiva",it.toString())

        })


    }
}