package com.aljon.newsnest.ui.articledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aljon.newsnest.databinding.ArticleDetailFragmentBinding
import com.aljon.newsnest.utils.hasNetworkAvailable
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.article_detail_fragment.*

class ArticleDetailFragment: Fragment() {

    private val viewModel: ArticleDetailViewModel by lazy {
        var url = ArticleDetailFragmentArgs.fromBundle(arguments!!).url
        ViewModelProvider(this, ArticleDetailViewModelFactory(url))
            .get(ArticleDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        val binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWebUrl()
    }

    fun observeWebUrl() {
        viewModel.webUrl.observe(viewLifecycleOwner, Observer {
            loadWebUrl(it)
        })
    }

    /**
     *  Initialize the webview and load the page
     *  @param url: Url to of the news page to be loaded
     */
    fun loadWebUrl(url: String) {
        webView.apply {
            setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            getSettings().setLoadsImagesAutomatically(true);
            getSettings().setJavaScriptEnabled(true);
            getSettings().setDomStorageEnabled(true);
            getSettings().setAppCacheEnabled(true)
            getSettings().setAllowFileAccess(true);
            getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

            //check if device has network and load the cache if there no connection
            if(!hasNetworkAvailable(context)) {
                getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }

            webViewClient = WebViewClient()
            loadUrl(url);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    /**
     * WebClient for removing the header of the news page
     * for a less cluttered page design
     * NOTE: Removing of header depends only if it is inside a header tag in HTML
     *//*
    fun initWebClient() : WebViewClient {
        return object : WebViewClient() {
            override fun onPageFinished(webView: WebView?, url: String?) {
                webView?.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByTagName('header')[0];"
                        + "head.parentNode.removeChild(head);" +
                        "})()")
            }
        }
    }*/
}