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
import com.fospe.remember.utility.setBackgroundForRegisterButton
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*


class NameFragment : Fragment() {


    lateinit var  viewForlayout :View
    lateinit var navController :NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_name, container, false)
        viewForlayout.btn_next.setOnClickListener { navController.navigate(R.id.action_nameFragment_to_passwordFragment) }
        viewForlayout.btn_cancel.setOnClickListener { activity?.finish() }

        viewForlayout.etName.afterTextChangedChangeButtonImage {btn_next.setBackgroundForRegisterButton(it) }


        return  viewForlayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
    }


    }
