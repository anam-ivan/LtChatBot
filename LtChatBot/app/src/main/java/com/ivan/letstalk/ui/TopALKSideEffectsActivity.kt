package com.ivan.letstalk.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.AlkMarkedSymptomsAdapter
import com.ivan.letstalk.adapter.AlkSideEffectsAdapter
import com.ivan.letstalk.adapter.ChatHistoryTitleAdapter
import com.ivan.letstalk.adapter.MedicalTermsGlossaryAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.AlkSideEffectsModel
import com.ivan.letstalk.model.faq.SortBy
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.model.markedAlkStatusChange.MarkedAlkStatusChangePayload
import com.ivan.letstalk.model.topAlkSideEffects.AlkSideEffectsPayload
import com.ivan.letstalk.model.topAlkSideEffects.Data
import com.ivan.letstalk.model.topAlkSideEffects.MarkedAlkSideEffectsPayload
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel


class TopALKSideEffectsActivity : AppCompatActivity() {
    private var transparentDialogLoader = TransparentDialogLoader()
    private var userId = ""
    private var markedCount = ""
    private lateinit var sessionManager : SessionManager
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tvAllSymptoms : AppCompatTextView
    private lateinit var tvMarkedSymptoms : AppCompatTextView
    private lateinit var tvMarkedSymptomsCount : AppCompatTextView
    // private val alkAllSymptomsModel = ArrayList<AlkSideEffectsModel>()
    private var alkAllSymptomsModel = ArrayList<Data>()
    // private val alkMarkedSymptomsModel = ArrayList<AlkSideEffectsModel>()
    private var alkMarkedSymptomsModel = ArrayList<com.ivan.letstalk.model.markedAlkSideEffects.Data>()
    private lateinit var rvAlkAllSymptoms: RecyclerView
    private lateinit var rvAlkMarkedSymptoms: RecyclerView
    private lateinit var alkSideEffectsAdapter: AlkSideEffectsAdapter

    private lateinit var alkMarkedSymptomsAdapter: AlkMarkedSymptomsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_alkside_effects)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        Log.d("userId", userId)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }

        setupViewModel()
        mSetupViewModel()
        allAlkSideEffectsObservers()
        // markedAlkSideEffectsObservers()

        tvAllSymptoms=  findViewById(R.id.tv_all_symptoms)
        tvMarkedSymptoms=  findViewById(R.id.tv_marked_symptoms)
        tvAllSymptoms.paint?.isUnderlineText = true
        tvAllSymptoms.setTextColor(ContextCompat.getColor(this, R.color.blue))
        tvMarkedSymptomsCount = findViewById(R.id.tv_marked_symptoms_count)

        rvAlkAllSymptoms = findViewById(R.id.rv_alk_all_symptoms)
        rvAlkAllSymptoms.visibility =  View.VISIBLE
        /*alkSideEffectsAdapter = AlkSideEffectsAdapter(alkAllSymptomsModel)
        rvAlkAllSymptoms.layoutManager = GridLayoutManager(this, 3)
        rvAlkAllSymptoms.itemAnimator = DefaultItemAnimator()
        rvAlkAllSymptoms.adapter = alkSideEffectsAdapter*/
        // initAllSymptomsData()

        rvAlkMarkedSymptoms = findViewById(R.id.rv_alk_marked_symptoms)
        /*alkMarkedSymptomsAdapter = AlkMarkedSymptomsAdapter(alkMarkedSymptomsModel)
        rvAlkMarkedSymptoms.layoutManager = GridLayoutManager(this, 3)
        rvAlkMarkedSymptoms.itemAnimator = DefaultItemAnimator()
        rvAlkMarkedSymptoms.adapter = alkMarkedSymptomsAdapter*/
        // initMarkedSymptomsData()


        tvMarkedSymptoms.setOnClickListener {
            /*tvAllSymptoms.paint?.isUnderlineText = false
            tvAllSymptoms.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

            tvAllSymptoms.visibility = View.VISIBLE
            tvMarkedSymptoms.paint?.isUnderlineText = true
            tvMarkedSymptoms.setTextColor(ContextCompat.getColor(this, R.color.blue))

            rvAlkAllSymptoms.visibility = View.GONE
            rvAlkMarkedSymptoms.visibility = View.VISIBLE*/
            markedAlkSideEffectsObservers()
            /*tvMarkedSymptomsCount.paint?.isUnderlineText = true
            tvMarkedSymptomsCount.setTextColor(ContextCompat.getColor(this, R.color.blue))
            tvMarkedSymptomsCount.text = "(".plus(markedCount).plus(")")*/
        }

        tvAllSymptoms.setOnClickListener {
            tvAllSymptoms.paint?.isUnderlineText = true
            tvAllSymptoms.setTextColor(ContextCompat.getColor(this, R.color.blue))

            tvMarkedSymptoms.paint?.isUnderlineText = false
            tvMarkedSymptoms.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

            tvMarkedSymptomsCount.paint?.isUnderlineText = false
            tvMarkedSymptomsCount.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

            rvAlkAllSymptoms.visibility = View.VISIBLE
            rvAlkMarkedSymptoms.visibility = View.GONE
            allAlkSideEffectsObservers()
        }
        /*setupViewModel()
        allAlkSideEffectsObservers()*/
    }

    private fun setupViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(PatientProfileViewModel::class.java)
    }

    private fun mSetupViewModel() {
        loginViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
    }

    /*private fun initAllSymptomsData() {
        var movie = AlkSideEffectsModel("Manage Weight Loss")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkAllSymptomsModel.add(movie)
        *//*movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkAllSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")*//*
        alkSideEffectsAdapter.notifyDataSetChanged()
    }*/

    /*private fun initMarkedSymptomsData() {
        var movie = AlkSideEffectsModel("Manage Weight Loss")
        alkMarkedSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkMarkedSymptomsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkMarkedSymptomsModel.add(movie)
        alkMarkedSymptomsAdapter.notifyDataSetChanged()
    }*/


    private fun allAlkSideEffectsObservers() {
        val body = AlkSideEffectsPayload.AlkSideEffects(
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
        patientProfileViewModel.allSideEffects(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            if (it.body() != null) {
                                if (it.body()!!.status == "success") {
                                    if (it.body()!!.data != null) {
                                        alkAllSymptomsModel = it.body()?.data as ArrayList<Data>
                                        alkSideEffectsAdapter =
                                            AlkSideEffectsAdapter(alkAllSymptomsModel)
                                        rvAlkAllSymptoms.layoutManager = GridLayoutManager(this, 3)
                                        rvAlkAllSymptoms.itemAnimator = DefaultItemAnimator()
                                        rvAlkAllSymptoms.adapter = alkSideEffectsAdapter
                                        Log.d(
                                            "TAG",
                                            "allAlkSideEffects: ${Gson().toJson(it.body())}"
                                        )
                                    }
                                }
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

    private fun markedAlkSideEffectsObservers() {
        val body = MarkedAlkSideEffectsPayload.MarkedAlkSideEffects(
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
        loginViewModel.markedSideEffects(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            if (it.body() != null) {
                                if (it.body()!!.status == "success") {
                                    if (it.body()!!.data != null) {
                                        markedCount = it.body()!!.count.toString()
                                        tvAllSymptoms.paint?.isUnderlineText = false
                                        tvAllSymptoms.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

                                        tvAllSymptoms.visibility = View.VISIBLE
                                        tvMarkedSymptoms.paint?.isUnderlineText = true
                                        tvMarkedSymptoms.setTextColor(ContextCompat.getColor(this, R.color.blue))

                                        rvAlkAllSymptoms.visibility = View.GONE
                                        rvAlkMarkedSymptoms.visibility = View.VISIBLE
                                        tvMarkedSymptomsCount.paint?.isUnderlineText = true
                                        tvMarkedSymptomsCount.setTextColor(ContextCompat.getColor(this, R.color.blue))
                                        tvMarkedSymptomsCount.text = "(".plus(markedCount).plus(")")
                                        alkMarkedSymptomsModel = it.body()?.data as ArrayList<com.ivan.letstalk.model.markedAlkSideEffects.Data>
                                        alkMarkedSymptomsAdapter = AlkMarkedSymptomsAdapter(
                                            alkMarkedSymptomsModel,
                                            object :
                                                AlkMarkedSymptomsAdapter.onClickListener {
                                                override fun onClick(pos: Int) {
                                                    it.body()!!.data[pos]._id.let {
                                                        Log.d("id", it)
                                                        markedSideEffectsStatusChange(it)
                                                        alkMarkedSymptomsAdapter.deleteItem(pos)
                                                        // markedAlkSideEffectsObservers()
                                                    }
                                                }

                                            })
                                        rvAlkMarkedSymptoms.layoutManager = GridLayoutManager(this, 3)
                                        rvAlkMarkedSymptoms.itemAnimator = DefaultItemAnimator()
                                        rvAlkMarkedSymptoms.adapter = alkMarkedSymptomsAdapter
                                        Log.d(
                                            "TAG",
                                            "markedAlkSideEffects: ${Gson().toJson(it.body()!!.data)}"
                                        )
                                    }
                                }
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

    private fun markedSideEffectsStatusChange(m_id: String) {
        val body = MarkedAlkStatusChangePayload.MarkedAlkStatusChange(
            m_id = m_id,
            status = "INACTIVE"
        )
        loginViewModel.markedSideEffectsStatusChange(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { it ->
                            if (it.body() != null) {
                                if (it.body()!!.status == "success") {
                                    Log.d("m_message",it.body()!!.message)
                                    markedAlkSideEffectsObservers()
                                    /*if (it.body()!!.data != null) {
                                        markedCount = it.body()!!.count.toString()
                                        tvAllSymptoms.paint?.isUnderlineText = false
                                        tvAllSymptoms.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))

                                        tvAllSymptoms.visibility = View.VISIBLE
                                        tvMarkedSymptoms.paint?.isUnderlineText = true
                                        tvMarkedSymptoms.setTextColor(ContextCompat.getColor(this, R.color.blue))

                                        rvAlkAllSymptoms.visibility = View.GONE
                                        rvAlkMarkedSymptoms.visibility = View.VISIBLE
                                        tvMarkedSymptomsCount.paint?.isUnderlineText = true
                                        tvMarkedSymptomsCount.setTextColor(ContextCompat.getColor(this, R.color.blue))
                                        tvMarkedSymptomsCount.text = "(".plus(markedCount).plus(")")
                                        alkMarkedSymptomsModel = it.body()?.data as ArrayList<com.ivan.letstalk.model.markedAlkSideEffects.Data>
                                        alkMarkedSymptomsAdapter = AlkMarkedSymptomsAdapter(alkMarkedSymptomsModel)
                                        rvAlkMarkedSymptoms.layoutManager = GridLayoutManager(this, 3)
                                        rvAlkMarkedSymptoms.itemAnimator = DefaultItemAnimator()
                                        rvAlkMarkedSymptoms.adapter = alkMarkedSymptomsAdapter
                                        Log.d(
                                            "TAG",
                                            "markedAlkSideEffects: ${Gson().toJson(it.body()!!.data)}"
                                        )
                                    }*/
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        // transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        // transparentDialogLoader.show(this)
                    }
                }

            }
        }
    }
}