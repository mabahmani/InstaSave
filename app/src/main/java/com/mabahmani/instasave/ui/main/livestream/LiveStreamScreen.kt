package com.mabahmani.instasave.ui.main.livestream

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mabahmani.instasave.R
import com.mabahmani.instasave.domain.model.LiveStream
import com.mabahmani.instasave.service.DownloadLiveStreamsService
import com.mabahmani.instasave.ui.common.EmptyView
import com.mabahmani.instasave.util.AppConstants
import com.mabahmani.instasave.util.NotificationHelper
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveStreamScreen(
    viewModel: LiveStreamViewModel = hiltViewModel()
) {

    val mContext = LocalContext.current

    LaunchedEffect(true) {
        viewModel.launch()
    }

    val list = remember {
        mutableStateOf<List<LiveStream>>(listOf())
    }

    val state = viewModel.liveStreamUiState.collectAsState()

    var isRefreshing = false

    Timber.d("LiveStreamScreen %s", state.value)

    when(state.value){
        is LiveStreamUiState.ShowLiveStreams ->{
            isRefreshing = false
            list.value = (state.value as LiveStreamUiState.ShowLiveStreams).liveStreams
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Text(
            text = stringResource(id = R.string.live_streams),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(16.dp)
        )

        Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)


        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.launch() }
        ){

            Column {
                AnimatedVisibility(visible = state.value is LiveStreamUiState.LoadingWithData && state.value !is LiveStreamUiState.Loading) {
                    Row(modifier = Modifier
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
                                                    putExtra(AppConstants.Args.USERNAME, it.username)
                                                    putExtra(AppConstants.Args.URL, it.playbackUrl)
                                                }
                                            )

                                            it.downloadState.value = LiveStream.DownloadState.COMPLETED

                                        } else {
                                            mContext.startService(
                                                Intent(
                                                    mContext,
                                                    DownloadLiveStreamsService::class.java
                                                ).apply {
                                                    putExtra(AppConstants.Args.ID, it.id)
                                                    putExtra(AppConstants.Args.USERNAME, it.username)
                                                    putExtra(AppConstants.Args.URL, it.playbackUrl)
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
                                                            LiveStream.DownloadState.DOWNLOADING -> stringResource(R.string.downloading)
                                                            LiveStream.DownloadState.MERGING -> stringResource(R.string.merging)
                                                            LiveStream.DownloadState.COMPLETED -> stringResource(R.string.completed)
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

                    this@Column.AnimatedVisibility(visible = state.value is LiveStreamUiState.EmptyList || state.value is LiveStreamUiState.NetworkError || state.value is LiveStreamUiState.Error) {
                        when(state.value){
                            is LiveStreamUiState.NetworkError -> EmptyView(title = stringResource(id = R.string.network_error))
                            is LiveStreamUiState.EmptyList -> EmptyView(title = stringResource(id = R.string.no_live_streams))
                            else -> EmptyView(title = stringResource(id = R.string.something_went_wrong))
                        }
                    }

                }
            }

        }



    }

}