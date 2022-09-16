package com.ivan.letstalk.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.AlkSideEffectsAdapter
import com.ivan.letstalk.adapter.WhatDoctorSayAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.helper.*
import com.ivan.letstalk.model.AlkSideEffectsModel
import com.ivan.letstalk.model.faq.SortBy
import com.ivan.letstalk.model.knowYourSideEffects.KnowYourSideEffectsPayload
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DashboardActivity : AppCompatActivity() {
    private lateinit var docViewPager: ViewPager2
    private var userId = ""
    private lateinit var sessionManager : SessionManager
    private lateinit var viewModel: LoginViewModel
    private lateinit var mViewModel: PatientProfileViewModel
    private lateinit var tvSeeAll: TextView
    private val alkSideEffectsModel = ArrayList<AlkSideEffectsModel>()
    private lateinit var alkSideEffectsAdapter: AlkSideEffectsAdapter
    private lateinit var  llSideEffects : LinearLayoutCompat
    private lateinit var  llGlossary : LinearLayoutCompat
    private lateinit var  rrDoctorSay : RelativeLayout

    private lateinit var ivHome: AppCompatImageView
    private lateinit var ivNotifications: AppCompatImageView
    private lateinit var ivChat: AppCompatImageView

    private lateinit var  bottomNavHome : LinearLayoutCompat
    private lateinit var  bottomNavChat : LinearLayoutCompat
    private lateinit var  bottomNavPerson : LinearLayoutCompat
    private lateinit var  bottomNavHamburger : LinearLayoutCompat

    private lateinit var  bottomIvHome : AppCompatImageView
    private lateinit var  bottomIvChat : AppCompatImageView
    private lateinit var  bottomIvPerson : AppCompatImageView
    private lateinit var  bottomIvHamburger : AppCompatImageView

    private lateinit var  btnStartChat : MaterialButton
    private lateinit var  tvUserName : MaterialTextView
    private lateinit var  tvLastVisitDate : MaterialTextView

    /*var view1: View? = null
    var view2: View? = null
    var view3: View? = null*/
    private lateinit var mGuideView: GuideView
    private var builder: GuideView.Builder? = null
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        viewPager = findViewById(R.id.view_pager)
        docViewPager = findViewById(R.id.doc_viewpager)
        /*view1 = findViewById(R.id.tv_username)
        view2 = findViewById(R.id.rr_doctor_say)
        view3 = findViewById(R.id.ll_side_effects)*/
        tvUserName = findViewById(R.id.tv_username)
        tvLastVisitDate = findViewById(R.id.tv_last_visit_date)

        // rrDoctorSay = findViewById(R.id.rr_doctor_say)
        ivChat = findViewById(R.id.iv_chat)

        btnStartChat = findViewById(R.id.btn_start_chat)
        ivNotifications = findViewById(R.id.iv_notifications)

        bottomNavHome =  findViewById(R.id.bottom_nav_home)
        bottomNavChat =  findViewById(R.id.bottom_nav_chat)
        bottomNavPerson =  findViewById(R.id.bottom_nav_person)
        bottomNavHamburger =  findViewById(R.id.bottom_nav_hamburger)

        bottomIvHome = findViewById(R.id.bottom_dot_home)
        bottomIvChat = findViewById(R.id.bottom_dot_chat)
        bottomIvPerson = findViewById(R.id.bottom_dot_person)
        bottomIvHamburger = findViewById(R.id.bottom_dot_hamburger)

        ivHome = findViewById(R.id.iv_home)

        bottomIvHome.visibility = View.VISIBLE
        ivHome.setBackgroundResource(R.drawable.ic_home_fill)

        llSideEffects = findViewById(R.id.ll_side_effects)
        llGlossary = findViewById(R.id.ll_glossary)
        tvSeeAll = findViewById(R.id.tv_see_all)
        tvSeeAll.paint?.isUnderlineText = true
        tvSeeAll.setOnClickListener {
            val intent = Intent(this, TopALKSideEffectsActivity::class.java)
            startActivity(intent)
        }
        /*val recyclerView: RecyclerView = findViewById(R.id.rv_alk)
        alkSideEffectsAdapter = AlkSideEffectsAdapter(alkSideEffectsModel)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = alkSideEffectsAdapter
        prepareMovieData()*/

        llSideEffects.setOnClickListener {
            val intent = Intent(this, KnowYourSideEffectsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        llGlossary.setOnClickListener {
            val intent = Intent(this, GlossaryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        /*bottomNavHome.setOnClickListener{
            bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvHome.visibility =  View.VISIBLE
            navigateToDashboard()
        }*/

        bottomNavChat.setOnClickListener{
            bottomIvHome.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            // bottomIvChat.visibility =  View.VISIBLE
            navigateToChat()
        }
        bottomNavPerson.setOnClickListener{
            /*bottomIvHome.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.VISIBLE*/
            navigateToProfile()
        }

        bottomNavHamburger.setOnClickListener{
            /*bottomIvHome.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.VISIBLE*/
            navigateToMenu()
        }

        ivNotifications.setOnClickListener {
            navigateToNotifications()
        }

        btnStartChat.setOnClickListener {
            navigateToChat()
        }

        /*docViewPager.apply {
            adapter = WhatDoctorSayAdapter("")
            offscreenPageLimit = 4
            // setPageTransformer(SliderTransformer(4))
            setPageTransformer(VerticalStackTransformer(3))
        }*/

        var size = addAnimals().size / 3
        if ((addAnimals().size % 3) > 0)
            size += 1

        pagerAdapter = PageAdapter(supportFragmentManager, size, addAnimals())
        viewPager.adapter = pagerAdapter
        setupViewModel()
        patientProfileObservers()
        setupMViewModel()
        whatDoctorSayObservers()
    }

    private fun prepareMovieData() {
        var movie = AlkSideEffectsModel("Manage Weight Loss")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        alkSideEffectsAdapter.notifyDataSetChanged()
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToChat() {
        val intent = Intent(this, ALKChatActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToNotifications() {
        val intent = Intent(this, NotificationsActivity::class.java)
        startActivity(intent)

    }

    override fun onRestart() {
        super.onRestart()
        bottomIvHome.visibility = View.VISIBLE
        bottomIvPerson.visibility =  View.INVISIBLE
        bottomIvHamburger.visibility =  View.INVISIBLE
        bottomIvChat.visibility =  View.INVISIBLE
    }

    private fun addAnimals(): ArrayList<String> {
        val animals = arrayListOf<String>()
        animals.add("Manage Weight Loss")
        animals.add("Having Persistent Cough ?")
        animals.add("Feeling Weak & Tired ?")
        animals.add("Having Persistent Cough ?")
        animals.add("Feeling Weak & Tired ?")
        animals.add("Having Persistent Cough ?")
        animals.add("Feeling Weak & Tired ?")
        animals.add("Having Persistent Cough ?")
        animals.add("Feeling Weak & Tired ?")
        return animals
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setupMViewModel() {
        mViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }


    private fun patientProfileObservers() {
        val body = UserDetailsBodies.UserDetails(
            user_id = userId
        )
        viewModel.mPatientProfileDetails(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "setupPatientProfileObservers: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                if (it.body()?.data!![0].basicDetails != null) {
                                    if (it.body()?.data!![0].basicDetails != null) {
                                        tvUserName.text =
                                            "Greetings,".plus(" ").plus(it.body()?.data?.get(0)?.basicDetails?.name ?: "")
                                        tvLastVisitDate.text = "Last Visit Date".plus(" ").plus(
                                            it.body()?.data?.get(0)?.basicDetails?.lastFuDate
                                                ?: ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        /*showErrorMsg(it.message.toString(),binding.root)
                        transparentDialogLoader.dismiss()*/
                    }
                    Status.LOADING -> {
                        // transparentDialogLoader.show(this)
                    }
                }

            }
        })
    }

    private fun whatDoctorSayObservers() {
        val body = KnowYourSideEffectsPayload.KnowYourSideEffects(
            draw = 1,
            limit = 50,
            page_no = 1,
            perpage = 1,
            search_key = "",
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        mViewModel.whatDoctorSay(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "whatDoctorSayObservers: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null && it.body()!!.data.size > 0) {
                                docViewPager.apply {
                                    adapter =
                                        WhatDoctorSayAdapter(it.body()?.data!!, applicationContext)
                                    offscreenPageLimit = 4
                                    setPageTransformer(VerticalStackTransformer(3))
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        /*showErrorMsg(it.message.toString(),binding.root)
                        transparentDialogLoader.dismiss()*/
                    }
                    Status.LOADING -> {
                        // transparentDialogLoader.show(this)
                    }
                }

            }
        })
    }

    /*private fun addData(): ArrayList<AlkSideEffectsModel> {
        var movie = AlkSideEffectsModel("Manage Weight Loss")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Having Persistent Cough ?")
        alkSideEffectsModel.add(movie)
        movie = AlkSideEffectsModel("Feeling Weak & Tired ?")
        alkSideEffectsModel.add(movie)
        return ArrayList<AlkSideEffectsModel>()
    }*/
}