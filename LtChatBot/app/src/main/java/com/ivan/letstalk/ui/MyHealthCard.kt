package com.ivan.letstalk.ui
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.alespero.expandablecardview.ExpandableCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.*
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityMyHealthCardBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.CommonModel
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel


class MyHealthCard : AppCompatActivity() {
    private var userId = ""
    private lateinit var binding: ActivityMyHealthCardBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var myHealthCardAdapter: MyHealthCardAdapter
    // private lateinit var transactionalDataAdapter: TransactionalDataAdapter
    private lateinit var regularizeDataAdapter: RegularizeDataAdapter
    private lateinit var firstLineOfTreatmentAdapter: FirstLineOfTreatmentAdapter
    private lateinit var secondLineOfTreatmentAdapter: SecondLineOfTreatmentAdapter
    private lateinit var thirdLineOfTreatmentAdapter: ThirdLineOfTreatmentAdapter
    private var newList = ArrayList<CommonModel>()
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var sessionManager : SessionManager
    private val FADE_DURATION = 1500
    private val tabArray = arrayOf(
        "Basic Info",
        "Line of Treatment",
        "Progressive Data",
        "Doctors Assigned"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_health_card)
        setupViewModel()
        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        userId = sessionManager.fetchUserId().toString()
        Log.d("usedId", userId)
        // patientProfileObservers()
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

        /*binding.cvProgressive.setOnClickListener(View.OnClickListener {
            if (binding.llProgressiveDetails.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.cvProgressive,
                    AutoTransition()
                )
                binding.llProgressiveDetails.visibility = View.GONE
                binding.ivProgressiveArrowButton.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cvProgressive,
                    AutoTransition()
                )
                binding.llProgressiveDetails.visibility = View.VISIBLE
                binding.ivProgressiveArrowButton.setImageResource(android.R.drawable.arrow_up_float)
                // binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                *//*binding.scrollView.post(Runnable {
                    binding.scrollView.scrollTo(
                        0,
                        binding.scrollView.bottom
                    )
                    setFadeAnimation(binding.scrollView)
                })*//*
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(
                        0,
                        binding.rvRegularize.bottom
                    )
                    // setFadeAnimation(binding.llProgressiveDetails)
                    viewExpand(binding.llProgressiveDetails)
                }
            }
        })*/

        /*binding.cvTransactional.setOnClickListener(View.OnClickListener {
            if (binding.llTransactionalDetails.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.cvTransactional,
                    AutoTransition()
                )
                binding.llTransactionalDetails.visibility = View.GONE
                binding.ivTransactionalArrowButton.setImageResource(android.R.drawable.arrow_down_float)

            } else {
                TransitionManager.beginDelayedTransition(
                    binding.cvTransactional,
                    AutoTransition()
                )
                binding.llTransactionalDetails.visibility = View.VISIBLE
                binding.ivTransactionalArrowButton.setImageResource(android.R.drawable.arrow_up_float)
                binding.scrollView.post(Runnable {
                    binding.scrollView.scrollTo(
                        0,
                        binding.scrollView.bottom
                    )
                    // setFadeAnimation(binding.scrollView)
                })
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(
                        0,
                        binding.rvTransactionalDetails.bottom
                    )
                    setFadeAnimation(binding.llTransactionalDetails)
                }
            }
        })*/
    }
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    /*private fun setupPatientProfileObservers() {
        viewModel.patientProfileDetails().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            *//*binding.tvPatientName.text = it.body()?.data!![0].basicDetails!!.name
                            binding.tvDob.text = it.body()?.data!![0].basicDetails!!.dob
                            binding.tvGender.text = it.body()?.data!![0].healthCard[4].valueDetails*//*
                            // var newList = ArrayList<CommonModel>()
                            newList = ArrayList<CommonModel>()
                            if (it.body()?.data!![0].basicDetails != null) {
                                val basic = it.body()?.data!![0].basicDetails
                                newList.add(CommonModel("Name", basic?.name?: "", ""))
                                newList.add(CommonModel("Date of Birth", basic?.dob?: "", ""))
                            }
                            for (i in it.body()?.data!![0]?.healthCard!!.indices)
                                newList.add(
                                    CommonModel(
                                        it.body()?.data!![0]?.healthCard!![i].name!!,
                                        it.body()?.data!![0]?.healthCard!![i].value!!,
                                        it.body()?.data!![0]?.healthCard!![i].valueDetails!!)
                                )
                            Log.e("TAG", "setupPatientProfileObservers: ${Gson().toJson(newList)}")
                        }
                        myHealthCardAdapter = MyHealthCardAdapter(newList)
                        binding.rvHealthCardDetails.layoutManager = LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.rvHealthCardDetails.itemAnimator = DefaultItemAnimator()
                        binding.rvHealthCardDetails.adapter = myHealthCardAdapter
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }*/

    /*private fun patientProfileObservers() {
        val body = UserDetailsBodies.UserDetails(
            user_id = userId
        )
        viewModel.mPatientProfileDetails(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            transparentDialogLoader.dismiss()
                            Log.d("TAG", "setupPatientProfileObservers: ${Gson().toJson(it.body())}")
                            if (it.body()?.data!= null) {
                                if (it.body()?.data!![0].basicDetails != null) {
                                    newList = ArrayList<CommonModel>()
                                    if (it.body()?.data!![0].basicDetails != null) {
                                        val basic = it.body()?.data!![0].basicDetails
                                        newList.add(CommonModel("Name",  "", basic?.name?: ""))
                                        newList.add(CommonModel("Cr No.", "", basic?.crNo?: ""))
                                        newList.add(CommonModel("Mobile", "", basic?.phoneNumber?: ""))
                                        newList.add(CommonModel("Gender", "", basic?.sex?: ""))
                                        newList.add(CommonModel("Age", "", basic?.dob?: ""))
                                        newList.add(CommonModel("Height", "", basic?.height?: ""))
                                        newList.add(CommonModel("Weight", "", basic?.weight?: ""))
                                        newList.add(CommonModel("Email", "", basic?.emailId?: ""))
                                        newList.add(CommonModel("Address", "", basic?.address?: ""))
                                        newList.add(CommonModel("Next Of Kin", "", basic?.next_of_kin_name?: ""))
                                        newList.add(CommonModel("Relation", "", basic?.next_of_kin_relation?: ""))
                                        newList.add(CommonModel("Next Of Kin Mobile", "", basic?.next_of_kin_mobile?: ""))
                                        newList.add(CommonModel("Registration No", "", basic?.registrationNo?: ""))
                                        newList.add(CommonModel("Date of Diagnosis", "", basic?.dateOfDiagnosis?: ""))
                                        newList.add(CommonModel("Next Follow up Date", "", basic?.next_followup_date?: ""))
                                    }
                                    myHealthCardAdapter = MyHealthCardAdapter(newList)
                                    binding.rvHealthCardDetails.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvHealthCardDetails.itemAnimator = DefaultItemAnimator()
                                    binding.rvHealthCardDetails.adapter = myHealthCardAdapter
                                    binding.cvProgressive.visibility = View.VISIBLE
                                    // binding.cvTransactional.visibility = View.VISIBLE
                                    *//*binding.tvName.text = it.body()?.data?.get(0)?.basicDetails?.name ?: ""
                                    binding.tvCrNo.text = it.body()?.data?.get(0)?.basicDetails?.crNo ?: ""
                                    binding.tvMobile.text = it.body()?.data?.get(0)?.basicDetails?.phoneNumber ?: ""
                                    if (it.body()?.data?.get(0)?.basicDetails?.sex.toString() == "1") {
                                        binding.tvGender.text = resources.getString(R.string.male)
                                    } else if (it.body()?.data?.get(0)?.basicDetails?.sex.toString() == "2") {
                                        binding.tvGender.text = resources.getString(R.string.female)
                                    }*//*
                                    // Transactional Data
                                    *//*transactionalDataAdapter = TransactionalDataAdapter(it.body()!!.data[0].healthCard[0].Transactional)
                                    binding.rvTransactionalDetails.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvTransactionalDetails.itemAnimator = DefaultItemAnimator()
                                    binding.rvTransactionalDetails.adapter = transactionalDataAdapter*//*

                                    *//*val card : ExpandableCardView = findViewById(R.id.profile)
                                    card.setOnExpandedListener { _, isExpanded ->
                                        Toast.makeText(applicationContext, if(isExpanded) "Expanded!" else "Collapsed!", Toast.LENGTH_SHORT).show()
                                    }
                                    val view = findViewById<RecyclerView>(R.id.rv_transactional_details)
                                    transactionalDataAdapter = TransactionalDataAdapter(it.body()!!.data[0].healthCard[0].Transactional)
                                    view.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    view.itemAnimator = DefaultItemAnimator()
                                    view.adapter = transactionalDataAdapter*//*

                                    // Regularize Data
                                    regularizeDataAdapter = RegularizeDataAdapter(it.body()!!.data[0].healthCard[0].Progressive[0].withoutGen)
                                    binding.rvRegularize.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvRegularize.itemAnimator = DefaultItemAnimator()
                                    binding.rvRegularize.adapter = regularizeDataAdapter

                                    // First Line Of Treatment
                                    firstLineOfTreatmentAdapter = FirstLineOfTreatmentAdapter(it.body()!!.data[0].healthCard[0].Progressive[0].gen[0].FirstLineOfTreatment)
                                    binding.rvFirstLineTreatment.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvFirstLineTreatment.itemAnimator = DefaultItemAnimator()
                                    binding.rvFirstLineTreatment.adapter = firstLineOfTreatmentAdapter

                                    // Second Line Of Treatment
                                    secondLineOfTreatmentAdapter = SecondLineOfTreatmentAdapter(it.body()!!.data[0].healthCard[0].Progressive[0].gen[1].SecondLineOfTreatment)
                                    binding.rvSecondLineTreatment.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvSecondLineTreatment.itemAnimator = DefaultItemAnimator()
                                    binding.rvSecondLineTreatment.adapter = secondLineOfTreatmentAdapter

                                    // Third Line Of Treatment
                                    thirdLineOfTreatmentAdapter = ThirdLineOfTreatmentAdapter(it.body()!!.data[0].healthCard[0].Progressive[0].gen[2].ThirdLineOfTreatment)
                                    binding.rvThirdLineTreatment.layoutManager = LinearLayoutManager(
                                        this,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                    binding.rvThirdLineTreatment.itemAnimator = DefaultItemAnimator()
                                    binding.rvThirdLineTreatment.adapter = thirdLineOfTreatmentAdapter
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        // showErrorMsg(it.message.toString(),binding.root)
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        })
    }*/

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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, com.ivan.letstalk.R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, com.ivan.letstalk.R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }

    fun expand(v: View) {
        if (v.visibility == View.VISIBLE) return
        val durations: Long
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (v.parent as View).width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        durations = ((targetHeight / v.context.resources
            .displayMetrics.density)).toLong()

        v.alpha = 0.0F
        v.visibility = View.VISIBLE
        v.animate().alpha(1.0F).setDuration(durations).setListener(null)

        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration = durations
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        if (v.visibility == View.GONE) return
        val durations: Long
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation
            ) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        durations = (initialHeight / v.context.resources
            .displayMetrics.density).toLong()

        v.alpha = 1.0F
        v.animate().alpha(0.0F).setDuration(durations)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    v.alpha = 1.0F
                }
            })

        // Collapse speed of 1dp/ms
        a.duration = durations
        v.startAnimation(a)
    }

    fun viewExpand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val va = ValueAnimator.ofInt(1, targetHeight)
        va.addUpdateListener { animation ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        va.duration = 300
        va.interpolator = OvershootInterpolator()
        va.start()
    }

    fun viewCollapse(v: View) {
        val initialHeight = v.measuredHeight
        val va = ValueAnimator.ofInt(initialHeight, 0)
        va.addUpdateListener { animation ->
            v.layoutParams.height = (animation.animatedValue as Int)
            v.requestLayout()
        }
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        va.duration = 300
        va.interpolator = DecelerateInterpolator()
        va.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}