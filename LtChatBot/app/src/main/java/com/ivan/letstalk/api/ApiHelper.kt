package com.ivan.letstalk.api

import com.ivan.letstalk.model.chatHistoryTitle.ChatHistoryTitlePayload
import com.ivan.letstalk.model.chatHistoryTitle.ChatIdBodies
import com.ivan.letstalk.model.deleteDocuments.MetaDeleteDocuments
import com.ivan.letstalk.model.documents.DocumentsPayload
import com.ivan.letstalk.model.faq.FaqPayload
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.model.healthVitals.MetaAddBodies
import com.ivan.letstalk.model.healthVitals.MetaDeleteBodies
import com.ivan.letstalk.model.healthVitals.MetaUpdateBodies
import com.ivan.letstalk.model.knowYourSideEffects.KnowYourSideEffectsPayload
import com.ivan.letstalk.model.login.RequestBodies
import com.ivan.letstalk.model.login.VerifyOTPBodies
import com.ivan.letstalk.model.markedAlkStatusChange.MarkedAlkStatusChangePayload
import com.ivan.letstalk.model.phonenumberupdate.PhoneNumberChangeBodies
import com.ivan.letstalk.model.profileUpdate.ProfileEditPayload
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectUserDetailsBodies
import com.ivan.letstalk.model.slug.SlugPayload
import com.ivan.letstalk.model.topAlkSideEffects.AlkSideEffectsPayload
import com.ivan.letstalk.model.topAlkSideEffects.MarkedAlkSideEffectsPayload
import com.ivan.letstalk.model.userDetails.PatientsAssignedDoctorListBodies
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Part

class ApiHelper (private val apiService: ApiService) {

    suspend fun login(user: RequestBodies.LoginBody) = apiService.loginUser(user)

    suspend fun validateOTP(validateOTP: VerifyOTPBodies.OTPLoginBody) = apiService.validateOTP(validateOTP)

    // suspend fun patientProfileDetails() = apiService.patientProfileDetails()

    suspend fun mPatientProfileDetails(userDetails: UserDetailsBodies.UserDetails) = apiService.mPatientProfileDetails(userDetails)

    suspend fun patientPhoneNumberUpdate(phoneNumberUpdate: PhoneNumberChangeBodies.PhoneNumberUpdateBody) = apiService.patientPhoneChangeRequest(phoneNumberUpdate)

    suspend fun vitalsMetaList() = apiService.vitalsMetaList()

    suspend fun addHealthVitals(addHealthVitals: MetaAddBodies.MetaAddBody) = apiService.addHealthVitals(addHealthVitals)

    suspend fun healthVitalsListing() = apiService.healthVitalsListing()

    suspend fun updateHealthVitals(updateHealthVitals: MetaUpdateBodies.MetaUpdateBody) = apiService.updateHealthVitals(updateHealthVitals)

    suspend fun deleteHealthVitals(deleteHealthVitals: MetaDeleteBodies.MetaDeleteBody) = apiService.deleteHealthVitals(deleteHealthVitals)

    suspend fun faqList(faqList: FaqPayload.Faq) = apiService.faqList(faqList)

    suspend fun glossaryList(glossaryList: GlossaryPayload.Glossary) = apiService.glossaryList(glossaryList)

    suspend fun slugDetails(slug: SlugPayload.Slug) = apiService.slugDetails(slug)

    suspend fun uploadImage(@Part image: MultipartBody.Part) = apiService.uploadImage(image)

    suspend fun editProfileWithoutImage(profileEdit: ProfileEditPayload.ProfileEdit) = apiService.editProfileWithoutImage(profileEdit)

    suspend fun sideEffectsHistory(userDetails: SideEffectUserDetailsBodies.UserDetails) = apiService.sideEffectsHistory(userDetails)

    suspend fun documentCategoryList() = apiService.documentCategoryList()

    suspend fun uploadDocument(
        @Part image: MultipartBody.Part,
        @Part document_category_id: RequestBody,
        @Part document_name: RequestBody,
        @Part record_date: RequestBody,
        @Part record_time: RequestBody
    ) = apiService.uploadDocument(
        image,
        document_category_id,
        document_name,
        record_date,
        record_time
    )
    /*suspend fun uploadDocument(
        @Part image: MultipartBody.Part,
        @PartMap params: Map<String, RequestBody>
    ) = apiService.uploadDocument(
        image,
        params
    )*/

    suspend fun documentsList(documentsList: DocumentsPayload.Documents) = apiService.documentsList(documentsList)

    suspend fun knowYourSideEffects(knowYourSideEffects: KnowYourSideEffectsPayload.KnowYourSideEffects) = apiService.knowYourSideEffects(knowYourSideEffects)

    suspend fun deleteDocuments(deleteDocuments: MetaDeleteDocuments.DeleteDocuments) = apiService.deleteDocument(deleteDocuments)

    suspend fun whatDoctorSay(whatDoctorSay: KnowYourSideEffectsPayload.KnowYourSideEffects) = apiService.whatDoctorSay(whatDoctorSay)

    suspend fun chatHistoryTitle(chatHistoryTitle : ChatHistoryTitlePayload.ChatHistoryTitle) = apiService.chatHistoryTitle(chatHistoryTitle)

    suspend fun chatHistory(chatHistory : ChatIdBodies.ChatId) = apiService.chatHistory(chatHistory)

    suspend fun allSideEffects(allSideEffects: AlkSideEffectsPayload.AlkSideEffects) = apiService.allSideEffects(allSideEffects)

    suspend fun markedSideEffects(markedSideEffects: MarkedAlkSideEffectsPayload.MarkedAlkSideEffects) = apiService.markedSideEffects(markedSideEffects)

    suspend fun markedSideEffectsStatusChange(markedSideEffectsStatusChange: MarkedAlkStatusChangePayload.MarkedAlkStatusChange) = apiService.markedSideEffectsStatusChange(markedSideEffectsStatusChange)

    suspend fun patientDoctorList(patientDoctorList: PatientsAssignedDoctorListBodies.PatientsAssignedDoctorList) = apiService.patientDoctorList(patientDoctorList)
}