package com.ivan.letstalk.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.MedicalTermsGlossaryAdapter
import com.ivan.letstalk.adapter.PrescriptionAbbreviationsGlossaryAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityGlossaryBinding
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.faq.SortBy
import com.ivan.letstalk.model.glossary.Data
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.viewModel.PatientProfileViewModel


class GlossaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGlossaryBinding
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private var transparentDialogLoader = TransparentDialogLoader()
    /*private lateinit var tvMedicalTerms: AppCompatTextView
    private lateinit var rvMedicalTerms: IndexFastScrollRecyclerView*/
    private var medicalTermsList: List<Data>? = null
    var prescriptionAbbreviationsList: List<Data>? = null
    private var isMedicalTermsSelected : Boolean = false
    private var isPrescriptionAbbreviationsSelected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_glossary)
        binding.tvMedicalTerms.paint?.isUnderlineText = true
        binding.tvMedicalTerms.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding.rvMedicalTerms.visibility = View.VISIBLE
        binding.edtMedicalSearch.visibility = View.VISIBLE

        binding.tvMedicalTerms.setOnClickListener {
            if (binding.edtMedicalSearch.visibility == View.GONE) {
                binding.edtMedicalSearch.visibility = View.VISIBLE
                binding.edtMedicalSearch.requestFocus()
                // binding.edtMedicalSearch.setText("")
                /*if (binding.edtMedicalSearch.text.toString().trim().isNotEmpty()) {
                    binding.edtMedicalSearch.text?.clear()
                }*/
            }
            if (!TextUtils.isEmpty(binding.edtMedicalSearch.text?.trim())) {
                // binding.edtMedicalSearch.text?.clear()
                // Toast.makeText(this@GlossaryActivity, "Its a toast!", Toast.LENGTH_SHORT).show()
            }
            // binding.edtMedicalSearch.setText("")
            if (binding.edtPrescriptionSearch.visibility == View.VISIBLE){
                binding.edtPrescriptionSearch.visibility = View.GONE
            }
            binding.tvPrescriptionAbbreviations.paint?.isUnderlineText = false
            binding.tvPrescriptionAbbreviations.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

            binding.tvPrescriptionAbbreviations.visibility = View.VISIBLE
            binding.tvMedicalTerms.paint?.isUnderlineText = true
            binding.tvMedicalTerms.setTextColor(ContextCompat.getColor(this, R.color.blue))

            binding.rvPrescriptionAbbreviations.visibility = View.GONE
            binding.rvMedicalTerms.visibility = View.VISIBLE
            /*isMedicalTermsSelected = true
            isPrescriptionAbbreviationsSelected = false*/
            // binding.edtMedicalSearch.setText("")
            glossaryMedicalTermsObservers()
        }

        binding.tvPrescriptionAbbreviations.setOnClickListener {
            binding.edtMedicalSearch.visibility = View.GONE
            binding.edtPrescriptionSearch.visibility = View.VISIBLE

            binding.tvPrescriptionAbbreviations.paint?.isUnderlineText = true
            binding.tvPrescriptionAbbreviations.setTextColor(ContextCompat.getColor(this, R.color.blue))

            binding.tvMedicalTerms.paint?.isUnderlineText = false
            binding.tvMedicalTerms.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

            binding.rvPrescriptionAbbreviations.visibility = View.VISIBLE
            binding.rvMedicalTerms.visibility = View.GONE
            glossaryPrescriptionAbbreviationsObservers()
            /*isPrescriptionAbbreviationsSelected = true
            isMedicalTermsSelected = false*/
            /*binding.edtPrescriptionSearch.text?.clear()
            binding.edtMedicalSearch.text?.clear()*/
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        setupGlossaryViewModel()
        glossaryMedicalTermsObservers()

        binding.edtMedicalSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchKey: String = s.toString()
                /*if (s.length == 2) {
                    glossaryMedicalTermsSearch(searchKey)
                }*/
                if (s.isNotEmpty()) {
                    glossaryMedicalTermsSearch(searchKey)
                }
                if (s.isEmpty()) {
                    glossaryMedicalTermsSearch(searchKey)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        // Prescription Abbreviations Search
        binding.edtPrescriptionSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchKey: String = s.toString()
                /*if (s.length == 2) {
                    glossaryPrescriptionAbbreviationsSearch(searchKey)
                }*/
                if (s.isNotEmpty()) {
                    glossaryPrescriptionAbbreviationsSearch(searchKey)
                }
                if (s.isEmpty()) {
                    glossaryPrescriptionAbbreviationsSearch(searchKey)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun setupGlossaryViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    private fun glossaryMedicalTermsObservers() {
        val body = GlossaryPayload.Glossary(
            type = "Medical Terms",
            draw = 1,
            limit = 100,
            page_no = 1,
            perpage = 1,
            search_key = "",
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.glossaryList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        // binding.edtMedicalSearch.text!!.clear()
                        resource.data?.let { it ->
                            medicalTermsList = it.body()?.data
                            /*Collections.sort(
                                medicalTermsList,
                                Comparator<Any?> { lhs, rhs -> //here getTitle() method return app name...
                                    lhs.().compareTo(rhs.getTitle())
                                })*/
                            // medicalTermsList?.sortedBy { it.title?.lowercase() }
                            Log.d("TAG", "medicalTermsList: ${Gson().toJson(it.body())}")
                            binding.rvMedicalTerms.layoutManager = LinearLayoutManager(this)
                            binding.rvMedicalTerms.adapter = medicalTermsList?.let { it1 ->
                                MedicalTermsGlossaryAdapter(
                                    it1
                                )
                            }
                            /*rvMedicalTerms.setIndexTextSize(12)
                            rvMedicalTerms.setIndexBarColor("#BDC5C4")
                            rvMedicalTerms.setIndexBarCornerRadius(0)
                            rvMedicalTerms.setIndexBarTransparentValue(0.4.toFloat())
                            // rvMedicalTerms.setIndexbarMargin(4F)
                            *//*rvMedicalTerms.setIndexbarTopMargin(60)
                            rvMedicalTerms.setIndexbarBottomMargin(100)
                            rvMedicalTerms.setIndexbarHorizontalMargin(20)*//*
                            rvMedicalTerms.setPreviewPadding(0)
                            rvMedicalTerms.setIndexBarTextColor("#FFFFFF")
                            rvMedicalTerms.setPreviewTextSize(60)
                            rvMedicalTerms.setPreviewColor("#33334c")
                            rvMedicalTerms.setPreviewTextColor("#FFFFFF")
                            rvMedicalTerms.setPreviewTransparentValue(0.6f)
                            rvMedicalTerms.setIndexBarCornerRadius(12)
                            rvMedicalTerms.setIndexBarVisibility(true)
                            rvMedicalTerms.setIndexBarHighLightTextVisibility(true)
                            Objects.requireNonNull<RecyclerView.LayoutManager>(rvMedicalTerms.layoutManager)
                                .scrollToPosition(0)*/
                            binding.rvMedicalTerms.setIndexTextSize(12)
                            binding.rvMedicalTerms.setIndexBarCornerRadius(12)
                            binding.rvMedicalTerms.setIndexBarTextColor("#898989")
                            binding.rvMedicalTerms.setIndexBarStrokeColor("#FFFFFF")
                            binding.rvMedicalTerms.setIndexBarColor("#E5E5E5")
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvMedicalTerms.setIndexBarTransparentValue(
                            1.0.toFloat()
                        )

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

    private fun glossaryPrescriptionAbbreviationsObservers() {
        val body = GlossaryPayload.Glossary(
            type = "Prescription Abbreviations",
            draw = 1,
            limit = 100,
            page_no = 1,
            perpage = 1,
            search_key = "",
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.glossaryList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let {
                            prescriptionAbbreviationsList = it.body()?.data
                            // prescriptionAbbreviationsList?.sortedBy { it.title?.lowercase() }
                            // prescriptionAbbreviationsList?.sortedByDescending { it.title }
                            Log.d("TAG", "prescriptionAbbreviationsList: ${Gson().toJson(it.body()?.data ?: "")}")
                            binding.rvPrescriptionAbbreviations.layoutManager = LinearLayoutManager(this)
                            binding.rvPrescriptionAbbreviations.adapter = prescriptionAbbreviationsList?.let { it1 ->
                                PrescriptionAbbreviationsGlossaryAdapter(
                                    it1
                                )
                            }
                            binding.rvPrescriptionAbbreviations.setIndexTextSize(12)
                            binding.rvPrescriptionAbbreviations.setIndexBarCornerRadius(12)
                            binding.rvPrescriptionAbbreviations.setIndexBarTextColor("#898989")
                            binding.rvPrescriptionAbbreviations.setIndexBarStrokeColor("#FFFFFF")
                            binding.rvPrescriptionAbbreviations.setIndexBarColor("#E5E5E5")
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvPrescriptionAbbreviations.setIndexBarTransparentValue(
                                1.0.toFloat()
                            )

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

    private fun glossaryMedicalTermsSearch(searchKey: String) {
        val body = GlossaryPayload.Glossary(
            type = "Medical Terms",
            draw = 1,
            limit = 100,
            page_no = 1,
            perpage = 1,
            search_key = searchKey,
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.glossaryList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            medicalTermsList = it.body()?.data
                            /*Collections.sort(
                                medicalTermsList,
                                Comparator<Any?> { lhs, rhs -> //here getTitle() method return app name...
                                    lhs.().compareTo(rhs.getTitle())
                                })*/
                            // medicalTermsList?.sortedBy { it.title?.lowercase() }
                            Log.d("TAG", "medicalTermsList: ${Gson().toJson(it.body())}")
                            binding.rvMedicalTerms.layoutManager = LinearLayoutManager(this)
                            binding.rvMedicalTerms.adapter = medicalTermsList?.let { it1 ->
                                MedicalTermsGlossaryAdapter(
                                    it1
                                )
                            }
                            /*rvMedicalTerms.setIndexTextSize(12)
                            rvMedicalTerms.setIndexBarColor("#BDC5C4")
                            rvMedicalTerms.setIndexBarCornerRadius(0)
                            rvMedicalTerms.setIndexBarTransparentValue(0.4.toFloat())
                            // rvMedicalTerms.setIndexbarMargin(4F)
                            *//*rvMedicalTerms.setIndexbarTopMargin(60)
                            rvMedicalTerms.setIndexbarBottomMargin(100)
                            rvMedicalTerms.setIndexbarHorizontalMargin(20)*//*
                            rvMedicalTerms.setPreviewPadding(0)
                            rvMedicalTerms.setIndexBarTextColor("#FFFFFF")
                            rvMedicalTerms.setPreviewTextSize(60)
                            rvMedicalTerms.setPreviewColor("#33334c")
                            rvMedicalTerms.setPreviewTextColor("#FFFFFF")
                            rvMedicalTerms.setPreviewTransparentValue(0.6f)
                            rvMedicalTerms.setIndexBarCornerRadius(12)
                            rvMedicalTerms.setIndexBarVisibility(true)
                            rvMedicalTerms.setIndexBarHighLightTextVisibility(true)
                            Objects.requireNonNull<RecyclerView.LayoutManager>(rvMedicalTerms.layoutManager)
                                .scrollToPosition(0)*/
                            binding.rvMedicalTerms.setIndexTextSize(12)
                            binding.rvMedicalTerms.setIndexBarCornerRadius(12)
                            binding.rvMedicalTerms.setIndexBarTextColor("#898989")
                            binding.rvMedicalTerms.setIndexBarStrokeColor("#FFFFFF")
                            binding.rvMedicalTerms.setIndexBarColor("#E5E5E5")
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvMedicalTerms.setIndexBarTransparentValue(
                                1.0.toFloat()
                            )
                            // binding.edtMedicalSearch.text?.clear()
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

    private fun glossaryPrescriptionAbbreviationsSearch(searchKey: String) {
        val body = GlossaryPayload.Glossary(
            type = "Prescription Abbreviations",
            draw = 1,
            limit = 100,
            page_no = 1,
            perpage = 1,
            search_key = searchKey,
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.glossaryList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let {
                            prescriptionAbbreviationsList = it.body()?.data
                            // prescriptionAbbreviationsList?.sortedBy { it.title?.lowercase() }
                            // prescriptionAbbreviationsList?.sortedByDescending { it.title }
                            Log.d("TAG", "prescriptionAbbreviationsList: ${Gson().toJson(it.body()?.data ?: "")}")
                            binding.rvPrescriptionAbbreviations.layoutManager = LinearLayoutManager(this)
                            binding.rvPrescriptionAbbreviations.adapter = prescriptionAbbreviationsList?.let { it1 ->
                                PrescriptionAbbreviationsGlossaryAdapter(
                                    it1
                                )
                            }
                            binding.rvPrescriptionAbbreviations.setIndexTextSize(12)
                            binding.rvPrescriptionAbbreviations.setIndexBarCornerRadius(12)
                            binding.rvPrescriptionAbbreviations.setIndexBarTextColor("#898989")
                            binding.rvPrescriptionAbbreviations.setIndexBarStrokeColor("#FFFFFF")
                            binding.rvPrescriptionAbbreviations.setIndexBarColor("#E5E5E5")
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvPrescriptionAbbreviations.setIndexBarTransparentValue(
                                1.0.toFloat()
                            )

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