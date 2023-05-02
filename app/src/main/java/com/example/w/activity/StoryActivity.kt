package com.example.w.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.w.R
import com.example.w.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class StoryActivity : AppCompatActivity() {

    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)


        // 获取传递过来的链接
        val url = intent.getStringExtra("url")
        val id = intent.getStringExtra("id")
        // 初始化 WebView
        val webView: WebView = findViewById(R.id.web_view)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // 加载链接
        if (url != null) {
            webView.loadUrl(url)
        }














        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // 处理点击事件
            when (item.itemId) {
                R.id.menu_web -> {
                    // 点击首页
                }
                R.id.menu_home -> {
                    val intent = Intent(this@StoryActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_comments -> {
                    val intent = Intent(this@StoryActivity, CommentsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }


    }

    override fun onBackPressed() {
        // 点击返回按钮时，如果 WebView 可以返回，则返回，否则关闭 Activity
        val webView: WebView = findViewById(R.id.web_view)
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }
}