package com.ivan.letstalk.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.ChatPossibleProblemsAdapter
import com.ivan.letstalk.adapter.DocumentCategoryListAdapter
import com.ivan.letstalk.adapter.DocumentsListAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.ActivityMyDocumentsBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.deleteDocuments.MetaDeleteDocuments
import com.ivan.letstalk.model.documents.Data
import com.ivan.letstalk.model.documents.DocumentsPayload
import com.ivan.letstalk.model.faq.SortBy
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class MyDocumentsActivity : AppCompatActivity() {
    private lateinit var pLoader: ProgressBar
    private lateinit var tvButton: TextView
    private lateinit var btnUpload: RelativeLayout
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var documentListAdapter: DocumentsListAdapter
    private var userId = ""
    private lateinit var sessionManager : SessionManager
    private lateinit var patientProfileViewModel: PatientProfileViewModel
    private lateinit var tvFileName : TextView
    private lateinit var edtRecordName : EditText
    private lateinit var tvRecordDate : TextView
    private lateinit var tvRecordTime : TextView
    lateinit var picFile :String
    private var filePath = ""
    private var docCategoryId = ""
    private var docTime = ""
    private var docDate = ""
    private var isSpinnerSelect: Boolean = false
    var isSpinnerClosed = true
    lateinit var binding: ActivityMyDocumentsBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var profileViewModel: PatientProfileViewModel
    lateinit var downloadListener: BroadcastReceiver
    /*private val myDocumentModel = ArrayList<MyDocumentModel>()
    private lateinit var myDocumentsAdapter: MyDocumentsAdapter
    private lateinit var rvMyDocuments: RecyclerView*/
    var documentsCategoryList = arrayListOf<Data>()
    private lateinit var dialog: BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_documents)
        sessionManager = SessionManager(this)
        userId = sessionManager.fetchUserId().toString()
        Log.d("userId", userId)
        // rvMyDocuments = findViewById(R.id.rv_my_documents)
        binding.btnUploadDoc.setOnClickListener {
            uploadNewDocumentDialog()
        }
        /*myDocumentsAdapter = MyDocumentsAdapter(myDocumentModel)
        rvMyDocuments.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )
        rvMyDocuments.itemAnimator = DefaultItemAnimator()
        rvMyDocuments.adapter = myDocumentsAdapter
        initAllDocs()*/

        binding.btnBack.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        setupViewModel()
        setupProfileViewModel()
        setupDocumentsViewModel()
        setUpDocumentsCategoryList()
        documentsListObservers()

        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchKey: String = s.toString()
                if (s.length == 2) {
                    searchDocumentsListObservers(searchKey)
                }
                if (s.isEmpty()) {
                    searchDocumentsListObservers(searchKey)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        // BroadCast Receiver for download
        /*downloadListener = object : BroadcastReceiver() {
            override fun onReceive(ct: Context?, intent: Intent?) {
                val builder = MaterialAlertDialogBuilder(
                    ct!!,
                    R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                builder.setMessage("Download Complete")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }*/
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
    }

    private fun setupProfileViewModel() {
        profileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    private fun setUpDocumentsCategoryList() {
        viewModel.documentCategoryList().observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            Log.d("TAG", "metaList: ${Gson().toJson(it.body())}")
                            documentsCategoryList.add(Data("", "", "Select from dropdown","",""))
                            it.body()?.data?.let { it1 -> documentsCategoryList.addAll(it1) }
                            // metaList = (it.body()?.data ?: "") as ArrayList<Data>
                            Log.d("TAG", "documentsCategoryList: ${Gson().toJson(documentsCategoryList)}")
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

    private fun uploadNewDocumentDialog() {
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // dialog.behavior.maxHeight = 1000 // set max height when expanded in PIXEL
        dialog.behavior.peekHeight = 1100 // set default height when collapsed in PIXEL
        val view = layoutInflater.inflate(R.layout.upload_new_document_dialog, null)
        // var btnUpload = view.findViewById<RelativeLayout>(R.id.btn_upload)
        edtRecordName = view.findViewById(R.id.edt_record_name)
        val documentCategorySpinner = view.findViewById<Spinner>(R.id.spCategory)
        val ivDropdown = view.findViewById<ImageView>(R.id.iv_dropdown)
        val ivClose = view.findViewById<ImageView>(R.id.iv_close)
        val rrRecordDate = view.findViewById<RelativeLayout>(R.id.rr_record_date)
        val rrRecordTime = view.findViewById<RelativeLayout>(R.id.rr_record_time)
        tvRecordDate = view.findViewById(R.id.tv_record_date)
        tvRecordTime = view.findViewById(R.id.tv_record_time)
        btnUpload = view.findViewById(R.id.btn_upload)
        pLoader = view.findViewById(R.id.pLoader)
        tvButton = view.findViewById(R.id.tvButton)
        tvFileName = view.findViewById(R.id.tv_file_name)
        val llUploadAttachment = view.findViewById<LinearLayoutCompat>(R.id.ll_upload_attachment)
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
        val adapter = DocumentCategoryListAdapter(
            this,
            documentsCategoryList
        )
        documentCategorySpinner.adapter = adapter
        documentCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                isSpinnerSelect = pos != 0
                if (parent != null) {
                    (parent.getItemAtPosition(pos) as Data).documentCategoryName?.let { Log.d("clicked", it) }
                    docCategoryId = (parent.getItemAtPosition(pos) as Data).Id.toString()
                    // Toast.makeText(this@MyDocumentsActivity, docCategoryId, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        ivDropdown.setOnClickListener {
            documentCategorySpinner.performClick()
        }
        llUploadAttachment.setOnClickListener {
            Log.d("clicked","clicked")
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    // permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    requestPermissions(permissions, ProfileActivity.PERMISSION_CODE);
                } else {
                    // permission already granted
                    pickFileFromGallery()
                }
            } else {
                // system OS is < Marshmallow
                pickFileFromGallery()
            }
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
                tvRecordDate.text = sdf.format(cal.time)
                docDate = sdf.format(cal.time).toString()
            }
            rrRecordDate.setOnClickListener {
                /*DatePickerDialog(
                    this,R.style.DialogTheme, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()*/
                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DialogTheme,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()
            }
        }

        rrRecordTime.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime[Calendar.HOUR_OF_DAY]
            val minute = currentTime[Calendar.MINUTE]
            val timePickerDialog: TimePickerDialog = TimePickerDialog(
                this,R.style.DialogTheme,
                { _, selectedHour, selectedMinute ->
                    docTime = String.format("%02d:%02d", selectedHour, selectedMinute).toString()
                    tvRecordTime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
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

        btnUpload.setOnClickListener {
            if (!isSpinnerSelect) {
                Toast.makeText(
                    this@MyDocumentsActivity,
                    "Please select Document Category",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edtRecordName.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    this@MyDocumentsActivity,
                    "Please enter Document Name",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (tvRecordDate.text.toString() == "dd mm yy") {
                Toast.makeText(
                    this@MyDocumentsActivity,
                    "Please enter Record Date",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (tvRecordTime.text.toString() == "HH mm") {
                Toast.makeText(
                    this@MyDocumentsActivity,
                    "Please enter Record Date",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (filePath.isEmpty()) {
                Toast.makeText(
                    this@MyDocumentsActivity,
                    "Please upload Attachment",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                uploadImageObservers(filePath)
            }
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    private fun uploadImageObservers(fileUri: String) {
        /*val body = DocumentUploadPayload.Document(
            document_category_id = docCategoryId,
            document_name = edtRecordName.text.toString().trim(),
            record_date = docDate,
            record_time = docTime,
        )*/
        val documentCategoryId = docCategoryId
        val documentName = edtRecordName.text.toString().trim()
        val documentRecordDate = docDate
        val documentRecordTime = docTime
        val file = File(fileUri)
        // val fileReqBody = file.asRequestBody("image/*".toMediaType())
        val fileReqBody = file.asRequestBody("*/*".toMediaType())
        val filePart = MultipartBody.Part.createFormData("thumb_image", file.name, fileReqBody)
        val documentCategoryIdBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), documentCategoryId)
        val documentNameBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), documentName)
        val documentRecordDateBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), documentRecordDate)
        val documentRecordTimeBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), documentRecordTime)

        /*val map: MutableMap<kotlin.String, RequestBody> = HashMap()
        map["document_category_id"] = documentCategoryIdBody
        map["document_name"] = documentNameBody
        map["record_date"] = documentRecordDateBody
        map["record_time"] = documentRecordTimeBody*/


        profileViewModel.uploadDocument(
            filePart,
            documentCategoryIdBody,
            documentNameBody,
            documentRecordDateBody,
            documentRecordTimeBody
        ).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            pLoader.visibility = View.GONE
                            tvButton.visibility = View.VISIBLE
                            btnUpload.isEnabled = true
                            if (it.body() != null) {
                                if (it.body()?.status.equals("success")) {
                                    dialog.dismiss()
                                    documentsListObservers()
                                }
                            }
                            // showSuccessMsg(it.body()?.message.toString(),binding.root)
                        }
                    }
                    Status.ERROR -> {
                        pLoader.visibility = View.GONE
                        tvButton.visibility = View.VISIBLE
                        btnUpload.isEnabled = true
                        Log.d("pic_error", it.message.toString())
                        // Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        pLoader.visibility = View.VISIBLE
                        tvButton.visibility = View.GONE
                        btnUpload.isEnabled = false
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

    private fun pickFileFromGallery() {
        // val intent = Intent()
        // intent.type = "application/pdf"
        // intent.type = "*/*";
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*|application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a File "), IMAGE_PICK_CODE)
    }

    private fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path: String? = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    // handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    // permission from popup granted
                    pickFileFromGallery()
                    // documentListAdapter.getResult(true)
                }
                else{
                    // permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    // documentListAdapter.getResult(false)
                }
            }
            DOWNLOAD_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    documentListAdapter.getResult(true)
                }
                else{
                    // permission from popup denied
                    Toast.makeText(this, "Download Permission denied", Toast.LENGTH_SHORT).show()
                    documentListAdapter.getResult(false)
                }
            }
        }
    }

    private fun setupDocumentsViewModel() {
        patientProfileViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PatientProfileViewModel::class.java)
    }

    //handle result of picked image
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val fileUri = data!!.data
            /*val file = fileUri?.toFile()
            Log.d("file_name", file.toString())*/
            // val uri: Uri = attr.data.getData()
            val uriString = fileUri.toString()
            val myFile = File(uriString)
            val path = myFile.absolutePath
            // var displayName: kotlin.String? = null
            val pathArr = myFile.absolutePath.split(":/".toRegex()).toTypedArray()
            val mPath = pathArr[pathArr.size - 1]
            Log.d("mPath", mPath)

            if (uriString.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = fileUri?.let { this.contentResolver.query(it, null, null, null, null) }
                    if (cursor != null && cursor.moveToFirst()) {
                        tvFileName.visibility = View.VISIBLE
                        tvFileName.text = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        /*displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))*/
                    }
                } finally {
                    cursor?.close()
                }
            } else if (uriString.startsWith("file://")) {
                tvFileName.visibility = View.VISIBLE
                /*displayName = myFile.name*/
                tvFileName.text
            } else if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        myCursor = fileUri?.let { applicationContext!!.contentResolver.query(it, null, null, null, null) }
                        if (myCursor != null && myCursor.moveToFirst()) {
                            /*pdfName = myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            pdfTextView.text = pdfName*/
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
            Log.d("path", path.toString())
            // filePath = path.toString()
            val selectedImageURI: Uri = data.data!!
            val imageFile = getRealPathFromURI(selectedImageURI,this)?.let { File(it) }
            if (imageFile != null) {
                Log.d("path", imageFile.path)
                filePath = imageFile.toString()
                /*picFile = GetRealPathFromUri.getPathFromUri(this, fileUri!!).toString()
                Log.d("picFile", picFile)*/
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri, context: Context): String? {
        val returnCursor: Cursor = context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file: File = File(context.filesDir, name)
        try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream.available()

            //int bufferSize = 1024;
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }

    /*private fun getPDFPath(uri: Uri?): kotlin.String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }*/

    companion object {
        // image pick code
        private val IMAGE_PICK_CODE = 1000
        // Permission code
        private val PERMISSION_CODE = 1001
        // Download Permission code
        private val DOWNLOAD_PERMISSION_CODE = 1002
    }

    private fun documentsListObservers() {
        val body = DocumentsPayload.Documents(
            user_id = userId,
            draw = 1,
            limit = 20,
            page_no = 1,
            perpage = 1,
            search_key = "",
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.documentsList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            Log.d("TAG", "documentsList: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                binding.tvTotalDocuments.text = "Total Documents:".plus(" ")
                                    .plus(it.body()?.data?.size.toString())
                                documentListAdapter = DocumentsListAdapter(it.body()?.data!!,object :
                                    DocumentsListAdapter.onClickListener{
                                    override fun onClick(pos: Int) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                                PackageManager.PERMISSION_DENIED
                                            ) {
                                                // permission denied
                                                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                // show popup to request runtime permission
                                                requestPermissions(permissions, DOWNLOAD_PERMISSION_CODE)
                                            } else {
                                                // permission already granted
                                                /*Toast.makeText(
                                                    this@MyDocumentsActivity, "Ok Permission",
                                                    Toast.LENGTH_SHORT
                                                ).show()*/
                                                documentListAdapter.getResult(true)
                                            }
                                        } else {
                                            // system OS is < Marshmallow
                                            /*Toast.makeText(
                                                this@MyDocumentsActivity, "Ok Permission",
                                                Toast.LENGTH_SHORT
                                            ).show()*/
                                            documentListAdapter.getResult(true)
                                        }
                                        /*checkPermission()
                                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        requestPermissions(permissions, ProfileActivity.PERMISSION_CODE)*/
                                        /*Toast.makeText(
                                            this@MyDocumentsActivity, "Test",
                                            Toast.LENGTH_SHORT
                                        ).show()*/
                                    }

                                })
                                binding.rvMyDocuments.layoutManager = LinearLayoutManager(
                                    this,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                binding.rvMyDocuments.itemAnimator = DefaultItemAnimator()
                                binding.rvMyDocuments.adapter = documentListAdapter
                            }
                        }
                    }
                    Status.ERROR -> {
                       transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                       transparentDialogLoader.show(this)
                    }
                }

            }
        }
    }

    private fun searchDocumentsListObservers(searchKey: String) {
        val body = DocumentsPayload.Documents(
            user_id = userId,
            draw = 1,
            limit = 20,
            page_no = 1,
            perpage = 1,
            search_key = searchKey,
            sort_by = SortBy(
                key = "",
                dir = ""
            )
        )
        patientProfileViewModel.documentsList(body).observe(this) { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        transparentDialogLoader.dismiss()
                        resource.data?.let { it ->
                            Log.d("TAG", "documentsList: ${Gson().toJson(it.body())}")
                            if (it.body()?.data != null) {
                                binding.tvTotalDocuments.text = "Total Documents:".plus(" ")
                                    .plus(it.body()?.data?.size.toString())
                                documentListAdapter = DocumentsListAdapter(it.body()?.data!!,object :
                                    DocumentsListAdapter.onClickListener{
                                    override fun onClick(pos: Int) {
                                        // checkPermission()
                                    }

                                })
                                binding.rvMyDocuments.layoutManager = LinearLayoutManager(
                                    this,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                binding.rvMyDocuments.itemAnimator = DefaultItemAnimator()
                                binding.rvMyDocuments.adapter = documentListAdapter
                            }
                        }
                    }
                    Status.ERROR -> {
                        transparentDialogLoader.dismiss()
                    }
                    Status.LOADING -> {
                        transparentDialogLoader.show(this)
                    }
                }

            }
        }
    }
     fun downloadFile(fileUrl: String) {
         val request = DownloadManager.Request(Uri.parse(fileUrl))
         val title: String = URLUtil.guessFileName(fileUrl, null, null)
         request.setTitle(title)
         request.setDescription("Downloading File please wait...")
         /*val cookie: String = CookieManager.getInstance().getCookie(fileUrl)
         request.addRequestHeader("cookie", cookie)*/
         request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
         request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
         // registerReceiver(downloadListener, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
         val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
         downloadManager.enqueue(request)
    }

    /*override fun onRestart() {
        super.onRestart()
        documentsListObservers()
    }*/

    fun deleteDocuments(body: MetaDeleteDocuments.DeleteDocuments) {
        patientProfileViewModel.deleteDocuments(body).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                            showSuccessMsg(it.body()?.message.toString(), binding.root)
                            documentsListObservers()
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

    fun checkPermission(): Boolean {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission error", "You have permission");
        }
        return true
    }

    /*override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.downloadListener)
    }*/
}