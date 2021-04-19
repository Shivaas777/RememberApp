package com.fospe.remember.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fospe.remember.R
import com.fospe.remember.utility.afterTextChangedChangeButtonImage
import com.fospe.remember.utility.changeBG
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*
import kotlinx.android.synthetic.main.fragment_name.view.btn_next
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_password.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment() {

    lateinit var  viewForlayout :View
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewForlayout = inflater.inflate(R.layout.fragment_password, container, false)
        viewForlayout.btn_next.setOnClickListener { navController.navigate(R.id.action_passwordFragment_to_mobileNumberFragment) }
        viewForlayout.btn_back.setOnClickListener { activity?.onBackPressed() }
        viewForlayout.etPassword.afterTextChangedChangeButtonImage{changeBG(it,viewForlayout.etConfirm_password,viewForlayout.btn_next) }
        viewForlayout.etConfirm_password.afterTextChangedChangeButtonImage{changeBG(it,viewForlayout.etPassword,viewForlayout.btn_next) }
        return  viewForlayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
    }




}
