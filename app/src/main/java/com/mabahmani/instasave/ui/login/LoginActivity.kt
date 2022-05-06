package com.mabahmani.instasave.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mabahmani.instasave.R
import com.mabahmani.instasave.ui.main.MainActivity
import com.mabahmani.instasave.ui.theme.InstaSaveTheme
import com.mabahmani.instasave.util.AppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this, MainActivity::class.java)

        setContent {
            InstaSaveTheme {
                WebViewComponent(url = AppConstants.Urls.INSTA_LOGIN)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun WebViewComponent(url: String){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(
                            MaterialTheme.colors.primaryVariant,
                            RoundedCornerShape(percent = 50)
                        )
                        .padding(horizontal = 12.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .width(14.dp)
                            .height(12.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = url, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.primary)
                }
            }

            Divider(color = MaterialTheme.colors.primaryVariant)

            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebClient()
                    loadUrl(url)
                }
            })
        }
    }

    inner class WebClient: WebViewClient(){

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            if (url.orEmpty().contains("instagram.com") && !url.orEmpty().contains("login")){
                viewModel.saveUserCookies(CookieManager.getInstance().getCookie(url))
                startActivity(intent)
                finish()
            }
        }
    }
}