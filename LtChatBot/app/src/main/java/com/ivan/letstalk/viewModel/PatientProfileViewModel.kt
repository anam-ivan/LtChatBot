package com.ivan.letstalk.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ivan.letstalk.helper.Resource
import com.ivan.letstalk.model.deleteDocuments.MetaDeleteDocuments
import com.ivan.letstalk.model.documents.DocumentUploadPayload
import com.ivan.letstalk.model.documents.DocumentsPayload
import com.ivan.letstalk.model.faq.FaqPayload
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.model.healthVitals.MetaAddBodies
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.healthVitals.MetaUpdateBodies
import com.ivan.letstalk.model.knowYourSideEffects.KnowYourSideEffectsPayload
import com.ivan.letstalk.model.profileUpdate.ProfileEditPayload
import com.ivan.letstalk.model.slug.SlugPayload
import com.ivan.letstalk.model.topAlkSideEffects.AlkSideEffectsPayload
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import com.ivan.letstalk.repo.LoginRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap

class PatientProfileViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun addHealthVitals(addHealthVitals: MetaAddBodies.MetaAddBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.addHealthVitals(addHealthVitals)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun updateHealthVitals(updateHealthVitals: MetaUpdateBodies.MetaUpdateBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.updateHealthVitals(updateHealthVitals)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteHealthVitals(deleteHealthVitals: MetaDeleteBodies.MetaDeleteBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.deleteHealthVitals(deleteHealthVitals)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun faqList(faq: FaqPayload.Faq) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.faqList(faq)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun glossaryList(glossaryList: GlossaryPayload.Glossary) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.glossaryList(glossaryList)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun slugDetails(slug: SlugPayload.Slug) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.slugDetails(slug)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun uploadImage(@Part image: MultipartBody.Part) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.uploadImage(image)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun editProfileWithoutImage(profileEdit: ProfileEditPayload.ProfileEdit) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.editProfileWithoutImage(profileEdit)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun uploadDocument(@Part image: MultipartBody.Part, @Part document_category_id: RequestBody,
                       @Part document_name: RequestBody,
                       @Part record_date: RequestBody,
                       @Part record_time: RequestBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.uploadDocument(image,document_category_id,document_name, record_date, record_time)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*fun uploadDocument(@Part image: MultipartBody.Part, @PartMap params: Map<String, RequestBody>) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = loginRepository.uploadDocument(image, params)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }*/
    fun documentsList(documentsList: DocumentsPayload.Documents) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.documentsList(documentsList)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun knowYourSideEffects(knowYourSideEffects: KnowYourSideEffectsPayload.KnowYourSideEffects) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.knowYourSideEffects(knowYourSideEffects)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteDocuments(deleteDocuments: MetaDeleteDocuments.DeleteDocuments) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.deleteDocuments(deleteDocuments)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun whatDoctorSay(whatDoctorSay: KnowYourSideEffectsPayload.KnowYourSideEffects) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.whatDoctorSay(whatDoctorSay)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun allSideEffects(allSideEffects: AlkSideEffectsPayload.AlkSideEffects) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.allSideEffects(allSideEffects)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*fun markedSideEffects(userDetails: UserDetailsBodies.UserDetails) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.markedSideEffects(userDetails)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }*/
}