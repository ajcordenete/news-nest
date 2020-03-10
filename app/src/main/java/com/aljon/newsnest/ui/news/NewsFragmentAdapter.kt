package com.aljon.newsnest.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class NewsFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    val CATEGORIES = arrayOf("Top Stories", "Business", "Technology", "Entertainment", "Sports", "Science", "Health")

    override fun getItemCount(): Int = CATEGORIES.size

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsFragment()

        // First item on the list is not a category
        // don't send it in the bundle
        if(position != 0) {
            fragment.arguments = Bundle().apply { putString(CATEGORY_KEY, CATEGORIES[position]) }
        }
        return fragment
    }




}