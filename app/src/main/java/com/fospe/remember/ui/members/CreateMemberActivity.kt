
package com.fospe.remember.ui.members

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_create_member.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class CreateMemberActivity : AppCompatActivity() {

    private lateinit var tablist :List<Int>
    private lateinit var pagerFragments: List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_member)
        setSupportActionBar(toolbar)
        changeToolbarText("Member Profile", supportActionBar, toolbar)
        tablist = listOf(R.string.tab1, R.string.tab2)
        pagerFragments = listOf(
            CreateMemberFragment.create(), AddMembersFragment.create())
        renderViewPager()
        renderTabLayer()
    }


    private fun renderViewPager() {
        tab_viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return tablist.size
            }
        }
    }

    private fun renderTabLayer() {
        TabLayoutMediator(tab_tablayout, tab_viewpager) { tab, position ->
            tab.text = getString(tablist[position])
        }.attach()
    }


}