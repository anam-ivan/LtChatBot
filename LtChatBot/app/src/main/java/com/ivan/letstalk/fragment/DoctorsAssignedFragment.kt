package com.ivan.letstalk.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ivan.letstalk.adapter.DoctorsAssignedForPatientAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.FragmentDoctorsAssignedBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.userDetails.PatientsAssignedDoctorListBodies
import com.ivan.letstalk.viewModel.LoginViewModel

class DoctorsAssignedFragment : Fragment() {
    private lateinit var doctorsAssignedForPatientAdapter: DoctorsAssignedForPatientAdapter
    private var _binding: FragmentDoctorsAssignedBinding? = null
    private val binding get() = _binding!!
    private var userId = ""
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var sessionManager : SessionManager
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorsAssignedBinding.inflate(inflater, container, false)
        sessionManager = activity?.let { SessionManager(it) }!!
        userId = sessionManager.fetchUserId().toString()
        Log.d("usedId", userId)
        setupViewModel()
        doctorsAssignedObservers()
        return binding.root
    }
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
    }
    private fun doctorsAssignedObservers() {
        val body = PatientsAssignedDoctorListBodies.PatientsAssignedDoctorList(
            patient_id = userId
        )
        activity?.let { it ->
            viewModel.patientDoctorList(body).observe(it, Observer { it ->
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let {
                                transparentDialogLoader.dismiss()
                                Log.d(
                                    "TAG",
                                    "doctorsAssignedObservers: ${Gson().toJson(it.body()!!.data[0].doctorDetails)}"
                                )
                                if (it.body()?.data != null) {
                                    doctorsAssignedForPatientAdapter = DoctorsAssignedForPatientAdapter(it.body()!!.data)
                                    binding.rvDoctorsAssigned.layoutManager =
                                        LinearLayoutManager(
                                            activity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    binding.rvDoctorsAssigned.itemAnimator =
                                        DefaultItemAnimator()
                                    binding.rvDoctorsAssigned.adapter = doctorsAssignedForPatientAdapter
                                }
                            }
                        }
                        Status.ERROR -> {
                            // showErrorMsg(it.message.toString(),binding.root)
                            transparentDialogLoader.dismiss()
                            Log.d("e_error", it.message.toString())
                        }
                        Status.LOADING -> {
                            activity?.let { it1 -> transparentDialogLoader.show(it1) }
                        }
                    }

                }
            })
        }
    }
    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}