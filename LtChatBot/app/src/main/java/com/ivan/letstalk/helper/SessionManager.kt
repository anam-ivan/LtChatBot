package com.ivan.letstalk.helper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.ivan.letstalk.BuildConfig
import com.ivan.letstalk.MyApplication
import com.ivan.letstalk.R
import com.ivan.letstalk.ui.LoginActivity

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val sharedPrefsEditor: SharedPreferences.Editor
    private val PREF_NAME: String = BuildConfig.APPLICATION_ID + "PreferenceStorage"

    private val masterKeyAlias by lazy {  MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)}

    companion object {
        const val USER_TOKEN = "user_token"
        const val CR_NO = "cr_no"
        const val USER_ID = "user_id"
        const val IS_LOGIN = "isLogIn"
    }

    init {
        prefs = EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPrefsEditor = prefs.edit()

        //sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        //sharedPrefsEditor = sharedPreferences.edit()
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        // val editor = prefs.edit()
        val editor = sharedPrefsEditor
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to save CR No.
     */
    fun saveCRNo(crNo: String) {
        // val editor = prefs.edit()
        val editor = sharedPrefsEditor
        editor.putString(CR_NO, crNo)
        editor.apply()
    }

    /**
     * Function to fetch CR No.
     */
    fun fetchCRNo(): String? {
        return prefs.getString(CR_NO, null)
    }


    /**
     * Function to save User Id.
     */
    fun saveUserId(userId: String) {
        val editor = sharedPrefsEditor
        editor.putString(USER_ID, userId)
        editor.apply()
    }


    /**
     * Function to fetch User Id.
     */
    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun checkLogIn(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }

    fun saveUserLoginStatus(isLogin: Boolean?) {
        val editor = sharedPrefsEditor
        if (isLogin != null) {
            editor.putBoolean(IS_LOGIN, isLogin)
        }
        editor.apply()
    }

    fun logoutUser() {
        // Clearing all data from Shared Preferences
        sharedPrefsEditor.clear()
        sharedPrefsEditor.commit()
        // After logout redirect user to Login Activity
        val intent = Intent(MyApplication.instance.applicationContext, LoginActivity::class.java)
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        MyApplication.instance.applicationContext.startActivity(intent)
    }
}