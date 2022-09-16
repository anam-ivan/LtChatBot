package com.ivan.letstalk.ui

import `in`.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.DummyAdapter
import com.ivan.letstalk.adapter.KnowYourSideEffectsAdapter
import com.ivan.letstalk.adapter.MedicalTermsGlossaryAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityGlossaryBinding
import com.ivan.letstalk.databinding.ActivityKnowYourSideEffectsBinding
import com.ivan.letstalk.helper.AlphabetItem
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.SideEffectsModel
import com.ivan.letstalk.model.faq.SortBy
import com.ivan.letstalk.model.glossary.Data
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.model.knowYourSideEffects.KnowYourSideEffectsPayload
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import java.util.*

open class KnowYourSideEffectsActivity : AppCompatActivity() {
    private var sideEffectsList: List<Data>? = null
    private lateinit var binding: ActivityKnowYourSideEffectsBinding
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var rvSideEffects: IndexFastScrollRecyclerView
    private var mDataArray: List<String>? = null
    private var mAlphabetItems: List<AlphabetItem>? = null

    var movieList: List<SideEffectsModel>? = null
    var dummyArray: ArrayList<String>? = null

    private val sideEffectsModel = ArrayList<SideEffectsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_know_your_side_effects)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_know_your_side_effects)
        rvSideEffects = findViewById(R.id.rv_side_effects)
        initialiseData()
        initialiseUI()

       binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        setupSideEffectsViewModel()
        knowYourSideEffectsList()

        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchKey: String = s.toString()
                if (s.length == 2) {
                    searchKnowYourSideEffectsList(searchKey)
                }
                if (s.isEmpty()) {
                    searchKnowYourSideEffectsList(searchKey)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        // knowYourSideEffectsAdapter = KnowYourSideEffectsAdapter(sideEffectsModel)
        /*rvSideEffects.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
        rvSideEffects.itemAnimator = DefaultItemAnimator()
        rvSideEffects.adapter = knowYourSideEffectsAdapter
        initSideEffects()*/
    }

    /*private fun initSideEffects() {
        var movie = SideEffectsModel("Nausea","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        sideEffectsModel.add(movie)
        movie = SideEffectsModel("Nasopharyngitis","A common viral infection of the nose and throat. In contrast to the flu, a common cold can be caused by many different types of viruses. The condition is generally harmless and symptoms usually resolve within two weeks.","Symptoms: Runny Nose, Sneezing & Congestion")
        sideEffectsModel.add(movie)
        knowYourSideEffectsAdapter.notifyDataSetChanged()
    }*/
    protected open fun initialiseData() {
        movieList = ArrayList()
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("A","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("B","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("C","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("D","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("E","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("F","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("G","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        (movieList as ArrayList<SideEffectsModel>).add(
            SideEffectsModel("H","Nausea is an uneasiness of the stomach that often comes before vomiting. Vomiting is the forcible voluntary or involuntary emptying (\"throwing up\") of stomach contents through the mouth.","Symptoms: Vomiting")
        )
        dummyArray = ArrayList<String>()
        for (i in (movieList as ArrayList<SideEffectsModel>).indices) {
            (movieList as ArrayList<SideEffectsModel>)[i].getAlkTitle()?.let { (dummyArray as ArrayList<String>).add(it) }
            // Toast.makeText(this, dummyArray.toString() + "", Toast.LENGTH_SHORT).show()
            Log.d("dummyArray", dummyArray.toString() + "")
        }
        //Recycler view full data
        //mDataArray = DataHelper.getAlphabetData();

        //Recycler view not full data
        // mDataArray = NotFullDataHelper.getAlphabetNotFullData();
        mDataArray = dummyArray


        // 123
        //Alphabet fast scroller data
        mAlphabetItems = ArrayList()
        val strAlphabets: MutableList<String> = ArrayList()
        for (i in (mDataArray as ArrayList<String>).indices) {
            val name = (mDataArray as ArrayList<String>)[i]
            if (name == null || name.trim { it <= ' ' }.isEmpty()) continue
            val word = name.substring(0, 1)
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word)
                (mAlphabetItems as ArrayList<AlphabetItem>).add(AlphabetItem(i, word, false))
            }
        }
    }

    private fun setupSideEffectsViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }
    private fun initialiseUI() {
        /*rvSideEffects.layoutManager = LinearLayoutManager(this)
        rvSideEffects.adapter = DummyAdapter(mDataArray)
        rvSideEffects.setIndexTextSize(12)
        rvSideEffects.setIndexBarColor("#33334c")
        rvSideEffects.setIndexBarCornerRadius(0)
        rvSideEffects.setIndexBarTransparentValue(0.4.toFloat())
        *//*rvSideEffects.setIndexbarTopMargin(60)
        rvSideEffects.setIndexbarBottomMargin(100)
        rvSideEffects.setIndexbarHorizontalMargin(20)*//*
        rvSideEffects.setPreviewPadding(0)
        rvSideEffects.setIndexBarTextColor("#FFFFFF")
        rvSideEffects.setPreviewTextSize(60)
        rvSideEffects.setPreviewColor("#33334c")
        rvSideEffects.setPreviewTextColor("#FFFFFF")
        rvSideEffects.setPreviewTransparentValue(0.6f)
        rvSideEffects.setIndexBarVisibility(true)
        rvSideEffects.setIndexBarStrokeVisibility(true)
        rvSideEffects.setIndexBarStrokeWidth(1)
        rvSideEffects.setIndexBarStrokeColor("#000000")
        rvSideEffects.setIndexbarHighLightTextColor("#33334c")
        rvSideEffects.setIndexBarHighLightTextVisibility(true)
        Objects.requireNonNull<RecyclerView.LayoutManager>(rvSideEffects.layoutManager)
            .scrollToPosition(0)*/
    }

    private fun knowYourSideEffectsList() {
        val body = KnowYourSideEffectsPayload.KnowYourSideEffects(
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
        patientProfileViewModel.knowYourSideEffects(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            if (it.body()?.data != null && it.body()!!.data.size > 0) {
                                binding.tvTotalResult.text = "Showing Results:".plus(" ")
                                    .plus(it.body()?.data!!.size.toString())
                                sideEffectsList = it.body()?.data
                                Log.d("TAG", "knowYourSideEffects: ${Gson().toJson(it.body())}")
                                binding.rvSideEffects.layoutManager = LinearLayoutManager(this)
                                binding.rvSideEffects.adapter = sideEffectsList?.let { it1 ->
                                    KnowYourSideEffectsAdapter(
                                        it1
                                    )
                                }
                                binding.rvSideEffects.setIndexTextSize(12)
                                binding.rvSideEffects.setIndexBarCornerRadius(12)
                                binding.rvSideEffects.setIndexBarTextColor("#898989")
                                binding.rvSideEffects.setIndexBarStrokeColor("#FFFFFF")
                                binding.rvSideEffects.setIndexBarColor("#E5E5E5")
                            }
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvSideEffects.setIndexBarTransparentValue(
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

    private fun searchKnowYourSideEffectsList(searchKey: String) {
        val body = KnowYourSideEffectsPayload.KnowYourSideEffects(
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
        patientProfileViewModel.knowYourSideEffects(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            if (it.body()?.data != null && it.body()!!.data.size > 0) {
                                binding.tvTotalResult.text = "Showing Results:".plus(" ")
                                    .plus(it.body()?.data!!.size.toString())
                                sideEffectsList = it.body()?.data
                                Log.d("TAG", "knowYourSideEffects: ${Gson().toJson(it.body())}")
                                binding.rvSideEffects.layoutManager = LinearLayoutManager(this)
                                binding.rvSideEffects.adapter = sideEffectsList?.let { it1 ->
                                    KnowYourSideEffectsAdapter(
                                        it1
                                    )
                                }
                                binding.rvSideEffects.setIndexTextSize(12)
                                binding.rvSideEffects.setIndexBarCornerRadius(12)
                                binding.rvSideEffects.setIndexBarTextColor("#898989")
                                binding.rvSideEffects.setIndexBarStrokeColor("#FFFFFF")
                                binding.rvSideEffects.setIndexBarColor("#E5E5E5")
                            }
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            /*mRecyclerView.setIndexbarHighLateTextColor("#FF4081");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);*/
                            binding.rvSideEffects.setIndexBarTransparentValue(
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
}