package com.ivan.letstalk.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.letstalk.api.ApiHelper
import com.ivan.letstalk.repo.LoginRepository
import com.ivan.letstalk.viewModel.LoginViewModel
import com.ivan.letstalk.viewModel.PatientProfileViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(PatientProfileViewModel::class.java)) {
            return PatientProfileViewModel(LoginRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}