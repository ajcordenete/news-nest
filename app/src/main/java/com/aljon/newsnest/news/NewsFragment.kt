package com.aljon.newsnest.news

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aljon.newsnest.R
import com.aljon.newsnest.databinding.NewsFragmentBinding
import com.aljon.newsnest.networking.ApiStatus
import com.google.android.material.snackbar.Snackbar


class NewsFragment: Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: NewsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, NewsViewModel.Factory(activity.application))
            .get(NewsViewModel::class.java)
    }

    private lateinit var binding: NewsFragmentBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = NewsFragmentBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        initAdapter()
        initSwipeRefresh()
        observeNavigateToArticleDetail()
        observeRequestStatus()
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Initialize our RecyclerView Adapter
     * and assign it to the binding
     */
    private fun initAdapter() {
        val span_count = 1
        var gridLayoutManager = GridLayoutManager(context, span_count)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position === 0) 1 else 1
            }
        }


        var adapter = NewsAdapter(NewsAdapter.OnItemSelectListener {
            viewModel.navigateToNewsDetail(it)
        })

        binding.newsList.layoutManager = gridLayoutManager
        binding.newsList.adapter = adapter
    }

    private fun observeRequestStatus() {
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                ApiStatus.SUCCESS ->   {
                    showSnackBar(R.string.success_refresh)
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
                this.findNavController().navigate(NewsFragmentDirections.actionNewsFragmentToArticleDetailFragment(it))
                viewModel.doneNavigatingToNewsDetail()
            }
        })
    }

    private fun initSwipeRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getNews()
        }
    }

    private fun completeRefresh() {
        binding.refreshLayout.isRefreshing = false
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
        this.findNavController().navigate(NewsFragmentDirections.actionNewsFragmentToSearchFragment())
    }
}