package com.aljon.newsnest.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aljon.newsnest.R
import com.aljon.newsnest.databinding.SearchFragmentBinding
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.ui.news.NewsAdapter
import com.aljon.newsnest.ui.news.NewsViewModel
import com.aljon.newsnest.utils.Constants
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.news_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        viewModel = ViewModelProvider(this, NewsViewModel.Factory(Constants.SEARCH))
            .get(NewsViewModel::class.java)

        val binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeRequestStatus()
    }

    /**
     * Initialize our RecyclerView Adapter
     * and assign it to the binding
     */
    private fun initAdapter() {
        var adapter = NewsAdapter(NewsAdapter.OnItemSelectListener {
            this.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArticleDetailFragment(it))
        })

        news_list.adapter = adapter
    }

    private fun observeRequestStatus() {
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                ApiStatus.DONE ->   {}
                ApiStatus.FAILED -> {}
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        initSearchViewFromMenu(searchItem)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchViewFromMenu(searchItem: MenuItem?) {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem?.actionView as SearchView
        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            onActionViewExpanded()

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    viewModel.searchNews(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            var prevQuery = viewModel.query
            if(prevQuery.isNotEmpty()) {
                setQuery(prevQuery, false)
                clearFocus()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

}