package com.ivan.letstalk.repo

import com.ivan.letstalk.api.ApiHelper
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
import retrofit2.http.Part

class LoginRepository(private val apiHelper: ApiHelper) {
    suspend fun loginPatient(user: RequestBodies.LoginBody) = apiHelper.login(user)

    suspend fun validateOTP(validateOTP: VerifyOTPBodies.OTPLoginBody) = apiHelper.validateOTP(validateOTP)

    // suspend fun patientProfileDetails() = apiHelper.patientProfileDetails()

    suspend fun mPatientProfileDetails(userDetails: UserDetailsBodies.UserDetails) = apiHelper.mPatientProfileDetails(userDetails)

    suspend fun patientPhoneChangeRequest(phoneNumberUpdate: PhoneNumberChangeBodies.PhoneNumberUpdateBody) = apiHelper.patientPhoneNumberUpdate(phoneNumberUpdate)

    suspend fun vitalsMetaList() = apiHelper.vitalsMetaList()

    suspend fun addHealthVitals(addHealthVitals: MetaAddBodies.MetaAddBody) = apiHelper.addHealthVitals(addHealthVitals)

    suspend fun healthVitalsListing() = apiHelper.healthVitalsListing()

    suspend fun updateHealthVitals(updateHealthVitals: MetaUpdateBodies.MetaUpdateBody) = apiHelper.updateHealthVitals(updateHealthVitals)

    suspend fun deleteHealthVitals(deleteHealthVitals: MetaDeleteBodies.MetaDeleteBody) = apiHelper.deleteHealthVitals(deleteHealthVitals)

    suspend fun faqList(faq: FaqPayload.Faq) = apiHelper.faqList(faq)

    suspend fun glossaryList(glossary: GlossaryPayload.Glossary) = apiHelper.glossaryList(glossary)

    suspend fun slugDetails(slug: SlugPayload.Slug) = apiHelper.slugDetails(slug)

    suspend fun uploadImage(@Part image: MultipartBody.Part) = apiHelper.uploadImage(image)

    suspend fun editProfileWithoutImage(profileEdit: ProfileEditPayload.ProfileEdit) = apiHelper.editProfileWithoutImage(profileEdit)

    suspend fun sideEffectsHistory(userDetails: SideEffectUserDetailsBodies.UserDetails) = apiHelper.sideEffectsHistory(userDetails)

    suspend fun documentCategoryList() = apiHelper.documentCategoryList()

    suspend fun uploadDocument(
        @Part image: MultipartBody.Part,
        @Part document_category_id: RequestBody,
        @Part document_name: RequestBody,
        @Part record_date: RequestBody,
        @Part record_time: RequestBody
    ) = apiHelper.uploadDocument(
        image,
        document_category_id,
        document_name,
        record_date,
        record_time
    )
    /*suspend fun uploadDocument(
        @Part image: MultipartBody.Part,
        @PartMap params: Map<String, RequestBody>
    ) = apiHelper.uploadDocument(
        image,
        params
    )*/

    suspend fun documentsList(documentList: DocumentsPayload.Documents) = apiHelper.documentsList(documentList)

    suspend fun knowYourSideEffects(knowYourSideEffects: KnowYourSideEffectsPayload.KnowYourSideEffects) = apiHelper.knowYourSideEffects(knowYourSideEffects)

    suspend fun deleteDocuments(deleteDocuments: MetaDeleteDocuments.DeleteDocuments) = apiHelper.deleteDocuments(deleteDocuments)

    suspend fun whatDoctorSay(whatDoctorSay: KnowYourSideEffectsPayload.KnowYourSideEffects) = apiHelper.whatDoctorSay(whatDoctorSay)

    suspend fun chatHistoryTitle(chatHistoryTitle : ChatHistoryTitlePayload.ChatHistoryTitle) = apiHelper.chatHistoryTitle(chatHistoryTitle)

    suspend fun chatHistory(chatHistory : ChatIdBodies.ChatId) = apiHelper.chatHistory(chatHistory)

    suspend fun allSideEffects(allSideEffects: AlkSideEffectsPayload.AlkSideEffects) = apiHelper.allSideEffects(allSideEffects)

    suspend fun markedSideEffects(markedSideEffects: MarkedAlkSideEffectsPayload.MarkedAlkSideEffects) = apiHelper.markedSideEffects(markedSideEffects)

    suspend fun markedSideEffectsStatusChange(markedSideEffectsStatusChange: MarkedAlkStatusChangePayload.MarkedAlkStatusChange) = apiHelper.markedSideEffectsStatusChange(markedSideEffectsStatusChange)

    suspend fun patientDoctorList(patientDoctorList: PatientsAssignedDoctorListBodies.PatientsAssignedDoctorList) = apiHelper.patientDoctorList(patientDoctorList)
}