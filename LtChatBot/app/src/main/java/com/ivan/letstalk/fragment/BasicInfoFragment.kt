package com.ivan.letstalk.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ivan.letstalk.adapter.MyHealthCardAdapter
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.api.RetrofitBuilder
import com.ivan.letstalk.databinding.FragmentBasicInfoBinding
import com.ivan.letstalk.helper.SessionManager
import com.ivan.letstalk.helper.Status
import com.ivan.letstalk.helper.TransparentDialogLoader
import com.ivan.letstalk.helper.ViewModelFactory
import com.ivan.letstalk.model.CommonModel
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.viewModel.LoginViewModel

class BasicInfoFragment : Fragment() {
    private lateinit var myHealthCardAdapter: MyHealthCardAdapter
    private var userId = ""
    private var transparentDialogLoader = TransparentDialogLoader()
    private var newList = ArrayList<CommonModel>()
    private lateinit var sessionManager : SessionManager
    private var _binding: FragmentBasicInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBasicInfoBinding.inflate(inflater, container, false)
        sessionManager = activity?.let { SessionManager(it) }!!
        userId = sessionManager.fetchUserId().toString()
        Log.d("usedId", userId)
        setupViewModel()
        patientProfileObservers()
        return binding.root
    }
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.commonApiService))
        ).get(LoginViewModel::class.java)
    }

    private fun patientProfileObservers() {
        val body = UserDetailsBodies.UserDetails(
            user_id = userId
        )
        activity?.let {
            viewModel.mPatientProfileDetails(body).observe(it, Observer { it ->
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let {
                                transparentDialogLoader.dismiss()
                                Log.d(
                                    "TAG",
                                    "setupPatientProfileObservers: ${Gson().toJson(it.body())}"
                                )
                                if (it.body()?.data != null) {
                                    if (it.body()?.data!![0].basicDetails != null) {
                                        newList = ArrayList<CommonModel>()
                                        if (it.body()?.data!![0].basicDetails != null) {
                                            val basic = it.body()?.data!![0].basicDetails
                                            newList.add(CommonModel("Name", "", basic?.name ?: ""))
                                            newList.add(
                                                CommonModel(
                                                    "Cr No.",
                                                    "",
                                                    basic?.crNo ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Mobile",
                                                    "",
                                                    basic?.phoneNumber ?: ""
                                                )
                                            )
                                            newList.add(CommonModel("Gender", "", basic?.sex ?: ""))
                                            newList.add(CommonModel("Age", "", basic?.dob ?: ""))
                                            newList.add(
                                                CommonModel(
                                                    "Height",
                                                    "",
                                                    basic?.height ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Weight",
                                                    "",
                                                    basic?.weight ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Email",
                                                    "",
                                                    basic?.emailId ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Address",
                                                    "",
                                                    basic?.address ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "City",
                                                    "",
                                                    basic?.city ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "State",
                                                    "",
                                                    basic?.state ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Pincode",
                                                    "",
                                                    basic?.pincode ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Next Of Kin",
                                                    "",
                                                    basic?.nextOfKinName ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Relation",
                                                    "",
                                                    basic?.nextOfKinRelation ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Next Of Kin Mobile",
                                                    "",
                                                    basic?.nextOfKinMobile ?: ""
                                                )
                                            )
                                            /*newList.add(
                                                CommonModel(
                                                    "Registration No",
                                                    "",
                                                    basic?.registrationNo ?: ""
                                                )
                                            )*/
                                            newList.add(
                                                CommonModel(
                                                    "Date of Diagnosis",
                                                    "",
                                                    basic?.dateDiagnosis ?: ""
                                                )
                                            )
                                            newList.add(
                                                CommonModel(
                                                    "Next Follow up Date",
                                                    "",
                                                    basic?.nextFollowupDate ?: ""
                                                )
                                            )
                                        }
                                        myHealthCardAdapter = MyHealthCardAdapter(newList)
                                        binding.rvBasicInfoDetails.layoutManager =
                                            LinearLayoutManager(
                                                activity,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                        binding.rvBasicInfoDetails.itemAnimator =
                                            DefaultItemAnimator()
                                        binding.rvBasicInfoDetails.adapter = myHealthCardAdapter
                                        /*binding.tvName.text = it.body()?.data?.get(0)?.basicDetails?.name ?: ""
                                            binding.tvCrNo.text = it.body()?.data?.get(0)?.basicDetails?.crNo ?: ""
                                            binding.tvMobile.text = it.body()?.data?.get(0)?.basicDetails?.phoneNumber ?: ""
                                            if (it.body()?.data?.get(0)?.basicDetails?.sex.toString() == "1") {
                                                binding.tvGender.text = resources.getString(R.string.male)
                                            } else if (it.body()?.data?.get(0)?.basicDetails?.sex.toString() == "2") {
                                                binding.tvGender.text = resources.getString(R.string.female)
                                            }*/
                                    }
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