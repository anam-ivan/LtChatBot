package com.ivan.letstalk.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.RegularizeDataAdapter
import com.ivan.letstalk.adapter.SideEffectsHistoryAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityMySideEffectHistoryBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.sideEffectsHistoryResponse.ExistingSideEffects
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectUserDetailsBodies
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectsHistoryDateDuration
import com.ivan.letstalk.viewModel.LoginViewModel

class MySideEffectHistory : AppCompatActivity() {
    private var isPreviousSideEffectLoaded = false
    private var mAbc =ArrayList<String>()
    private var previousSideEffectsChipList =ArrayList<String>()
    private var storeSideEffectsId =ArrayList<String>()
    private var storePreviousEffectsId =ArrayList<String>()
    private var userId = ""
    private var chipId = 0
    private var chipName = ""
    private lateinit var sessionManager : SessionManager
    lateinit var binding: ActivityMySideEffectHistoryBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var tvExistingSideEffects: AppCompatTextView
    private lateinit var tvPreviousSideEffects: AppCompatTextView
    private lateinit var tvSideEffect: AppCompatTextView
    private var existingSideEffects = ArrayList<ExistingSideEffects>()
    private var previousSideEffects = ArrayList<ExistingSideEffects>()
    var sideEffectsDateDuration = ArrayList<SideEffectsHistoryDateDuration>()
    private lateinit var cgExistingSideEffects: ChipGroup
    private lateinit var cgPreviousSideEffects: ChipGroup

    private var existingSideEffectsList = arrayOf(
        "Abdominal Pain", "Constipation", "Dyspepsia", "Dysphagia", "Electrocardiogram QT prlonged",
        "Nausea", "Vomiting", "Vision Disorder", "Constipation",  "Dyspepsia", "Nausea", "Abdominal Pain", "Constipation", "Dyspepsia", "Dysphagia", "Electrocardiogram QT prlonged",
        "Nausea", "Vomiting", "Vision Disorder", "Constipation",  "Dyspepsia", "Nausea"
    )

    var previousSideEffectsList = arrayOf(
        "Dyspepsia", "Nausea", "Abdominal Pain", "Constipation", "Dyspepsia", "Dysphagia", "Electrocardiogram QT prlonged",
        "Nausea", "Vomiting", "Vision Disorder", "Constipation",  "Dyspepsia", "Nausea"
    )
    // get selected language list
    private var existingSideEffectsChipItems = ArrayList<String>()
    private var previousSideEffectsChipItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_side_effect_history)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_side_effect_history)
        tvSideEffect = findViewById(R.id.tv_side_effect)
        cgExistingSideEffects = findViewById(R.id.chip_existing_side_effects)
        cgPreviousSideEffects = findViewById(R.id.chip_previous_side_effects)

        tvExistingSideEffects=  findViewById(R.id.tv_existing_side_effects)
        tvPreviousSideEffects=  findViewById(R.id.tv_preview_side_effects)

        cgExistingSideEffects.visibility = View.VISIBLE
        tvExistingSideEffects.paint?.isUnderlineText = true
        tvExistingSideEffects.setTextColor(ContextCompat.getColor(this, R.color.blue))

        setupViewModel()
        existingSideEffectsHistoryObservers()
        /*val sideEffectsHistoryAdapter = SideEffectsHistoryAdapter(sideEffectsDateDuration)
        binding.rvSideEffectHistory.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvSideEffectHistory.itemAnimator = DefaultItemAnimator()
        binding.rvSideEffectHistory.adapter = sideEffectsHistoryAdapter*/

        tvExistingSideEffects.setOnClickListener {
            binding.rvPreviousSideEffectHistory.visibility = View.GONE
            binding.rvExitingSideEffectHistory.visibility = View.GONE
            if (tvSideEffect.text.toString().isNotEmpty()) {
                tvSideEffect.text = ""
                sideEffectsDateDuration.clear()
            }
            // Clear chip
            for (i in 0 until cgExistingSideEffects.childCount) {
                (cgExistingSideEffects.getChildAt(i) as Chip).isChecked =
                    false
            }
            tvExistingSideEffects.paint?.isUnderlineText = true
            tvExistingSideEffects.setTextColor(ContextCompat.getColor(this, R.color.blue))
            cgExistingSideEffects.visibility = View.VISIBLE
            cgPreviousSideEffects.visibility = View.GONE

            tvPreviousSideEffects.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))
            tvPreviousSideEffects.paint?.isUnderlineText = false
        }

        tvPreviousSideEffects.setOnClickListener {
            if (!isPreviousSideEffectLoaded) {
                previousSideEffectsHistoryObservers()
            }
            if (tvSideEffect.text.toString().isNotEmpty()) {
                tvSideEffect.text = ""
            }
            // Clear chip
            for (i in 0 until cgPreviousSideEffects.childCount) {
                (cgPreviousSideEffects.getChildAt(i) as Chip).isChecked =
                    false
            }
            binding.rvExitingSideEffectHistory.visibility = View.GONE
            // binding.rvPreviousSideEffectHistory.visibility = View.VISIBLE
            binding.rvPreviousSideEffectHistory.visibility = View.GONE
            tvExistingSideEffects.paint?.isUnderlineText = false
            tvExistingSideEffects.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))
            cgExistingSideEffects.visibility = View.GONE
            cgPreviousSideEffects.visibility = View.VISIBLE

            tvPreviousSideEffects.setTextColor(ContextCompat.getColor(this, R.color.blue))
            tvPreviousSideEffects.paint?.isUnderlineText = true
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        /*setupViewModel()
        sideEffectsHistoryObservers()*/
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun initExistingSideEffectsData() {
        for (i in mAbc.indices) {
            cgExistingSideEffects = findViewById(R.id.chip_existing_side_effects)
            val entryChip2: Chip = getChip(mAbc[i])
            entryChip2.id = i
            //set default selected language
            //entryChip2.setChecked(true);
            cgExistingSideEffects.addView(entryChip2)
        }
    }

    private fun initPreviousSideEffectsData() {
        for (i in previousSideEffectsChipList.indices) {
            cgPreviousSideEffects = findViewById(R.id.chip_previous_side_effects)
            val entryChip2: Chip = getPreviousChip(previousSideEffectsChipList[i])
            entryChip2.id = i
            //set default selected language
            //entryChip2.setChecked(true);
            cgPreviousSideEffects.addView(entryChip2)
        }
    }

    private fun getChip(text: String): Chip {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 50f,
            resources.displayMetrics
        ).toInt()
        chip.setChipBackgroundColorResource(R.color.white)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        chip.isCloseIconVisible = false
        chip.isCheckedIconVisible = false
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rvExitingSideEffectHistory.visibility = View.VISIBLE
                sideEffectsDateDuration.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.blue)
                    tvSideEffect.text = buttonView.text.toString()
                    chipId = buttonView.id
                    chipName = buttonView.text.toString()
                    Log.d("chipName", chipName)
                    existingSideEffectsChipItems.add(chip.text.toString())
                    mExistingSideEffectsHistoryObservers(chipId)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
                chip.isChecked = true
            } else {
                binding.rvExitingSideEffectHistory.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.white)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.black))
                chip.isChecked = false
                existingSideEffectsChipItems.remove(chip.text.toString())
            }
        }
        return chip
    }

    private fun getPreviousChip(text: String): Chip {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.my_chip))
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 50f,
            resources.displayMetrics
        ).toInt()
        chip.setChipBackgroundColorResource(R.color.white)
        chip.setTextColor(ContextCompat.getColor(this, R.color.black))
        chip.isCloseIconVisible = false
        chip.isCheckedIconVisible = false
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.text = text
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rvPreviousSideEffectHistory.visibility = View.VISIBLE
                sideEffectsDateDuration.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.blue)
                    tvSideEffect.text = buttonView.text.toString()
                    chipId = buttonView.id
                    previousSideEffectsChipItems.add(chip.text.toString())
                    sideEffectsDateDuration.clear()
                    mPreviousSideEffectsHistoryObservers(chipId)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
                chip.isChecked = true
            } else {
                binding.rvPreviousSideEffectHistory.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    chip.chipBackgroundColor = getColorStateList(R.color.white)
                }
                chip.setTextColor(ContextCompat.getColor(this, R.color.black))
                chip.isChecked = false
                previousSideEffectsChipItems.remove(chip.text.toString())
            }
        }
        return chip
    }

    private fun existingSideEffectsHistoryObservers() {
        val body = SideEffectUserDetailsBodies.UserDetails(
            user_side_id = userId
        )
        viewModel.sideEffectsHistory(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "sideEffectsHistoryData: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                existingSideEffects = ArrayList()
                                for (i in 0 until it.body()!!.data.size) {
                                    existingSideEffects.add(ExistingSideEffects(it.body()!!.data[i].sideEffectName.toString()))
                                    if (it.body()!!.data[i].status == "ACTIVE") {
                                        it.body()!!.data[i].sideEffectName?.let { it1 ->
                                            mAbc.add(
                                                it1
                                            )
                                        }
                                        it.body()!!.data[i]._id?.let { it1 ->
                                            storeSideEffectsId.add(
                                                it1
                                            )
                                        }
                                        // it.body()!!.data[i].sideEffectId?.let { it1 -> storeSideEffectsId.add(it1)}
                                        /*sideEffectsDateDuration.add(
                                            SideEffectsHistoryDateDuration(
                                                it.body()!!.data[i].created_at.toString(),
                                                it.body()!!.data[i].updated_at.toString()
                                            )
                                        )*/
                                    }
                                    /*sideEffectsDateDuration.add(SideEffectsHistoryDateDuration(it.body()!!.data[i].created_at.toString(),
                                        it.body()!!.data[i].updated_at.toString()
                                    ))
                                    it.body()!!.data[i].sideEffectName?.let { it1 -> mAbc.add(it1) }*/
                                }
                                Log.d("TAG", "storeIds: ${Gson().toJson(storeSideEffectsId)}")
                                Log.d("storeSideEffectsId",storeSideEffectsId[chipId])

                                for (i in 0 until it.body()!!.data.size) {
                                    /*if (it.body()!!.data[i].status == "ACTIVE") {
                                        if (it.body()!!.data[i].sideEffectId == storeSideEffectsId[chipId]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }
                                    }*/
                                    if (it.body()!!.data[i].status == "ACTIVE") {
                                        if (it.body()!!.data[i]._id == storeSideEffectsId[chipId]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }
                                    }
                                    /*if (it.body()!!.data[i].status == "ACTIVE") {
                                        sideEffectsDateDuration.add(
                                            SideEffectsHistoryDateDuration(
                                                it.body()!!.data[i].created_at.toString(),
                                                it.body()!!.data[i].updated_at.toString()
                                            )
                                        )
                                    }*/
                                }
                                Log.d("TAG", "check: ${Gson().toJson(it.body()!!.data[chipId].created_at)}")
                                Log.d("TAG", "sideEffectsHistory: ${Gson().toJson(mAbc)}")
                                Log.d("TAG", "sideEffectsDateDuration: ${Gson().toJson(sideEffectsDateDuration)}")
                            }
                            initExistingSideEffectsData()
                        }
                    }
                    Status.ERROR -> {
                        Log.d("issue",it.message.toString())
                        showErrorMsg(it.message.toString(),binding.root)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }


    private fun mExistingSideEffectsHistoryObservers(chipId: Int) {
        val body = SideEffectUserDetailsBodies.UserDetails(
            user_side_id = userId
        )
        viewModel.sideEffectsHistory(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "sideEffectsHistoryData: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                existingSideEffects = ArrayList()
                                for (i in 0 until it.body()!!.data.size) {
                                    existingSideEffects.add(ExistingSideEffects(it.body()!!.data[i].sideEffectName.toString()))
                                    if (it.body()!!.data[i].status == "ACTIVE") {
                                        it.body()!!.data[i].sideEffectName?.let { it1 -> mAbc.add(it1) }
                                        it.body()!!.data[i]._id?.let { it1 -> storeSideEffectsId.add(it1) }
                                    }
                                }
                                Log.d("TAG", "storeIds: ${Gson().toJson(storeSideEffectsId)}")

                                for (i in 0 until it.body()!!.data.size) {
                                    if (it.body()!!.data[i].status == "ACTIVE") {
                                        if (it.body()!!.data[i]._id == storeSideEffectsId[chipId]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }
                                    }
                                }
                                Log.d("TAG", "check: ${Gson().toJson(it.body()!!.data[chipId].created_at)}")
                                Log.d("TAG", "sideEffectsHistory: ${Gson().toJson(mAbc)}")
                                Log.d("TAG", "sideEffectsDateDuration: ${Gson().toJson(sideEffectsDateDuration)}")
                            }
                            val sideEffectsHistoryAdapter = SideEffectsHistoryAdapter(sideEffectsDateDuration)
                            binding.rvExitingSideEffectHistory.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.rvExitingSideEffectHistory.itemAnimator = DefaultItemAnimator()
                            binding.rvExitingSideEffectHistory.adapter = sideEffectsHistoryAdapter
                        }
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    private fun previousSideEffectsHistoryObservers() {
        val body = SideEffectUserDetailsBodies.UserDetails(
            user_side_id = userId
        )
        viewModel.sideEffectsHistory(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "sideEffectsHistoryData: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                previousSideEffects = ArrayList()
                                for (i in 0 until it.body()!!.data.size) {
                                    previousSideEffects.add(ExistingSideEffects(it.body()!!.data[i].sideEffectName.toString()))
                                    isPreviousSideEffectLoaded = true
                                    if (it.body()!!.data[i].status == "INACTIVE") {
                                        it.body()!!.data[i].sideEffectName?.let { it1 -> previousSideEffectsChipList.add(it1) }
                                        it.body()!!.data[i]._id?.let { it1 -> storeSideEffectsId.add(it1) }
                                        /*sideEffectsDateDuration.add(
                                            SideEffectsHistoryDateDuration(
                                                it.body()!!.data[i].created_at.toString(),
                                                it.body()!!.data[i].updated_at.toString()
                                            )
                                        )*/
                                    } else {
                                        Log.d("inactive","There is no data in Inactive")
                                    }
                                    /*sideEffectsDateDuration.add(SideEffectsHistoryDateDuration(it.body()!!.data[i].created_at.toString(),
                                        it.body()!!.data[i].updated_at.toString()
                                    ))
                                    it.body()!!.data[i].sideEffectName?.let { it1 -> mAbc.add(it1) }*/
                                }
                                Log.d("TAG", "storeIds: ${Gson().toJson(storeSideEffectsId)}")

                                for (i in 0 until it.body()!!.data.size) {
                                    if (it.body()!!.data[i].status == "INACTIVE") {
                                        if (it.body()!!.data[i]._id == storeSideEffectsId[chipId]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }
                                        /*if (it.body()!!.data[i].sideEffectName == mAbc[chipName]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }*/
                                    }
                                }
                            }
                            initPreviousSideEffectsData()
                        }
                    }
                    Status.ERROR -> {
                        Log.d("issue",it.message.toString())
                        showErrorMsg(it.message.toString(),binding.root)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    private fun mPreviousSideEffectsHistoryObservers(chipId: Int) {
        val body = SideEffectUserDetailsBodies.UserDetails(
            user_side_id = userId
        )
        viewModel.sideEffectsHistory(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "sideEffectsHistoryData: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                previousSideEffects = ArrayList()
                                for (i in 0 until it.body()!!.data.size) {
                                    previousSideEffects.add(ExistingSideEffects(it.body()!!.data[i].sideEffectName.toString()))
                                    if (it.body()!!.data[i].status == "INACTIVE") {
                                        it.body()!!.data[i].sideEffectName?.let { it1 -> previousSideEffectsChipList.add(it1) }
                                        it.body()!!.data[i]._id?.let { it1 -> storePreviousEffectsId.add(it1) }
                                    }
                                }
                                Log.d("TAG", "storeIds: ${Gson().toJson(storePreviousEffectsId)}")

                                for (i in 0 until it.body()!!.data.size) {
                                    if (it.body()!!.data[i].status == "INACTIVE") {
                                        if (it.body()!!.data[i]._id == storePreviousEffectsId[chipId]) {
                                            sideEffectsDateDuration.add(
                                                SideEffectsHistoryDateDuration(
                                                    it.body()!!.data[i].created_at.toString(),
                                                    it.body()!!.data[i].updated_at.toString()
                                                )
                                            )
                                        }
                                    }
                                }
                                Log.d("TAG", "check: ${Gson().toJson(it.body()!!.data[chipId].created_at)}")
                                Log.d("TAG", "sideEffectsHistory: ${Gson().toJson(mAbc)}")
                                Log.d("TAG", "sideEffectsDateDuration: ${Gson().toJson(sideEffectsDateDuration)}")
                            }
                            // sideEffectsDateDuration.clear()
                            val sideEffectsHistoryAdapter = SideEffectsHistoryAdapter(sideEffectsDateDuration)
                            binding.rvPreviousSideEffectHistory.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.rvPreviousSideEffectHistory.itemAnimator = DefaultItemAnimator()
                            binding.rvPreviousSideEffectHistory.adapter = sideEffectsHistoryAdapter
                        }
                    }
                    Status.ERROR -> {

                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    private fun showErrorMsg(msg: String, view: View) {
        val snackbar = Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_LONG
        )

        val snack_root_view = snackbar.view
        snackbar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        val snack_text_view = snack_root_view
            .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }
}