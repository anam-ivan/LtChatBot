package com.ivan.letstalk.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ivan.letstalk.R
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityLoginBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.login.RequestBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var otp: String = ""
    private var phoneNumber: String = ""
    private var userId: String = ""
    private var isOthersPhoneNumber: Boolean = false
    private lateinit var sessionManager : SessionManager
    /*private lateinit var tvHelp: TextView
    private lateinit var tvTermsConditions: TextView
    private lateinit var tvUpdateNumber: TextView
    private lateinit var btnLogin: MaterialButton
    private lateinit var etPhone: AppCompatEditText
    private lateinit var llPhone: LinearLayoutCompat
    private lateinit var rrPhone: RelativeLayout
    private lateinit var ivLoginError: ImageView
    private lateinit var updateNumber: TextView
    private lateinit var rrErrorLayout: RelativeLayout
    private lateinit var ivDropdown: ImageView
    private lateinit var rrOthers: RelativeLayout
    private lateinit var tvCountryCode: TextView
    private lateinit var tvOthers: TextView
    private lateinit var tvEnterYourEmail: TextView
    private lateinit var etEmail: AppCompatEditText*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sessionManager = SessionManager(this)
        /*tvEnterYourEmail = findViewById(R.id.tv_enter_your_email)
        etEmail = findViewById(R.id.et_email)
        tvCountryCode = findViewById(R.id.tv_country_code)
        tvOthers = findViewById(R.id.tv_others)
        rrOthers = findViewById(R.id.rr_others)
        updateNumber = findViewById(R.id.tv_error_third_text)
        rrErrorLayout = findViewById(R.id.rr_error_layout)
        llPhone = findViewById(R.id.llPhone)
        etPhone = findViewById(R.id.et_phone)
        rrPhone = findViewById(R.id.rr_phone)
        tvHelp = findViewById(R.id.tv_help)
        ivLoginError = findViewById(R.id.iv_login_error)
        tvTermsConditions = findViewById(R.id.tv_terms_conditions)
        tvUpdateNumber = findViewById(R.id.tv_update_number)
        btnLogin = findViewById(R.id.btn_login)
        ivDropdown = findViewById(R.id.iv_dropdown)
        tvHelp.paint?.isUnderlineText = true
        tvTermsConditions.paint?.isUnderlineText = true
        tvUpdateNumber.paint?.isUnderlineText = true
        updateNumber.paint?.isUnderlineText = true*/

        setupViewModel()
        // setupLoginObservers()
        binding.tvHelp.paint?.isUnderlineText = true
        binding.tvTermsConditions.paint?.isUnderlineText = true
        binding.tvUpdateNumber.paint?.isUnderlineText = true
        binding.tvUpdateNumber.paint?.isUnderlineText = true
        binding.otpBox.requestFocus()
        /*binding.otpBox.otpValue.observe(this) {
            it?.let {
                if (it.isEmpty()) {
                    showErrorMsg("Please enter CR Number",binding.root)
                }
            }
        }*/

        binding.ivDropdown.setOnClickListener {
            if (binding.tvCountryCode.text.toString() == "Others") {
                binding.ivDropdown.rotation = 180f
                binding.tvCountryCode.text = "+91"
                binding.tvCountryCode.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.greyy
                    )
                )
                // rrPhone.setBackgroundResource((R.drawable.others_back))
                binding.rrPhone.setBackgroundResource((R.drawable.country_code_focused))
                binding.rrOthers.visibility = View.VISIBLE
                /*etPhone.hint = "Please enter mobile number along with country code"
                etPhone.letterSpacing = 0.2F*/
                binding.tvEnterYourEmail.visibility = View.GONE
                binding.etEmail.visibility = View.GONE
                // ivDropdown.isClickable = false
                binding.ivDropdown.setColorFilter(
                    ContextCompat.getColor(this, R.color.greyy),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.ivDropdown.rotation = 180f
                // rrPhone.setBackgroundResource((R.drawable.others_back))
                binding.rrPhone.setBackgroundResource((R.drawable.country_code_focused))
                binding.tvCountryCode.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.greyy
                    )
                )
                binding.ivDropdown.setColorFilter(
                    ContextCompat.getColor(this, R.color.greyy),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.rrOthers.visibility = View.VISIBLE
            }
        }

        binding.tvCountryCode.setOnClickListener {
            binding.rrPhone.setBackgroundResource((R.drawable.country_code_back))
            binding.tvCountryCode.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
            binding.ivDropdown.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.rrOthers.visibility = View.GONE
            binding.ivDropdown.rotation = 360f
            binding.etPhone.hint = "0000000000"
            binding.etPhone.letterSpacing = 0.3F
            binding.etPhone.filters = arrayOf<InputFilter>(LengthFilter(10))
            isOthersPhoneNumber = false
            Log.d("isOthers",isOthersPhoneNumber.toString())
            binding.etPhone.inputType = InputType.TYPE_CLASS_NUMBER
        }

        /*ivDropdown.setOnClickListener {
            rrOthers.visibility = View.GONE
            ivDropdown.rotation = 180f
        }*/

        binding.rrOthers.setOnClickListener {
            // ivDropdown.isClickable = true
            binding.rrOthers.visibility = View.GONE
            binding.tvCountryCode.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
            binding.tvCountryCode.text = "Others"
            binding.ivDropdown.rotation = 360f
            binding.etPhone.hint = "Ex.61-442087599036"
            binding.etPhone.letterSpacing = 0.1F
            binding.rrPhone.setBackgroundResource((R.drawable.country_code_back))
            binding.tvEnterYourEmail.visibility = View.VISIBLE
            binding.etEmail.visibility = View.VISIBLE
            binding.etPhone.filters = arrayOf<InputFilter>(LengthFilter(15))
            binding.ivDropdown.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            isOthersPhoneNumber = true
            Log.d("isOthers",isOthersPhoneNumber.toString())
            binding.etPhone.inputType = InputType.TYPE_CLASS_TEXT
        }

        binding.tvUpdateNumber.setOnClickListener {
            val intent = Intent(this, UpdatePhoneNumberActivity::class.java)
            startActivity(intent)
        }

        binding.tvTermsConditions.setOnClickListener {
            val intent = Intent(this, TermsConditionsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        /*binding.btnLogin.setOnClickListener {
            if (binding.etPhone.text.toString() == "8888888888") {
                binding.llPhone.setBackgroundResource(R.drawable.edit_text_error_border)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.rrPhone.setBackgroundResource(R.drawable.country_code_error_back)
                }
                binding.rrErrorLayout.visibility = View.VISIBLE
                binding.ivLoginError.visibility = View.VISIBLE
            } else {
                navigateToVerifyOTP()
            }
        }*/
        binding.otpBox.otpValue.observe(this) {
            it?.let {
                if (it.length == 6) {
                    binding.etPhone.requestFocus()
                    viewModel.crNo.value = it
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            if (binding.otpBox.otpValue.value?.isEmpty() == true || binding.otpBox.otpValue.value!!.length != 6) {
                showErrorMsg("Please enter CR Number", binding.root)
            } else if (!isValidUserData()) {
                showErrorMsg("Please enter Mobile Number", binding.root)
            } else if (binding.tvCountryCode.text.equals("Others")) {
                if (binding.etEmail.text?.isEmpty() == true) {
                    showErrorMsg("Please enter a Email Address", binding.root)
                } else {
                    if (!isValidEmailId(binding.etEmail.text.toString())) {
                        showErrorMsg("Please enter a Valid Email Address", binding.root)
                    } else {
                        if (!binding.checkbox.isChecked) {
                            showErrorMsg("Please accept Terms & Conditions", binding.root)
                        } else {
                            // navigateToVerifyOTP()
                            setupLoginObservers()
                        }
                    }
                }
            } else {
                if (!binding.checkbox.isChecked) {
                    showErrorMsg("Please accept Terms & Conditions", binding.root)
                } else {
                    // navigateToVerifyOTP()
                    setupLoginObservers()
                }
            }
        }

        /*etPhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                llPhone.setBackgroundResource(R.drawable.edit_text_border_focused)
            } else {
                llPhone.setBackgroundResource(R.drawable.edit_text_border)
            }
        }*/

        binding.etPhone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.llPhone.setBackgroundResource(R.drawable.edit_text_border_focused)
                binding.rrPhone.setBackgroundResource(R.drawable.country_code_blue_focused)
            } else {
                binding.llPhone.setBackgroundResource(R.drawable.edit_text_border)
                binding.rrPhone.setBackgroundResource(R.drawable.country_code_back)
            }
        }
        binding.tvUpdateNumber.setOnClickListener {
            val intent = Intent(this, UpdatePhoneNumberActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val strText: String = s.toString()
                if (strText.isEmpty()) {
                    binding.llPhone.setBackgroundResource(R.drawable.edit_text_border)
                    binding.rrPhone.setBackgroundResource(R.drawable.country_code_back)
                    if (binding.ivLoginError.visibility == View.VISIBLE) {
                        binding.ivLoginError.visibility = View.GONE
                    }
                    binding.rrErrorLayout.visibility = View.GONE
                } else {
                    binding.llPhone.setBackgroundResource(R.drawable.edit_text_border_focused)
                }
            }
        })

        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        /*toast("PackageName = " + info.packageName + "\nVersionCode = "
                + info.versionCode + "\nVersionName = "
                + info.versionName + "\nPermissions = " + info.permissions)*/
        binding.tvHelp.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
    }

    private fun navigateToVerifyOTP() {
        val intent = Intent(this, VerifyOTPActivity::class.java)
        intent.putExtra(mobileNumber, binding.etPhone.text.toString())
        intent.putExtra(crNo, viewModel.crNo.value.toString())
        intent.putExtra(otpValue, otp)
        intent.putExtra(userID, userId)
        intent.putExtra("isOthersPhone", isOthersPhoneNumber)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
        /*viewModel =
            ViewModelProvider(this,ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(LoginViewModel::class.java)*/
    }

    private fun setupLoginObservers() {
        if (isOthersPhoneNumber) {
            phoneNumber = (binding.etPhone.text.toString().trim())
        } else {
            phoneNumber = "91-".plus(binding.etPhone.text.toString().trim())
        }
        val body = RequestBodies.LoginBody(
            email_id = binding.etEmail.text.toString().trim(),
            // cr_no = binding.otpBox.otpValue.toString(),
            cr_no = viewModel.crNo.value.toString(),
            // phone_number = "91".plus(binding.etPhone.text.toString().trim())
            phone_number = phoneNumber
        )
        viewModel.getLogin(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            binding.pLoader.visibility = View.GONE
                            binding.tvButton.visibility = View.VISIBLE
                            binding.btnLogin.isEnabled = true
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            if (it.body() != null) {
                                if (it.body()?.status.equals("success")) {
                                    otp = it.body()!!.data?.otp.toString()
                                    userId = it.body()!!.data?.Id.toString()
                                    sessionManager.saveUserId(userId)
                                    it.body()!!.data!!.otp?.let { it1 -> Log.d("otp", it1) }
                                    // viewModel.otp.value = it.body()!!.data!!.otp
                                    // Toast.makeText(this, it.body()?.message.toString(), Toast.LENGTH_LONG).show()
                                    showSuccessMsg(it.body()?.message.toString(),binding.root)
                                    it.body()?.data!!.token?.let { it1 -> sessionManager.saveAuthToken(it1) }
                                    navigateToVerifyOTP()
                                } else if (it.body()?.status.equals("error")){
                                    Toast.makeText(this, it.body()?.message.toString(), Toast.LENGTH_LONG).show()
                                    binding.llPhone.setBackgroundResource(R.drawable.edit_text_error_border)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        binding.rrPhone.setBackgroundResource(R.drawable.country_code_error_back)
                                    }
                                    binding.rrErrorLayout.visibility = View.VISIBLE
                                    binding.ivLoginError.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.pLoader.visibility = View.GONE
                        binding.tvButton.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = true
                        showErrorMsg(it.message.toString(),binding.root)
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.pLoader.visibility = View.VISIBLE
                        binding.tvButton.visibility = View.GONE
                        binding.btnLogin.isEnabled = false
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
        //val snack_action_view = snack_root_view
        // .findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
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

    private fun isValidUserData(): Boolean {
        if (TextUtils.isEmpty(binding.etPhone.text.toString().trim())) {
            showErrorMsg("Please enter Mobile Number",binding.root)
            return false
        }
        return true
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    companion object {
        const val crNo = "crNo"
        const val mobileNumber = "mobileNumber"
        const val otpValue = "otp"
        const val userID = "user_id"
    }
}