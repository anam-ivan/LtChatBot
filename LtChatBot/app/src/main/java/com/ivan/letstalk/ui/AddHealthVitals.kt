package com.ivan.letstalk.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.AddEditHealthVitalListAdapter
import com.ivan.letstalk.adapter.VitalsMetaListAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityAddHealthVitalsBinding
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.healthVitals.*
import com.ivan.letstalk.model.myHealthVitals.Value
import com.ivan.letstalk.model.myHealthVitals.Vital
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddHealthVitals : AppCompatActivity() {
    private var isSpinnerSelect: Boolean = false
    private var metaId = ""
    private var vitalTime = ""
    private var vitalDate = ""
    private var listPosition = 0
    private lateinit var viewModel: LoginViewModel
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private var healthVitalsElementsList = ArrayList<HealthVitalsElement>()
    private lateinit var addEditHealthVitalListAdapter: AddEditHealthVitalListAdapter
    private lateinit var binding: ActivityAddHealthVitalsBinding
    private lateinit var llOxygenSaturation: LinearLayoutCompat
    private lateinit var edtTitle: EditText
    private lateinit var edtValue: EditText
    private lateinit var edtUnit: EditText
    private lateinit var pLoader: ProgressBar
    private lateinit var tvButton: TextView
    private lateinit var btnAdd: RelativeLayout
    private lateinit var dialog: BottomSheetDialog
    private var isFromHealthVitalCard : Boolean = false
    var vitalsList = ArrayList<Vital>()
    var data = ArrayList<com.ivan.letstalk.model.myHealthVitals.Data>()
    // var dataValue = ArrayList<com.ivan.letstalk.model.myHealthVitals.Value>()
    var dataValue = Value()
    var metaList = arrayListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_health_vitals)
        val isFromEdit = intent.getBooleanExtra("isFromEdit", false)
        if (isFromEdit) {
            llOxygenSaturation.visibility = View.VISIBLE
        }
        if (intent.hasExtra("isFromHealthVitalCard")) {
            isFromHealthVitalCard = intent.extras!!.getBoolean("isFromHealthVitalCard")
        }
        if (intent != null && intent.hasExtra("data")){
            // data = intent.extras!!.get("data") as ArrayList<com.ivan.letstalk.model.myHealthVitals.Data>
            dataValue = intent.extras!!.get("data") as Value
            Log.d("TAG", "dataValue: ${Gson().toJson(dataValue)}")
            vitalsList = dataValue.vital
            Log.d("TAG", "vitalsList: ${Gson().toJson(vitalsList)}")
        }
        if (intent != null && intent.hasExtra("position")) {
            listPosition = intent.getIntExtra("position", 0)
            Log.d("listPosition",listPosition.toString())
            // vitalsList = data[listPosition].value[listPosition].vital
            // vitalsList = data[listPosition].value[listPosition].vital
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        binding.llAddNewVital.setOnClickListener {
            addNewVitalDialog()
        }
        setupViewModel()
        setupPatientProfileViewModel()
        setupVitalsMetaList()
        if (vitalsList != null) {
            for (i in vitalsList.indices)
                healthVitalsElementsList.add(
                    HealthVitalsElement(
                        vitalsList[i].Id!!,
                        vitalsList[i].title!!,
                        vitalsList[i].value!!,
                        vitalsList[i].unit!!,
                        vitalsList[i].icon!!,
                        vitalsList[i].metaId!!,
                        vitalsList[i].date!!,
                        vitalsList[i].time!!,
                    )
                )
            Log.d("TAG", "healthVitalsElementsList: ${Gson().toJson(healthVitalsElementsList)}")
            if (healthVitalsElementsList != null && healthVitalsElementsList.size == 0) {
                Log.d("No Vital", "No Vital")
            }
            addEditHealthVitalListAdapter = AddEditHealthVitalListAdapter(healthVitalsElementsList)
            binding.rvHealthVitals.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.rvHealthVitals.itemAnimator = DefaultItemAnimator()
            binding.rvHealthVitals.adapter = addEditHealthVitalListAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setupPatientProfileViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    private fun setupVitalsMetaList() {
        viewModel.vitalsMetaList().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "metaList: ${Gson().toJson(it.body())}")
                            metaList.add(Data("", "", "","Select a vital",""))
                            it.body()?.data?.let { it1 -> metaList.addAll(it1) }
                            // metaList = (it.body()?.data ?: "") as ArrayList<Data>
                            Log.d("TAG", "newMeta: ${Gson().toJson(metaList)}")
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
        })
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun addNewVitalDialog() {
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // dialog.behavior.maxHeight = 1000 // set max height when expanded in PIXEL
        dialog.behavior.peekHeight = 1100 // set default height when collapsed in PIXEL
        val view = layoutInflater.inflate(R.layout.add_new_vital_dialog, null)
        val metaSpinner = view.findViewById<Spinner>(R.id.spinner_meta)
        val ivDropdown = view.findViewById<ImageView>(R.id.iv_dropdown)
        val ivClose = view.findViewById<ImageView>(R.id.iv_close)
        val recordDate = view.findViewById<TextView>(R.id.record_date)
        val rrRecordDate = view.findViewById<RelativeLayout>(R.id.rr_record_date)
        val recordTime = view.findViewById<TextView>(R.id.record_time)
        val rrRecordTime = view.findViewById<RelativeLayout>(R.id.rr_record_time)
        btnAdd = view.findViewById(R.id.btn_add)
        edtTitle = view.findViewById(R.id.edt_title)
        edtValue = view.findViewById(R.id.edt_value)
        // edtUnit = view.findViewById(R.id.edt_unit)
        pLoader = view.findViewById(R.id.pLoader)
        tvButton = view.findViewById(R.id.tvButton)
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
        val adapter = VitalsMetaListAdapter(
            this,
            metaList
        )
        metaSpinner.adapter = adapter
        // metaSpinner.setOnItemSelectedListener(this)
        metaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                isSpinnerSelect = pos != 0
                // Toast.makeText(this@AddHealthVitals, "" + (parent?.getItemAtPosition(pos) as Data).name, Toast.LENGTH_SHORT).show()
                if (parent != null) {
                    (parent.getItemAtPosition(pos) as Data).name?.let { Log.d("clicked", it) }
                    // (parent.getItemAtPosition(pos) as Data).unit?.let { Log.d("check_unit", it) }
                    metaId = (parent.getItemAtPosition(pos) as Data).Id.toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        // adapter.notifyDataSetChanged()
        ivDropdown.setOnClickListener {
            metaSpinner.performClick()
        }
        /*val onOpenListener: SpinnerOpenCloseListener = object : SpinnerOpenCloseListener(metaSpinner) {
            override fun onOpen() {
                // Toast.makeText(this@AddHealthVitals, "Spinner was opened", Toast.LENGTH_LONG).show()
                ivDropdown.setBackgroundResource(R.drawable.ic_arrow_down)
                ivDropdown.rotation = 180f
            }

            override fun onClose() {
                // Toast.makeText(this@AddHealthVitals, "Spinner was closed", Toast.LENGTH_LONG).show()
                ivDropdown.setBackgroundResource(R.drawable.ic_arrow_down)
                ivDropdown.rotation = 0f
            }
        }*/
        if (isFromHealthVitalCard) {
            // val date = "15/02/2014"
            /*val date = data[0].date.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            try {
                val d = dateFormat.parse(date)
                if (d != null) {
                    recordDate.text = d.toString()
                }
                println("DATE$d")
                println("Formated" + dateFormat.format(d))
            } catch (e: Exception) {
                println("Excep$e")
            }*/
            // recordDate.text = data[0].date.toString()
            recordDate.text = vitalsList[0].date.toString()
            // Log.d("vitalDate", vitalsList[0].date.toString())
        }
        if (isFromHealthVitalCard) {
            // val vitalTime = data[0].value[0].time.toString()
            val vitalTime = vitalsList[0].time.toString()
            Log.d("vitalTime", vitalTime)
            if (vitalTime.isNotEmpty()) {
                val str = vitalTime.split(":")
                val hour: String = str[0]
                val minute: String = str[1]
                recordTime.text = "$hour:$minute"
            }
            // recordTime.text = data[0].value[0].time.toString()
        }
        rrRecordDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // val myFormat = "dd.MM.yyyy"
                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                recordDate.text = sdf.format(cal.time)
                vitalDate = sdf.format(cal.time).toString()
            }
            rrRecordDate.setOnClickListener {
                DatePickerDialog(
                    this,R.style.DialogTheme, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        rrRecordTime.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime[Calendar.HOUR_OF_DAY]
            val minute = currentTime[Calendar.MINUTE]
            // val sec = currentTime[Calendar.SECOND]
            val timePickerDialog: TimePickerDialog = TimePickerDialog(
                this,R.style.DialogTheme,
                { _, selectedHour, selectedMinute ->
                    // recordTime.text = "$selectedHour:$selectedMinute"
                    vitalTime = java.lang.String.format("%02d:%02d", selectedHour, selectedMinute).toString()
                    recordTime.text = java.lang.String.format("%02d:%02d", selectedHour, selectedMinute)
                },
                hour,
                minute,
                true
            )
            timePickerDialog.setOnShowListener {
                val positiveColor =
                    ContextCompat.getColor(view.context, R.color.hex_blue)
                val negativeColor =
                    ContextCompat.getColor(view.context, R.color.black)
                timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor)
                timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(negativeColor)
            }
            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }
        btnAdd.setOnClickListener {
            if (!isSpinnerSelect) {
                Toast.makeText(
                    this@AddHealthVitals,
                    "Please select Health Vital",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edtTitle.text.toString().trim().isEmpty()) {
                Toast.makeText(this@AddHealthVitals, "Please enter Title", Toast.LENGTH_SHORT).show()
            } else if (edtValue.text.toString().trim().isEmpty()) {
                Toast.makeText(this@AddHealthVitals, "Please enter Value", Toast.LENGTH_SHORT).show()
            } /*else if (edtUnit.text.toString().trim().isEmpty()) {
                Toast.makeText(this@AddHealthVitals, "Please enter Unit", Toast.LENGTH_SHORT).show()
            }*/ /*else if (!isFromHealthVitalCard) {
                if (recordDate.text.toString() == "dd mm yy") {
                    Toast.makeText(this@AddHealthVitals, "Please enter Date", Toast.LENGTH_SHORT)
                        .show()
                } else if (recordTime.text.toString() == "HH mm") {
                    Toast.makeText(this@AddHealthVitals, "Please enter Time", Toast.LENGTH_SHORT)
                        .show()
                }
                *//*if (recordTime.text.toString() == "HH mm") {
                    Toast.makeText(this@AddHealthVitals, "Please enter Time", Toast.LENGTH_LONG)
                        .show()
                }*//*
            }*/ else {
                addHealthVitalsObservers()
            }
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun testDialog() {
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.test_layout, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }


    /*fun setCustomAdapterSpinner() {
        val country_list = arrayListOf<Data>()
        country_list.add(CountryData("India", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("United States", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("Indonesia", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("France", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("China", R.drawable.ic_flag_black_24dp))

        val adapter = VitalsMetaListAdapter(
            this,
            country_list
        )

        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                Toast.makeText(this@AddHealthVitals, "" + (parent?.getItemAtPosition(pos) as Data).countryName, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        })

        // dynamically adding data after setting adapter to spinner

        country_list.add(CountryData("Japan", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("New Zealand", R.drawable.ic_flag_black_24dp))
        country_list.add(CountryData("Other", R.drawable.ic_flag_black_24dp))

        adapter.notifyDataSetChanged()
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
        snack_root_view.setBackgroundColor(ContextCompat.getColor(this, R.color.chat_answer_select))
        snack_text_view.setTextColor(Color.WHITE)
        snack_text_view.textSize = 12.2f
        val tf = ResourcesCompat.getFont(this, R.font.gilroy_medium)
        snack_text_view.typeface = tf
        snackbar.show()
    }

    private fun addHealthVitalsObservers() {
        val time : String = if (isFromHealthVitalCard){
            // data[0].value[0].time.toString()
            vitalsList[0].time.toString()
        } else {
            // data[0].value[0].time.toString().plus(":").plus("00")
            vitalTime.plus(":").plus("00")
        }
        val date: String = if (isFromHealthVitalCard) {
            // data[0].date.toString()
            vitalsList[0].date.toString()
        } else {
            vitalDate
        }
        val body = MetaAddBodies.MetaAddBody(
            title = edtTitle.text.toString().trim(),
            value = edtValue.text.toString().trim(),
            meta_id = metaId,
            date,
            // date = data[0].date.toString(),
            time
            // time = data[0].value[0].time.toString().plus(":").plus("00")
            // time = data[0].value[0].time.toString()
        )
        patientProfileViewModel.addHealthVitals(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            pLoader.visibility = View.GONE
                            tvButton.visibility = View.VISIBLE
                            btnAdd.isEnabled = true
                            Toast.makeText(this, it.body()?.message.toString(), Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                            navigateToMyHealthVitals()
                        }
                    }
                    Status.ERROR -> {
                        pLoader.visibility = View.GONE
                        tvButton.visibility = View.VISIBLE
                        btnAdd.isEnabled = true
                        showErrorMsg(it.message.toString(), binding.root)
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        // Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                        pLoader.visibility = View.VISIBLE
                        tvButton.visibility = View.GONE
                        btnAdd.isEnabled = false
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

    private fun navigateToMyHealthVitals() {
        val intent = Intent(this, MyHealthVitals::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    fun updateHealthVitalsObservers(body: MetaUpdateBodies.MetaUpdateBody) {
        patientProfileViewModel.updateHealthVitals(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            navigateToHealthVitals()
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

    fun deleteHealthVitalsObservers(body: MetaDeleteBodies.MetaDeleteBody) {
        patientProfileViewModel.deleteHealthVitals(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            navigateToHealthVitals()
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
    private fun navigateToHealthVitals() {
        val intent = Intent(this, MyHealthVitals::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        finish()
    }

}