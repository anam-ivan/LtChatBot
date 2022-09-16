package com.ivan.letstalk.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityProfileBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class ProfileActivity : AppCompatActivity() {
    private var userId = ""
    lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var profileViewModel: PatientProfileViewModel
    private lateinit var sessionManager : SessionManager
    private var transparentDialogLoader = TransparentDialogLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        setupViewModel()
        setupProfileViewModel()
        patientProfileObservers()
        // setupPatientProfileObservers()

        binding.llHealthVitals.setOnClickListener {
            navigateToMyHealthVitals()
        }

        binding.llSideEffects.setOnClickListener {
            navigateToSideEffects()
        }

        binding.llMedicine.setOnClickListener {
            navigateToMedicineReminder()
        }

        binding.llDocument.setOnClickListener {
            navigateToMyDocuments()
        }

        binding.rlHealthCard.setOnClickListener {
            navigateToMyHealthCard()
        }

        binding.ivSettings.setOnClickListener {
            navigateToEditProfile()
        }

        /*binding.ivProfile.setOnClickListener{
                // check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    // permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    // permission already granted
                    pickImageFromGallery()
                }
            } else {
                // system OS is < Marshmallow
                pickImageFromGallery()
            }
        }*/

        binding.bottomDotPerson.visibility = View.VISIBLE
        binding.ivHome.setBackgroundResource(R.drawable.ic_home)
        binding.ivPerson.visibility = View.GONE
        binding.ivPersonFill.visibility = View.VISIBLE
        binding.ivPerson.setBackgroundResource(R.drawable.ic_person_fill)

        binding.bottomNavHome.setOnClickListener{
            /*bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvHome.visibility =  View.VISIBLE*/
            navigateToDashboard()
        }

        binding.bottomNavChat.setOnClickListener{
            binding.bottomDotHome.visibility =  View.INVISIBLE
            binding.bottomDotPerson.visibility =  View.INVISIBLE
            binding.bottomDotHamburger.visibility =  View.INVISIBLE
            // bottomIvChat.visibility =  View.VISIBLE
            navigateToChat()
        }

        binding.bottomNavHamburger.setOnClickListener{
            /*bottomIvHome.visibility =  View.INVISIBLE
            bottomIvChat.visibility =  View.INVISIBLE
            bottomIvPerson.visibility =  View.INVISIBLE
            bottomIvHamburger.visibility =  View.VISIBLE*/
            navigateToMenu()
        }
    }

    private fun navigateToMyHealthVitals() {
        val intent = Intent(this, MyHealthVitals::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToSideEffects() {
        val intent = Intent(this, MySideEffectHistory::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMedicineReminder() {
        val intent = Intent(this, MedicineReminderActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMyDocuments() {
        val intent = Intent(this, MyDocumentsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToMyHealthCard() {
        val intent = Intent(this, MyHealthCard::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun navigateToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
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

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    override fun onRestart() {
        super.onRestart()
        binding.bottomDotPerson.visibility = View.VISIBLE
        patientProfileObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setupProfileViewModel() {
        profileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    /*private fun setupPatientProfileObservers() {
        viewModel.patientProfileDetails().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                            binding.tvPatientName.text = it.body()?.data!![0].basicDetails!!.name
                            binding.tvPhone.text = it.body()?.data!![0].basicDetails!!.phoneNumber
                            binding.tvPhone.text = it.body()?.data!![0].basicDetails!!.phoneNumber
                            binding.tvDiagnosis.text = it.body()?.data!![0].basicDetails!!.dateOfDiagnosis
                            Glide.with(this)
                                .load("http://letstalk.dev13.ivantechnology.in/".plus(it.body()?.data!![0].basicDetails!!.image))
                                .into(binding.ivProfile)
                        }
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


    private fun patientProfileObservers() {
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
                            if (it.body()?.data != null) {
                                if (it.body()?.data!![0].basicDetails != null) {
                                    if (it.body()?.data!![0].basicDetails != null) {
                                        binding.tvPatientName.text =
                                            it.body()?.data?.get(0)?.basicDetails?.name ?: ""
                                        binding.tvCrNo.text = "CR No:".plus(" ")
                                            .plus(it.body()?.data?.get(0)?.basicDetails?.crNo ?: "")
                                        val time = it.body()?.data?.get(0)?.basicDetails?.createdAt ?: ""
                                        var str1 = ""
                                        if (time.isNotEmpty()) {
                                            str1 = time
                                        }
                                        var splitTime = ""
                                        if (str1.isNotEmpty()) {
                                            splitTime = str1.split("\\s".toRegex())[0]
                                            Log.d("splitTime", splitTime)
                                        }
                                        /*binding.tvRegistrationDate.text = "Registration Date:".plus(" ")
                                            .plus(splitTime)*/

                                        val phoneNumber = StringBuilder()
                                        phoneNumber.append("+")
                                        phoneNumber.append(it.body()?.data?.get(0)?.basicDetails?.phoneNumber)
                                        binding.tvPhone.text =
                                            phoneNumber.toString()
                                        /*binding.tvPhone.text =
                                            it.body()?.data?.get(0)?.basicDetails?.phoneNumber ?: ""*/
                                        binding.tvDiagnosis.text =
                                            it.body()?.data?.get(0)?.basicDetails?.dateDiagnosis ?: ""
                                        /*if (it.body()?.data?.get(0)?.basicDetails?.address?.isEmpty() == true) {
                                            binding.tvAddress.text = resources.getString(R.string.no_address)
                                        } else {
                                            binding.tvAddress.text =
                                                it.body()?.data?.get(0)?.basicDetails?.address ?: ""
                                        }*/
                                        binding.tvAddress.text =
                                            it.body()?.data?.get(0)?.basicDetails?.address ?: ""
                                        binding.tvIsASmoker.text =  getCapsSentences(it.body()?.data?.get(0)?.basicDetails?.smoking ?: "")
                                        binding.tvLastVisitDate.text = it.body()?.data?.get(0)?.basicDetails?.lastFuDate ?: ""
                                        /*val format = SimpleDateFormat("yyyy-MM-dd")
                                        val date: String =
                                            format.format(Date.parse(splitTime))
                                        Log.d("date", date)*/
                                        val date = splitTime
                                        var spf = SimpleDateFormat("yyyy-MM-dd")
                                        val newDate = spf.parse(date)
                                        spf = SimpleDateFormat("dd/MM/yyyy")
                                        val newDateString = newDate?.let { spf.format(it) }
                                        println(newDateString)
                                        Log.d("newDateString",newDateString.toString())
                                        binding.tvRegDate.text = newDateString

                                        Glide.with(this)
                                            .load("http://letstalk.dev13.ivantechnology.in/".plus(it.body()?.data!![0].basicDetails!!.image))
                                            .placeholder(R.drawable.img_placeholder)
                                            // .transition(DrawableTransitionOptions.withCrossFade())
                                            .into(binding.ivProfile)

                                        val formatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                        } else {
                                            TODO("VERSION.SDK_INT < O")
                                        }
                                        val today: LocalDate = LocalDate.now()
                                        val birthday: LocalDate = LocalDate.parse(it.body()?.data?.get(0)?.basicDetails?.dob, formatter)


                                        val p: Period = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            Period.between(birthday, today)
                                        } else {
                                            TODO("VERSION.SDK_INT < O")
                                        }
                                        binding.tvAge.text = p.years.toString() + "Y " + p.months.toString() +
                                                "M "
                                        if (!it.body()?.data?.get(0)?.basicDetails?.weight.isNullOrEmpty()) {
                                            binding.tvWeight.text =
                                                it.body()?.data?.get(0)?.basicDetails?.weight.plus(" ")
                                                    .plus("kg")
                                        }
                                        if (!it.body()?.data?.get(0)?.basicDetails?.height.isNullOrEmpty()) {
                                            binding.tvHeight.text =
                                                it.body()?.data?.get(0)?.basicDetails?.height.plus(" ")
                                                    .plus("cm")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        showErrorMsg(it.message.toString(),binding.root)
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        })
    }

    /*private fun uploadImageObservers() {
        var file = File()
        var requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        var filePart = MultipartBody.Part.createFormData("upload_file", file.name, requestBody)

        profileViewModel.uploadImage().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Toast.makeText(this, it.body()?.message, Toast.LENGTH_LONG).show()
                        }
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    // permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    // permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getCapsSentences(tagName: String): String {
        val splits = tagName.lowercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()
        val sb = java.lang.StringBuilder()
        for (i in splits.indices) {
            val eachWord = splits[i]
            if (i > 0 && eachWord.isNotEmpty()) {
                sb.append(" ")
            }
            val cap = (eachWord.substring(0, 1).uppercase(Locale.getDefault())
                    + eachWord.substring(1))
            sb.append(cap)
        }
        return sb.toString()
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            binding.ivProfile.setImageURI(data?.data)
        }
    }

    companion object {
        // image pick code
        private val IMAGE_PICK_CODE = 1000
        // Permission code
        val PERMISSION_CODE = 1001
    }
}