package com.ivan.letstalk.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ivan.letstalk.helper.Resource
import com.ivan.letstalk.model.chatHistoryTitle.ChatHistoryTitlePayload
import com.ivan.letstalk.model.chatHistoryTitle.ChatIdBodies
import com.ivan.letstalk.model.login.RequestBodies
import com.ivan.letstalk.model.login.VerifyOTPBodies
import com.ivan.letstalk.model.markedAlkStatusChange.MarkedAlkStatusChangePayload
import com.ivan.letstalk.model.phonenumberupdate.PhoneNumberChangeBodies
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectUserDetailsBodies
import com.ivan.letstalk.model.topAlkSideEffects.MarkedAlkSideEffectsPayload
import com.ivan.letstalk.model.userDetails.PatientsAssignedDoctorListBodies
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.repo.LoginRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val crNo: MutableLiveData<String> = MutableLiveData()
    // val otp: MutableLiveData<String> = MutableLiveData()

    fun getLogin(user: RequestBodies.LoginBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.loginPatient(user)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun validateOTP(validateOTP: VerifyOTPBodies.OTPLoginBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.validateOTP(validateOTP)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*fun patientProfileDetails() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.patientProfileDetails()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/

    fun mPatientProfileDetails(userDetails: UserDetailsBodies.UserDetails) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = loginRepository.mPatientProfileDetails(userDetails)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun patientPhoneNumberUpdate(phoneNumberUpdate: PhoneNumberChangeBodies.PhoneNumberUpdateBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.patientPhoneChangeRequest(phoneNumberUpdate)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun vitalsMetaList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.vitalsMetaList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun healthVitalsListing() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.healthVitalsListing()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun sideEffectsHistory(userDetails: SideEffectUserDetailsBodies.UserDetails) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = loginRepository.sideEffectsHistory(userDetails)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    fun documentCategoryList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.documentCategoryList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*fun whatDoctorSay(whatDoctorSay: KnowYourSideEffectsPayload.KnowYourSideEffects) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.whatDoctorSay(whatDoctorSay)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/

    fun chatHistoryTitle(chatHistoryTitle : ChatHistoryTitlePayload.ChatHistoryTitle) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.chatHistoryTitle(chatHistoryTitle)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun chatHistory(chatHistory : ChatIdBodies.ChatId) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.chatHistory(chatHistory)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun markedSideEffects(markedSideEffects: MarkedAlkSideEffectsPayload.MarkedAlkSideEffects) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.markedSideEffects(markedSideEffects)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun markedSideEffectsStatusChange(markedSideEffectsStatusChange: MarkedAlkStatusChangePayload.MarkedAlkStatusChange) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.markedSideEffectsStatusChange(markedSideEffectsStatusChange)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun patientDoctorList(patientDoctorList: PatientsAssignedDoctorListBodies.PatientsAssignedDoctorList) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = loginRepository.patientDoctorList(patientDoctorList)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
}