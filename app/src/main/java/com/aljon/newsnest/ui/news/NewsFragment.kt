package com.aljon.newsnest.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aljon.newsnest.R
import com.aljon.newsnest.databinding.NewsFragmentBinding
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.ui.MainContainerFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.news_fragment.*


const val CATEGORY_KEY = "category_key"

class NewsFragment: Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated.
     * This ensures that activity is not null when we initialize the fragment.
     */
    private val viewModel: NewsViewModel by lazy {
        // Get the category that is passed from the bundle
        // returns an empty string if no arguments found
        var category = arguments?.getString(CATEGORY_KEY) ?: ""

        ViewModelProvider(this, NewsViewModel.Factory(category))
            .get(NewsViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = NewsFragmentBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSwipeRefresh()
        observeNavigateToArticleDetail()
        observeRequestStatus()
    }

    /**
     * Initialize our RecyclerView Adapter
     * and assign it to the binding
     */
    private fun initAdapter() {
        var adapter = NewsAdapter(NewsAdapter.OnItemSelectListener {
            viewModel.navigateToNewsDetail(it)
        })

        news_list.adapter = adapter
    }

    private fun observeRequestStatus() {
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                ApiStatus.SUCCESS ->   {
                    completeRefresh()
                }
                ApiStatus.FAILED -> {
                    showSnackBar(R.string.failed_refresh)
                    completeRefresh()
                }
            }
        })
    }

    private fun showSnackBar(message: Int) {
        Snackbar.make(view!!,
            context!!.resources.getString(message), Snackbar.LENGTH_SHORT).show()
    }

    private fun observeNavigateToArticleDetail() {
        viewModel.navigateToArticleDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainContainerFragmentDirections.actionMainContainerFragmentToArticleDetailFragment(it))
                viewModel.doneNavigatingToNewsDetail()
            }
        })
    }

    private fun initSwipeRefresh() {
        refresh_layout.setOnRefreshListener {
            viewModel.getNews()
        }
    }

    private fun completeRefresh() {
        refresh_layout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
}