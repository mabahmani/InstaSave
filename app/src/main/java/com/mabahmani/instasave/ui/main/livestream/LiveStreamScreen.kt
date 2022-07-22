package com.mabahmani.instasave.ui.main.livestream

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.LiveStream
import com.mabahmani.instasave.service.DownloadLiveStreamsService
import com.mabahmani.instasave.ui.common.EmptyView
import com.mabahmani.instasave.ui.intro.IntroActivity
import com.mabahmani.instasave.ui.login.LoginViewModel
import com.mabahmani.instasave.util.AppConstants
import com.mabahmani.instasave.util.NotificationHelper
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveStreamScreen(
    viewModel: LiveStreamViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val mContext = LocalContext.current

    LaunchedEffect(true) {
        viewModel.launch()
    }

    val context = LocalContext.current

    val list = remember {
        mutableStateOf<List<LiveStream>>(listOf())
    }

    val state = viewModel.liveStreamUiState.collectAsState()

    var isRefreshing = false

    val openLogoutDialog = remember { mutableStateOf(false) }

    if (openLogoutDialog.value) {
        Dialog(
            onDismissRequest = { openLogoutDialog.value = false },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background, RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.logout_title),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(9.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            openLogoutDialog.value = false
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
                            openLogoutDialog.value = false
                            (context as Activity).finishAffinity()
                            context.startActivity(Intent(context, IntroActivity::class.java))
                            loginViewModel.logout()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.logout),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }

    when (state.value) {
        is LiveStreamUiState.ShowLiveStreams -> {
            isRefreshing = false
            list.value = (state.value as LiveStreamUiState.ShowLiveStreams).liveStreams
        }
    }

    DownloadLiveStreamsService.callBack = { item ->

        try {
            list.value.find {
                it.id == item.id
            }?.apply {
                downloadState.value = item.downloadState.value
                remindSegments.value = item.videoSegmentsUrl.size
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.live_streams),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            )

            TextButton(
                onClick = { openLogoutDialog.value = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right_from_bracket),
                    contentDescription = "",
                    Modifier
                        .width(18.dp)
                        .height(18.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        }


        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)


        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.launch() }
        ) {

            Column {
                AnimatedVisibility(visible = state.value is LiveStreamUiState.LoadingWithData && state.value !is LiveStreamUiState.Loading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp),
                            color = MaterialTheme.colors.primary,
                            strokeWidth = 1.dp
                        )
                    }

                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    this@Column.AnimatedVisibility(visible = state.value is LiveStreamUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colors.primary,
                            strokeWidth = 2.dp
                        )
                    }

                    this@Column.AnimatedVisibility(visible = state.value is LiveStreamUiState.ShowLiveStreams || state.value is LiveStreamUiState.LoadingWithData) {

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(3),
                            contentPadding = PaddingValues(2.dp),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            items(
                                items = list.value
                            ) {

                                Box(modifier = Modifier
                                    .padding(1.dp)
                                    .clickable {


                                        if (it.downloadState.value == LiveStream.DownloadState.DOWNLOADING) {
                                            mContext.startService(
                                                Intent(
                                                    mContext,
                                                    DownloadLiveStreamsService::class.java
                                                ).apply {
                                                    action = NotificationHelper.STOP_RECORDING_LIVE
                                                    putExtra(AppConstants.Args.ID, it.id)
                                                    putExtra(
                                                        AppConstants.Args.USERNAME,
                                                        it.username
                                                    )
                                                    putExtra(
                                                        AppConstants.Args.URL,
                                                        it.playbackUrl
                                                    )
                                                }
                                            )

                                            it.downloadState.value =
                                                LiveStream.DownloadState.COMPLETED

                                        } else {
                                            mContext.startService(
                                                Intent(
                                                    mContext,
                                                    DownloadLiveStreamsService::class.java
                                                ).apply {
                                                    putExtra(AppConstants.Args.ID, it.id)
                                                    putExtra(
                                                        AppConstants.Args.USERNAME,
                                                        it.username
                                                    )
                                                    putExtra(
                                                        AppConstants.Args.URL,
                                                        it.playbackUrl
                                                    )
                                                }
                                            )

                                            it.downloadState.value =
                                                LiveStream.DownloadState.DOWNLOADING

                                        }

                                    }) {
                                    Box(modifier = Modifier.background(color = MaterialTheme.colors.secondaryVariant)) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = it.preview),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(0.6f),
                                            contentScale = ContentScale.FillBounds
                                        )

                                        this@Column.AnimatedVisibility(visible = it.downloadState.value != LiveStream.DownloadState.IDLE) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(4.dp, 4.dp)
                                                    .background(
                                                        MaterialTheme.colors.primary,
                                                        shape = RoundedCornerShape(4.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = kotlin.run {
                                                        when (it.downloadState.value) {
                                                            LiveStream.DownloadState.DOWNLOADING -> stringResource(
                                                                R.string.downloading
                                                            )
                                                            LiveStream.DownloadState.MERGING -> stringResource(
                                                                R.string.merging_format
                                                            ).format(it.remindSegments.value)
                                                            LiveStream.DownloadState.COMPLETED -> stringResource(
                                                                R.string.completed
                                                            )
                                                            else -> {
                                                                stringResource(R.string.completed)
                                                            }
                                                        }
                                                    },
                                                    textAlign = TextAlign.Center,
                                                    color = MaterialTheme.colors.primaryVariant,
                                                    modifier = Modifier.padding(4.dp, 4.dp),
                                                    style = MaterialTheme.typography.body1,
                                                    fontSize = 8.sp
                                                )
                                            }
                                        }


                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                                .align(Alignment.BottomCenter),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Image(
                                                painter = rememberAsyncImagePainter(model = it.avatar),
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

                                            Text(
                                                text = it.username,
                                                style = MaterialTheme.typography.h6,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colors.primaryVariant,
                                                modifier = Modifier.padding(0.dp, 2.dp),
                                                maxLines = 1
                                            )
                                            Text(
                                                text = it.elapsedTime,
                                                style = MaterialTheme.typography.caption,
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colors.primaryVariant,
                                                maxLines = 1
                                            )
                                        }

                                    }

                                }
                            }
                        }
                    }

                    this@Column.AnimatedVisibility(visible = state.value is LiveStreamUiState.EmptyList || state.value is LiveStreamUiState.NetworkError || state.value is LiveStreamUiState.Error || state.value is LiveStreamUiState.Unauthorized) {
                        when (state.value) {
                            is LiveStreamUiState.NetworkError -> EmptyView(title = stringResource(id = R.string.network_error))
                            is LiveStreamUiState.EmptyList -> EmptyView(title = stringResource(id = R.string.no_live_streams))
                            is LiveStreamUiState.Unauthorized -> EmptyView(title = stringResource(id = R.string.unauthorized_msg))
                            else -> EmptyView(title = stringResource(id = R.string.something_went_wrong))
                        }
                    }

                }
            }

        }


    }

}