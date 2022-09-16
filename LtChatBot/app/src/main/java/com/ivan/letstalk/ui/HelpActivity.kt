package com.ivan.letstalk.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ivan.letstalk.R
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityHelpBinding
import com.ivan.letstalk.databinding.ActivityTermsConditionsBinding
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.slug.SlugPayload
import com.ivan.letstalk.viewModel.PatientProfileViewModel

class HelpActivity : AppCompatActivity() {
    lateinit var binding: ActivityHelpBinding
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var viewModel: PatientProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help)
        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        setupTermsConditionsViewModel()
        helpObservers()
    }
    private fun setupTermsConditionsViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }
    private fun helpObservers() {
        val body = SlugPayload.Slug(
            slug = "help",
        )
        viewModel.slugDetails(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            Log.d("help",it.body()?.data?.description.toString())
                            binding.tvHelp.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(it.body()?.data?.description.toString(), Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                Html.fromHtml(it.body()?.data?.description.toString())
                            }
                        }
                    }
                    Status.ERROR -> {
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}