package com.ivan.letstalk.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.CustomAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityLoginBinding
import com.ivan.letstalk.databinding.FragmentAddNewVitalBinding
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.phonenumberupdate.PhoneNumberChangeBodies
import com.ivan.letstalk.viewModel.LoginViewModel


class AddNewVitalFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: LoginViewModel
    lateinit var binding: FragmentAddNewVitalBinding
    private var fruits = arrayOf(
        "Blood Pressure (SYS)",
        "Blood Pressure (DIA)",
        "Blood Sugar (PP)",
        "Blood Sugar (Fasting)",
        "Oxygen Saturation (SpO2)"
    )
    private var images = intArrayOf(
        R.drawable.ic_blood_pressure,
        R.drawable.ic_documents,
        R.drawable.ic_blood_sugar,
        R.drawable.ic_blood_sugar,
        R.drawable.ic_oxygen
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_add_new_vital, container, false)
        val ivDropdown = view.findViewById<ImageView>(R.id.iv_dropdown)
        val spin = view.findViewById<Spinner>(R.id.spCategory)
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                /*Toast.makeText(
                    requireActivity(),
                    "You Select Position: " + position + " " + fruits[position],
                    Toast.LENGTH_SHORT
                ).show()*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val customAdapter =
            CustomAdapter(requireActivity(), images, fruits)
        spin.adapter = customAdapter

        ivDropdown.setOnClickListener {
            spin.performClick()
        }
        // setupViewModel()
        // setupVitalsMetaList()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupVitalsMetaList()
        Toast.makeText(requireActivity(), "Worked", Toast.LENGTH_LONG).show()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setupVitalsMetaList() {
        viewModel.vitalsMetaList().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            // Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            Log.d("TAG", "metaList: ${Gson().toJson(it.body().toString())}")
                            // Toast.makeText(requireActivity(), "Worked", Toast.LENGTH_LONG).show()
                        }
                    }
                    Status.ERROR -> {
                        showErrorMsg(it.message.toString(), binding.root)
                        Toast.makeText(requireActivity(), "Error Worked", Toast.LENGTH_LONG).show()
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.green))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(requireActivity(), R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(requireActivity(), R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }
}