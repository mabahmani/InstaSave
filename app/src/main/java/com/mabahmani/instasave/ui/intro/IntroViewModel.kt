package com.mabahmani.instasave.ui.intro

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabahmani.instasave.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(@ApplicationContext context: Context, private val sharedPreferences: SharedPreferences
):ViewModel() {

    private val _introUiState = MutableStateFlow<IntroUiState>(IntroUiState.Loading)
    val introUiState: StateFlow<IntroUiState> = _introUiState

    private var userLoggedIn = false
    private var accessStoragePermission = false
    private var permissionFirstTimeDenied = false

    init {
        checkStoragePermission(context)
    }

    fun onIntroActionClicked(){
        if (accessStoragePermission){
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.NavigateToLoginScreen)
            }
        }

        else{
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.RequestStoragePermission)
            }
        }
    }

    fun onPermissionGranted(){
        accessStoragePermission = true

        if (userLoggedIn){
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.NavigateToMainScreen)
            }
        }

        else{
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.ShowNeedLoginUi)
            }
        }
    }

    fun onPermissionDenied(shouldShowRequestPermissionRationale: Boolean){
        if (shouldShowRequestPermissionRationale || shouldShowRequestPermissionRationale == permissionFirstTimeDenied){
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.ShowShouldAllowPermissionStorageUi)
            }
        }

        else{
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.NavigateToSettingScreen)
            }
        }

        permissionFirstTimeDenied = true
        sharedPreferences.edit {
            putBoolean(AppConstants.Prefs.PERMISSION_FIRST_TIME_DENIED, true)
        }

    }

    private fun checkStoragePermission(context: Context) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                accessStoragePermission = true
                checkUserIsLoggedIn()
            }
            else -> {
                accessStoragePermission = false
                permissionFirstTimeDenied = sharedPreferences.getBoolean(AppConstants.Prefs.PERMISSION_FIRST_TIME_DENIED, false)
                viewModelScope.launch {
                    _introUiState.emit(IntroUiState.ShowShouldAllowPermissionStorageUi)
                }
            }
        }
    }

    private fun checkUserIsLoggedIn() {

        userLoggedIn = sharedPreferences.getBoolean(AppConstants.Prefs.USER_IS_LOGGED_IN, false)

        if (userLoggedIn){
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.NavigateToMainScreen)
            }
        }
        else{
            viewModelScope.launch {
                _introUiState.emit(IntroUiState.ShowNeedLoginUi)
            }
        }
    }
}