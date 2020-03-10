package com.aljon.newsnest.news

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aljon.newsnest.R
import com.aljon.newsnest.databinding.MainContainerFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainContainerFragment: Fragment() {

    private lateinit var binding: MainContainerFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = MainContainerFragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = NewsFragmentAdapter(this)
        binding.pager.adapter = adapter

        binding.pager.isUserInputEnabled = false
        binding.pager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = adapter.CATEGORIES[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.news_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.search -> navigateToSearch()
        }
        return true
    }

    private fun navigateToSearch() {
        this.findNavController().navigate(MainContainerFragmentDirections.actionMainContainerFragmentToSearchFragment())
    }
}