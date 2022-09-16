package com.ivan.letstalk.ui

import android.Manifest
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
import com.ivan.letstalk.databinding.ActivityEditProfileBinding
import com.ivan.letstalk.helper.*
import com.ivan.letstalk.model.profileUpdate.ProfileEditPayload
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class EditProfileActivity : AppCompatActivity() {
    // lateinit var picFile : File
    private lateinit var sessionManager : SessionManager
    private var userId = ""
    lateinit var picFile : String
    lateinit var binding: ActivityEditProfileBinding
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var profileViewModel: PatientProfileViewModel
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        setupViewModel()
        setupProfileViewModel()
        patientProfileObservers()
        // uploadImageObservers()
        binding.btnEdit.setOnClickListener {
            // check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    // permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    requestPermissions(permissions, ProfileActivity.PERMISSION_CODE);
                } else {
                    // permission already granted
                    pickImageFromGallery()
                }
            } else {
                // system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
        /*binding.btnUpdate.setOnClickListener {
            if (this::picFile.isInitialized) {
                uploadImageObservers(picFile)
                // editProfileWithOutImage()
            } else {
                // showSuccessMsg("Please select a picture", binding.root)
                editProfileWithOutImage()
            }
        }*/
        binding.btnUpdatePic.setOnClickListener {
            if (this::picFile.isInitialized) {
                uploadImageObservers(picFile)
            }
        }
        binding.btnUpdate.setOnClickListener {
            editProfileWithOutImage()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
    }

    private fun uploadImageObservers(fileUri: String) {
        val file = File(fileUri)
        val fileReqBody = file.asRequestBody("image/*".toMediaType())
        // var requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val filePart = MultipartBody.Part.createFormData("profile_pic", file.name, fileReqBody)

        profileViewModel.uploadImage(filePart).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            showSuccessMsg(it.body()?.message.toString(),binding.root)
                            // editProfileWithOutImage()
                            // patientProfileObservers()
                            Glide.with(this)
                                .load("http://letstalk.dev13.ivantechnology.in/".plus(it.body()?.data!!.image))
                                .into(binding.ivProfile)
                        }
                    }
                    Status.ERROR -> {
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
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

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val fileUri = data!!.data
            // GetRealPathFromUri.getPathFromUri(this, fileUri!!)?.let { uploadImageObservers(it) }
            picFile = GetRealPathFromUri.getPathFromUri(this, fileUri!!).toString()
            Log.d("picFile",picFile)
            binding.ivProfile.setImageURI(data.data)
        }
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
                                        val name = (it.body()?.data?.get(0)?.basicDetails?.name)
                                        binding.edtName.setText(name)
                                        /*binding.edtName.text =
                                            (it.body()?.data?.get(0)?.basicDetails?.name ?: "") as Editable?*/
                                        binding.edtAddress.setText(it.body()?.data?.get(0)?.basicDetails?.address)
                                        // binding.edtPhone.setText(it.body()?.data?.get(0)?.basicDetails?.phoneNumber)
                                        binding.edtCity.setText(it.body()?.data?.get(0)?.basicDetails?.city)
                                        binding.edtState.setText(it.body()?.data?.get(0)?.basicDetails?.state)
                                        binding.edtPincode.setText(it.body()?.data?.get(0)?.basicDetails?.pincode)
                                        // binding.edtAge.setText(it.body()?.data?.get(0)?.basicDetails?.dob)
                                        binding.edtWeight.setText(it.body()?.data?.get(0)?.basicDetails?.weight)
                                        binding.edtHeight.setText(it.body()?.data?.get(0)?.basicDetails?.height)
                                        Glide.with(this)
                                            .load("http://letstalk.dev13.ivantechnology.in/".plus(it.body()?.data!![0].basicDetails!!.image))
                                            .placeholder(R.drawable.img_placeholder)
                                            .into(binding.ivProfile)
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

    private fun editProfileWithOutImage() {
        val body = ProfileEditPayload.ProfileEdit(
            height = binding.edtHeight.text.toString().trim(),
            weight = binding.edtWeight.text.toString().trim(),
            address = binding.edtAddress.text.toString().trim(),
            name = binding.edtName.text.toString().trim(),
            city = binding.edtCity.text.toString().trim(),
            state = binding.edtState.text.toString().trim(),
            pincode = binding.edtPincode.text.toString().trim()
        )
        profileViewModel.editProfileWithoutImage(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            transparentDialogLoader.dismiss()
                            Log.d("TAG", "editProfileWithOutImage: ${Gson().toJson(it.body())}")
                            showSuccessMsg(it.body()?.message.toString(),binding.root)
                            /*if (it.body()?.updatedData != null) {
                                if (it.body()?.updatedData!![0].basicDetails != null) {
                                    *//*if (it.body()?.data!![0].basicDetails != null) {
                                        val name = (it.body()?.data?.get(0)?.basicDetails?.name)
                                        binding.edtName.setText(name)
                                        *//**//*binding.edtName.text =
                                            (it.body()?.data?.get(0)?.basicDetails?.name ?: "") as Editable?*//**//*
                                        binding.edtAddress.setText(it.body()?.data?.get(0)?.basicDetails?.address)
                                        binding.edtCity.setText(it.body()?.data?.get(0)?.basicDetails?.city)
                                        binding.edtState.setText(it.body()?.data?.get(0)?.basicDetails?.state)
                                        binding.edtPincode.setText(it.body()?.data?.get(0)?.basicDetails?.pincode)
                                        binding.edtAge.setText(it.body()?.data?.get(0)?.basicDetails?.dob)
                                        binding.edtWeight.setText(it.body()?.data?.get(0)?.basicDetails?.weight)
                                        binding.edtHeight.setText(it.body()?.data?.get(0)?.basicDetails?.height)
                                        Glide.with(this)
                                            .load("http://letstalk.dev13.ivantechnology.in/".plus(it.body()?.data!![0].basicDetails!!.image))
                                            .placeholder(R.drawable.img_placeholder)
                                            .into(binding.ivProfile)
                                    }*//*
                                }
                            }*/
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

    companion object {
        // image pick code
        private val IMAGE_PICK_CODE = 1000
        // Permission code
        private val PERMISSION_CODE = 1001
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }
}