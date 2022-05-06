package com.mabahmani.instasave.ui.login

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.mabahmani.instasave.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    fun saveUserCookies(cookie: String){
        sharedPreferences.edit {
            putString(AppConstants.Prefs.COOKIE, cookie)
            putBoolean(AppConstants.Prefs.USER_IS_LOGGED_IN, true)
        }
    }

    fun logout(){
        sharedPreferences.edit {
            putString(AppConstants.Prefs.COOKIE, "")
            putBoolean(AppConstants.Prefs.USER_IS_LOGGED_IN, false)
        }
    }
}