package com.mabahmani.instasave.ui.main.download

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.Download
import com.mabahmani.instasave.domain.model.enums.DownloadStatus
import com.mabahmani.instasave.domain.model.enums.MediaType
import com.mabahmani.instasave.ui.common.EmptyView
import com.mabahmani.instasave.ui.theme.Ubuntu
import com.mabahmani.instasave.util.DownloadManager
import com.mabahmani.instasave.util.timeStampToHumanReadable
import com.mabahmani.instasave.util.toPercentString
import com.mabahmani.instasave.util.toast
import timber.log.Timber


@Composable
fun DownloadScreen(
    sharedLink: String = "",
    viewModel: DownloadViewModel = hiltViewModel()
) {

    LaunchedEffect(true) {
        viewModel.getDownloadList()
        viewModel.fetchLinkData(sharedLink)
    }

    val context = LocalContext.current
    val inputValue = remember { mutableStateOf(sharedLink) }
    val openDialog = remember { mutableStateOf(false) }
    val list = remember { mutableStateOf<List<Download>>(listOf()) }

    val state = viewModel.downloadUiState.collectAsState()

    Timber.d("DownloadScreen %s", state.value)

    DownloadManager.progressCallback { fileId, progress ->

        Timber.d("DownloadManager.progressCallback %s %s", fileId, progress)

        try {
            list.value.find {
                it.fileId == fileId
            }?.apply {
                this.downloadProgress.value = progress
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    DownloadManager.statusCallback { fileId, downloadStatus ->

        Timber.d("DownloadManager.statusCallback %s %s", fileId, downloadStatus)

        try {
            viewModel.updateDownloadStatus(fileId, downloadStatus)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    DownloadManager.finishCallback { fileId, downloadStatus, filePath, fileName, fileLength ->

        Timber.d("DownloadManager.finishCallback %s %s", fileId, downloadStatus)

        try {
            viewModel.updateDownloadInfoStatus(
                fileId,
                downloadStatus,
                filePath,
                fileName,
                fileLength
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    when (state.value) {
        is DownloadUiState.AddToDownload -> {
            openDialog.value = false
            inputValue.value = ""
            viewModel.addToDownloads((state.value as DownloadUiState.AddToDownload).downloads)
        }
        is DownloadUiState.ShowDownloadsList -> {
            list.value = (state.value as DownloadUiState.ShowDownloadsList).downloads
        }

        is DownloadUiState.AlreadyDownloaded -> {
            LocalContext.current.toast(
                stringResource(R.string.already_downloaded)
            )
        }

        is DownloadUiState.ShowCheckUrlDialog ->{
            openDialog.value = true
        }

        is DownloadUiState.FetchLinkDataFailed ->{
            openDialog.value = false

            inputValue.value = ""

            LocalContext.current.toast(
                stringResource(R.string.invalid_url)
            )
        }
    }

    if (openDialog.value){
        Dialog(onDismissRequest = { openDialog.value = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier
                        .padding(6.dp, 0.dp, 0.dp, 0.dp)
                        .size(24.dp), strokeWidth = 1.dp)
                    Text(
                        text = stringResource(R.string.check_url),
                        Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Text(
            text = stringResource(id = R.string.downloads),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(16.dp)
        )

        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.padding(horizontal = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_link), contentDescription = "",
                    Modifier
                        .width(22.dp)
                        .height(16.dp),
                    tint = MaterialTheme.colors.secondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = inputValue.value,
                    onValueChange = {
                        viewModel.fetchLinkData(it)
                        inputValue.value = it
                    },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 12.sp,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Light,
                    ),
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primaryVariant)
                        .fillMaxWidth(),
                    decorationBox = {
                        if (inputValue.value.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.downloadInputHint),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.secondary,
                            )
                        }

                        it()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colors.secondary),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)

        AnimatedVisibility(visible = state.value is DownloadUiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 2.dp
                )

            }
        }

        AnimatedVisibility(visible = state.value is DownloadUiState.EmptyList) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyView(title = stringResource(id = R.string.no_downloads))
            }
        }

        AnimatedVisibility(visible = state.value !is DownloadUiState.Loading && state.value !is DownloadUiState.EmptyList) {
            LazyColumn(contentPadding = PaddingValues(top = 4.dp, start= 16.dp, end=16.dp)) {
                items(
                    items = list.value
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {

                                if (it.status.value != DownloadStatus.COMPLETED) {
                                    if (it.status.value != DownloadStatus.DOWNLOADING) {
                                        DownloadManager.startDownload(it)
                                    } else {
                                        DownloadManager.stopDownload(it.fileId)
                                    }
                                } else {
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_VIEW

                                    when(it.mediaType){
                                        MediaType.IMAGE->{
                                            intent.setDataAndType(
                                                Uri.parse(it.filePath),
                                                "image/*"
                                            )
                                        }

                                        MediaType.VIDEO->{
                                            intent.setDataAndType(
                                                Uri.parse(it.filePath),
                                                "video/*"
                                            )
                                        }
                                    }

                                    context.startActivity(intent)
                                }

                            }) {

                        Row(
                            Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it.previewImageUrl),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(56.dp)
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.primaryVariant,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = it.username,
                                        style = MaterialTheme.typography.h6,
                                        color = MaterialTheme.colors.primary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "%s | %s".format(
                                            it.createdAt.timeStampToHumanReadable(),
                                            it.mediaType.name.lowercase()
                                        ),
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.secondary
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = kotlin.run {
                                        when (it.status.value) {
                                            DownloadStatus.CREATED -> "Created"
                                            DownloadStatus.PAUSED -> "Paused"
                                            DownloadStatus.DOWNLOADING -> it.downloadProgress.value.toPercentString()
                                            DownloadStatus.COMPLETED -> android.text.format.Formatter.formatFileSize(
                                                context,
                                                it.fileLength
                                            )
                                            DownloadStatus.FAILED -> "Failed"
                                        }
                                    },
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.primary
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Icon(
                                    painter = kotlin.run {
                                        when (it.status.value) {
                                            DownloadStatus.CREATED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                            DownloadStatus.PAUSED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                            DownloadStatus.DOWNLOADING -> painterResource(id = R.drawable.ic_circle_pause)
                                            DownloadStatus.COMPLETED -> painterResource(id = R.drawable.ic_circle_check)
                                            DownloadStatus.FAILED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                        }
                                    },
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height(18.dp)
                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                    }
                }
            }
        }

    }
}