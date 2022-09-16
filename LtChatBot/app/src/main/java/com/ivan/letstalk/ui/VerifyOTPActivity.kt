package com.ivan.letstalk.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ivan.letstalk.R
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityVerifyOtpactivityBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.login.RequestBodies
import com.ivan.letstalk.model.login.VerifyOTPBodies
import com.ivan.letstalk.viewModel.LoginViewModel

class VerifyOTPActivity : AppCompatActivity() {
    lateinit var binding: ActivityVerifyOtpactivityBinding
    private lateinit var viewModel: LoginViewModel
    private var otp: String = ""
    private var getOtp: String = ""
    private var crNo: String = ""
    private var phoneNumber: String = ""
    private var userId: String = ""
    private lateinit var sessionManager : SessionManager
    private var cTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otpactivity)
        setupViewModel()
        sessionManager = SessionManager(this)
        binding.tvEditMobile.paint?.isUnderlineText = true

        phoneNumber = intent.getStringExtra(LoginActivity.mobileNumber).toString()
        Log.d("phoneNumber",phoneNumber)
        // binding.tvMobile.text = "+91".plus(" ").plus(phoneNumber)
        if (intent != null && intent.hasExtra("isOthersPhone")) {
            val isOthersPhone = intent.extras!!.getBoolean("isOthersPhone")
            Log.d("isOthersPhone", isOthersPhone.toString())
            if (isOthersPhone) {
                binding.tvMobile.text = "+".plus(phoneNumber)
            } else {
                binding.tvMobile.text = "+91".plus("-").plus(phoneNumber)
                phoneNumber = "91".plus("-").plus(phoneNumber)
            }
        }
        // binding.tvMobile.text = "+".plus(phoneNumber)
        crNo = intent.getStringExtra(LoginActivity.crNo).toString()
        Log.d("crNo", crNo)

        otp = intent.getStringExtra(LoginActivity.otpValue).toString()
        Log.d("otp", otp)

        userId = intent.getStringExtra(LoginActivity.userID).toString()
        Log.d("userId",userId)


        binding.tvEditMobile.setOnClickListener {
            val intent = Intent(this, UpdatePhoneNumberActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }



        binding.otpBox.otpValue.observe(this) {
            it?.let {
                if (it.length == 6) {
                    getOtp = it
                    // Toast.makeText(applicationContext,it.length.toString(),Toast.LENGTH_SHORT).show()
                    // Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnVerifyOtp.setOnClickListener {
            if (binding.otpBox.otpValue.value?.isEmpty() == true || binding.otpBox.otpValue.value!!.length != 6) {
                showErrorMsg("Please enter OTP", binding.root)
            } else {
                setupVerifyOTPObservers()
            }
        }

        sessionManager.fetchAuthToken()?.let {
            Log.d("saveToken",it)
        }

        binding.tvResendOtp.setOnClickListener {
            setupResendOTPObservers()
            binding.tvResendOtp.visibility= View.GONE
            if (!binding.tvSecondsRemaining.isVisible) {
                binding.tvSecondsRemaining.visibility = View.VISIBLE
            }
            startOTPTimer()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
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
        //val snack_action_view = snack_root_view
        // .findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
//    snack_action_view.typeface = tf
//    snack_action_view.setTextColor(ContextCompat.getColor(this, R.color.Sunglow))
//    snackbar.setAction("Retry") {
//
//    }
        snackbar.show()
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

    private fun setupVerifyOTPObservers() {
        val body = VerifyOTPBodies.OTPLoginBody(
            user_id = userId,
            // otp = otp
            otp = getOtp
        )
        viewModel.validateOTP(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            binding.pLoader.visibility = View.GONE
                            binding.tvButton.visibility = View.VISIBLE
                            binding.btnVerifyOtp.isEnabled = true
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            if (it.body() != null) {
                                if (it.body()?.status.equals("success")) {
                                    // Toast.makeText(this, it.body()?.message.toString(), Toast.LENGTH_LONG).show()
                                    sessionManager.saveUserLoginStatus(true)
                                    showSuccessMsg(it.body()?.message.toString(),binding.root)
                                    navigateToDashboard()
                                } else if (it.body()?.status.equals("error")){
                                    // Toast.makeText(this, it.body()?.message.toString(), Toast.LENGTH_LONG).show()
                                    showSuccessMsg(it.body()?.message.toString(),binding.root)
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.pLoader.visibility = View.GONE
                        binding.tvButton.visibility = View.VISIBLE
                        binding.btnVerifyOtp.isEnabled = true
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        showErrorMsg(it.message.toString(),binding.root)
                    }
                    Status.LOADING -> {
                        binding.pLoader.visibility = View.VISIBLE
                        binding.tvButton.visibility = View.GONE
                        binding.btnVerifyOtp.isEnabled = false
                    }
                }

            }
        })
    }

    private fun setupResendOTPObservers() {
        val body = RequestBodies.LoginBody(
            email_id = "",
            cr_no = crNo,
            // phone_number = "91-".plus(phoneNumber)
            phone_number = phoneNumber
        )
        viewModel.getLogin(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            showSuccessMsg(it.body()?.message.toString(),binding.root)
                        }
                    }
                    Status.ERROR -> {
                        showErrorMsg(it.message.toString(),binding.root)
                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun startOTPTimer() {
        cTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvSecondsRemaining.text =
                    "Resend Otp in" + " " + millisUntilFinished / 1000 + "s"
            }
            override fun onFinish() {
                binding.tvResendOtp.visibility = View.VISIBLE
                binding.tvSecondsRemaining.visibility = View.GONE
            }
        }
        (cTimer as CountDownTimer).start()
    }

    private fun cancelOTPTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelOTPTimer()
    }
}