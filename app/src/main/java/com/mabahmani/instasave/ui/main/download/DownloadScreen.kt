package com.mabahmani.instasave.ui.main.download

import android.content.Intent
import android.net.Uri
import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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


@OptIn(ExperimentalFoundationApi::class)
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
    val openCheckUrlDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val list = remember { mutableStateOf<List<Download>>(listOf()) }
    val itemToDelete = remember { mutableStateOf<Download?>(null) }

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
            openCheckUrlDialog.value = false
            inputValue.value = ""
            viewModel.addToDownloads((state.value as DownloadUiState.AddToDownload).downloads)
            viewModel.setIdleState()
        }
        is DownloadUiState.ShowDownloadsList -> {
            list.value = (state.value as DownloadUiState.ShowDownloadsList).downloads
        }

        is DownloadUiState.AlreadyDownloaded -> {
            context.toast(
                stringResource(R.string.already_downloaded)
            )

            viewModel.setIdleState()
        }

        is DownloadUiState.ShowCheckUrlDialog -> {
            openCheckUrlDialog.value = true
        }

        is DownloadUiState.FetchLinkDataFailed -> {
            openCheckUrlDialog.value = false
            inputValue.value = ""

            context.toast(
                stringResource(R.string.invalid_url)
            )

            viewModel.setIdleState()
        }

        is DownloadUiState.ShowDeleteDialog -> {
            itemToDelete.value = (state.value as DownloadUiState.ShowDeleteDialog).download
            openDeleteDialog.value = true
            viewModel.setIdleState()
        }

        is DownloadUiState.Idle -> {
            openCheckUrlDialog.value = false
            inputValue.value = ""
        }
    }

    if (openDeleteDialog.value) {
        Dialog(
            onDismissRequest = { openDeleteDialog.value = false },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.delete_download),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(9.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            openDeleteDialog.value = false
                            viewModel.deleteDownload(itemToDelete.value, true)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.with_file),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }

                    Row {
                        TextButton(
                            onClick = {
                                openDeleteDialog.value = false
                                itemToDelete.value = null
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.primary
                            )
                        }

                        TextButton(
                            onClick = {
                                openDeleteDialog.value = false
                                viewModel.deleteDownload(itemToDelete.value)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.ok),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.primary
                            )
                        }

                    }
                }
            }
        }
    }

    if (openCheckUrlDialog.value) {
        Dialog(
            onDismissRequest = { openCheckUrlDialog.value = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colors.background, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(6.dp, 0.dp, 0.dp, 0.dp)
                            .size(24.dp), strokeWidth = 1.dp
                    )
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
            LazyColumn(contentPadding = PaddingValues(top = 4.dp)) {
                items(
                    items = list.value
                ) {
                    Column(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    Timber.d("onClick")

                                    if (it.status.value != DownloadStatus.COMPLETED) {
                                        if (it.status.value != DownloadStatus.DOWNLOADING && it.status.value != DownloadStatus.CONNECTING) {
                                            DownloadManager.startDownload(it)
                                        } else {
                                            DownloadManager.stopDownload(it.fileId)
                                        }
                                    } else {
                                        val intent = Intent()
                                        intent.action = Intent.ACTION_VIEW

                                        when (it.mediaType) {
                                            MediaType.IMAGE -> {
                                                intent.setDataAndType(
                                                    Uri.parse(it.filePath),
                                                    "image/*"
                                                )
                                            }

                                            MediaType.VIDEO -> {
                                                intent.setDataAndType(
                                                    Uri.parse(it.filePath),
                                                    "video/*"
                                                )
                                            }
                                        }

                                        context.startActivity(intent)
                                    }

                                },
                                onLongClick = {
                                    Timber.d("onLongClick")
                                    viewModel.onShowDeleteDialog(it)
                                }
                            )) {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                                        .width(64.dp)
                                        .height(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.primaryVariant,
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Row {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = it.profilePictureUrl),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .width(32.dp)
                                            .height(32.dp)
                                            .clip(CircleShape)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colors.primaryVariant,
                                                CircleShape
                                            ),
                                        contentScale = ContentScale.FillBounds
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Column {
                                        Text(
                                            text = it.username,
                                            style = MaterialTheme.typography.h6,
                                            color = MaterialTheme.colors.primary
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = it.fullName,
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.primary
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(
                                                    id = R.drawable.ic_calendar_arrow_down
                                                ), contentDescription = "",
                                                Modifier.width(12.dp),
                                                tint = MaterialTheme.colors.secondary
                                            )

                                            Spacer(modifier = Modifier.width(6.dp))

                                            Text(
                                                text = it.createdAt.timeStampToHumanReadable(),
                                                style = MaterialTheme.typography.caption,
                                                color = MaterialTheme.colors.secondary
                                            )
                                        }

                                    }
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Icon(
                                        painter = painterResource(id = kotlin.run {
                                            if (it.mediaType == MediaType.IMAGE)
                                                R.drawable.ic_image
                                            else
                                                R.drawable.ic_clapperboard
                                        }), contentDescription = "",
                                        Modifier.width(16.dp),
                                        tint = MaterialTheme.colors.primary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = kotlin.run {
                                            when (it.status.value) {
                                                DownloadStatus.CREATED -> stringResource(R.string.created)
                                                DownloadStatus.PAUSED -> stringResource(R.string.paused)
                                                DownloadStatus.DOWNLOADING -> it.downloadProgress.value.toPercentString()
                                                DownloadStatus.COMPLETED -> Formatter.formatFileSize(
                                                    context,
                                                    it.fileLength
                                                )
                                                DownloadStatus.FAILED -> stringResource(R.string.failed)
                                                DownloadStatus.CONNECTING -> stringResource(R.string.connecting)
                                            }
                                        },
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.primary
                                    )
                                }


                                Spacer(modifier = Modifier.width(12.dp))

                                Icon(
                                    painter = kotlin.run {
                                        when (it.status.value) {
                                            DownloadStatus.CREATED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                            DownloadStatus.PAUSED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                            DownloadStatus.DOWNLOADING -> painterResource(id = R.drawable.ic_circle_pause)
                                            DownloadStatus.COMPLETED -> painterResource(id = R.drawable.ic_circle_check)
                                            DownloadStatus.FAILED -> painterResource(id = R.drawable.ic_circle_arrow_down)
                                            DownloadStatus.CONNECTING -> painterResource(id = R.drawable.ic_circle_pause)
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

                        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
                    }
                }
            }
        }

    }
}