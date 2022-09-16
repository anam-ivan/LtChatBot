package com.ivan.letstalk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.github.barteksc.pdfviewer.PDFView
import com.ivan.letstalk.R
import com.ivan.letstalk.databinding.ActivityFileViewerBinding
import java.net.URLConnection


class FileViewerActivity : AppCompatActivity(){
    private val TAG = "FileViewerActivity"
    lateinit var binding: ActivityFileViewerBinding
    var pdfView: PDFView? = null
    private var pdfFileName: String? = null
    private var filePath = ""
    var fullFilePath = ""
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_viewer)
        if (intent != null && intent.hasExtra("filePath")) {
            filePath = intent.getStringExtra("filePath").toString()
            fullFilePath = "http://letstalk.dev13.ivantechnology.in/$filePath"
            Log.d(fullFilePath, fullFilePath)
            if (fullFilePath.isNotEmpty()) {
                if (isImageFile(fullFilePath)) {
                    binding.imgView.visibility = View.VISIBLE
                    Glide.with(this)
                        .load("http://letstalk.dev13.ivantechnology.in/".plus(filePath))
                        .placeholder(R.drawable.img_placeholder)
                        .into(binding.imgView)
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.webView.visibility = View.VISIBLE
                    binding.webView.webViewClient = WebViewClient()
                    binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$fullFilePath")
                    binding.webView.settings.setSupportZoom(true)
                    binding.webView.settings.javaScriptEnabled = true
                }
            }
        }
        val path = fullFilePath
        if (fullFilePath.isNotEmpty()) {
            val filename = path.substring(path.lastIndexOf("/") + 1)
            binding.tvFileName.text = filename
        }
        binding.btnBack.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
    }
    private fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
    open inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }
    }
}