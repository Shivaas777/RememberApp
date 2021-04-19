package com.fospe.remember.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fospe.remember.R
import com.fospe.remember.ui.HomeScreen.HomeActivity
import kotlinx.android.synthetic.main.fragment_get_started.*
import kotlinx.android.synthetic.main.fragment_get_started.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GetStartedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GetStartedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewForLayout :View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_get_started, container, false)
        viewForLayout.btn_getstarted.setOnClickListener{
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return viewForLayout
    }

}