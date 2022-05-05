package com.mabahmani.instasave.ui.intro

sealed class IntroUiState{
    object Loading: IntroUiState()
    object ShowShouldAllowPermissionStorageUi: IntroUiState()
    object ShowNeedLoginUi: IntroUiState()
    object RequestStoragePermission: IntroUiState()
    object NavigateToLoginScreen: IntroUiState()
    object NavigateToMainScreen: IntroUiState()
    object NavigateToSettingScreen: IntroUiState()
}
