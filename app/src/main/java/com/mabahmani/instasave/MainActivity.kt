package com.mabahmani.instasave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mabahmani.instasave.ui.theme.BlueGrey900
import com.mabahmani.instasave.ui.theme.Cookie
import com.mabahmani.instasave.ui.theme.InstaSaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_InstaSave)
        setContent {
            InstaSaveTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = getString(R.string.permission),
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
                            getString(R.string.allow_storage_permission),
                            getString(R.string.grant)
                        )
                    }
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
            onClick = { },
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