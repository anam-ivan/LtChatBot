package com.ivan.letstalk.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.ivan.letstalk.R
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityUpdatePhoneNumberBinding
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.phonenumberupdate.PhoneNumberChangeBodies
import com.ivan.letstalk.viewModel.LoginViewModel

class UpdatePhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePhoneNumberBinding
    private lateinit var viewModel: LoginViewModel
    private var isOthersPhoneNumber: Boolean = false
    private var isOthersConfirmPhoneNumber: Boolean = false
    private var isCountryCodeAdded: Boolean = false
    private var isConfirmMobileCountryCodeAdded: Boolean = false
    private var phoneNumber: String = ""
    private var confirmPhoneNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_phone_number)
        setupViewModel()
        binding.tvCancel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            startActivity(intent)
        }
        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateMobileConfirmationActivity::class.java)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            startActivity(intent)
        }

        binding.edtMobile.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.llPhone.setBackgroundResource(R.drawable.edit_text_border_focused)
            } else {
                binding.llPhone.setBackgroundResource(R.drawable.edit_text_border)
            }

        }

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
            binding.edtMobile.hint = "Ex.61-442087599036"
            binding.edtMobile.letterSpacing = 0.1F
            binding.rrPhone.setBackgroundResource((R.drawable.country_code_back))
            /*tvEnterYourEmail.visibility = View.VISIBLE
            etEmail.visibility = View.VISIBLE*/
            binding.ivDropdown.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.edtMobile.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
            isOthersPhoneNumber = true
            Log.d("isOthers",isOthersPhoneNumber.toString())
        }

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
                binding.ivDropdown.setColorFilter(ContextCompat.getColor(this, R.color.greyy), android.graphics.PorterDuff.Mode.SRC_IN)
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
            binding.edtMobile.hint = "0000000000"
            binding.edtMobile.letterSpacing = 0.4F
            binding.edtMobile.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
            isOthersPhoneNumber = false
            Log.d("isOthers",isOthersPhoneNumber.toString())
        }




        binding.ivSecondDropDown.setOnClickListener {
            if (binding.tvConfirmMobileCountryCode.text.toString() == "Others") {
                binding.ivSecondDropDown.rotation = 180f
                binding.tvConfirmMobileCountryCode.text = "+91"
                binding.tvConfirmMobileCountryCode.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.greyy
                    )
                )
                // rrConfirmPhone.setBackgroundResource((R.drawable.others_back))
                binding.rrConfirmPhone.setBackgroundResource((R.drawable.country_code_focused))
                binding.rrOthersTwo.visibility = View.VISIBLE
                binding.ivSecondDropDown.setColorFilter(ContextCompat.getColor(this, R.color.greyy), android.graphics.PorterDuff.Mode.SRC_IN)
            } else {
                binding.ivSecondDropDown.rotation = 180f
                // rrConfirmPhone.setBackgroundResource((R.drawable.others_back))
                binding.rrConfirmPhone.setBackgroundResource((R.drawable.country_code_focused))
                binding.tvConfirmMobileCountryCode.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.greyy
                    )
                )
                binding.ivSecondDropDown.setColorFilter(
                    ContextCompat.getColor(this, R.color.greyy),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.rrOthersTwo.visibility = View.VISIBLE
            }
        }



        binding.rrOthersTwo.setOnClickListener {
            binding.rrOthersTwo.visibility = View.GONE
            binding.tvConfirmMobileCountryCode.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
            binding.tvConfirmMobileCountryCode.text = "Others"
            binding.ivSecondDropDown.rotation = 360f
            binding.edtConfirmMobile.hint = "Ex.61-442087599036"
            binding.edtConfirmMobile.letterSpacing = 0.1F
            binding.rrConfirmPhone.setBackgroundResource((R.drawable.country_code_back))
            binding.ivSecondDropDown.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            isOthersConfirmPhoneNumber = true
            binding.edtConfirmMobile.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))
            Log.d("isOthersConfirm",isOthersConfirmPhoneNumber.toString())
        }

        binding.tvConfirmMobileCountryCode.setOnClickListener {
            binding.rrConfirmPhone.setBackgroundResource((R.drawable.country_code_back))
            binding.tvConfirmMobileCountryCode.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
            binding.ivSecondDropDown.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            binding.rrOthersTwo.visibility = View.GONE
            binding.ivSecondDropDown.rotation = 360f
            binding.edtConfirmMobile.hint = "0000000000"
            binding.edtConfirmMobile.letterSpacing = 0.4F
            isOthersConfirmPhoneNumber = false
            Log.d("isOthersConfirm",isOthersConfirmPhoneNumber.toString())
        }

        binding.otpBox.otpValue.observe(this) {
            it?.let {
                if (it.length == 6) {
                    binding.edtMobile.requestFocus()
                    viewModel.crNo.value = it
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            if (binding.otpBox.otpValue.value?.isEmpty() == true || binding.otpBox.otpValue.value!!.length != 6) {
                showErrorMsg("Please enter CR Number", binding.root)
            } else if (!isValidUserData()) {
                // showErrorMsg("Please enter Mobile Number", binding.root)
            } else {
                setupPhoneNumberUpdateObservers()
            }
        }

        binding.edtMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val strText: String = s.toString()
                if (strText.contains("-")) {
                    /*Toast.makeText(
                        this@UpdatePhoneNumberActivity,
                        "Its a toast!",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    isCountryCodeAdded = true
                } else {
                    isCountryCodeAdded = false
                }
            }
        })

        binding.edtConfirmMobile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val strText: String = s.toString()
                if (!strText.contains("-")) {
                    Toast.makeText(
                        this@UpdatePhoneNumberActivity,
                        "Its a toast!",
                        Toast.LENGTH_SHORT
                    ).show()
                    isConfirmMobileCountryCodeAdded = true
                } else {
                    isConfirmMobileCountryCodeAdded = false
                }
            }
        })
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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }

    private fun isValidUserData(): Boolean {
        if (TextUtils.isEmpty(binding.edtMobile.text.toString().trim())) {
            showErrorMsg("Please enter Mobile Number", binding.root)
            return false
        } /*else if (isOthersPhoneNumber) {
            if (binding.edtMobile.text.toString().trim().contains("-")){
                showErrorMsg("Please enter Country Code while entering Mobile Number", binding.root)
                return false
            }
        }*/
        /*else if (isOthersPhoneNumber) {
            if (!isCountryCodeAdded){
                showErrorMsg("Please enter Country Code while entering Mobile Number", binding.root)
                return false
            }
        }*/ else if (TextUtils.isEmpty(binding.edtConfirmMobile.text.toString().trim())) {
            showErrorMsg("Please enter Confirm Mobile Number", binding.root)
            return false
        } /*else if (isOthersConfirmPhoneNumber) {
            if (!isConfirmMobileCountryCodeAdded) {
                showErrorMsg("Please enter Country Code while entering Confirm Mobile Number", binding.root)
                return false
            }
        }*/ else if (binding.edtMobile.text.toString()
                .trim() != binding.edtConfirmMobile.text.toString().trim()
        ) {
            showErrorMsg("Mobile Number & Confirm Mobile Number does not matched", binding.root)
            return false
        }
        return true
    }

    private fun setupPhoneNumberUpdateObservers() {
        if (isOthersPhoneNumber) {
            phoneNumber = (binding.edtMobile.text.toString().trim())
        } else {
            phoneNumber = "91-".plus(binding.edtMobile.text.toString().trim())
        }
        if (isOthersConfirmPhoneNumber) {
            confirmPhoneNumber = (binding.edtConfirmMobile.text.toString().trim())
        } else {
            confirmPhoneNumber = "91-".plus(binding.edtConfirmMobile.text.toString().trim())
        }
        val body = PhoneNumberChangeBodies.PhoneNumberUpdateBody(
            cr_no = viewModel.crNo.value.toString(),
            // phone_no = binding.edtMobile.text.toString().trim(),
            phone_no = phoneNumber,
            // confirm_phone_no = binding.edtConfirmMobile.text.toString().trim()
            confirm_phone_no = confirmPhoneNumber
        )
        viewModel.patientPhoneNumberUpdate(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            binding.pLoader.visibility = View.GONE
                            binding.tvButton.visibility = View.VISIBLE
                            binding.btnUpdate.isEnabled = true
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            if (it.body()?.status.equals("success")) {
                                navigateToUpdateMobileConfirmationActivity()
                            } else if (it.body()?.status.equals("val_error")){
                                showErrorMsg(it.body()?.message.toString(), binding.root)
                            }
                            // navigateToUpdateMobileConfirmationActivity()
                        }
                    }
                    Status.ERROR -> {
                        binding.pLoader.visibility = View.GONE
                        binding.tvButton.visibility = View.VISIBLE
                        binding.btnUpdate.isEnabled = true
                        showErrorMsg(it.message.toString(), binding.root)
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                        binding.pLoader.visibility = View.VISIBLE
                        binding.tvButton.visibility = View.GONE
                        binding.btnUpdate.isEnabled = false
                    }
                }

            }
        })
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

    private fun navigateToUpdateMobileConfirmationActivity() {
        val intent = Intent(this, UpdateMobileConfirmationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}