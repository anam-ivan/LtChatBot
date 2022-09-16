package com.ivan.letstalk.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.HealthVitalsAdapter
import com.ivan.letstalk.adapter.MyHealthCardAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityLoginBinding
import com.ivan.letstalk.databinding.ActivityMyHealthVitalsBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.healthVitals.Data
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

class MyHealthVitals : AppCompatActivity() {
    private var userId = ""
    lateinit var binding: ActivityMyHealthVitalsBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private lateinit var healthVitalsAdapter: HealthVitalsAdapter
    private lateinit var sessionManager : SessionManager
    private var transparentDialogLoader = TransparentDialogLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_health_vitals)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        sessionManager.fetchAuthToken()?.let { Log.d("token", it) }
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        binding.addFab.setOnClickListener {
            navigateToAddHealthVitals()
        }
        /*binding.cvCalender.setOnClickListener{
            showDatePickerDialog()
        }*/
        setupViewModel()
        myHealthVitalsListing()
        setupPatientProfileViewModel()

        binding.btnInfo.setOnClickListener {
            SimpleTooltip.Builder(this)
                .anchorView(binding.btnInfo)
                .text("Text to do Tooltip")
                .gravity(Gravity.BOTTOM)
                .textColor(Color.WHITE)
                .backgroundColor(getColor(R.color.hex_blue))
                .arrowColor(getColor(R.color.hex_blue))
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show()
        }
    }
    private fun navigateToAddHealthVitals() {
        val intent = Intent(this, AddHealthVitals::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToEditHeathVitals() {
        val intent = Intent(this, AddHealthVitals::class.java)
        intent.putExtra("isFromEdit",true)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun showHealthVitalDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.my_health_vital_dialog)
        val btnCancel = dialog.findViewById(R.id.btnCancel) as TextView
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
        /*viewModel =
            ViewModelProvider(this,ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(LoginViewModel::class.java)*/
    }

    private fun setupPatientProfileViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    private fun myHealthVitalsListing() {
        viewModel.healthVitalsListing().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            transparentDialogLoader.dismiss()
                            // showSuccessMsg(it.body()?.message.toString(), binding.root)
                            Log.d("TAG", "healthVitals: ${Gson().toJson(it.body())}")
                            // vitalsDetails.sortByDescending{it.updateDate}
                            // healthVitalsAdapter = HealthVitalsAdapter(it.body()!!.data[0].value[0].vital)
                            // healthVitalsAdapter = HealthVitalsAdapter(it.body()!!.data[0].value)
                            healthVitalsAdapter = HealthVitalsAdapter(it.body()!!.data)
                            // Log.d("TAG", "metaList: ${Gson().toJson(it.body()?.data!![0].value[0].vital)}")
                            binding.rvHealthVitals.layoutManager = LinearLayoutManager(
                                this,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.rvHealthVitals.itemAnimator = DefaultItemAnimator()
                            binding.rvHealthVitals.adapter = healthVitalsAdapter

                        }
                    }
                    Status.ERROR -> {
                        // showErrorMsg(it.message.toString(), binding.root)
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        })
        /*viewModel.vitalsMetaList().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            // showSuccessMsg(it.body()?.message.toString(), binding.root)
                            Log.d("TAG", "metaList: ${Gson().toJson(it.body())}")
                        }
                    }
                    Status.ERROR -> {
                        // showErrorMsg(it.message.toString(), binding.root)
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })*/
    }

    private fun showSuccessMsg(msg: String, view: View) {
        val snackbar = Snackbar.make(
            view,
            msg,
            Snackbar.LENGTH_LONG
        )
        val snack_root_view = snackbar.view
        snackbar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        val snack_text_view = snack_root_view
            .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
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

    private fun showDatePickerDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.date_picker_dialog)
        /*val btnCancel = dialog.findViewById(R.id.btnCancel) as TextView
        btnCancel.setOnClickListener { dialog.dismiss() }*/
        dialog.show()
    }

    fun deleteHealthVitalsObservers(body: MetaDeleteBodies.MetaDeleteBody) {
        patientProfileViewModel.deleteHealthVitals(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            /*finish()
                            overridePendingTransition(0, 0)
                            startActivity(intent)
                            overridePendingTransition(0, 0)*/
                        }
                    }
                    Status.ERROR -> {
                        showErrorMsg(it.message.toString(), binding.root)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        myHealthVitalsListing()
    }
}