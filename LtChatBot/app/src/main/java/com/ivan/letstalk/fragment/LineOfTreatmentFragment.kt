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
import com.ivan.letstalk.R
import com.ivan.letstalk.adapter.DoctorsAssignedForPatientAdapter
import com.ivan.letstalk.adapter.LineOfTreatmentAdapter
import com.ivan.letstalk.adapter.ProgressiveDataAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.FragmentDoctorsAssignedBinding
import com.ivan.letstalk.databinding.FragmentLineOfTreatmentBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.userDetails.PatientsAssignedDoctorListBodies
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel

class LineOfTreatmentFragment : Fragment() {
    private lateinit var lineOfTreatmentAdapter: LineOfTreatmentAdapter
    private var _binding: FragmentLineOfTreatmentBinding? = null
    private val binding get() = _binding!!
    private var userId = ""
    private var transparentDialogLoader = TransparentDialogLoader()
    private lateinit var sessionManager : SessionManager
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLineOfTreatmentBinding.inflate(inflater, container, false)
        sessionManager = activity?.let { SessionManager(it) }!!
        userId = sessionManager.fetchUserId().toString()
        Log.d("usedId", userId)
        setupViewModel()
        lineOfTreatmentObservers()
        return binding.root
    }
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }
    private fun lineOfTreatmentObservers() {
        val body = UserDetailsBodies.UserDetails(
            user_id = userId
        )
        activity?.let { it ->
            viewModel.mPatientProfileDetails(body).observe(it, Observer { it ->
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let {
                                transparentDialogLoader.dismiss()
                                Log.d(
                                    "TAG",
                                    "lineOfTreatmentObservers: ${Gson().toJson(it.body()!!.data[0].healthCard[0].Progressive[0].withoutGen)}"
                                )
                                if (it.body()?.data != null) {
                                    lineOfTreatmentAdapter = LineOfTreatmentAdapter(it.body()!!.data[0].healthCard[0].Progressive[0].gen)
                                    binding.rvLineOfTreatment.layoutManager =
                                        LinearLayoutManager(
                                            activity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    binding.rvLineOfTreatment.itemAnimator =
                                        DefaultItemAnimator()
                                    binding.rvLineOfTreatment.adapter = lineOfTreatmentAdapter
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
}