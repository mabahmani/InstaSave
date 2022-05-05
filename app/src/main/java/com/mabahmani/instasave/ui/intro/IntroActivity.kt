package com.mabahmani.instasave.ui.intro

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mabahmani.instasave.R
import com.mabahmani.instasave.ui.theme.Cookie
import com.mabahmani.instasave.ui.theme.InstaSaveTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroActivity : ComponentActivity() {

    private val introViewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_InstaSave)

        setContent {
            InstaSaveTheme {

                val state = introViewModel.introUiState.collectAsState()

                val appBarTitle = remember {
                    mutableStateOf("")
                }

                val title = remember {
                    mutableStateOf("")
                }

                val buttonText = remember {
                    mutableStateOf("")
                }

                when (state.value) {
                    IntroUiState.Loading -> {}
                    IntroUiState.NavigateToLoginScreen -> {}
                    IntroUiState.NavigateToMainScreen -> {}
                    IntroUiState.RequestStoragePermission -> {
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                    IntroUiState.ShowNeedLoginUi -> {
                        appBarTitle.value = getString(R.string.login)
                        title.value = getString(R.string.login_to_instagram_account)
                        buttonText.value = getString(R.string.login)
                    }
                    IntroUiState.ShowShouldAllowPermissionStorageUi -> {
                        appBarTitle.value = getString(R.string.permission)
                        title.value = getString(R.string.allow_storage_permission)
                        buttonText.value = getString(R.string.grant)
                    }
                    IntroUiState.NavigateToSettingScreen -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = appBarTitle.value,
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        IntroAction(
                            title.value,
                            buttonText.value
                        )
                    }
                }

            }
        }
    }

    @Composable
    fun IntroAction(title: String, buttonText: String) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary,
                fontSize = 32.sp,
                fontFamily = Cookie
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { introViewModel.onIntroActionClicked() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                elevation = null,
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                introViewModel.onPermissionGranted()
            }
            else{

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    introViewModel.onPermissionDenied(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                }

                else{
                    introViewModel.onPermissionDenied(true)
                }
            }
        }
}

