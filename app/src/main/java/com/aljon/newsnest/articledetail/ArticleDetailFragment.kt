package com.aljon.newsnest.articledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aljon.newsnest.databinding.ArticleDetailFragmentBinding
import com.aljon.newsnest.utils.hasNetworkAvailable

class ArticleDetailFragment: Fragment() {

    private lateinit var binding: ArticleDetailFragmentBinding

    private val viewModel: ArticleDetailViewModel by lazy {
        var url = ArticleDetailFragmentArgs.fromBundle(arguments!!).url
        ViewModelProvider(this, ArticleDetailViewModelFactory(url))
            .get(ArticleDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        observeWebUrl()

        return binding.root
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
        binding.webView.apply {
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