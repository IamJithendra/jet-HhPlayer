package com.hh.composeplayer.ui.video

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.http.SslError
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hh.composeplayer.R
import com.just.agentweb.AgentWeb
import com.just.agentweb.BaseIndicatorView
import com.just.agentweb.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async

class WebController {
    private var agentWeb: AgentWeb? = null

    suspend fun loadUrl(activity: AppCompatActivity, url: String?, container: ViewGroup, coroutineScope: CoroutineScope) {
        agentWeb = AgentWeb.with(activity)
            .setAgentWebParent(
                container,
                ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            .setCustomIndicator(CustomIndicator(container.context))
            .setWebViewClient(getWebViewClient(coroutineScope))
            .createAgentWeb()
            .ready()
            .go(url)
            .apply {
                webCreator.webView.setBackgroundColor(Color.BLACK)
            }
    }

    fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
    }

    fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
    }

    fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
    }

    fun onBackPressed(): Boolean {
        return agentWeb?.back() == true
    }


    private suspend fun getWebViewClient(coroutineScope: CoroutineScope): WebViewClient {
        val job = coroutineScope.async(IO) {
             val web =    object : WebViewClient() {
                    @SuppressLint("WebViewClientOnReceivedSslError")
                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler,
                        error: SslError?
                    ) {
                        handler.proceed()
                        onResume()
                    }
                }
            web
        }
        return job.await()
    }

    inner class CustomIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : BaseIndicatorView(context, attrs, defStyleAttr) {
        var tvProgress:TextView? = null

        init {
            val v = View.inflate(context, R.layout.custom_web_indicator, this)
            tvProgress = v.findViewById(R.id.tvProgress)
        }

        override fun offerLayoutParams(): LayoutParams {
            return LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        override fun show() {
            super.show()
            visibility = View.VISIBLE
        }

        override fun hide() {
            super.hide()
            visibility = View.GONE
        }

        @SuppressLint("SetTextI18n")
        override fun setProgress(newProgress: Int) {
            super.setProgress(newProgress)
            tvProgress?.text = "$newProgress%"
        }

        override fun reset() {
            super.reset()
            tvProgress?.text = "加载中..."
        }
    }
}



