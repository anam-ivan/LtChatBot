package com.ivan.letstalk.api

import com.ivan.letstalk.model.chatHistory.ChatHistoryResponse
import com.ivan.letstalk.model.chatHistoryTitle.ChatHistoryTitlePayload
import com.ivan.letstalk.model.chatHistoryTitle.ChatHistoryTitleResponse
import com.ivan.letstalk.model.chatHistoryTitle.ChatIdBodies
import com.ivan.letstalk.model.deleteDocuments.DeleteDocumentResponse
import com.ivan.letstalk.model.deleteDocuments.MetaDeleteDocuments
import com.ivan.letstalk.model.doctorsAssigned.DoctorsAssignedForPatientsResponse
import com.ivan.letstalk.model.documents.*
import com.ivan.letstalk.model.faq.FaqPayload
import com.ivan.letstalk.model.faq.FaqResponse
import com.ivan.letstalk.model.glossary.GlossaryListResponse
import com.ivan.letstalk.model.glossary.GlossaryPayload
import com.ivan.letstalk.model.healthVitals.*
import com.ivan.letstalk.model.imageUpload.ImageUploadResponse
import com.ivan.letstalk.model.knowYourSideEffects.KnowYourSideEffectsPayload
import com.ivan.letstalk.model.login.LoginResponse
import com.ivan.letstalk.model.login.RequestBodies
import com.ivan.letstalk.model.login.VerifyOTPBodies
import com.ivan.letstalk.model.myHealthVitals.MyHealthVitalsResponse
import com.ivan.letstalk.model.phonenumberupdate.PhoneNumberChangeBodies
import com.ivan.letstalk.model.profileUpdate.ProfileEditPayload
import com.ivan.letstalk.model.profileUpdate.ProfileUpdateResponse
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectHistoryResponse
import com.ivan.letstalk.model.sideEffectsHistoryResponse.SideEffectUserDetailsBodies
import com.ivan.letstalk.model.slug.SlugPayload
import com.ivan.letstalk.model.slug.SlugResponse
import com.ivan.letstalk.model.topAlkSideEffects.AlkSideEffectsPayload
import com.ivan.letstalk.model.topAlkSideEffects.MarkedAlkSideEffectsPayload
import com.ivan.letstalk.model.markedAlkSideEffects.MarkedAlkSideEffectsResponse
import com.ivan.letstalk.model.markedAlkStatusChange.MarkedAlkStatusChangePayload
import com.ivan.letstalk.model.markedAlkStatusChange.MarkedAlkStatusChangeResponse
import com.ivan.letstalk.model.topAlkSideEffects.TopAllSideEffectsResponse
import com.ivan.letstalk.model.userDetails.PatientsAssignedDoctorListBodies
import com.ivan.letstalk.model.userDetails.ProfileResponse
import com.ivan.letstalk.model.userDetails.UserDetailsBodies
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("applogin")
    suspend fun loginUser(@Body body: RequestBodies.LoginBody): Response<LoginResponse>

    @POST("validateotp")
    suspend fun validateOTP(@Body body: VerifyOTPBodies.OTPLoginBody): Response<LoginResponse>

    /*@POST("user_details")
    suspend fun patientProfileDetails(): Response<PatientProfileResponse>*/

    @POST("user_details_update")
    suspend fun mPatientProfileDetails(@Body body: UserDetailsBodies.UserDetails): Response<ProfileResponse>

    @POST("user_details_update")
    suspend fun patientProfileDetails_Updated(@Body body: UserDetailsBodies.UserDetails): Response<ProfileResponse>

    @POST("userphonechangerequest")
    suspend fun patientPhoneChangeRequest(@Body body: PhoneNumberChangeBodies.PhoneNumberUpdateBody): Response<LoginResponse>

    @POST("vitals/meta/details")
    suspend fun vitalsMetaList(): Response<VitalsMetaListResponse>

    @POST("vitals/add")
    suspend fun addHealthVitals(@Body body: MetaAddBodies.MetaAddBody): Response<AddVitalsResponse>

    @POST("vitals/details")
    suspend fun healthVitalsListing(): Response<MyHealthVitalsResponse>

    @POST("vitals/update")
    suspend fun updateHealthVitals(@Body body: MetaUpdateBodies.MetaUpdateBody): Response<AddVitalsResponse>

    @POST("vitals/delete")
    suspend fun deleteHealthVitals(@Body body: MetaDeleteBodies.MetaDeleteBody): Response<AddVitalsResponse>

    @POST("allappfaq")
    suspend fun faqList(@Body body: FaqPayload.Faq): Response<FaqResponse>

    @POST("appallglossary")
    suspend fun glossaryList(@Body body: GlossaryPayload.Glossary): Response<GlossaryListResponse>

    @POST("slugdetail")
    suspend fun slugDetails(@Body body: SlugPayload.Slug): Response<SlugResponse>

    @Multipart
    @POST("profile_picture")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>
    // Call<ServerResponse> uploadImage(@Part MultipartBody.Part image);

    @POST("patientownprofileedit")
    suspend fun editProfileWithoutImage(@Body body: ProfileEditPayload.ProfileEdit): Response<ProfileUpdateResponse>

    @POST("usersideeffectsview")
    suspend fun sideEffectsHistory(@Body body: SideEffectUserDetailsBodies.UserDetails): Response<SideEffectHistoryResponse>

    @POST("documentcategorylist")
    suspend fun documentCategoryList(): Response<DocumentsCategoryListResponse>

    @Multipart
    @POST("useradddocument")
    suspend fun uploadDocument(@Part image: MultipartBody.Part,
                               @Part("dc_id") dc_id: RequestBody,
                               @Part("document_name") document_name: RequestBody,
                               @Part("record_date") record_date: RequestBody,
                               @Part("record_time") record_time: RequestBody,): Response<AddDocumentUploadResponse>

    /*@Multipart
    @POST("useradddocument")
    suspend fun uploadDocument(
        @Part image: MultipartBody.Part,
        @PartMap params: Map<String, RequestBody>
    ): Response<AddDocumentUploadResponse>*/

    @POST("ualldocument")
    suspend fun documentsList(@Body body: DocumentsPayload.Documents): Response<DocumentListResponse>

    @POST("appallsearticle")
    suspend fun knowYourSideEffects(@Body body: KnowYourSideEffectsPayload.KnowYourSideEffects): Response<GlossaryListResponse>

    @POST("udstatusupdate")
    suspend fun deleteDocument(@Body body: MetaDeleteDocuments.DeleteDocuments): Response<DeleteDocumentResponse>

    @POST("appallduk")
    suspend fun whatDoctorSay(@Body body: KnowYourSideEffectsPayload.KnowYourSideEffects): Response<GlossaryListResponse>

    @POST("chat_hist/title")
    suspend fun chatHistoryTitle(@Body body: ChatHistoryTitlePayload.ChatHistoryTitle): Response<ChatHistoryTitleResponse>

    @POST("chat_hist/details")
    suspend fun chatHistory(@Body body: ChatIdBodies.ChatId): Response<ChatHistoryResponse>

    @POST("alltas")
    suspend fun allSideEffects(@Body body: AlkSideEffectsPayload.AlkSideEffects): Response<TopAllSideEffectsResponse>

    @POST("userallmarktas")
    suspend fun markedSideEffects(@Body body: MarkedAlkSideEffectsPayload.MarkedAlkSideEffects): Response<MarkedAlkSideEffectsResponse>

    @POST("marktasstatusupdate")
    suspend fun markedSideEffectsStatusChange(@Body body: MarkedAlkStatusChangePayload.MarkedAlkStatusChange): Response<MarkedAlkStatusChangeResponse>

    @POST("apppdoctorlist")
    suspend fun patientDoctorList(@Body body: PatientsAssignedDoctorListBodies.PatientsAssignedDoctorList): Response<DoctorsAssignedForPatientsResponse>
}
