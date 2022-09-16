package com.ivan.letstalk.helper

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.MutableLiveData
import com.ivan.letstalk.R
import com.ivan.letstalk.databinding.LayoutOtpBinding

class EditTextOtp @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener,
    TextWatcher, View.OnKeyListener {
    private lateinit var layoutOtpBinding: LayoutOtpBinding
    private lateinit var etCurrentFocus: AppCompatEditText
    val otpValue: MutableLiveData<String> = MutableLiveData()

    init {
        initialize()
        otpValue.value = ""
    }

    private fun initialize() {
//        val inflater = context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        inflater.inflate(R.layout.layout_otp, this)

        layoutOtpBinding = LayoutOtpBinding.inflate(LayoutInflater.from(context), this, true)
        initFocusListener()
        initTextChangeListener()
        initKeyListener()
        // layoutOtpBinding.etOtp1.requestFocus()
        // initFocusChangeListener()
    }

    private fun initFocusListener() {
        layoutOtpBinding.etOtp1.onFocusChangeListener = this
        layoutOtpBinding.etOtp2.onFocusChangeListener = this
        layoutOtpBinding.etOtp3.onFocusChangeListener = this
        layoutOtpBinding.etOtp4.onFocusChangeListener = this
        layoutOtpBinding.etOtp5.onFocusChangeListener = this
        layoutOtpBinding.etOtp6.onFocusChangeListener = this
    }

    private fun initTextChangeListener() {
        layoutOtpBinding.etOtp1.addTextChangedListener(this)
        layoutOtpBinding.etOtp2.addTextChangedListener(this)
        layoutOtpBinding.etOtp3.addTextChangedListener(this)
        layoutOtpBinding.etOtp4.addTextChangedListener(this)
        layoutOtpBinding.etOtp5.addTextChangedListener(this)
        layoutOtpBinding.etOtp6.addTextChangedListener(this)
    }

    private fun initFocusChangeListener() {
        layoutOtpBinding.etOtp1.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp1.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp1.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }

        layoutOtpBinding.etOtp2.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp2.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp2.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }

        layoutOtpBinding.etOtp3.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp3.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp3.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }

        layoutOtpBinding.etOtp4.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp4.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp4.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }

        layoutOtpBinding.etOtp5.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp5.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp5.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }

        layoutOtpBinding.etOtp6.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                layoutOtpBinding.etOtp6.setBackgroundResource(
                    R.drawable.otp_active_back
                )
            } else {
                layoutOtpBinding.etOtp6.setBackgroundResource(
                    R.drawable.otp_deactivate_back
                )
            }
        }
    }

    private fun initKeyListener() {
        layoutOtpBinding.etOtp1.setOnKeyListener(this)
        layoutOtpBinding.etOtp2.setOnKeyListener(this)
        layoutOtpBinding.etOtp3.setOnKeyListener(this)
        layoutOtpBinding.etOtp4.setOnKeyListener(this)
        layoutOtpBinding.etOtp5.setOnKeyListener(this)
        layoutOtpBinding.etOtp6.setOnKeyListener(this)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        etCurrentFocus = v as AppCompatEditText
        etCurrentFocus.text?.let { etCurrentFocus.setSelection(it.length) }
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp1.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp2.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp3.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp4.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp5.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        if (hasFocus)
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_active_back
            ) else
            layoutOtpBinding.etOtp6.setBackgroundResource(
                R.drawable.otp_deactivate_back
            )
        /*else if (etCurrentFocus.text!!.isNotEmpty())
            v.background = AppCompatResources.getDrawable(
                context,
                R.drawable.otp_deactivate_back
            ) else v.background = AppCompatResources.getDrawable(
            context,
            R.drawable.otp_active_back
        )*/
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        try {
            etCurrentFocus.text?.let {
                if (it.length >= 1 && etCurrentFocus !== layoutOtpBinding.etOtp6)
                    etCurrentFocus.focusSearch(FOCUS_RIGHT).requestFocus()
                else if (it.length >= 1 && etCurrentFocus == layoutOtpBinding.etOtp6) {
                    val imm = context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)
//                    layoutOtpBinding.etOtp4.background = AppCompatResources.getDrawable(
//                        context,
//                        R.drawable.otp_deactive_background
//                    )
                } else {
                    val currentValue: String = etCurrentFocus.text.toString()
                    if (currentValue.isEmpty() && etCurrentFocus.selectionStart <= 0) {
                        etCurrentFocus.focusSearch(FOCUS_LEFT).requestFocus()
                    } else return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun afterTextChanged(s: Editable?) {
        otpValue.value = makeOtp()
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (v.id) {
                R.id.et_otp1 -> return isKeyDel(layoutOtpBinding.etOtp1, keyCode)
                R.id.et_otp2 -> return isKeyDel(layoutOtpBinding.etOtp2, keyCode)
                R.id.et_otp3 -> return isKeyDel(layoutOtpBinding.etOtp3, keyCode)
                R.id.et_otp4 -> return isKeyDel(layoutOtpBinding.etOtp4, keyCode)
                R.id.et_otp5 -> return isKeyDel(layoutOtpBinding.etOtp5, keyCode)
                R.id.et_otp6 -> return isKeyDel(layoutOtpBinding.etOtp6, keyCode)
            }
        }
        return false
    }

    fun getOtp(): String {
        return makeOtp()
    }

    fun setOtp(otp: String) {
        if (otp.length != 6) return
        layoutOtpBinding.etOtp1.setText(otp[0].toString())
        layoutOtpBinding.etOtp2.setText(otp[1].toString())
        layoutOtpBinding.etOtp3.setText(otp[2].toString())
        layoutOtpBinding.etOtp4.setText(otp[3].toString())
        layoutOtpBinding.etOtp5.setText(otp[4].toString())
        layoutOtpBinding.etOtp6.setText(otp[5].toString())
    }

    private fun makeOtp(): String {
        val sb = StringBuilder()
        sb.append(layoutOtpBinding.etOtp1.text.toString())
        sb.append(layoutOtpBinding.etOtp2.text.toString())
        sb.append(layoutOtpBinding.etOtp3.text.toString())
        sb.append(layoutOtpBinding.etOtp4.text.toString())
        sb.append(layoutOtpBinding.etOtp5.text.toString())
        sb.append(layoutOtpBinding.etOtp6.text.toString())
        return sb.toString()
    }

    private fun isKeyDel(etDigit: EditText, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            etDigit.text = null
            return true
        }
        return false
    }
}