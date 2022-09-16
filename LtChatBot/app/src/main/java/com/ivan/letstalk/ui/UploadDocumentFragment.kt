package com.ivan.letstalk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivan.letstalk.R

class UploadDocumentFragment : BottomSheetDialogFragment(),AdapterView.OnItemSelectedListener {
    private lateinit var spCategory: Spinner
    private lateinit var ivDropdown: AppCompatImageView
    private var docCategory = arrayOf(
        "Bills", "Hospitalization",
        "Insurance", "Prescription",
        "Test Report", "Other"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_upload_document, container, false)
        ivDropdown = view.findViewById(R.id.iv_dropdown)
        spCategory = view.findViewById(R.id.spCategory)
        spCategory.onItemSelectedListener = this

        val ad: ArrayAdapter<Any?>? = activity?.let {
            ArrayAdapter<Any?>(
                it,
                android.R.layout.simple_spinner_item,
                docCategory
            )
        }
        ad?.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spCategory.adapter = ad

        ivDropdown.setOnClickListener {
            spCategory.performClick()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        /*Toast.makeText(
            activity,
            docCategory[p2],
            Toast.LENGTH_LONG)
            .show()*/
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /*override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }*/
}