package com.ivan.letstalk.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.databinding.ActivityHelpBinding
import com.ivan.letstalk.databinding.ActivityKnowYourSideEffectsDetailsBinding
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.model.myHealthVitals.Value

class KnowYourSideEffectsDetails : AppCompatActivity() {
    var desc = ""
    var title = ""
    var symptoms = ""
    var shortDesc = ""
    lateinit var binding: ActivityKnowYourSideEffectsDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_know_your_side_effects_details)
        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        if (intent != null && intent.hasExtra("description")) {
            desc = intent.getStringExtra("description").toString()
            Log.d("desc", desc)
            binding.tvDesc.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(desc)
            }
            // binding.tvDesc.text = desc
        }
        if (intent != null && intent.hasExtra("title")) {
            title = intent.getStringExtra("title").toString()
            Log.d("title", title)
            binding.tvTitle.text = title
        }
        if (intent != null && intent.hasExtra("symptoms")) {
            symptoms = intent.getStringExtra("symptoms").toString()
            Log.d("symptoms", symptoms)
            binding.tvSymptoms.text = symptoms
        }
        if (intent != null && intent.hasExtra("shortDesc")) {
            shortDesc = intent.getStringExtra("shortDesc").toString()
            Log.d("shortDesc", shortDesc)
            binding.tvShortDesc.text = shortDesc
        }
        // binding.tvDesc.text = desc
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}