package com.omegamark.remember.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.omegamark.remember.R
import com.omegamark.remember.utility.afterTextChangedChangeButtonImage
import com.omegamark.remember.utility.changeBG
import com.omegamark.remember.utility.snack
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*
import kotlinx.android.synthetic.main.fragment_name.view.btn_next
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_password.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "name"


/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment() {
    private var name: String? = null

    lateinit var  viewForlayout :View
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            Toast.makeText(activity,name,Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewForlayout = inflater.inflate(R.layout.fragment_password, container, false)
        viewForlayout.btn_next.setOnClickListener {

            if(etPassword.text.length >7 && etConfirm_password.text.length>7 ) {
                if(etPassword.text.toString().equals(etConfirm_password.text.toString(),false)) {
                    val bundle = Bundle()
                    bundle.putString("name", name)
                    bundle.putString("password", viewForlayout.etPassword.text.toString())
                    navController.navigate(R.id.action_passwordFragment_to_mobileNumberFragment, bundle)
                }
                else {
                    viewForlayout.btn_next.snack("passwords doesnot match")
                }
            }
            else {
                viewForlayout.btn_next.snack("password should be of minimum length of 8")
            }

        }
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
